package test;

import io.restassured.RestAssured;
import io.restassured.response.Response;

import org.junit.*;

import io.qameta.allure.junit4.DisplayName; 
import io.qameta.allure.Step; 

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.is; 
import static org.hamcrest.Matchers.notNullValue; 

import org.junit.runner.RunWith;
import org.junit.runners.Parameterized; 

import test.model.*;
import test.service.*;

@RunWith(Parameterized.class)
public class TestOrderCreation extends ScooterSetupSuite  {

    private Order order;

    @Parameterized.Parameters
    public static Object[][] getTestData() {
        return new Object[][] {
                {new Order(
                    "Freddy", "Trump", "In the middle of nowhere", 
                        "Central", "+7 800 415 24 24", 5, 
                            "2020-06-06", "Make it fast please", new String[]{"BLACK"})},
                {new Order(
                    "Freddy", "Trump", "In the middle of nowhere", 
                        "Central", "+7 800 415 24 24", 5, 
                            "2020-06-06", "Make it fast please", new String[]{"GREY"})},
                {new Order(
                    "Freddy", "Trump", "In the middle of nowhere", 
                        "Central", "+7 800 415 24 24", 5, 
                            "2020-06-06", "Make it fast please", new String[]{"BLACK", "GREY"})},
                {new Order(
                    "Freddy", "Trump", "In the middle of nowhere", 
                        "Central", "+7 800 415 24 24", 5, 
                            "2020-06-06", "Make it fast please", null)},
        };
    }
    
    public TestOrderCreation(Order order){
        this.order = order;
    }
    
    @Before
    public void setUp() {
        super.setUp();
    }

    @Test
    @DisplayName("Check if it is possible to create order {index}")
    public void orderCreation() {
        Response createResponse = this.createOrder(this.order);
        createResponse.then().assertThat().statusCode(201).and().body("track", is(notNullValue()));
    }
    
   
    @After
    public void clear () {
        this.cancelOrder(this.order);
    }
    
    @Step("Create order via POST /api/v1/orders")
    public Response createOrder(Order order) {
        return (new OrderService(order)).createOrder();
    }

    
    @Step("Cancel order via PUT /api/v1/orders/cancel")
    public Response cancelOrder(Order order) {
        return (new OrderService(order)).cancelOrder();
    }

}


