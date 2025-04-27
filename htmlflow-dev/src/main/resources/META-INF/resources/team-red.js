import Base from "./base.js";

class TeamRed extends Base {
    constructor() {
        super();
    }
    fetchData(){
        fetch(this.MFE_URL_RESOURCE).then(data =>{
            data.text().then(data => {
                this.innerHTML = data;
                this.buildButtons();

            });
        }, ()=> this.innerHTML = "This team is unavailable!")
    }

    buildButtons() {
        const checkoutButton = this.querySelector('#triggerOrder');
        const increaseQuantity = this.querySelectorAll('#increaseQuantity');
        const decreaseQuantity = this.querySelectorAll('#decreaseQuantity');
        const deleteCartItem = this.querySelectorAll('#deleteCartItem');
        if (checkoutButton) {
            checkoutButton.addEventListener('click', () => this.buttonTriggerOrders());
        }
        if (increaseQuantity && decreaseQuantity && deleteCartItem) {
            increaseQuantity.forEach(button => button.addEventListener('click', () => this.handleQuantity('increase', button.getAttribute("value"))));
            decreaseQuantity.forEach(button => button.addEventListener('click', () => this.handleQuantity('decrease', button.getAttribute("value"))));
            deleteCartItem.forEach(button => button.addEventListener('click', () => this.deleteItem(button.getAttribute("value"))));
        }
    }

    async buttonTriggerOrders() {
        const endpoint = `http://localhost:8080/order/finalise`;
        try {
            const response = await fetch(endpoint, {
                method: 'POST',
                headers: {
                    'Content-Type': 'application/json'
                },
                body: JSON.stringify({})
            });
            this.triggerEvent(response, 'triggerOrderEvent');
        } catch (error) {
            console.error('Error during POST:', error);
        }
    }

    async handleQuantity(type, id) {
        const endpoint = `http://localhost:8080/cart/updateQuantity/${id}/${type}`;
        try {
            const response = await fetch(endpoint, {
                method: 'POST'
            });

            this.triggerEvent(response, 'triggerCartEvent');
        } catch (error) {
            console.error('Error during POST:', error);
        }
    }

    async deleteItem(id) {
        const endpoint = `http://localhost:8080/cart/deleteItem/${id}`;
        try {
            const response = await fetch(endpoint, {
                method: 'POST'
            });
        this.triggerEvent(response, 'triggerCartEvent');

        } catch (error) {
            console.error('Error during POST:', error);
        }
    }

    triggerEvent(response, eventName){
        if (response.ok) {
            // Dispatch a custom event named "postSuccess"
            const event = new CustomEvent(eventName, {
                detail: { message: 'Post succeeded' },
                bubbles: true,    // Allows the event to bubble up the DOM tree
                composed: true    // Allows the event to cross shadow DOM boundaries
            });
            this.dispatchEvent(event);
        } else {
            console.error('POST failed with status:', response.status);
        }
    }
}

export default TeamRed;

