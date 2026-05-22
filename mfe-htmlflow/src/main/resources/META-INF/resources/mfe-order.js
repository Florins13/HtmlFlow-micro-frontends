console.log("Loading script of mfe3!")

const mfeTriggerBikeEvent = "triggerBikeEvent";
const mfeTriggerCartEvent = "triggerCartEvent";

mfe("mfe3", ({triggerMfeEvent, listenMfeEvent})=> {
    listenMfeEvent(() => {
        triggerMfeEvent('trigger bike event', {type: 'reload'}, mfeTriggerBikeEvent);
        triggerMfeEvent('trigger cart event', {type: 'reload'}, mfeTriggerCartEvent);
    });
})