package test.service;

import test.model.*;

import io.restassured.RestAssured;
import io.restassured.response.Response;
import static io.restassured.RestAssured.given;

import static test.ScooterSetupSuite.*;

import io.qameta.allure.Step; 

public class CourierService {
    
    @Step("Create courier via POST /api/v1/courier")
    public Response createCourier(Courier courier) {
        return given()
                .header("Content-type", "application/json") 
                .and()
                .body(courier) 
                .when()
                .post(CREATE_COURIER_URL);
    }
    
    @Step("Login courier via POST /api/v1/courier/login")
    public Response loginCourier(Courier courier) {
        Response loginResponse =  given()
                .header("Content-type", "application/json")
                .and()
                .body(courier)
                .post(LOGIN_COURIER_URL);
                
        courier.setId(loginResponse.then().extract().jsonPath().getString("id"));
        
        return loginResponse;
    }
    
    @Step("Delete courier via DELETE /api/v1/courier/:id")
    public Response deleteCourier(Courier courier) {
        if (courier.getId() == null) return null;
        
        return given()
                .header("Content-type", "application/json")
                .delete(DELETE_COURIER_URL, courier.getId());
    }
}