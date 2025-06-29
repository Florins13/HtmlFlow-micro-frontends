import React, { StrictMode } from 'react';
import { createRoot } from 'react-dom/client';
import './index.css';
import App from './App.jsx';

// Export a function to mount your app to a given container
createRoot(document.getElementById('root')).render(
    <StrictMode>
        <App />
    </StrictMode>,
)

// window.addEventListener("DOMContentLoaded", () => {
//     let host = document.getElementById("shadow-host");
//     if (!host) {
//         host = document.createElement("div");
//         host.id = "shadow-host";
//         document.body.appendChild(host);
//     }
//
//     let shadowRoot = host.shadowRoot;
//     if (!shadowRoot) {
//         shadowRoot = host.attachShadow({ mode: "open" });
//     }
//
//     let mountNode = shadowRoot.querySelector("#react-root");
//     if (!mountNode) {
//         mountNode = document.createElement("div");
//         mountNode.id = "react-root";
//         shadowRoot.appendChild(mountNode);
//     }
//
//     // Mount React app into shadow DOM container
//     mountApp(mountNode);
// });