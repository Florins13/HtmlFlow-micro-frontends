package com.dev;

import io.vertx.core.Vertx;
import io.vertx.core.http.HttpClient;
import io.vertx.core.http.HttpMethod;
import io.vertx.ext.web.Router;
import io.vertx.ext.web.proxy.handler.ProxyHandler;
import io.vertx.httpproxy.HttpProxy;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.enterprise.event.Observes;

@ApplicationScoped
public class RedirectFilter {

    void setup(@Observes Router router, Vertx vertx) {
        HttpClient client = vertx.createHttpClient();

        HttpProxy bikes = HttpProxy.reverseProxy(client)
                .origin(8081, "localhost");
        HttpProxy cart = HttpProxy.reverseProxy(client)
                .origin(8083, "localhost");
        HttpProxy order = HttpProxy.reverseProxy(client)
                .origin(8084, "localhost");

        // check paths to forward
        for (HttpMethod m : HttpMethod.values()) {
            router.route(m, "/bikes/*").handler(ProxyHandler.create(bikes));
            router.route(m, "/cart/*").handler(ProxyHandler.create(cart));
            router.route(m, "/order/*").handler(ProxyHandler.create(order));
        }
    }
}