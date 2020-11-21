package com.example;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.context.ApplicationContext;
import org.springframework.core.env.ConfigurableEnvironment;
import reactor.netty.http.server.HttpServer;

import java.time.Duration;

public class Main {

    private static final Logger logger = LogManager.getLogger(Main.class);

    public static void main(String[] args) {
        ConfigurableEnvironment environment = AppConfiguration.environment();
        ApplicationContext context = AppConfiguration.createApplicationContext(environment);

        HttpServer httpServer = AppConfiguration.createHttpServer(context);

        httpServer.bindUntilJavaShutdown(
            Duration.ofSeconds(5),
            server -> logger.info("Server running at http://localhost:{}", server.port())
        );
    }
}
