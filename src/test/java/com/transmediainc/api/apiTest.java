package com.transmediainc.api;

import com.thedeanda.lorem.LoremIpsum;
import io.restassured.response.Response;
import org.testng.annotations.Test;

import java.util.HashMap;
import java.util.Map;
import java.util.Random;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.equalTo;

public class apiTest {
    @Test
    public void createAndDeleteListShouldSucceed() {
        String listname = LoremIpsum.getInstance().getTitle(1);
        int orderId = new Random().nextInt((1000 - 100) + 1) + 100;

        Map<String, Object> json = new HashMap<>();
        json.put("boardId", 57);
        json.put("name", listname);
        json.put("order", orderId);

        Response response = given()
                .baseUri("http://localhost")
                .port(3000)
                .header("Content-Type", "application/json")
                .body(json)
                .log().uri()
                .log().body()
                .when()
                .post("/api/lists")
                .then()
                .log().body()
                .statusCode(201)
                .body("name", equalTo(listname))
                .body("order", equalTo(orderId))
                .extract().response();

        int neworderId = response.jsonPath().getInt("id");

        Response deleteResponse = given()
                .baseUri("http://localhost")
                .port(3000)
                .log().uri()
                .when()
                .delete("/api/lists/" + neworderId)
                .then()
                .log().body()
                .statusCode(200)
                .extract().response();
    }
}
