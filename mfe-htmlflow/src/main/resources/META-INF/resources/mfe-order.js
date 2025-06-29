console.log("Loading script of mfe3!")

let mfeElement = {};

const mfeTriggerBikeEvent = "triggerBikeEvent";
const mfeTriggerCartEvent = "triggerCartEvent";
const mfeListenOrderEvent = 'triggerOrderEvent';
const mfeShellEventName = 'mfe3-fragment-ready';

window.addEventListener(mfeShellEventName, (e) => {
    mfeElement = document.querySelector('[mfe-name="mfe3"]');
});

window.addEventListener(mfeListenOrderEvent, (e) => {
    mfeElement.triggerEvent(mfeTriggerBikeEvent, 'trigger bike event', {});
    mfeElement.triggerEvent(mfeTriggerCartEvent, 'trigger cart event', {});
});