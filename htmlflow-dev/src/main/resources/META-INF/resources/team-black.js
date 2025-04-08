import Base from "./base.js";

class TeamBlack extends Base {
    constructor() {
        super();
    }

    async handleButtonClick(id) {
        const endpoint = `http://localhost:8080/cart/add/${id}`; // post
        const payload = { id: id };
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
                const event = new CustomEvent(this.MFE_TRIGGERS_EVENT_NAME, {
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
        fetch(this.MFE_URL_RESOURCE).then(data =>{
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
                            button.addEventListener('click', () => this.handleButtonClick(button.getAttribute("mfe-id")));
                        } else {
                            console.warn("Button not found!", this);
                        }
                    })
                })
                .catch(() => this.innerHTML = "This team is unavailable!");
        }, ()=> this.innerHTML = "This team is unavailable!")
    }

}

export default TeamBlack;

