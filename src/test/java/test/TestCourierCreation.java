package test;

import io.restassured.RestAssured;
import io.restassured.response.Response;

import org.junit.*;

import io.qameta.allure.junit4.DisplayName; 
import io.qameta.allure.Step; 

import static org.hamcrest.Matchers.equalTo;

import test.model.*;
import test.service.*;

public class TestCourierCreation extends ScooterSetupSuite {

    private Courier courier;
    
    
    @Before
    public void setUp() {
        super.setUp();
    }


    @Test
    @DisplayName("Check if it is possible to create courier")
    public void courierCreation() {
        this.courier  = new Courier ("modenovaelena_sprint7_" + System.currentTimeMillis(),
                "Elena", "sakke"); 
                
        Response createResponse = this.createCourier(this.courier);
        createResponse.then().assertThat().statusCode(201);
        createResponse.then().assertThat().body("ok",equalTo(true));
        
        this.loginCourier(this.courier);
    }
    
    @Test
    @DisplayName("Check that it is not possible to create two similar couriers twice")
    public void duplicateCourierCreation() {
        this.courier  = new Courier ("modenovaelena_sprint7_" + System.currentTimeMillis(),
                "Elena", "sakke"); 
              
        // create first courier  
        Response createResponse1 = this.createCourier(this.courier);
        createResponse1.then().assertThat().statusCode(201);
        
        // attempt to create second courier with the same data
        Response createResponse2 = this.createCourier(this.courier);
        createResponse2.then().assertThat().statusCode(409);
        
        this.loginCourier(this.courier);
    }
    
    @Test
    @DisplayName("Check that it is not possible to create couriers with same login")
    public void sameLoginCourierCreation() {
        String login = "modenovaelena_sprint7_" + System.currentTimeMillis();
        
        this.courier = new Courier (login, "Elena", "sakke"); 
        Courier courier2  = new Courier (login, "Elena", "sakke"); 
              
        // create first courier  
        Response createResponse1 = this.createCourier(this.courier);
        createResponse1.then().assertThat().statusCode(201);
        
        // attempt to create second courier with the same data
        Response createResponse2 = this.createCourier(courier2);
        createResponse2.then().assertThat().statusCode(409);
        
        this.loginCourier(this.courier);
    }
    
    @Test
    @DisplayName("Check that it is not possible to create courier without login")
    public void courierCreationWithoutLogin() {
        this.courier =  new Courier(null, "Elena", "sakke");
     
        Response createResponse = this.createCourier(this.courier);
        createResponse.then().assertThat().statusCode(400);
    }
    
    @Test
    @DisplayName("Check that it is not possible to create couriers without password")
    public void courierCreationWithoutPassword() {
        this.courier =  new Courier("modenovaelena_sprint7_" + System.currentTimeMillis(), null, "sakke");
        
        Response createResponse = this.createCourier(this.courier);
        createResponse.then().assertThat().statusCode(400);
    }
    
    @Test
    @DisplayName("Check that it is OK to create couriers without first name")
    public void notFilledOptionalFieldsCourierCreation() {
        this.courier  = new Courier ("modenovaelena_sprint7_" + System.currentTimeMillis(),
                "Elena", null); 
                
        Response createResponse = this.createCourier(this.courier);
        createResponse.then().assertThat().statusCode(201);
        createResponse.then().assertThat().body("ok",equalTo(true));
        
        this.loginCourier(this.courier);
    }

    @After
    public void clear () {
        this.deleteCourier(this.courier);
    }
    
    @Step("Create courier via POST /api/v1/courier")
    public Response createCourier(Courier courier) {
        return (new CourierService(courier)).createCourier();
    }
    
    @Step("Login courier via POST /api/v1/courier/login")
    public Response loginCourier(Courier courier) {
        return (new CourierService(courier)).loginCourier();
    }
    
    @Step("Delete courier via DELETE /api/v1/courier/:id")
    public Response deleteCourier(Courier courier) {
        return (new CourierService(courier)).deleteCourier();
    }

}


