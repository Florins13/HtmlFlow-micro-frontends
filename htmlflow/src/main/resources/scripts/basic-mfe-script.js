import Base from "./base";

class BaseMfeScript extends Base {
    constructor() {
        super();
    }

    fetchData(){
        fetch(this.MFE_URL_RESOURCE).then(data =>{
            data.text().then(htmlString => {
                const parser = new DOMParser();
                const doc = parser.parseFromString(htmlString, 'text/html');
                this.innerHTML = null;
                this.insertAdjacentElement("beforeend",doc.body);
                const event = new CustomEvent(this.MFE_TRIGGERS_EVENT_NAME, {
                    detail: { message: 'Fetching succeded.' },
                    bubbles: true,    // Allows the event to bubble up the DOM tree
                    composed: true    // Allows the event to cross shadow DOM boundaries
                });
                this.dispatchEvent(event);
            });
        }, ()=> this.innerHTML = "This team is unavailable!")
    }
}

export default Base;

