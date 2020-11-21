package com.example;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.context.ApplicationContext;
import reactor.netty.http.server.HttpServer;

import java.time.Duration;

public class Main {

    private static final Logger logger = LogManager.getLogger(Main.class);

    public static void main(String[] args) {
        ApplicationContext context = AppConfiguration.createApplicationContext();

        HttpServer httpServer = context.getBean(HttpServer.class);

        httpServer.port(8081).bindUntilJavaShutdown(
            Duration.ofMillis(1000),
            server -> logger.info("Server running at http://localhost:{}", server.port())
        );
    }
}
