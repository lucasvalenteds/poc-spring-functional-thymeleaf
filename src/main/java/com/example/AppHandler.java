package com.example;

import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;

import java.util.Map;

public final class AppHandler {

    public Mono<ServerResponse> handleIndex() {
        return ServerResponse.ok()
            .contentType(MediaType.TEXT_HTML)
            .render("index", Map.ofEntries(
                Map.entry("headline", "Hello World!")
            ));
    }

    public Mono<ServerResponse> handleNotFound() {
        return ServerResponse.notFound()
            .header(HttpHeaders.CONTENT_TYPE, MediaType.TEXT_HTML_VALUE)
            .build();
    }
}
