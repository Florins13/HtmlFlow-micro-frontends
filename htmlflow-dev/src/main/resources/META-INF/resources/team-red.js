import Base from "./base.js";

class TeamRed extends Base {
    constructor() {
        super();
    }
    fetchData(){
        fetch(this.MFE_URL_RESOURCE).then(data =>{
            data.text().then(data => {
                this.innerHTML = data;
                const button = this.querySelector('#triggerOrder');
                button.addEventListener('click', () => this.buttonTriggerOrders());
            });
        }, ()=> this.innerHTML = "This team is unavailable!")
    }

    async buttonTriggerOrders() {
        const endpoint = `http://localhost:8080/order/finalise`;
        const payload = {};
        try {
            const response = await fetch(endpoint, {
                method: 'POST',
                headers: {
                    'Content-Type': 'application/json'
                },
                body: JSON.stringify({})
            });

            if (response.ok) {
                // Dispatch a custom event named "postSuccess"
                const event = new CustomEvent('triggerOrderEvent', {
                    detail: { message: 'Post succeeded', payload },
                    bubbles: true,    // Allows the event to bubble up the DOM tree
                    composed: true    // Allows the event to cross shadow DOM boundaries
                });
                this.dispatchEvent(event);
            } else {
                console.error('POST failed with status:', response.status);
            }
        } catch (error) {
            console.error('Error during POST:', error);
        }
    }
}

export default TeamRed;

