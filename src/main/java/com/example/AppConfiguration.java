package com.example;

import org.springframework.context.ApplicationContext;
import org.springframework.context.support.GenericApplicationContext;
import org.springframework.http.MediaType;
import org.springframework.http.server.reactive.HttpHandler;
import org.springframework.http.server.reactive.ReactorHttpHandlerAdapter;
import org.springframework.web.reactive.function.server.HandlerStrategies;
import org.springframework.web.reactive.function.server.RouterFunctions;
import org.springframework.web.reactive.result.view.ViewResolver;
import org.thymeleaf.spring5.SpringWebFluxTemplateEngine;
import org.thymeleaf.spring5.view.reactive.ThymeleafReactiveViewResolver;
import org.thymeleaf.templateresolver.ClassLoaderTemplateResolver;
import reactor.netty.http.server.HttpServer;

import java.util.List;

public class AppConfiguration {

    private AppConfiguration() {
    }

    public static ApplicationContext createApplicationContext() {
        GenericApplicationContext context = new GenericApplicationContext();

        context.registerBean(ViewResolver.class, () -> {
            GenericApplicationContext templateContext = new GenericApplicationContext();
            SpringWebFluxTemplateEngine templateEngine = new SpringWebFluxTemplateEngine();

            ClassLoaderTemplateResolver templateResolver = new ClassLoaderTemplateResolver();
            templateResolver.setPrefix("/templates/");
            templateResolver.setSuffix(".html");

            templateEngine.setTemplateResolver(templateResolver);

            ThymeleafReactiveViewResolver resolver = new ThymeleafReactiveViewResolver();
            resolver.setApplicationContext(templateContext);
            resolver.setTemplateEngine(templateEngine);
            resolver.setSupportedMediaTypes(List.of(MediaType.TEXT_HTML));

            return resolver;
        });

        context.registerBean(AppHandler.class);
        context.registerBean(AppRouter.class);

        context.registerBean(HandlerStrategies.class, () ->
            HandlerStrategies.builder()
                .viewResolver(context.getBean(ViewResolver.class))
                .build()
        );

        context.registerBean(HttpHandler.class, () ->
            RouterFunctions.toHttpHandler(
                context.getBean(AppRouter.class).create(),
                context.getBean(HandlerStrategies.class)
            )
        );

        context.registerBean(HttpServer.class, () ->
            HttpServer.create()
                .handle(new ReactorHttpHandlerAdapter(context.getBean(HttpHandler.class)))
        );

        context.refresh();

        return context;
    }
}
