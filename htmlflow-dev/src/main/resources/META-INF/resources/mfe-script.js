import TeamAlpha from "./team-alpha.js";
import TeamBeta from "./team-beta.js"
import TeamOmega from "./team-omega.js";

window.customElements.define('team-alpha', TeamAlpha);
window.customElements.define('team-beta', TeamBeta);
window.customElements.define('team-omega', TeamOmega);

export function goToCheckout(){
    window.location.replace(window.location.origin + "/cart/checkout");
}