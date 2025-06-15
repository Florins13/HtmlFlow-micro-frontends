console.log("Loading script of mfe3!")


const mfeTriggerBikeEvent = "triggerBikeEvent";
const mfeTriggerCartEvent = "triggerCartEvent";
const mfeListenOrderEvent = 'triggerOrderEvent';

const mfeElement = document.querySelector('[mfe-name="mfe3"]');

window.addEventListener(mfeListenOrderEvent, (e) => {
    mfeElement.triggerEvent(mfeTriggerBikeEvent, 'trigger bike event', {});
    mfeElement.triggerEvent(mfeTriggerCartEvent, 'trigger cart event', {});
});