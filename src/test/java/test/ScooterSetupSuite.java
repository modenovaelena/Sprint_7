package test;

import io.restassured.RestAssured;

public abstract class ScooterSetupSuite {
    
    public static final String BASE_URI = "http://qa-scooter.praktikum-services.ru";
    
    public static final String CREATE_COURIER_URL = "/api/v1/courier";
    
    public static final String LOGIN_COURIER_URL = "/api/v1/courier/login";
    
    public static final String DELETE_COURIER_URL = "/api/v1/courier/{id}";
    
    public static final String CREATE_ORDER_URL = "/api/v1/orders";
    
    public static final String LIST_ORDER_URL = "/api/v1/orders";
    
    public static final String CANCEL_ORDER_URL = "/api/v1/orders/cancel";
    
    

    public void setUp() {
        RestAssured.baseURI = ScooterSetupSuite.BASE_URI;
    }
}


