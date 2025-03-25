class TeamBlack extends HTMLElement {
    urlResource = '';
    constructor() {
        super();
    }

    connectedCallback() {
        window.addEventListener('triggerBikeList', this.fetchData.bind(this))
        this.urlResource = this.getAttribute("url");
        if(this.urlResource) {
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

    async handleButtonClick(id) {
        let myId = id.replace("cenas","")
        const endpoint = `http://localhost:8080/cart/add/${myId}`; // Replace with your POST endpoint
        const payload = { id: myId }; // Adjust payload as needed

        try {
            const response = await fetch(endpoint, {
                method: 'POST',
                headers: {
                    'Content-Type': 'application/json'
                },
                body: JSON.stringify(payload)
            });

            if (response.ok) {
                // Dispatch a custom event named "postSuccess"
                const event = new CustomEvent('triggerCartEvent', {
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

    fetchData(){
        let url_resource = this.urlResource;
        fetch(url_resource).then(data =>{
            if(url_resource.includes("4200")){
                data.text()
                    .then(htmlString => {
                        // Create a container element for the fetched HTML
                        const parser = new DOMParser();
                        const doc = parser.parseFromString(htmlString, 'text/html');
                        this.innerHTML = null;
                        this.insertAdjacentElement("beforeend",doc.body);
                        const button = this.querySelectorAll('button');
                        button.forEach(button=>{
                            if (button) {
                                // Attach a click event listener to the button
                                button.addEventListener('click', () => this.handleButtonClick(button.getAttribute("id")));
                            } else {
                                console.warn("Button not found!", this);
                            }
                        })
                    })
                    .catch(() => this.innerHTML = "This team is unavailable!");
            }
            else{
                data.text().then(data => this.innerHTML = data);
            }

        }, ()=> this.innerHTML = "This team is unavailable!")
    }

}

function goToCheckout(){
    window.location.replace(window.location.origin + "/cart/checkout");
}

export default TeamBlack;

