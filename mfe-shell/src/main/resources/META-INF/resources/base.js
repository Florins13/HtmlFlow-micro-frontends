class Mfe extends HTMLElement {

    constructor() {
        super();
        // this.attachShadow({ mode: 'open' }); htmx doesnt support shadow dom well.
    }

    connectedCallback() {}

    triggerEvent(eventName, message, payload, bubbles = true, composed = true){
        const event = new CustomEvent(eventName, {
            detail: { message: message , payload},
            bubbles: bubbles,    // Allows the event to bubble up the DOM tree
            composed: composed    // Allows the event to cross shadow DOM boundaries
        });
        document.body.dispatchEvent(event);
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