/*
    This to discuss if there should be a script that pregenerates the needed javascript files.
 */
class Base extends HTMLElement {
    MFE_URL_RESOURCE = "";
    MFE_LISTENING_EVENT_NAME = "";
    MFE_TRIGGERS_EVENT_NAME = "";
    constructor() {
        super();
    }

    connectedCallback() {
        this.MFE_URL_RESOURCE = this.getAttribute("mfe-url");
        this.MFE_LISTENING_EVENT_NAME = this.getAttribute("mfe-listen-event");
        this.MFE_TRIGGERS_EVENT_NAME = this.getAttribute("mfe-trigger-event");
        window.addEventListener(this.MFE_LISTENING_EVENT_NAME, this.fetchData.bind(this));
        if(this.MFE_URL_RESOURCE) {
            this.fetchData();
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

export default Base;

