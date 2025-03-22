class TeamBeta extends HTMLElement {
    constructor() {
        super();
    }

    connectedCallback() {
        const url_resource = this.getAttribute("url");
        let test = this;
        console.log(url_resource);
        if(url_resource){
            fetchStreamData(test, url_resource).then(r => console.info("Finished fetching stream!")).catch(err=> console.error(err));
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

async function fetchStreamData(test, url) {
    const response = await fetch(url);
    for await (const value of response.body) {
        const uint8Array = new Uint8Array(value);
        const chunk = new TextDecoder().decode(uint8Array);
        console.log(chunk)

        const table = test.getElementsByTagName("table");
        if(table.length){ // after table exists add each chunk inside the table
            table.item(0).getElementsByTagName("tbody").item(0).insertAdjacentHTML("beforeend", chunk);
        }else{ // add table tag, first chunk of the stream
            test.insertAdjacentHTML("beforeend", chunk);
        }
    }
}



export default TeamBeta;