package test.service;

import test.model.*;

import io.restassured.RestAssured;
import io.restassured.response.Response;
import static io.restassured.RestAssured.given;

import static test.ScooterSetupSuite.*;

public class CourierService {
    
    private Courier courier;
    
    public CourierService (Courier courier) {
        this.courier = courier;
    }
    
    public Response createCourier() {
        return given()
                .header("Content-type", "application/json") 
                .and()
                .body(this.courier) 
                .when()
                .post(CREATE_COURIER_URL);
    }
    
    public Response loginCourier() {
        Response loginResponse =  given()
                .header("Content-type", "application/json")
                .and()
                .body(this.courier)
                .post(LOGIN_COURIER_URL);
                
        this.courier.setId(loginResponse.then().extract().jsonPath().getString("id"));
        
        return loginResponse;
    }
    
    public Response deleteCourier() {
        if (this.courier.getId() == null) return null;
        
        return given()
                .header("Content-type", "application/json")
                .delete(DELETE_COURIER_URL, this.courier.getId());
    }
}