package com.example;

import io.netty.handler.codec.http.HttpResponseStatus;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import reactor.netty.http.server.HttpServer;

import java.time.Duration;

public class Main {

    private static final Logger logger = LogManager.getLogger(Main.class);

    public static void main(String[] args) {
        HttpServer httpServer = HttpServer.create()
            .port(8081)
            .handle((request, response) -> response.status(HttpResponseStatus.OK).send());

        httpServer.bindUntilJavaShutdown(
            Duration.ofMillis(1000),
            server -> logger.info("Server running at http://localhost:{}", server.port())
        );
    }
}
