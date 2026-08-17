# HtmlFlow Micro Frontends Guide

This guide documents the `.mfe(...)` functionality currently implemented in this repository. It covers the Java DSL in `htmlflow-core`, the generated HTML contract, and the browser runtime implemented in `ts-engine/src/base.ts`.

> This guide is based on the shipped code, not only on the internal design notes. Some demo snippets in `mfe-shell/src/main/java/com/dev/HtmlMfeResource.java` are currently stale, especially around `/view` routes and service ports. Those mismatches are called out explicitly below.

## Where to look in this repo

| Concern | File |
| --- | --- |
| Shell composition example | `mfe-shell/src/main/java/com/dev/HtmlMfeResource.java` |
| Java MFE config model | `htmlflow-core/src/main/java/htmlflow/HtmlMfeConfig.java` |
| Generated HTML attributes | `htmlflow-core/src/main/java/htmlflow/visitor/HtmlVisitor.java` |
| Automatic `<head>` script injection | `htmlflow-core/src/main/java/htmlflow/visitor/PreprocessingVisitorMfe.java` |
| Browser runtime source | `ts-engine/src/base.ts` |
| Built runtime served by the shell | `mfe-shell/src/main/resources/META-INF/resources/base.js` |
| Companion script examples | `mfe-spring/.../mfe-bikes.js`, `mfe-qute/.../mfe-cart.js`, `mfe-htmlflow/.../mfe-order.js` |

## What `.mfe(...)` does

HtmlFlow's MFE support lets a server-rendered shell declare remote UI fragments in Java and defer the actual fragment composition to the browser.

At a high level:

1. The shell view calls `.mfe(cfg -> { ... })`.
2. HtmlFlow renders a custom element such as `<micro-frontend ...></micro-frontend>`.
3. HtmlFlow injects `base.js` plus any configured companion scripts into `<head>`.
4. The browser runtime fetches the fragment HTML from `mfe-url`.
5. The fragment is mounted inside a Shadow DOM root.
6. A companion script can attach behavior through `window.mfe(name, callback)`.

This splits responsibilities cleanly:

- **HtmlFlow / the shell** decides **what** fragments exist and how they are wired.
- **Fragment services** return the HTML content for each fragment.
- **The runtime** handles fetch, Shadow DOM mounting, styles, reloads, and the inter-MFE event bus.
- **Companion scripts** add behavior without having to manage timing or direct shell DOM queries.

## Quick start

### 1. Enable MFE support on the shell view

```java
HtmlView<?> shell = HtmlFlow.ViewFactory.builder()
    .mfeEnabled(true)
    .build()
    .view(page -> page
        .html()
            .head().__()
            .body()
                .div()
                    .mfe(cfg -> {
                        cfg.setMfeUrlResource("http://localhost:8081/bikes");
                        cfg.setMfeName("bikes");
                        cfg.setMfeListeningEventName("reload-bikes");
                        cfg.setMfeTriggersEventName("cart-events");
                        cfg.setMfeScriptUrl("http://localhost:8081/js/mfe-bikes.js");
                        cfg.setMfeStylingUrl("http://localhost:8081/css/style.css");
                    }).__()
            .__()
        .__()
    );
```

### 2. Write the companion script with `window.mfe(...)`

```js
mfe('bikes', ({ root, triggerMfeEvent }) => {
    root?.querySelectorAll('button[data-bike-id]').forEach(button => {
        button.addEventListener('click', () => {
            triggerMfeEvent('bike selected', {
                type: 'add',
                id: button.getAttribute('data-bike-id')
            });
        });
    });
});
```

The important constraint is that the `mfe('bikes', ...)` name must match the Java-side `cfg.setMfeName("bikes")`.

## What HtmlFlow generates

### View factory requirements

The feature is enabled from the view factory:

```java
HtmlFlow.ViewFactory.builder().mfeEnabled(true).build()
```

`mfeEnabled(true)` requires `preEncoding` to stay enabled. The builder throws an `IllegalStateException` if you try to enable MFE support while disabling preprocessing:

```java
HtmlFlow.ViewFactory.builder()
    .mfeEnabled(true)
    .preEncoding(false) // invalid combination
    .build();
```

For most users this is transparent, because `preEncoding` already defaults to `true`.

### Java DSL configuration reference

`.mfe(cfg -> { ... })` is backed by `HtmlMfeConfig.Builder`.

| Java setter | Required | Effect | Notes |
| --- | --- | --- | --- |
| `setMfeUrlResource(String)` | Yes | Defines the fragment URL written to `mfe-url` | Must be non-blank |
| `setMfeName(String)` | Yes | Defines the fragment logical name written to `mfe-name` | Must be non-blank; used by `window.mfe(name, ...)` |
| `setMfeListeningEventName(String)` | No | Writes `mfe-listen-event` and defines the default event listened to by the runtime and `listenMfeEvent` | If you pass `""`, the empty attribute is still emitted; prefer omitting it when unused |
| `setMfeTriggersEventName(String)` | No | Writes `mfe-trigger-event` and defines the default event used by `triggerMfeEvent` | Same empty-string caveat as above |
| `setMfeScriptUrl(String)` | No | Injects a `<script type="module" src="..."></script>` tag into `<head>` | Not rendered as an element attribute |
| `setMfeScriptIntegrity(String)` | No | Adds `integrity="..." crossorigin="anonymous"` to the injected script tag | Only matters when `mfeScriptUrl` is present |
| `setMfeStylingUrl(String)` | No | Writes `mfe-styling-url` so the runtime loads CSS into the Shadow DOM | Empty string is ignored |
| `setMfeStreamingData(boolean)` | No | Writes `mfe-stream-data="true"` and switches the runtime to streaming mode | Streaming has lifecycle differences; see below |
| `setMfeElementName(String)` | No | Changes the emitted custom tag name | Advanced option; the bundled runtime only registers `micro-frontend` |

`HtmlMfeConfig.Builder.build()` currently validates only two fields:

- `mfeUrlResource` is required
- `mfeName` is required

Everything else is optional.

### Generated HTML contract

The `.mfe(...)` call ultimately produces a custom element with attributes consumed by the browser runtime:

```html
<micro-frontend
  mfe-url="http://localhost:8081/bikes"
  mfe-name="bikes"
  mfe-styling-url="http://localhost:8081/css/style.css"
  mfe-trigger-event="cart-events"
  mfe-listen-event="reload-bikes">
</micro-frontend>
```

When streaming is enabled, HtmlFlow also adds:

```html
mfe-stream-data="true"
```

Attribute behavior in the current implementation:

| HTML attribute | Source | Used by | Meaning |
| --- | --- | --- | --- |
| `mfe-url` | `setMfeUrlResource` | `connectedCallback`, `fetchData`, `fetchStreamData` | Fragment endpoint |
| `mfe-name` | `setMfeName` | Runtime registry, ready lifecycle, loading/error labels | Unique logical fragment name on the page |
| `mfe-listen-event` | `setMfeListeningEventName` | Runtime auto-reload subscription and `listenMfeEvent` default | Default incoming event name |
| `mfe-trigger-event` | `setMfeTriggersEventName` | `triggerMfeEvent` default | Default outgoing event name |
| `mfe-styling-url` | `setMfeStylingUrl` | `loadStylesheet` | Optional fragment-local stylesheet |
| `mfe-stream-data` | `setMfeStreamingData(true)` | `loadFragment` | Switches to streaming fetch path |

There is intentionally **no** element attribute for the companion script URL or script integrity. Those affect `<head>` script injection, not the fragment tag itself.

### Automatic script injection

When MFE support is enabled, `PreprocessingVisitorMfe` injects scripts into `<head>` automatically:

1. `base.js` is always injected.
2. Each non-empty `mfeScriptUrl` is injected as `type="module"`.

That means you do **not** need to add companion script tags manually if the page is already using `.mfe(...)`.

The generated `<head>` looks like this:

```html
<head>
  <script type="module" src="base.js"></script>
  <script type="module" src="http://localhost:8081/js/mfe-bikes.js"></script>
</head>
```

Practical implications:

- Your page should include a `<head>` element, because that is where injection happens.
- `mfeScriptUrl` scripts are loaded as **ES modules**.
- The runtime script URL is currently hardcoded as `base.js`, so the shell that serves the composed page must expose that file under a path that resolves from the page.

## Browser runtime API

The runtime is authored in `ts-engine/src/base.ts` and built into `mfe-shell/src/main/resources/META-INF/resources/base.js`.

It defines:

- a custom element: `micro-frontend`
- a global helper: `window.mfe(name, callback)`

### `window.mfe(name, callback)`

Use `window.mfe(...)` as the public way to attach behavior to a fragment:

```js
mfe('bikes', (ctx) => {
    // ctx.root is the fragment ShadowRoot
});
```

This API is **order-independent**:

- If the script runs **before** the element connects, the callback is queued.
- If the element exists but the fragment is still loading, the callback waits for the fragment-ready lifecycle event.
- If everything is already ready, the callback runs immediately.

That is why the companion scripts in this repo do not need `document.querySelector(...)` or ad-hoc timing logic.

### `MfeContext`

The callback receives an `MfeContext` object:

```ts
interface MfeContext {
    root: ShadowRoot | null;
    triggerMfeEvent: (message: string, payload: unknown, eventName?: string) => void;
    listenMfeEvent: (listener: EventListener, eventName?: string) => void;
    reloadMfe: () => void;
    mfeEvents: typeof MfcEventType;
}
```

The runtime behavior behind each property is:

| Context field | What it does | Current behavior |
| --- | --- | --- |
| `root` | Gives access to the fragment Shadow DOM | Query inside the fragment with `root?.querySelector(...)`; avoid querying the shell document |
| `triggerMfeEvent(message, payload, eventName?)` | Emits a `CustomEvent` on the shared MFE event bus | If `eventName` is omitted, the runtime uses the current element's `mfe-trigger-event` |
| `listenMfeEvent(listener, eventName?)` | Subscribes to the shared MFE event bus | If `eventName` is omitted, the runtime uses the current element's `mfe-listen-event` |
| `reloadMfe()` | Refetches and rerenders the current fragment | Equivalent to a local reload event with payload `{ type: mfeEvents.RELOAD }` |
| `mfeEvents.RELOAD` | Built-in event constant | Reserved by the runtime to mean "reload this fragment" |

> The current TypeScript source types `eventName` as required, but the runtime implementation treats it as optional, and the shipped scripts rely on the omitted-argument behavior.

### Lifecycle

For the regular, non-streaming path, the runtime lifecycle is:

1. The element reads its `mfe-*` attributes in `connectedCallback`.
2. It registers itself in a static runtime registry keyed by `mfe-name`.
3. It subscribes to its configured listen-event for built-in reload handling.
4. It fetches `mfe-url`.
5. It mounts the returned HTML into an **open Shadow DOM**.
6. It loads and caches the optional stylesheet from `mfe-styling-url`.
7. It marks itself ready and replays any queued `mfe(name, callback)` registrations.

On reload, the runtime rebuilds the fragment and runs the callback again with the new Shadow DOM contents.

The runtime also cleans up listeners registered through `listenMfeEvent(...)` before re-initializing the callback. That prevents listener stacking across reloads.

### Event model

The current runtime uses a shared static `EventTarget` (`Mfe.mfeEventBus`) as the MFE event bus.

This is important:

- Events are **not** currently dispatched on `window`
- Events are **not** DOM-bubbled between sibling custom elements
- Cross-MFE communication works because all instances share the same in-memory event bus

There are two event layers:

1. **Your application payloads**
2. **The built-in reload convention**

The reload convention is special:

```js
{ type: mfeEvents.RELOAD }
```

If a fragment is configured to listen on a given event name and receives a `CustomEvent` whose `detail.payload.type === mfeEvents.RELOAD`, the runtime refetches that fragment automatically.

Any other payload shape is application-defined and must be handled by companion scripts.

#### Example: bikes -> cart

In `mfe-bikes.js`, the bikes fragment emits an application event:

```js
triggerMfeEvent('add bike to cart', { type: 'add', id: '123' });
```

In `mfe-cart.js`, the cart fragment listens to its configured default event, inspects `e.detail.payload.type === 'add'`, calls its business API, and then uses `reloadMfe()` to refresh itself.

#### Example: order -> bikes + cart fan-out

`mfe-order.js` demonstrates the explicit override form:

```js
mfe('mfe3', ({ triggerMfeEvent, listenMfeEvent, mfeEvents }) => {
    listenMfeEvent(() => {
        triggerMfeEvent('reload bikes', { type: mfeEvents.RELOAD }, 'triggerBikeEvent');
        triggerMfeEvent('reload cart', { type: mfeEvents.RELOAD }, 'triggerCartEvent');
    });
});
```

Use this form when one fragment needs to target more than one downstream event name.

## Styling, isolation, and HTML handling

### Shadow DOM isolation

Each fragment is mounted under its own Shadow DOM root:

- the shell cannot accidentally style fragment internals with ordinary page CSS
- one fragment's CSS does not leak into another fragment
- companion scripts should query `ctx.root`, not `document`

### Stylesheet loading

If `mfe-styling-url` is present, the runtime:

1. fetches the CSS
2. creates a `CSSStyleSheet`
3. applies it via `shadowRoot.adoptedStyleSheets`
4. caches the stylesheet by URL

That cache means multiple fragments using the same stylesheet URL do not refetch it every time.

### HTML sanitization

Fetched HTML is not inserted directly with raw `innerHTML`.

The runtime uses `createSafeHtml()`:

- if `Element.prototype.setHTML` exists, it uses the browser sanitizer with the allowlist from `Mfe.ALLOWED_ELEMENTS`
- otherwise it falls back to `setHTMLUnsafe(...)`

Practical note: if a fragment depends on unusual tags or custom elements, review the allowlist in `ts-engine/src/base.ts`.

## Streaming fragments

If you call:

```java
cfg.setMfeStreamingData(true);
```

HtmlFlow emits `mfe-stream-data="true"` and the runtime uses `fetchStreamData()` instead of the normal `fetchData()` path.

The current streaming path is intended for progressively appended markup, including `data-stream` markers.

Current caveats:

- it does **not** currently mark the fragment as ready in the same way as the regular path
- it does **not** dispatch the fragment-ready lifecycle event used by `window.mfe(...)`
- it does **not** use the same abort/reload behavior as the regular path

In practice, today the companion-script lifecycle is designed primarily around the non-streaming path.

## Demo applications in this repository

This repo contains three fragment providers plus one shell.

### Fragment providers

| Module | Rendering tech | Current module route | Companion script | Styles |
| --- | --- | --- | --- | --- |
| `mfe-spring` | Spring MVC + Thymeleaf | `http://localhost:8081/bikes` | `http://localhost:8081/js/mfe-bikes.js` | `http://localhost:8081/css/style.css` |
| `mfe-qute` | Quarkus + Qute | `http://localhost:8083/cart` | `http://localhost:8083/mfe-cart.js` | `http://localhost:8083/style.css` |
| `mfe-htmlflow` | Quarkus + HtmlFlow | `http://localhost:8084/order/history` | `http://localhost:8084/mfe-order.js` | No style is currently wired from the shell example |

### Important note about `HtmlMfeResource`

`mfe-shell/src/main/java/com/dev/HtmlMfeResource.java` is still the best single-file illustration of how the shell is intended to compose fragments, but some of its URLs are no longer aligned with the running module code:

- it uses `/bikes/view` and `/cart/view`, while the current fragment routes are `/bikes` and `/cart`
- it points cart to port `8082`, but the current cart module is configured on `8083`
- it points order-related resources to `8083`, while `mfe-htmlflow` is configured on `8084`

Treat the module routes above as the authoritative demo endpoints.

### Important note about business APIs inside scripts

The companion scripts in `mfe-qute` and other demo apps also contain application-specific downstream API calls. Those URLs are part of the demo business logic, **not** part of HtmlFlow's MFE contract.

When documenting or reusing the feature, separate these concerns:

- **HtmlFlow MFE contract**: `.mfe(...)`, generated attributes, `window.mfe(...)`, event bus, Shadow DOM, reloads
- **Application logic**: what your script does after it receives an event or button click

## Recommended authoring rules

For a clean developer experience, treat these as the default conventions:

1. Keep `mfeEnabled(true)` on any shell view that uses `.mfe(...)`.
2. Give every fragment a stable, unique `mfeName`.
3. Use `window.mfe(name, callback)` instead of querying the page manually.
4. Query only `ctx.root`, never the shell document, for fragment internals.
5. Let Java be the source of truth for the default event names.
6. Pass explicit `eventName` overrides only when you need multi-target fan-out.
7. Prefer omitting optional config fields instead of passing empty strings.
8. Treat `setMfeElementName(...)` as advanced/manual unless you are also shipping a matching custom element definition.

## Building and running the demo

### Build the runtime from TypeScript

The runtime source lives in `ts-engine/src/base.ts`.

From `ts-engine/`:

```bash
npm install
npm run build:minified
```

That writes the built runtime to:

```text
../mfe-shell/src/main/resources/META-INF/resources/base.js
```

Alternative commands, watch mode, and type-check-only commands are documented in `ts-engine/COMMANDS.md`.

### Start the demo stack

From the repository root:

```bash
docker compose up --build
```

Useful follow-up commands:

```bash
docker compose ps
docker compose logs -f shell
```

`DOCKER.md` documents the available compose workflows.

The checked-in compose topology currently documents:

| Service | Port |
| --- | --- |
| `api` | `8080` |
| `mfe-spring` | `8081` |
| `mfe-qute` | `8083` |
| `mfe-htmlflow` | `8084` |
| `shell` | `8082` |

However, the shell module itself still contains a `quarkus.http.port=8080` setting, so use `docker compose ps` as the final source of truth for the running shell port in your environment.

## Troubleshooting and current limitations

| Symptom | Likely cause | What to check |
| --- | --- | --- |
| `IllegalStateException: MFE support requires preEncoding to be enabled` | `mfeEnabled(true)` was combined with `preEncoding(false)` | Leave preprocessing enabled |
| Fragment HTML never initializes its script | `mfeName` does not match `window.mfe(...)`, the script URL is wrong, or the fragment is using the streaming path | Check `mfe-name`, `mfeScriptUrl`, and whether `setMfeStreamingData(true)` is involved |
| `triggerMfeEvent(...)` does nothing | No default trigger event is configured, or the target listener name does not match | Set `setMfeTriggersEventName(...)` or pass an explicit third argument |
| `listenMfeEvent(...)` never fires | No default listen event is configured, or the wrong event name is being used | Set `setMfeListeningEventName(...)` or pass an explicit second argument |
| The custom tag renders but never upgrades | `setMfeElementName(...)` changed the tag name, but only `micro-frontend` is registered by the bundled runtime | Either keep the default tag name or register a compatible custom element yourself |
| `base.js` is missing | The runtime script path is hardcoded to `base.js` during preprocessing | Ensure the shell serves `base.js` from a path that resolves from the composed page |
| Shell demo URLs return 404 | `HtmlMfeResource` still references older `/view` routes and inconsistent ports | Use the module routes documented in this guide instead |
| Styles do not apply inside the fragment | The CSS was not provided through `mfe-styling-url` | Remember that ordinary shell CSS does not cross the Shadow DOM boundary |

## Summary

The current `.mfe(...)` feature in this repository is a three-part contract:

1. **Java DSL** declares the fragment and its wiring.
2. **HtmlFlow preprocessing** injects the runtime and companion scripts.
3. **The browser runtime** fetches, mounts, styles, reloads, and coordinates fragments through `window.mfe(...)`.

If you adopt the recommended path:

- keep the default `micro-frontend` element
- serve `base.js`
- let Java define the default event names
- write companion scripts against `ctx.root`

then the feature is already usable as a clean, developer-friendly composition model for client-side micro-frontends in HtmlFlow.
