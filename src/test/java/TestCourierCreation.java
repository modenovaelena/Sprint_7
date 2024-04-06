import io.qameta.allure.Description;
import io.restassured.RestAssured;
import org.junit.*;

import java.io.File;

import static io.restassured.RestAssured.given;

public class TestCourierCreation {

    private String id;

    @Before
    public void setUp() {
        RestAssured.baseURI = "http://qa-scooter.praktikum-services.ru";
    }
    // positive case status code 201
    @Test
    public void CourierCreation() {
        Courier courier  = new Courier ("naalena_"+System.currentTimeMillis(),
                "1234","sakke"); // создай объект, который соответствует JSON
        given()
                .header("Content-type", "application/json") // заполни header
                .and()
                .body(courier) // заполни body
                .when()
                .post("/api/v1/courier") // отправь запрос на ручку
                .then().statusCode(201);

        id = given()
                .header("Content-type", "application/json")
                .and()
                .body(courier)
                .post("/api/v1/courier/login")
                .then().extract().jsonPath().getString("id");

    }

    @After
    public void clear () {
        given()
                .header("Content-type", "application/json")
                .delete("/api/v1/courier/{id}",id);

    }



}


