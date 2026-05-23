interface MfeContext {
    root: ShadowRoot | null;
    triggerMfeEvent: (message: string, payload: unknown, eventName: string, ) => void;
    listenMfeEvent: (listener: EventListener, eventName: string) => void;
    reloadMfe: () => void;
}

declare global {
    interface Window {
        mfe: (name: string, callback: (mfeContext: MfeContext) => void) => void;
    }
}

class Mfe extends HTMLElement {
    private mfeName: string | null = "";
    private mfeUrlResource: string | null = "";
    private mfeListeningEventName: string | null = "";
    private mfeTriggerEventName: string | null = "";
    private readyEventSuffix: string = "-fragment-ready";
    private mfeStylingUrl: string | null = "";
    private isMfeStreamingData: string | null = "";
    private abortController: AbortController | null = null;
    private bindReload = this.reloadFragment.bind(this); // prevent new reference
    public mfeReady: boolean = false;
    private static registry = new Map<string, Mfe>();
    private static pendingCallBacks = new Map<string, ((mfeRoot: MfeContext)=>void)[]>();


    private windowListeners: { eventName: string; handler: EventListener }[] = [];

    private cleanupWindowListeners() {
        this.windowListeners.forEach(({ eventName, handler }) => {
            window.removeEventListener(eventName, handler);
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
            window.addEventListener(this.mfeListeningEventName, this.bindReload);
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
        else this.addEventListener(this.mfeName + this.readyEventSuffix, () => {
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
                const name = eventName ?? this.mfeListeningEventName;
                // register on window
                window.addEventListener(name, listener);
                // track it so we can remove it on next reload
                this.windowListeners.push({eventName: name, handler: listener});
            },
            reloadMfe: () => this.reloadFragment(new CustomEvent("reload", { detail: { payload: {type:"reload" }} }))
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

    private toFragment(html:string) {
        const template = document.createElement('template');

        template.innerHTML = html;

        return template.content;
    }

    private async fetchStreamData() {
        const decoder = new TextDecoder();
        if(this.shadowRoot){
            this.shadowRoot.innerHTML = "";
        }
        if(this.mfeUrlResource){
            const response = await fetch(this.mfeUrlResource);
            if (!response.ok) {
                throw new Error(`[micro-frontend "${this.mfeName}"] fetch failed: ${response.status}`);
            }
            if(response.body){
                for await (const value of response.body as any) {
                const chunk = decoder.decode(new Uint8Array(value), {stream: true});
                const doc = this.toFragment(chunk);

                const hostSelector = `[data-stream="host"]`;

                const host = doc.querySelector(hostSelector);
                if(host) {
                    this.shadowRoot?.appendChild(doc);
                }else{
                    const streamnode = doc.querySelector('[data-stream]');
                    if(streamnode){
                        this.shadowRoot?.querySelector(`[data-stream="${streamnode.getAttribute('data-stream')}"]`)?.append(streamnode);
                    }else{
                        this.shadowRoot?.appendChild(doc);
                    }
                }
            }
            }
        }
    }

    private buildFragment(html: string) {
        const parser = new DOMParser();
        const doc = parser.parseFromString(html, 'text/html');
        if(this.shadowRoot){
            this.shadowRoot.innerHTML = "";
        }
        const fragment = document.createDocumentFragment();
        const link = document.createElement('link');
        if(this.mfeStylingUrl){
            link.setAttribute('rel', 'stylesheet');
            link.setAttribute('href', this.mfeStylingUrl);
            fragment.append(link);
        }
        doc.body.childNodes.forEach(child => {
            fragment.append(child)
        });

        return this.shadowRoot?.appendChild(fragment);
    }

    public triggerEvent(
        eventName: string,
        message: string,
        payload: unknown,
        bubbles: boolean = true,
        composed: boolean = true
    ): void {
        const event = new CustomEvent(eventName, {
            detail: { message, payload },
            bubbles,
            composed
        });
        this.dispatchEvent(event); // the target is the microfrontend isntead of window, composed true allows for this.
    }

    private reloadFragment(event:Event){
        if(event instanceof CustomEvent){
            this.mfeReady = false;
            if(event.detail.payload.type === "reload"){
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
                    this.dispatchEvent(new Event(this.mfeName + this.readyEventSuffix, { bubbles: true, composed: true }));
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
        if(this.mfeListeningEventName) window.removeEventListener(this.mfeListeningEventName, this.bindReload);
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
}

window.customElements.define('micro-frontend', Mfe);
window.mfe = (name, callbackFn) => Mfe.onReady(name, callbackFn);
export default Mfe;