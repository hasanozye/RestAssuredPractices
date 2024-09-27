package zippo;

import org.testng.annotations.Test;
import io.restassured.builder.RequestSpecBuilder;
import io.restassured.builder.ResponseSpecBuilder;
import io.restassured.http.ContentType;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;
import io.restassured.specification.ResponseSpecification;
import org.apache.commons.lang3.RandomStringUtils;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;

import java.util.HashMap;
import java.util.Map;
import java.util.Random;

import static io.restassured.RestAssured.*;
import static org.hamcrest.Matchers.*;


public class Zippo {

    @Test
    public void test1() {
        get("https://api.zippopotam.us/us/90210")
                .then()
                .statusCode(200)
                .log().body()
                .body("country",equalTo("United States"))
                .body("places[0].state",equalTo("California"))
                .body("'country abbreviation'",equalTo("US"))
                .body("places[0].'place name'",equalTo("Beverly Hills"))

        ;
    }

    @Test
    public void test2() {
        get("https://api.zippopotam.us/TR/34200")
                .then()
                .statusCode(200)
                .log().body()
        ;
    }



}
