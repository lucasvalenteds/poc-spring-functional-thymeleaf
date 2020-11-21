package com.example;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.GenericApplicationContext;
import org.springframework.core.env.ConfigurableEnvironment;
import org.springframework.core.env.StandardEnvironment;
import org.springframework.core.io.support.ResourcePropertySource;
import org.springframework.http.MediaType;
import org.springframework.http.server.reactive.ReactorHttpHandlerAdapter;
import org.springframework.web.reactive.function.server.HandlerStrategies;
import org.springframework.web.reactive.function.server.RouterFunctions;
import org.springframework.web.reactive.result.view.ViewResolver;
import org.springframework.web.server.WebHandler;
import org.springframework.web.server.adapter.WebHttpHandlerBuilder;
import org.thymeleaf.spring5.SpringWebFluxTemplateEngine;
import org.thymeleaf.spring5.view.reactive.ThymeleafReactiveViewResolver;
import org.thymeleaf.templateresolver.ClassLoaderTemplateResolver;
import reactor.netty.http.server.HttpServer;

import java.io.IOException;
import java.util.List;

public class AppConfiguration {

    private static final Logger LOGGER = LogManager.getLogger(AppConfiguration.class);

    private AppConfiguration() {
    }

    public static class PropertyBasedEnvironment extends StandardEnvironment {

        public PropertyBasedEnvironment(String filename) {
            try {
                getPropertySources().addLast(new ResourcePropertySource(filename));
            } catch (IOException exception) {
                LOGGER.error("Could not read the file {}.", filename);
            }
        }
    }

    public static ConfigurableEnvironment environment() {
        return new PropertyBasedEnvironment("classpath:/application.properties");
    }

    public static ApplicationContext createApplicationContext(ConfigurableEnvironment environment) {
        GenericApplicationContext context = new GenericApplicationContext();

        context.setEnvironment(environment);

        context.registerBean(ViewResolver.class, () -> {
            GenericApplicationContext templateContext = new GenericApplicationContext();
            SpringWebFluxTemplateEngine templateEngine = new SpringWebFluxTemplateEngine();

            ClassLoaderTemplateResolver templateResolver = new ClassLoaderTemplateResolver();
            templateResolver.setPrefix(environment.getRequiredProperty("templates.path", String.class));
            templateResolver.setSuffix(".html");

            templateEngine.setTemplateResolver(templateResolver);

            ThymeleafReactiveViewResolver resolver = new ThymeleafReactiveViewResolver();
            resolver.setApplicationContext(templateContext);
            resolver.setTemplateEngine(templateEngine);
            resolver.setSupportedMediaTypes(List.of(MediaType.TEXT_HTML));

            return resolver;
        });

        context.registerBean(HandlerStrategies.class, () ->
            HandlerStrategies.builder()
                .viewResolver(context.getBean(ViewResolver.class))
                .build()
        );

        context.registerBean(AppHandler.class);
        context.registerBean(AppRouter.class);

        context.registerBean(
            WebHttpHandlerBuilder.WEB_HANDLER_BEAN_NAME,
            WebHandler.class,
            () -> RouterFunctions.toWebHandler(
                context.getBean(AppRouter.class).create(),
                context.getBean(HandlerStrategies.class)
            )
        );

        context.refresh();

        return context;
    }

    public static HttpServer createHttpServer(ApplicationContext context) {
        return HttpServer.create()
            .port(context.getEnvironment().getRequiredProperty("server.port", Integer.class))
            .handle(new ReactorHttpHandlerAdapter(WebHttpHandlerBuilder.applicationContext(context).build()));
    }
}
