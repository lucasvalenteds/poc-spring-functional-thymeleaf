package com.example;

import org.hamcrest.CoreMatchers;
import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;
import org.springframework.test.web.reactive.server.WebTestClient;

class MainTest {

    private static final WebTestClient client = WebTestClient
        .bindToApplicationContext(AppConfiguration.createApplicationContext(AppConfiguration.environment()))
        .build();

    @Test
    void testRenderingIndexPageDefaultHeadline() {
        client.get().uri("/")
            .exchange()
            .expectStatus().isOk()
            .expectHeader().contentType(MediaType.TEXT_HTML)
            .expectBody(String.class)
            .value(CoreMatchers.containsString("<h1>Hello World!</h1>"));
    }

    @Test
    void testRenderingIndexPageCustomHeadline() {
        String phrase = "Breaking news!";

        client.get().uri(uri -> uri.path("/").queryParam("phrase", phrase).build())
            .exchange()
            .expectStatus().isOk()
            .expectHeader().contentType(MediaType.TEXT_HTML)
            .expectBody(String.class)
            .value(CoreMatchers.containsString("<h1>" + phrase + "</h1>"));
    }

    @Test
    void testRenderingNotFoundPage() {
        client.get().uri("/about")
            .exchange()
            .expectStatus().isNotFound()
            .expectBody().isEmpty();
    }
}
