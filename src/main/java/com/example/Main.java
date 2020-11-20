package com.example;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.context.support.GenericApplicationContext;
import org.springframework.http.MediaType;
import org.springframework.http.server.reactive.HttpHandler;
import org.springframework.http.server.reactive.ReactorHttpHandlerAdapter;
import org.springframework.web.reactive.function.server.HandlerStrategies;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.RouterFunctions;
import org.springframework.web.reactive.function.server.ServerResponse;
import org.thymeleaf.spring5.SpringWebFluxTemplateEngine;
import org.thymeleaf.spring5.view.reactive.ThymeleafReactiveViewResolver;
import org.thymeleaf.templateresolver.ClassLoaderTemplateResolver;
import reactor.netty.http.server.HttpServer;

import java.time.Duration;
import java.util.List;
import java.util.Map;

public class Main {

    private static final Logger logger = LogManager.getLogger(Main.class);


    public static void main(String[] args) {

        RouterFunction<ServerResponse> router = RouterFunctions.route()
            .GET("/", request ->
                ServerResponse.ok()
                    .contentType(MediaType.TEXT_HTML)
                    .render("index", Map.ofEntries(
                        Map.entry("headline", "Hello World")
                    ))
            )
            .build();

        SpringWebFluxTemplateEngine templateEngine = new SpringWebFluxTemplateEngine();
        ClassLoaderTemplateResolver templateResolver = new ClassLoaderTemplateResolver();
        templateResolver.setPrefix("/templates/");
        templateResolver.setSuffix(".html");
        templateEngine.setTemplateResolver(templateResolver);

        ThymeleafReactiveViewResolver resolver = new ThymeleafReactiveViewResolver();
        GenericApplicationContext context = new GenericApplicationContext();
        context.refresh();
        resolver.setApplicationContext(context);
        resolver.setTemplateEngine(templateEngine);
        resolver.setSupportedMediaTypes(List.of(MediaType.TEXT_HTML));

        HandlerStrategies strategies = HandlerStrategies.builder()
            .viewResolver(resolver)
            .build();
        HttpHandler handler = RouterFunctions.toHttpHandler(router, strategies);
        ReactorHttpHandlerAdapter adapter = new ReactorHttpHandlerAdapter(handler);

        HttpServer httpServer = HttpServer.create()
            .port(8081)
            .handle(adapter);

        httpServer.bindUntilJavaShutdown(
            Duration.ofMillis(1000),
            server -> logger.info("Server running at http://localhost:{}", server.port())
        );
    }
}
