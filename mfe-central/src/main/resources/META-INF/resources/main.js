import Base from "./base.js";

export class TeamBlack extends Base {
    constructor() {
        super();
    }

    connectedCallback() {
        super.connectedCallback();
        this.loadFragment();
    }

    reloadFragment(){
        this.loadFragment();
    }

    loadFragment() {
        this.fetchData().then(r => {
            this.buildFragment(r);
            const button = this.shadowRoot.querySelectorAll('button');
            button.forEach(button => {
                if (button) {
                    // Attach a click event listener to the button
                    button.addEventListener('click', () => this.buttonEmitMfeEvent(button.getAttribute("mfe-id")));
                } else {
                    console.warn("Button not found!", this);
                }
            })
        });
    }

    buttonEmitMfeEvent(id){
        this.triggerEvent(this.MFE_TRIGGERS_EVENT_NAME, `add to cart bike with ${id}`, {type: 'add', id: id})
    }
}

export class TeamRed extends Base {
    constructor() {
        super();
    }

    connectedCallback() {
        super.connectedCallback();
        this.loadFragment();
    }

    loadFragment() {
        this.fetchData().then(r => {
            this.buildFragment(r);
            this.buildButtons();
        });
    }

    reloadFragment(e){
        if(e.detail?.payload?.type === 'add'){
            this.addBikeToCart(e.detail?.payload?.id).then(test => {
                this.triggerEvent('triggerCartEvent', 'post success', {})
            })
            return;
        }
        this.loadFragment();
    }

    buildButtons() {
        const checkoutButton = this.shadowRoot.querySelector('#triggerOrder');
        const increaseQuantity = this.shadowRoot.querySelectorAll('#increaseQuantity');
        const decreaseQuantity = this.shadowRoot.querySelectorAll('#decreaseQuantity');
        const deleteCartItem = this.shadowRoot.querySelectorAll('#deleteCartItem');
        if (checkoutButton) {
            checkoutButton.addEventListener('click', () => this.buttonTriggerOrders());
        }
        if (increaseQuantity && decreaseQuantity && deleteCartItem) {
            increaseQuantity.forEach(button => button.addEventListener('click', () => this.handleQuantity('increase', button.getAttribute("value"))));
            decreaseQuantity.forEach(button => button.addEventListener('click', () => this.handleQuantity('decrease', button.getAttribute("value"))));
            deleteCartItem.forEach(button => button.addEventListener('click', () => this.deleteItem(button.getAttribute("value"))));
        }
    }

    /**
     * Makes an API call to the specified endpoint
     * @param {Object} options - Configuration options
     * @param {string} options.endpoint - The API endpoint to call (will be appended to the base URL)
     * @param {string} options.method - HTTP method (GET, POST, PUT, DELETE)
     * @param {Object} [options.payload] - Optional request body payload
     * @param {string} [options.eventName] - Optional event name to trigger on success
     * @returns {Promise<Response>} - The fetch response
     */
    async makeApiCall({ endpoint, method = 'GET', payload = null, eventName = null }) {
        const url = `http://localhost:8080${endpoint}`;

        try {
            const requestOptions = {
                method,
                headers: {}
            };

            const response = await fetch(url, requestOptions);

            if (eventName && response.ok) {
                this.triggerEvent(eventName, 'Post success', {});
            } else if (!response.ok) {
                console.error('API call failed with status:', response.status);
            }

            return response;
        } catch (error) {
            console.error(`Error during ${method} to ${url}:`, error);
            throw error;
        }

    }

    async buttonTriggerOrders() {
        await this.makeApiCall({
            endpoint: '/order/finalise',
            method: 'POST',
            payload: {},
            eventName: 'triggerOrderEvent'
        });
    }

    async handleQuantity(type, id) {
        await this.makeApiCall({
            endpoint: `/cart/updateQuantity/${id}/${type}`,
            method: 'POST',
            eventName: 'triggerCartEvent'
        });
    }

    async deleteItem(id) {
        await this.makeApiCall({
            endpoint: `/cart/deleteItem/${id}`,
            method: 'POST',
            eventName: 'triggerCartEvent'
        });
    }

    async addBikeToCart(id) {
        await this.makeApiCall({
            endpoint: `/cart/add/${id}`,
            method: 'POST'
        });
    }

}

export class TeamGreen extends Base {
    constructor() {
        super();
    }

    connectedCallback() {
        super.connectedCallback();
        this.loadFragment();
    }

    reloadFragment(){
        this.loadFragment();
    }

    loadFragment() {
        this.fetchData().then(r => {
            this.buildFragment(r);
            this.triggerEvent(this.MFE_TRIGGERS_EVENT_NAME, 'bike event', {});
            // trigger the cart event to fetch the cart after successfull transaction
            this.triggerEvent('triggerCartEvent', 'cart event', {});
        });
    }
}


window.customElements.define('team-black', TeamBlack);
window.customElements.define('team-red', TeamRed);
window.customElements.define('team-green', TeamGreen);

