package com.example;

import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.RouterFunctions;
import org.springframework.web.reactive.function.server.ServerResponse;

import java.util.Map;

public final class AppRouter {

    public RouterFunction<ServerResponse> create() {
        return RouterFunctions.route()
            .GET("/", request ->
                ServerResponse.ok()
                    .contentType(MediaType.TEXT_HTML)
                    .render("index", Map.ofEntries(
                        Map.entry("headline", "Hello World!")
                    ))
            )
            .GET("*", request ->
                ServerResponse.notFound()
                    .header(HttpHeaders.CONTENT_TYPE, MediaType.TEXT_HTML_VALUE)
                    .build()
            )
            .build();
    }
}
