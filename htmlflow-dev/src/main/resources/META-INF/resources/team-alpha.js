class TeamAlpha extends HTMLElement {
    constructor() {
        super();
    }

    connectedCallback() {
        const url_resource = this.getAttribute("url");
        console.log(url_resource);
        if(url_resource){
            fetch(url_resource).then(data =>{
                data.text().then(data => this.innerHTML = data);
            }, ()=> this.innerHTML = "This team is unavailable!")
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
}

export default TeamAlpha;