package test;

import io.restassured.RestAssured;
import io.restassured.response.Response;
import java.util.List;

import org.junit.*;

import io.qameta.allure.junit4.DisplayName; 

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
        
    private OrderService orderService = new OrderService();

    @Before
    public void setUp() {
        super.setUp();
        
        for (Order order: orders){
            this.orderService.createOrder(order);
        }
    }

    @Test
    @DisplayName("Check if order retrival operation returns a list of orders")
    public void orderRetrival() {
        Response ordersResponse = this.orderService.listOrders();
        ordersResponse.then().assertThat().statusCode(200).and().body("orders", is(notNullValue()));

        List<Order> orders = ordersResponse.jsonPath().getList("orders", Order.class);
        assertTrue(orders.size() > 0);
    }
    
   
    @After
    public void clear () {
        for (Order order: orders){
            this.orderService.cancelOrder(order);
        }
    }
    
}


