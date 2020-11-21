package com.example;

import org.springframework.http.MediaType;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.RouterFunctions;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;

import java.util.Map;

public final class AppRouter {

    public RouterFunction<ServerResponse> create() {
        return RouterFunctions.route()
            .GET("/", request ->
                Mono.justOrEmpty(request.queryParam("phrase"))
                    .map(phrase -> Map.of("headline", phrase))
                    .defaultIfEmpty(Map.of("headline", "Hello World!"))
                    .flatMap(model ->
                        ServerResponse.ok()
                            .contentType(MediaType.TEXT_HTML)
                            .render("index", model)
                    )
            )
            .GET("*", request -> ServerResponse.notFound().build())
            .build();
    }
}
