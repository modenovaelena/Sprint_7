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

public class TestOrderList {

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
        RestAssured.baseURI = "http://qa-scooter.praktikum-services.ru";

        String track = null;
        for (Order order: orders){
            Response createResponse = this.createOrder(order);
            track = createResponse.then().extract().jsonPath().getString("track");
            order.setTrack(track);
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
        return given()
                .header("Content-type", "application/json") 
                .and()
                .body(order) 
                .when()
                .post("/api/v1/orders");
    }
    
    @Step("List orders via GET /api/v1/orders")
    public Response listOrders() {
        return given()
                .header("Content-type", "application/json") 
                .and().queryParam("limit","3")
                .when()
                .get("/api/v1/orders");
    }

    
    @Step("Cancel order via PUT /api/v1/orders/cancel")
    public Response cancelOrder(Order order) {
        if (order.getTrack() == null) return null;
        
        return given()
                .header("Content-type", "application/json")
                .and()
                .body(new Order(order.getTrack()))
                .when()
                .put("/api/v1/orders/cancel");
    }

}


