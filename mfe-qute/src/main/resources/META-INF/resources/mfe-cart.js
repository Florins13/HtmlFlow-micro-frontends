console.log("Loading script of mfe2!")


const mfeTriggerOrderEvent = "triggerOrderEvent";
const mfeTriggerCartEvent = 'triggerCartEvent';
const mfeListenAddEvent = 'triggerAddEvent';
const mfeShellEventName = 'mfe2-fragment-ready';
let mfeElement = {};

window.addEventListener(mfeShellEventName, (e) => {
    buildButtons();
});

window.addEventListener(mfeListenAddEvent, (e) => {
    if(e.detail?.payload?.type === 'add'){
        addBikeToCart(e.detail?.payload?.id);
    }
});


function buildButtons() {
    mfeElement = document.querySelector('[mfe-name="mfe2"]');
    const checkoutButton = mfeElement.shadowRoot.querySelector('#triggerOrder');
    const increaseQuantity = mfeElement.shadowRoot.querySelectorAll('#increaseQuantity');
    const decreaseQuantity = mfeElement.shadowRoot.querySelectorAll('#decreaseQuantity');
    const deleteCartItem = mfeElement.shadowRoot.querySelectorAll('#deleteCartItem');
    if (checkoutButton) {
        checkoutButton.addEventListener('click', () => finalizeOrder().then(() => mfeElement.triggerEvent(mfeTriggerOrderEvent, 'post success', { type: 'reload'})));
    }
    if (increaseQuantity && decreaseQuantity && deleteCartItem) {
        increaseQuantity.forEach(button => button.addEventListener('click', () => handleQuantity('increase', button.getAttribute("value"))));
        decreaseQuantity.forEach(button => button.addEventListener('click', () => handleQuantity('decrease', button.getAttribute("value"))));
        deleteCartItem.forEach(button => button.addEventListener('click', () => deleteItem(button.getAttribute("value"))));
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
async function makeApiCall({ endpoint, method = 'GET', payload = null, eventName = null }) {
    const url = `http://localhost:8080${endpoint}`;

    try {
        const requestOptions = {
            method,
            headers: {}
        };

        const response = await fetch(url, requestOptions);

        if (eventName && response.ok) {
            mfeElement.triggerEvent(eventName, 'Post success', {});
        } else if (!response.ok) {
            console.error('API call failed with status:', response.status);
        }

        return response;
    } catch (error) {
        console.error(`Error during ${method} to ${url}:`, error);
        throw error;
    }

}

async function finalizeOrder() {
    await makeApiCall({
        endpoint: '/order/finalise',
        method: 'POST',
        payload: {}
    });
}

async function handleQuantity(type, id) {
    await makeApiCall({
        endpoint: `/cart/updateQuantity/${id}/${type}`,
        method: 'POST',
        eventName: mfeTriggerCartEvent
    });
}

async function deleteItem(id) {
    await makeApiCall({
        endpoint: `/cart/deleteItem/${id}`,
        method: 'POST',
        eventName: mfeTriggerCartEvent
    });
}

async function addBikeToCart(id) {
    await makeApiCall({
        endpoint: `/cart/add/${id}`,
        method: 'POST',
        eventName: mfeTriggerCartEvent
    });
}