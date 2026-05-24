console.log("Loading script of mfe3!")

const mfeTriggerBikeEvent = "triggerBikeEvent";
const mfeTriggerCartEvent = "triggerCartEvent";

mfe("mfe3", ({triggerMfeEvent, listenMfeEvent, mfeEvents})=> {
    listenMfeEvent(() => {
        triggerMfeEvent('trigger bike event', {type: mfeEvents.RELOAD}, mfeTriggerBikeEvent);
        triggerMfeEvent('trigger cart event', {type: mfeEvents.RELOAD}, mfeTriggerCartEvent);
    });
})