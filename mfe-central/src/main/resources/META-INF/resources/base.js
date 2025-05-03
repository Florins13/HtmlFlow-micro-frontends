export class Base extends HTMLElement {
    MFE_URL_RESOURCE = "";
    MFE_LISTENING_EVENT_NAME = "";
    MFE_TRIGGERS_EVENT_NAME = "";

    constructor() {
        super();
        this.attachShadow({ mode: 'open' });
    }

    connectedCallback() {
        this.MFE_URL_RESOURCE = this.getAttribute("mfe-url");
        this.MFE_LISTENING_EVENT_NAME = this.getAttribute("mfe-listen-event");
        this.MFE_TRIGGERS_EVENT_NAME = this.getAttribute("mfe-trigger-event");
        document.addEventListener(this.MFE_LISTENING_EVENT_NAME, this.reloadFragment.bind(this));
    }

    async fetchData() {
        const response = await fetch(this.MFE_URL_RESOURCE);
        if (!response.ok) {
            throw new Error(`Failed to fetch ${this.MFE_URL_RESOURCE}: ${response.status}`);
        }
        return response.text();
    }

    buildFragment(html) {
        const parser = new DOMParser();
        const doc = parser.parseFromString(html, 'text/html');
        this.shadowRoot.innerHTML = null;
        const fragment = document.createDocumentFragment();
        doc.body.childNodes.forEach(child => {
            fragment.append(child)
        })
        return this.shadowRoot.appendChild(fragment);
    }

    triggerEvent(eventName, message, payload, bubbles = true, composed = true){
        const event = new CustomEvent(eventName, {
            detail: { message: message , payload},
            bubbles: bubbles,    // Allows the event to bubble up the DOM tree
            composed: composed    // Allows the event to cross shadow DOM boundaries
        });
        this.dispatchEvent(event);
    }

    reloadFragment(){
        throw new Error("You need to load implement this method in order to load the fragment!")
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



export default Base;




