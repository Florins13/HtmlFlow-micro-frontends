console.log("Loading script of mfe1!")

const mfeTriggerAddEvent = 'triggerAddEvent';
const mfeShellEventName = 'mfe1-fragment-ready';


window.addEventListener(mfeShellEventName, () => {
    const mfeElement = document.querySelector('[mfe-name="mfe1"]');
    const btn = mfeElement.shadowRoot.querySelectorAll('button')
    btn?.forEach(button => {
        button.addEventListener('click', () => {
            console.log('trigger cart event triggered on click')
            mfeElement.triggerEvent(mfeTriggerAddEvent, `add to cart bike with ${button.getAttribute("mfe-id")}`, {type: 'add', id: button.getAttribute("mfe-id")})
        });
    })
});