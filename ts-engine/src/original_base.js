class Mfe extends HTMLElement {
    mfeName = "";
    mfeUrlResource = "";
    mfeListeningEventName = "";
    mfeTriggerEventName = "";
    readyEventSuffix = "-fragment-ready";
    mfeStylingUrl = "";
    isMfeStreamingData = "";

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
        window.addEventListener(this.mfeListeningEventName, this.reloadFragment.bind(this));
        this.loadFragment();
    }

    async fetchData() {
        const response = await fetch(this.mfeUrlResource);
        if (!response.ok) {
            throw new Error(`Failed to fetch ${this.mfeUrlResource}: ${response.status}`);
        }
        return response.text();
    }

    async fetchStreamData() {
        this.wrapper = document.createElement('div');
        const response = await fetch(this.mfeUrlResource);
        for await (const value of response.body) {
            const uint8Array = new Uint8Array(value);
            const chunk = new TextDecoder().decode(uint8Array);
            this.wrapper.insertAdjacentHTML('beforeend', chunk);
            this.shadowRoot.appendChild(this.wrapper);
        }
    }

    buildFragment(html) {
        const parser = new DOMParser();
        const doc = parser.parseFromString(html, 'text/html');
        this.shadowRoot.innerHTML = null;

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

        return this.shadowRoot.appendChild(fragment);
    }

    triggerEvent(eventName, message, payload, bubbles = true, composed = true){
        const event = new CustomEvent(eventName, {
            detail: { message: message , payload},
            bubbles: bubbles,    // Allows the event to bubble up the DOM tree
            composed: composed    // Allows the event to cross shadow DOM boundaries
        });
        dispatchEvent(event);
    }

    reloadFragment(){
        this.loadFragment();
    }

    loadFragment() {
        if(this.isMfeStreamingData === "true"){
            this.fetchStreamData().then(r => console.info("Finished fetching stream!")).catch(err=> console.error(err));
        }
        else{
            this.fetchData().then(r => {
                this.buildFragment(r);
                console.log("eventname: ", this.mfeName + this.readyEventSuffix)
                this.dispatchEvent(new Event(this.mfeName + this.readyEventSuffix, { bubbles: true, composed: true }));
            });
        }
    }

    disconnectedCallback() {
        console.log("Custom element removed from page.");
    }

    adoptedCallback() {
        console.log("Custom element moved to new page.");
    }

    attributeChangedCallback(name, oldValue, newValue) {
        console.log(`Attribute ${name} has changed.`);
    }
}

window.customElements.define('micro-frontend', Mfe);