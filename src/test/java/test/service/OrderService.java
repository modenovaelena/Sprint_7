package test.service;

import test.model.*;

import io.restassured.RestAssured;
import io.restassured.response.Response;
import static io.restassured.RestAssured.given;

import static test.ScooterSetupSuite.*;

public class OrderService {
    
    private Order order;
    
    public OrderService (Order order) {
        this.order = order;
    }
    
    public Response createOrder() {
        Response createResponse = given()
                .header("Content-type", "application/json") 
                .and()
                .body(this.order) 
                .when()
                .post(CREATE_ORDER_URL);
                
        String track = createResponse.then().extract().jsonPath().getString("track");
        this.order.setTrack(track);
        
        return createResponse;
    }
    
    public Response cancelOrder() {
        if (this.order.getTrack() == null) return null;
        
        return given()
                .header("Content-type", "application/json")
                .and()
                .body(new Order(this.order.getTrack()))
                .when()
                .put(CANCEL_ORDER_URL);
    }
    
    public static Response listOrders(String limit) {
        return given()
                .header("Content-type", "application/json") 
                .and().queryParam("limit", limit)
                .when()
                .get(LIST_ORDER_URL);
    }
    
    
    public static Response listOrders() {
        return listOrders ("3");
    }

}