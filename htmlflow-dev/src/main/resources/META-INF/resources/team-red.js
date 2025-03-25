class TeamRed extends HTMLElement {
    test = '';
    constructor() {
        super();
    }

    connectedCallback() {
        window.addEventListener('triggerCartEvent', this.fetchData.bind(this))
        this.test = this.getAttribute("url");
        console.log(this.test);
        if(this.test) {
            this.fetchData();
        }
    }

    disconnectedCallback() {
        console.log("Custom element removed from page.");
    }

    adoptedCallback() {
        console.log("Custom element moved to new page.");
    }

    attributeChangedCallback(name, oldValue, newValue) {
        console.log(`Attribute ${name} has changed.`);
    }

    fetchData(){
        let url_resource = "http://localhost:8080/cart";
        console.log("fetching data from cart")
        fetch(url_resource).then(data =>{
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

function goToCheckout(){
    window.location.replace(window.location.origin + "/cart/checkout");
}

export default TeamRed;

