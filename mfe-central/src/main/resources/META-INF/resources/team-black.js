import {TeamBlack} from "./base.js";

export class Test extends TeamBlack{
    constructor(props) {
        super(props);
    }

    connectedCallback() {
        super.connectedCallback();
        super.reloadFragment();
        this.reloadFragment();
    }

    reloadFragment() {
        this.fetchData().then(test => {
            console.log("I can override here the team black")
            this.innerHTML = "<div>hello</div>"
        })
    }
}

export default Test;


