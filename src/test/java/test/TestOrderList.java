package test;

import io.restassured.RestAssured;
import io.restassured.response.Response;
import java.util.List;

import org.junit.*;

import io.qameta.allure.junit4.DisplayName; 
import io.qameta.allure.Step; 

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.is; 
import static org.hamcrest.Matchers.notNullValue; 
import static org.junit.Assert.*;

import test.model.*;
import test.service.*;

public class TestOrderList extends ScooterSetupSuite  {

    private Order[] orders = new Order[]{
            new Order(
                    "Freddy", "Trump", "In the middle of nowhere", 
                        "Central", "+7 800 415 24 24", 5, 
                            "2020-06-06", "Make it fast please", new String[]{"BLACK"}),
            new Order(
                    "Freddy", "Trump", "In the middle of nowhere", 
                        "Central", "+7 800 415 24 24", 5, 
                            "2020-06-06", "Make it fast please", new String[]{"GREY"})
        };

    @Before
    public void setUp() {
        super.setUp();
        
        for (Order order: orders){
            this.createOrder(order);
        }
    }

    @Test
    @DisplayName("Check if order retrival operation returns a list of orders")
    public void orderRetrival() {
        Response ordersResponse = this.listOrders();
        ordersResponse.then().assertThat().statusCode(200).and().body("orders", is(notNullValue()));

        List<Order> orders = ordersResponse.jsonPath().getList("orders", Order.class);
        assertTrue(orders.size() > 0);
    }
    
   
    @After
    public void clear () {
        for (Order order: orders){
            this.cancelOrder(order);
        }
    }
    
    
    @Step("Create order via POST /api/v1/orders")
    public Response createOrder(Order order) {
        return (new OrderService(order)).createOrder();
    }

    
    @Step("Cancel order via PUT /api/v1/orders/cancel")
    public Response cancelOrder(Order order) {
        return (new OrderService(order)).cancelOrder();
    }

    
    @Step("List orders via GET /api/v1/orders")
    public Response listOrders() {
        return OrderService.listOrders();
    }

}


