console.log("Loading script of mfe1!")

const mfeTriggerAddEvent = 'triggerAddEvent';
const mfeShellEventName = 'htmx:afterSwap';

const mfeElement = document.querySelector('[mfe-name="mfe1"]');
mfeElement.addEventListener(mfeShellEventName, (event) => {
    console.log('Micro-frontend content loaded and swapped!');
    // You can run post-load logic here
    const btn = mfeElement.querySelectorAll('button')
    btn?.forEach(button => {
        button.addEventListener('click', () => {
            console.log('trigger cart event triggered on click')
            mfeElement.triggerEvent(mfeTriggerAddEvent, `add to cart bike with ${button.getAttribute("mfe-id")}`, {type: 'add', id: button.getAttribute("mfe-id")})
        });
    })
});
