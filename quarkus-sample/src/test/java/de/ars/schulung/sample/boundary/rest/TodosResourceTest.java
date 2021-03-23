package de.ars.schulung.sample.boundary.rest;

import io.quarkus.test.junit.QuarkusTest;
import org.junit.jupiter.api.Test;

import static io.restassured.RestAssured.given;
import static org.hamcrest.CoreMatchers.is;

@QuarkusTest
public class TodosResourceTest {

    @Test
    public void testHelloEndpoint() {
        given()
          .when().get("/api/v1/todos")
          .then()
             .statusCode(200);
    }

}