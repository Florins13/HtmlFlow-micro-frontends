import Base from "./base.js";

class TeamGreen extends Base {
    constructor() {
        super();
    }

    fetchData(){
        fetch(this.MFE_URL_RESOURCE).then(data =>{
            data.text().then(data => {
                this.innerHTML = data;
                const triggerBikeEvent = new CustomEvent(this.MFE_TRIGGERS_EVENT_NAME, {
                    detail: { message: 'Post succeeded' },
                    bubbles: true,    // Allows the event to bubble up the DOM tree
                    composed: true    // Allows the event to cross shadow DOM boundaries
                });
                this.dispatchEvent(triggerBikeEvent);
                const triggerCartEvent = new CustomEvent('triggerCartEvent', {
                    detail: { message: 'Post succeeded' },
                    bubbles: true,    // Allows the event to bubble up the DOM tree
                    composed: true    // Allows the event to cross shadow DOM boundaries
                });
                this.dispatchEvent(triggerCartEvent);

            });
        }, ()=> this.innerHTML = "This team is unavailable!")
    }
}

export default TeamGreen;

