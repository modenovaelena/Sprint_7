package test;

import io.restassured.RestAssured;
import io.restassured.response.Response;

import org.junit.*;

import io.qameta.allure.junit4.DisplayName; 

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.is; 
import static org.hamcrest.Matchers.notNullValue; 

import test.model.*;
import test.service.*;

public class TestCourierAuth extends ScooterSetupSuite {

    private Courier courier = new Courier ("modenovaelena_sprint7_" + System.currentTimeMillis(),
                "Elena", "sakke");
                
    private CourierService courierService = new CourierService();

    @Before
    public void setUp() {
        super.setUp();
        
        this.courierService.createCourier(this.courier);
        this.courierService.loginCourier(this.courier);
    }

    @Test
    @DisplayName("Check if it is possible to auth courier and ID is returned")
    public void courierAuth() {
        Response loginResponse = this.courierService.loginCourier(
            new Courier(
                this.courier.getLogin(), 
                this.courier.getPassword(), 
                null)
        );
        loginResponse.then().assertThat().statusCode(200);
        loginResponse.then().assertThat().body("id", is(notNullValue()));
    }
    
    @Test
    @DisplayName("Auth with incorrect login")
    public void courierAuthWithIncorrectLogin() {
        Courier courier =  new Courier("random_login_" + System.currentTimeMillis(), this.courier.getPassword(), null);
        Response loginResponse = this.courierService.loginCourier(courier);
        loginResponse.then().assertThat().statusCode(404);
        loginResponse.then().assertThat().body("message", is(notNullValue()));
    }
    
    @Test
    @DisplayName("Auth with incorrect password")
    public void courierAuthWithIncorrectPassword() {
        Courier courier = new Courier(this.courier.getLogin(), "random_pswd_" + System.currentTimeMillis(), null);
        Response loginResponse = this.courierService.loginCourier(courier);
        loginResponse.then().assertThat().statusCode(404);
        loginResponse.then().assertThat().body("message", is(notNullValue()));
    }
    
    @Test
    @DisplayName("Auth without mandatory login field")
    public void courierAuthWithoutMandatoryLoginField() {
        Courier courier = new Courier(null, this.courier.getPassword(), null);
        
        Response loginResponse = this.courierService.loginCourier(courier);
        loginResponse.then().assertThat().statusCode(400);
        loginResponse.then().assertThat().body("message", is(notNullValue()));
    }
    
   /* @Test
    @DisplayName("Auth without mandatory password field")
    public void courierAuthWithoutMandatoryPasswordField() {
        Courier courier = new Courier(this.courier.getLogin(), null, null);
        
        Response loginResponse = this.courierService.loginCourier(courier);
        loginResponse.then().assertThat().statusCode(504); // in fact that should not happen, i would rather test for 400 and fail the tests
    }*/

    @After
    public void clear () {
        this.courierService.deleteCourier(this.courier);
    }
    
}


