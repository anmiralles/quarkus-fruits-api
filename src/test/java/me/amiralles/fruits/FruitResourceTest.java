package me.amiralles.fruits;

import io.quarkus.test.junit.QuarkusTest;
import org.junit.jupiter.api.Test;

import static io.restassured.RestAssured.given;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.notNullValue;

@QuarkusTest
public class FruitResourceTest {

    /*@Test
    public void testGetFruitsEndpoint() {
        given()
                .when().get("/fruits")
                .then()
                .statusCode(200)
                .body(is(notNullValue()));
    }

    @Test
    public void testGetFruitEndpoint() {
        Long id = 1L; // replace with an id that exists in your database
        given()
                .when().get("/fruits/" + id)
                .then()
                .statusCode(200)
                .body(is(notNullValue()));
    }

    @Test
    public void testCreateFruitEndpoint() {
        Fruit fruit = new Fruit(); // replace with a valid fruit object
        fruit.name = "Strawberry";

        given()
                .contentType(APPLICATION_JSON)
                .body(fruit)
                .when().post("/fruits")
                .then()
                .statusCode(201);
    }

    @Test
    public void testUpdateFruitEndpoint() {
        long id = 1L; // replace with an id that exists in your database
        Fruit fruit = new Fruit(); // replace with a valid fruit object
        fruit.name = "Apple";

        given()
                .contentType(APPLICATION_JSON)
                .body(fruit)
                .when().put("/fruits/" + id)
                .then()
                .statusCode(200);
    }

    @Test
    public void testDeleteFruitEndpoint() {
        long id = 4L; // replace with an id that exists in your database

        given()
                .when().delete("/fruits/" + id)
                .then()
                .statusCode(204);
    }*/
}