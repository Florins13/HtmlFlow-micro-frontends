class TeamOmega extends HTMLElement {
    test = '';
    constructor() {
        super();
    }

    connectedCallback() {
        window.addEventListener('postSuccess', this.fetchData.bind(this))
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
                    const parser = new DOMParser();
                    const doc = parser.parseFromString(data, 'text/html');
                    const node = doc; // or .firstElementChild
                    console.log(node);
                    this.innerHTML = data;
                });
        }, ()=> this.innerHTML = "This team is unavailable!")
    }

}

function goToCheckout(){
    window.location.replace(window.location.origin + "/cart/checkout");
}

export default TeamOmega;

