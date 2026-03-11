package com.fulfilment.application.monolith.stores;

import static io.restassured.RestAssured.given;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.notNullValue;

import io.quarkus.test.junit.QuarkusTest;
import org.junit.jupiter.api.Test;

@QuarkusTest
public class StoreResourceTest {

    @Test
    public void testCreateStorePositive() {
        String uniqueName = "TestStore_" + System.currentTimeMillis();

        given()
            .contentType("application/json")
            .body("{\"name\": \"" + uniqueName + "\", \"quantityProductsInStock\": 10}")
            .when().post("/store")
            .then()
            .statusCode(201)
            .body("id", notNullValue())
            .body("name", is(uniqueName))
            .body("quantityProductsInStock", is(10));
    }

    @Test
    public void testGetSingleStorePositive() {
        String uniqueName = "TestStore_" + System.currentTimeMillis();

        // First, create a store
        Integer storeId = given()
            .contentType("application/json")
            .body("{\"name\": \"" + uniqueName + "\", \"quantityProductsInStock\": 5}")
            .when().post("/store")
            .then()
            .statusCode(201)
            .extract().path("id");

        // Now, retrieve the store
        given()
            .when().get("/store/" + storeId)
            .then()
            .statusCode(200)
            .body("id", is(storeId.intValue()))  // RestAssured treats Long as Integer in JSON
            .body("name", is(uniqueName))
            .body("quantityProductsInStock", is(5));
    }
}
