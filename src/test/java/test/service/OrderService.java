package test.service;

import test.model.*;

import io.restassured.RestAssured;
import io.restassured.response.Response;
import static io.restassured.RestAssured.given;

import static test.ScooterSetupSuite.*;

import io.qameta.allure.Step; 

public class OrderService {
    
    @Step("Create order via POST /api/v1/orders")
    public Response createOrder(Order order) {
        Response createResponse = given()
                .header("Content-type", "application/json") 
                .and()
                .body(order) 
                .when()
                .post(CREATE_ORDER_URL);
                
        String track = createResponse.then().extract().jsonPath().getString("track");
        order.setTrack(track);
        
        return createResponse;
    }
    
    @Step("Cancel order via PUT /api/v1/orders/cancel")
    public Response cancelOrder(Order order) {
        if (order.getTrack() == null) return null;
        
        return given()
                .header("Content-type", "application/json")
                .and()
                .body(new Order(order.getTrack()))
                .when()
                .put(CANCEL_ORDER_URL);
    }
    
    @Step("List orders via GET /api/v1/orders")
    public Response listOrders(String limit) {
        return given()
                .header("Content-type", "application/json") 
                .and().queryParam("limit", limit)
                .when()
                .get(LIST_ORDER_URL);
    }
    
    
    @Step("List 3 orders via GET /api/v1/orders")
    public Response listOrders() {
        return listOrders ("3");
    }

}