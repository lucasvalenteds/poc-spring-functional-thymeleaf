package com.example;

import org.springframework.http.MediaType;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;

import java.util.Map;

public final class AppHandler {

    public Mono<ServerResponse> handleIndex(ServerRequest request) {
        return ServerResponse.ok()
            .contentType(MediaType.TEXT_HTML)
            .render("index", Map.ofEntries(
                Map.entry("headline", "Hello World!")
            ));
    }

    public Mono<ServerResponse> handleAbout(ServerRequest request) {
        return ServerResponse.notFound().build();
    }
}
