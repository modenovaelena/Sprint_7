import io.restassured.RestAssured;
import io.restassured.response.Response;

import org.junit.*;

import io.qameta.allure.junit4.DisplayName; 
import io.qameta.allure.Step; 

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.equalTo;

public class TestCourierCreation {

    private String id;

    @Before
    public void setUp() {
        RestAssured.baseURI = "http://qa-scooter.praktikum-services.ru";
    }

    @Test
    @DisplayName("Check if it is possible to create courier")
    public void courierCreation() {
        Courier courier  = new Courier ("modenovaelena_sprint7_" + System.currentTimeMillis(),
                "1234", "sakke"); 
                
        Response createResponse = this.createCourier(courier);
        createResponse.then().assertThat().statusCode(201);
        createResponse.then().assertThat().body("ok",equalTo(true));
        
        Response loginResponse = this.loginCourier(courier);
        id = loginResponse.then().extract().jsonPath().getString("id");
    }
    
    @Test
    @DisplayName("Check that it is not possible to create two similar couriers twice")
    public void duplicateCourierCreation() {
        Courier courier  = new Courier ("modenovaelena_sprint7_" + System.currentTimeMillis(),
                "1234", "sakke"); 
              
        // create first courier  
        Response createResponse1 = this.createCourier(courier);
        createResponse1.then().assertThat().statusCode(201);
        
        // attempt to create second courier with the same data
        Response createResponse2 = this.createCourier(courier);
        createResponse2.then().assertThat().statusCode(409);
        
        Response loginResponse = this.loginCourier(courier);
        id = loginResponse.then().extract().jsonPath().getString("id");
    }
    
    @Test
    @DisplayName("Check that it is not possible to create couriers with same login")
    public void sameLoginCourierCreation() {
        String login = "modenovaelena_sprint7_" + System.currentTimeMillis();
        Courier courier1  = new Courier (login, "1234", "sakke"); 
        Courier courier2  = new Courier (login, "1234", "sakke"); 
              
        // create first courier  
        Response createResponse1 = this.createCourier(courier1);
        createResponse1.then().assertThat().statusCode(201);
        
        // attempt to create second courier with the same data
        Response createResponse2 = this.createCourier(courier2);
        createResponse2.then().assertThat().statusCode(409);
        
        Response loginResponse = this.loginCourier(courier1);
        id = loginResponse.then().extract().jsonPath().getString("id");
    }
    
    @Test
    @DisplayName("Check that it is not possible to create couriers with at least one mandatory field missing")
    public void notFilledMandatoryFieldsCourierCreation() {
        Courier[] couriers = new Courier[] {
            new Courier(null, "1234", "sakke"),
            new Courier("modenovaelena_sprint7_" + System.currentTimeMillis(), null, "sakke")
        };
        
        for (Courier courier : couriers) {
            Response createResponse = this.createCourier(courier);
            createResponse.then().assertThat().statusCode(400);
        }
    }
    
    @Test
    @DisplayName("Check that it is OK to create couriers without first name")
    public void notFilledOptionalFieldsCourierCreation() {
        Courier courier  = new Courier ("modenovaelena_sprint7_" + System.currentTimeMillis(),
                "1234", null); 
                
        Response createResponse = this.createCourier(courier);
        createResponse.then().assertThat().statusCode(201);
        createResponse.then().assertThat().body("ok",equalTo(true));
        
        Response loginResponse = this.loginCourier(courier);
        id = loginResponse.then().extract().jsonPath().getString("id");
    }

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


