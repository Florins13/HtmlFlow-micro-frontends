// import Base from "./base.js";

// class TeamGreen extends Base {
//     constructor() {
//         super();
//     }
//
//     connectedCallback() {
//         super.connectedCallback();
//         this.reloadFragment();
//     }
//
//     // TODO: event is always triggered without a post or get, just event triggered, needs to have a type and optional ids or something.
//
//     reloadFragment(){
//         this.fetchData().then(r => {
//             this.buildFragment(r);
//             const triggerBikeEvent = new CustomEvent(this.MFE_TRIGGERS_EVENT_NAME, {
//                 detail: { message: 'Post succeeded' },
//                 bubbles: true,    // Allows the event to bubble up the DOM tree
//                 composed: true    // Allows the event to cross shadow DOM boundaries
//             });
//             this.dispatchEvent(triggerBikeEvent);
//             const triggerCartEvent = new CustomEvent('triggerCartEvent', {
//                 detail: { message: 'Post succeeded' },
//                 bubbles: true,    // Allows the event to bubble up the DOM tree
//                 composed: true    // Allows the event to cross shadow DOM boundaries
//             });
//             this.dispatchEvent(triggerCartEvent);
//         });
//     }
// }

// export default TeamGreen;

