class TeamGreen extends HTMLElement {
    test = '';
    constructor() {
        super();
    }

    connectedCallback() {
        window.addEventListener('triggerOrderEvent', this.fetchData.bind(this))
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
        let url_resource = "http://localhost:8080/order/history";
        console.log("fetching data order history");
        fetch(url_resource).then(data =>{
            data.text().then(data => {
                this.innerHTML = data;
                const event = new CustomEvent('triggerCartEvent', {
                    detail: { message: 'Post succeeded' },
                    bubbles: true,    // Allows the event to bubble up the DOM tree
                    composed: true    // Allows the event to cross shadow DOM boundaries
                });
                this.dispatchEvent(event);
                const refreshBikeList = new CustomEvent('triggerBikeList', {
                    detail: { message: 'Post succeeded' },
                    bubbles: true,    // Allows the event to bubble up the DOM tree
                    composed: true    // Allows the event to cross shadow DOM boundaries
                });
                this.dispatchEvent(refreshBikeList);
            });
        }, ()=> this.innerHTML = "This team is unavailable!")
    }
}

export default TeamGreen;

