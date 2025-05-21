console.log("Loading script of mfe1!")

const MFE_TRIGGERS_EVENT_NAME = "triggerCartEvent";

function triggerEvent(eventName, message, payload, bubbles = true, composed = true){
    const event = new CustomEvent(eventName, {
        detail: { message: message , payload},
        bubbles: bubbles,    // Allows the event to bubble up the DOM tree
        composed: composed    // Allows the event to cross shadow DOM boundaries
    });
    dispatchEvent(event);
}

document.addEventListener('DOMContentLoaded', () => {

    console.log("DOM fully loaded and parsed");
});
window.addEventListener('fragment-ready', () => {

    console.log("fragment ready from micro");
    // const btn = document.querySelector('micro-frontend').shadowRoot.querySelector('button')
    // btn.addEventListener('click', () => {
    //     console.log('clicked')
    //     triggerEvent(MFE_TRIGGERS_EVENT_NAME, `add to cart bike with ${btn.getAttribute("mfe-id")}`, {type: 'add', id: btn.getAttribute("mfe-id")})
    // });
});


customElements.whenDefined('micro-frontend').then(() => {
    console.log("defined micro-frontend")
    const btn = document.querySelector('micro-frontend').shadowRoot.querySelector('button')
    btn.addEventListener('click', () => {
        console.log('clicked')
        triggerEvent(MFE_TRIGGERS_EVENT_NAME, `add to cart bike with ${btn.getAttribute("mfe-id")}`, {type: 'add', id: btn.getAttribute("mfe-id")})
    });

});

// function buttonEmitMfeEvent(id){
//     console.log("bike id event trigger", id);
//
// }

// window.buttonEmitMfeEvent = buttonEmitMfeEvent;