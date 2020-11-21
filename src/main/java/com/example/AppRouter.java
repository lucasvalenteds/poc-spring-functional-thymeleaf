package com.example;

import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.RouterFunctions;
import org.springframework.web.reactive.function.server.ServerResponse;

public final class AppRouter {

    private final AppHandler handler;

    public AppRouter(AppHandler handler) {
        this.handler = handler;
    }

    public RouterFunction<ServerResponse> create() {
        return RouterFunctions.route()
            .GET("/", handler::handleIndex)
            .GET("*", handler::handleNotFound)
            .build();
    }
}
