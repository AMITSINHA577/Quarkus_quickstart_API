package org.acme.openapi.swaggerui;

import static io.restassured.RestAssured.given;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.Matchers.containsInAnyOrder;

import jakarta.ws.rs.core.MediaType;

import org.junit.jupiter.api.Test;

import io.quarkus.test.junit.QuarkusTest;

@QuarkusTest
public class FruitResourceTest {

    @Test
    public void testList() {
        given()
                .when().get("/fruits")
                .then()
                .statusCode(200)
                .body("$.size()", is(2),
                        "name", containsInAnyOrder("Apple", "Pineapple"),
                        "description", containsInAnyOrder("Winter fruit", "Tropical fruit"));
    }

    @Test
    public void testAdd() {
        given()
                .body("{\"name\": \"Pear\", \"description\": \"Winter fruit\"}")
                .header("Content-Type", MediaType.APPLICATION_JSON)
                .when()
                .post("/fruits")
                .then()
                .statusCode(200)
                .body("$.size()", is(3),
                        "name", containsInAnyOrder("Apple", "Pineapple", "Pear"),
                        "description", containsInAnyOrder("Winter fruit", "Tropical fruit", "Winter fruit"));

        given()
                .body("{\"id\": 3}")
                .header("Content-Type", MediaType.APPLICATION_JSON)
                .when()
                .delete("/fruits")
                .then()
                .statusCode(200)
                .body("$.size()", is(2),
                        "name", containsInAnyOrder("Apple", "Pineapple"),
                        "description", containsInAnyOrder("Winter fruit", "Tropical fruit"));
    }

    @Test
    public void testUpdate() {
        given()
                .body("{\"name\": \"Apple\", \"description\": \"Updated description\"}")
                .header("Content-Type", MediaType.APPLICATION_JSON)
                .when()
                .put("/fruits/1")
                .then()
                .statusCode(200)
                .body("description", is("Updated description"));
    }

    @Test
    public void testGet() {
        given()
                .when()
                .get("/fruits/1")
                .then()
                .statusCode(200)
                .body("name", is("Apple"),
                      "description", is("Winter fruit"));
    }

    @Test
    public void testUpdateName() {
        given()
                .body("{\"name\": \"Updated Apple\"}")
                .header("Content-Type", MediaType.APPLICATION_JSON)
                .when()
                .put("/fruits/updateName/1")
                .then()
                .statusCode(200)
                .body("name", is("Updated Apple"));
    }
}
