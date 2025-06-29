package com.dev;



import htmlflow.HtmlFlow;
import htmlflow.HtmlMfeConfig;
import htmlflow.HtmlView;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

@Path("/mfe")
public class HtmlMfeResource {

    @GET
    @Produces(MediaType.TEXT_HTML)
    public Response getHtml() {
        HtmlView<?> mfe = HtmlFlow.mfe(page -> {
            page.html()
                    .head()
                    // Reference JS file in META-INF/resources/main.js
                    .__()
                        .body()
                        .div().addAttr("style", "display: flex; justify-content: center;height: 100px;border: blue 1px solid;")
                            .h1().dynamic((h1,model)-> h1.attrId("header-id")).text("Header -> Hello from HtmlFlow!").__()
                        .__()
                        .div().addAttr("style", "display: flex;")
                            .div().addAttr("style", "width: 75%;border: black 1px solid; margin: 20px")
                                .mfe((cfg)-> {
                                    cfg.setMfeUrlResource("http://localhost:8081/bikes");
                                    cfg.setMfeName("mfe1");
                                    cfg.setMfeListeningEventName("triggerBikeEvent");
                                    cfg.setMfeTriggersEventName("triggerCartEvent");
                                    cfg.setMfeScriptUrl("http://localhost:8081/js/mfe-bikes.js");
                                    cfg.setMfeStylingUrl("http://localhost:8081/css/style.css");
                                }).__()
                            .div().addAttr("style", "width: 20%;border: red 1px solid; margin: 20px")
                                .mfe((cfg)-> {
                                    cfg.setMfeUrlResource("http://localhost:8083/cart");
                                    cfg.setMfeName("mfe2");
                                    cfg.setMfeListeningEventName("triggerCartEvent");
                                    cfg.setMfeTriggersEventName("triggerOrderEvent");
                                    cfg.setMfeScriptUrl("http://localhost:8083/mfe-cart.js");
                                    cfg.setMfeStylingUrl("http://localhost:8083/style.css");
                                }).__()
                    .div().addAttr("id", "root").__()
                        .__()
                        .div()
                            .div().addAttr("style", "border: green 1px solid; margin: 20px")
                                .mfe((cfg)-> {
                                    cfg.setMfeUrlResource("http://localhost:8084/order/history");
                                    cfg.setMfeName("mfe3");
                                    cfg.setMfeListeningEventName("triggerOrderEvent");
                                    cfg.setMfeScriptUrl("http://localhost:8084/mfe-order.js");
                                    cfg.setMfeStylingUrl("");
                                }).__()
                        .__()
                    .div().addAttr("style", "display: flex; justify-content: center;height: 50px;border: yellow 1px solid;")
                        .div().mfe((cfg)-> {
                                    cfg.setMfeUrlResource("http://localhost:8080/html-chunked/stream");
                                    cfg.setMfeName("mfe4");
                                    cfg.setMfeListeningEventName("triggerStreamEvent");
                                    cfg.setMfeStreamingData(true);
                        }).__()
                    .__()
                    .__();
        });

        String html = mfe.render();
        return Response.ok(html).build();

    }
}
