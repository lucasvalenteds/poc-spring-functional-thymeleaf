# POC: Spring Functional Thymeleaf

It demonstrates how to render HTML views using Thymeleaf as template engine without using Spring annotations.

We want to provision a reactive HTTP server that renders a phrase passed via query parameter as an HTML page. When not informed, we should render any phrase.

We also want to write unit tests for the code to verify that the template engine is changing the variables as expected.

## How to run

| Command | Description |
| :--- | :--- |
| Run tests | `./gradlew test` |
| Run application | `./gradlew run` |
