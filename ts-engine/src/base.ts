interface MfeContext {
    root: ShadowRoot | null;
    triggerMfeEvent: (message: string, payload: unknown, eventName: string, ) => void;
    listenMfeEvent: (listener: EventListener, eventName: string) => void;
    reloadMfe: () => void;
    mfeEvents: typeof MfcEventType
}

declare global {
    interface Window {
        mfe: (name: string, callback: (mfeContext: MfeContext) => void) => void;
    }
}

enum MfcEventType {
    RELOAD = "RELOAD"
}

class Mfe extends HTMLElement {
    private mfeReady: boolean = false;
    private mfeName: string | null = "";
    private mfeUrlResource: string | null = "";
    private mfeListeningEventName: string | null = "";
    private mfeTriggerEventName: string | null = "";
    private readonly readyEventSuffix: string = "-fragment-ready";
    private mfeStylingUrl: string | null = "";
    private isMfeStreamingData: string | null = "";
    private abortController: AbortController | null = null;
    private bindReload = this.reloadFragment.bind(this); // prevent new reference
    private static registry = new Map<string, Mfe>();
    private static pendingCallBacks = new Map<string, ((mfeRoot: MfeContext)=>void)[]>();
    private static mfeEventBus = new EventTarget();
    private static styleSheetCache = new Map<string, CSSStyleSheet>();
    private static readonly ALLOWED_ELEMENTS: string[] = [
        // Structural / sectioning
        'div', 'span', 'main', 'section', 'article', 'aside', 'nav', 'header', 'footer',
        // Headings
        'h1', 'h2', 'h3', 'h4', 'h5', 'h6',
        // Text content
        'p', 'br', 'hr', 'blockquote', 'pre', 'code', 'em', 'strong', 'small', 'b', 'i', 'u', 'mark', 'sub', 'sup', 'abbr', 'cite', 'q', 'time',
        // Lists
        'ul', 'ol', 'li', 'dl', 'dt', 'dd',
        // Tables
        'table', 'thead', 'tbody', 'tfoot', 'tr', 'th', 'td', 'caption', 'colgroup', 'col',
        // Forms & interactive
        'form', 'input', 'button', 'select', 'option', 'optgroup', 'textarea', 'label', 'fieldset', 'legend', 'output', 'datalist',
        // Media & embedded
        'img', 'picture', 'source', 'figure', 'figcaption', 'video', 'audio',
        // Links & resources
        'a', 'link',
        // Misc
        'details', 'summary', 'template', 'slot', 'data', 'meter', 'progress',
    ];

    private windowListeners: { eventName: string; handler: EventListener }[] = [];

    private cleanupWindowListeners() {
        this.windowListeners.forEach(({ eventName, handler }) => {
            Mfe.mfeEventBus.removeEventListener(eventName, handler);
        });
        this.windowListeners = [];
    }

    constructor() {
        super();
        this.attachShadow({ mode: 'open' });
    }

    connectedCallback() {
        this.mfeUrlResource = this.getAttribute("mfe-url");
        this.mfeName = this.getAttribute("mfe-name");
        this.mfeStylingUrl = this.getAttribute("mfe-styling-url");
        this.mfeListeningEventName = this.getAttribute("mfe-listen-event");
        this.mfeTriggerEventName = this.getAttribute("mfe-trigger-event");
        this.isMfeStreamingData = this.getAttribute("mfe-stream-data");
        if (this.mfeListeningEventName) {
            console.log(this.mfeListeningEventName);
            Mfe.mfeEventBus.addEventListener(this.mfeListeningEventName, this.bindReload);
        }

        if(!this.mfeUrlResource){
            console.error(`MFE -> ${this.mfeName} is missing the "mfe-url" attribute.`);
             return;
        }
        if(!this.mfeName) {
            console.error(`MFE -> ${this.mfeName} is missing the "mfe-name" attribute. The ready event will be dispatched with an undefined name.`);
            return;
        }
        if(!this.mfeTriggerEventName) {
            console.error(`MFE -> ${this.mfeName} is missing the "mfe-trigger-event" attribute. The fragment will not be able to trigger any custom events.`);
            // return;
        }
        if(!this.mfeListeningEventName) {
            console.error(`MFE -> ${this.mfeName} is missing the "mfe-listen-event" attribute. The fragment will not reload on any event.`);
            // return;
        }
        if(!this.mfeStylingUrl) {
            console.warn(`MFE -> ${this.mfeName} is missing the "mfe-styling-url" attribute. The fragment will not fetch any custom styling.`);
        }
        this.shadowRoot?.appendChild(document.createTextNode(`Loading ${this.mfeName}...`));
        Mfe.registry.set(this.mfeName, this);
        Mfe.pendingCallBacks.get(this.mfeName)?.forEach(cb => this.onInit(cb))
        Mfe.pendingCallBacks.delete(this.mfeName);

        this.loadFragment();
    }


    public onInit(callBackFn: (shadowRoot: MfeContext) => void): void {
        if(this.mfeReady && this.shadowRoot){
            callBackFn(this.createContext(this.shadowRoot));
        }
        else Mfe.mfeEventBus.addEventListener(this.mfeName + this.readyEventSuffix, () => {
            this.cleanupWindowListeners();
            callBackFn(this.createContext(this.shadowRoot))
        });
    };

    private createContext(root: ShadowRoot | null): MfeContext {
        // here I can add more functions and enrich the API
        return {
            root,
            triggerMfeEvent: (message: string, payload: unknown, eventName: string) => this.triggerEvent(eventName ?? this.mfeTriggerEventName, message, payload),
            listenMfeEvent: (listener, eventName) => {
                const evName = eventName ?? this.mfeListeningEventName;
                Mfe.mfeEventBus.addEventListener(evName, listener);
                // track it so we can remove it on next reload
                this.windowListeners.push({eventName: evName, handler: listener});
            },
            reloadMfe: () => this.reloadFragment(new CustomEvent("reload", { detail: { payload: {type: MfcEventType.RELOAD }} })),
            mfeEvents: MfcEventType
        }
    }

    public static onReady(mfeName: string, cbFn:(mfeCtx: MfeContext) => void): void {
        const mfeInstance = Mfe.registry.get(mfeName);
        if(mfeInstance){
           mfeInstance.onInit(cbFn);
        }else{
            const queue = Mfe.pendingCallBacks.get(mfeName) ?? [];
            queue.push(cbFn);
            Mfe.pendingCallBacks.set(mfeName, queue);
        }
    }

    private async fetchData() {
        this.abortController?.abort();
        this.abortController = new AbortController();
        if(this.mfeUrlResource){
            const response = await fetch(this.mfeUrlResource, {
                signal: this.abortController.signal,
            });
            if (!response.ok) {
                throw new Error(`Failed to fetch ${this.mfeUrlResource}: ${response.status}`);
            }
            return response.text();
        }
    }

    private async fetchStreamData() {
        const decoder = new TextDecoder();
        if(this.shadowRoot){
            this.shadowRoot.replaceChildren();
        }
        if(this.mfeUrlResource){
            const response = await fetch(this.mfeUrlResource);
            if (!response.ok) {
                throw new Error(`[micro-frontend "${this.mfeName}"] fetch failed: ${response.status}`);
            }
            if(response.body){
                for await (const value of response.body as any) {
                const chunk = decoder.decode(new Uint8Array(value), {stream: true});
                const node = this.createSafeHtml(chunk);

                const hostSelector = `[data-stream="host"]`;

                const host = node.querySelector(hostSelector);
                if(host) {
                    this.shadowRoot?.append(node.content);
                }else{
                    const streamnode = node.querySelector('[data-stream]');
                    if(streamnode){
                        this.shadowRoot?.querySelector(`[data-stream="${streamnode.getAttribute('data-stream')}"]`)?.append(streamnode);
                    }else{
                        this.shadowRoot?.append(node.content);
                    }
                }
            }
            }
        }
    }

    createSafeHtml(html:string) {
        const fragment = document.createElement('template');
        if('setHTML' in Element.prototype){
            (fragment as any).setHTML(html, { sanitizer: {
                elements: Mfe.ALLOWED_ELEMENTS
            }});
        }else{
            // here I could use the escape policy to sanitize the string or I could use DOMPurify or a similar library to sanitize the string before setting it as innerHTML
            // and support older browsers
            fragment.setHTMLUnsafe(html);
        }
        return fragment;
    }

    private async loadStylesheet(url: string): Promise<CSSStyleSheet> {
        const cached = Mfe.styleSheetCache.get(url);
        if (cached) return cached;
        const res = await fetch(url);
        const css = await res.text();
        const sheet = new CSSStyleSheet();
        sheet.replaceSync(css);
        Mfe.styleSheetCache.set(url, sheet);
        return sheet;
    }

    private buildFragment(html: string) {
        if(this.shadowRoot){
            this.shadowRoot.replaceChildren();
        }
        const fragment = this.createSafeHtml(html);
        if (this.mfeStylingUrl && this.shadowRoot) {
            this.loadStylesheet(this.mfeStylingUrl).then(cssSheet =>{
                if(this.shadowRoot && cssSheet){
                    this.shadowRoot.adoptedStyleSheets = [cssSheet];
                }
            }).catch(err => console.error(`Failed to load stylesheet for MFE -> ${this.mfeName}`, err));
        }
        // the idea here is to use shadowRoot.setHTML but its supported only by firefox so far.
        this.shadowRoot?.append(fragment.content);
    }

    public triggerEvent(
        eventName: string,
        message: string,
        payload: unknown
    ): void {
        const event = new CustomEvent(eventName, {
            detail: { message, payload }
        });
        Mfe.mfeEventBus.dispatchEvent(event); // the target is the microfrontend isntead of window, composed true allows for this.
    }

    private reloadFragment(event:Event){
        if(event instanceof CustomEvent){
            this.mfeReady = false;
            if(event.detail.payload.type === MfcEventType.RELOAD){
                this.loadFragment();
            }
        }else{
            console.warn(`MFE -> ${this.mfeName} received an event to reload, but the event is not a CustomEvent.`, event);
        }
    }

    private loadFragment() {
        if(this.isMfeStreamingData === "true"){
            this.fetchStreamData().then(r => console.info("Finished fetching stream!")).catch(err=> console.error(err));
        }
        else{
            this.fetchData().then(r => {
                if(r){
                    this.buildFragment(r);
                    this.mfeReady = true;
                    console.log("eventname: ", this.mfeName + this.readyEventSuffix)
                    Mfe.mfeEventBus.dispatchEvent(new Event(this.mfeName + this.readyEventSuffix));
                }
            }).catch(err => {
                this.shadowRoot?.appendChild(document.createTextNode(`Failed to fetch: ${this.mfeName}`));
                console.error(`An error occured for MFE -> ${this.mfeName}`, err)
            });
        }
    }


    disconnectedCallback() {
        this.cleanupWindowListeners();
        console.log(`MFE -> ${this.mfeName} removed from page.`);
        if(this.mfeListeningEventName) Mfe.mfeEventBus.removeEventListener(this.mfeListeningEventName, this.bindReload);
        this.abortController?.abort();
    }

    adoptedCallback() {
        console.log(`MFE -> ${this.mfeName} moved to new page.`);
    }

    attributeChangedCallback(name: string, oldValue: string | null, newValue: string | null) {
        console.log(`MFE -> attribute "${name}" changed from "${oldValue}" to "${newValue}"`);

        // if (name === 'mfe-url' && oldValue !== newValue) {
        //     this.mfeUrlResource = newValue;
        //     this.loadFragment();
        // }
    }

    // a different way
    public mfe(name:string, callback: (mfeContext: MfeContext) => void) {
        Mfe.onReady(name, callback);
    }

}

window.customElements.define('micro-frontend', Mfe);
window.mfe = (name, callbackFn) => Mfe.onReady(name, callbackFn);
export default Mfe;