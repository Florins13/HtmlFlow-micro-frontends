package com.dev;



import htmlflow.HtmlFlow;
import htmlflow.HtmlMfeConfig;
import htmlflow.HtmlView;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import org.xmlet.htmlapifaster.EnumRelType;
import org.xmlet.htmlapifaster.EnumTypeScriptType;

@Path("/mfe")
public class HtmlMfeResource {

    @GET
    @Produces(MediaType.TEXT_HTML)
    public Response getHtml() {
        HtmlMfeConfig mfeBike = new HtmlMfeConfig("http://localhost:8081/bikes", "mfe1", "triggerBikeEvent", "triggerCartEvent", "http://localhost:8081/js/mfe-bikes.js", "http://localhost:8081/css/style.css");
        HtmlMfeConfig mfeCart = new HtmlMfeConfig("http://localhost:8083/cart", "mfe2", "triggerCartEvent", "triggerOrderEvent", "http://localhost:8083/mfe-cart.js", "http://localhost:8083/style.css");
        HtmlMfeConfig mfeOrder = new HtmlMfeConfig("http://localhost:8084/order/history", "mfe3", "triggerOrderEvent", "triggerBikeEvent", "http://localhost:8084/mfe-order.js", "");
        HtmlMfeConfig mfeStream = new HtmlMfeConfig("http://localhost:8080/html-chunked/stream", "mfe4","triggerStreamEvent", "", "", "", true);

        HtmlView<?> mfe = HtmlFlow.mfe(page -> {
            page.html()
                    .head()
                    .script().attrSrc("https://unpkg.com/htmx.org@1.9.10").__()
                    .script().attrSrc("https://unpkg.com/htmx.org/dist/ext/sse.js").addAttr("crossorigin", "anonymous").__()
                    .__()
                        .body()
                        .div().addAttr("style", "display: flex; justify-content: center;height: 100px;border: blue 1px solid;")
                            .h1().text("Header -> Hello from HtmlFlow!").__()
                        .__()
                        .div().addAttr("style", "display: flex;")
                            .div().addAttr("style", "width: 75%;border: black 1px solid; margin: 20px")
                                .custom("micro-frontend").addAttr("mfe-name", "mfe1").addAttr("hx-get", "http://localhost:8081/bikes").addAttr("hx-trigger", "load, triggerBikeEvent from:body").addAttr("hx-swap", "innerHTML").__()
                                    .script().attrSrc("http://localhost:8081/js/mfe-bikes.js").attrType(EnumTypeScriptType.MODULE).__()
                                    .link().attrHref("http://localhost:8081/css/style.css").attrRel(EnumRelType.STYLESHEET).__()
//                                .mfe(mfeBike).__()
                            .div().addAttr("style", "width: 20%;border: red 1px solid; margin: 20px")
                                .custom("micro-frontend").addAttr("mfe-name", "mfe2").addAttr("hx-get", "http://localhost:8083/cart").addAttr("hx-trigger", "load, triggerCartEvent from:body").addAttr("hx-swap", "innerHTML").__()
                                    .script().attrSrc("http://localhost:8083/mfe-cart.js").attrType(EnumTypeScriptType.MODULE).__()
                                    .link().attrHref("http://localhost:8083/style.css").attrRel(EnumRelType.STYLESHEET).__()
//                                .mfe(mfeCart).__()
                        .__()
                        .div()
                            .div().addAttr("style", "border: green 1px solid; margin: 20px")
                                .custom("micro-frontend").addAttr("mfe-name", "mfe3").addAttr("hx-get", "http://localhost:8084/order/history").addAttr("hx-trigger", "load, triggerOrderEvent from:body").addAttr("hx-swap", "innerHTML").__()
                                    .script().attrSrc("http://localhost:8084/mfe-order.js").attrType(EnumTypeScriptType.MODULE).__()
//                                .mfe(mfeOrder).__()
                        .__()
                    .div().addAttr("style", "display: flex; justify-content: center;height: 50px;border: yellow 1px solid;")
                    // we he to change backend for this to work since htmx supports, CORS origin issues
//                        .div().mfe(mfeStream).__()
//                            .custom("micro-frontend").addAttr("hx-ext","sse").addAttr("sse-connect", "http://localhost:8080/html-chunked/stream").addAttr("sse-event", "message").addAttr("hx-swap", "beforeend").__()
                    .__()
                    .__();
        });

        String html = mfe.render();
        return Response.ok(html).build();

    }
}
