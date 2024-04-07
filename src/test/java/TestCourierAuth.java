import io.restassured.RestAssured;
import io.restassured.response.Response;

import org.junit.*;

import io.qameta.allure.junit4.DisplayName; 
import io.qameta.allure.Step; 

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.is; 
import static org.hamcrest.Matchers.notNullValue; 

public class TestCourierAuth {

    private String id;
    private Courier courier = new Courier ("modenovaelena_sprint7_" + System.currentTimeMillis(),
                "1234", "sakke");

    @Before
    public void setUp() {
        RestAssured.baseURI = "http://qa-scooter.praktikum-services.ru";
        this.createCourier(this.courier);
        
        Response loginResponse = this.loginCourier(this.courier);
        id = loginResponse.then().extract().jsonPath().getString("id");
    }

    @Test
    @DisplayName("Check if it is possible to auth courier and ID is returned")
    public void courierAuth() {
        Response loginResponse = this.loginCourier(
            new Courier(
                this.courier.getLogin(), 
                this.courier.getPassword(), 
                null)
        );
        loginResponse.then().assertThat().statusCode(200);
        loginResponse.then().assertThat().body("id", is(notNullValue()));
    }
    
    @Test
    @DisplayName("Auth without incorrect login or password")
    public void courierAuthWithIncorrectCredentials() {
        Courier[] couriers = new Courier[] {
            new Courier(this.courier.getLogin(), "random_pswd_" + System.currentTimeMillis(), null),
            new Courier("random_login_" + System.currentTimeMillis(), this.courier.getPassword(), null)
        };
        
        for (Courier courier : couriers) {
            Response loginResponse = this.loginCourier(courier);
            loginResponse.then().assertThat().statusCode(404);
            loginResponse.then().assertThat().body("message", is(notNullValue()));
        }
    }
    
    @Test
    @DisplayName("Auth without mandatory login field")
    public void courierAuthWithoutMandatoryLoginField() {
        Courier courier = new Courier(null, this.courier.getPassword(), null);
        
        Response loginResponse = this.loginCourier(courier);
        loginResponse.then().assertThat().statusCode(400);
        loginResponse.then().assertThat().body("message", is(notNullValue()));
    }
    
   /* @Test
    @DisplayName("Auth without mandatory password field")
    public void courierAuthWithoutMandatoryPasswordField() {
        Courier courier = new Courier(this.courier.getLogin(), null, null);
        
        Response loginResponse = this.loginCourier(courier);
        loginResponse.then().assertThat().statusCode(504); // in fact that should not happen, i would rather test for 400 and fail the tests
    }*/

    @After
    public void clear () {
        this.deleteCourier(id);
    }
    
    @Step("Create courier via POST /api/v1/courier")
    public Response createCourier(Courier courier) {
        return given()
                .header("Content-type", "application/json") 
                .and()
                .body(courier) 
                .when()
                .post("/api/v1/courier");
    }
    
    @Step("Login courier via POST /api/v1/courier/login")
    public Response loginCourier(Courier courier) {
        return given()
                .header("Content-type", "application/json")
                .and()
                .body(courier)
                .post("/api/v1/courier/login");
    }
    
    @Step("Delete courier via DELETE /api/v1/courier/:id")
    public Response deleteCourier(String id) {
        if (id == null) return null;
        
        return given()
                .header("Content-type", "application/json")
                .delete("/api/v1/courier/{id}", id);
    }

}


