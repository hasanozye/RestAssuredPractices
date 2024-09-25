package gorest;

import io.restassured.builder.RequestSpecBuilder;
import io.restassured.builder.ResponseSpecBuilder;
import io.restassured.http.ContentType;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;
import io.restassured.specification.ResponseSpecification;
import org.apache.commons.lang3.RandomStringUtils;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;

import java.util.Random;

import static io.restassured.RestAssured.*;
import static org.hamcrest.Matchers.*;

public class GoRestUserTest {

    RequestSpecification reqSpec;
    ResponseSpecification resSpec;

    @BeforeTest
    public void beforeTest() {
        // Sabitler specler
        reqSpec = new RequestSpecBuilder()
                .addHeader("Authorization", "Bearer ecb8cf5d97b3ac267e2add8243779043280018a78c26f40e9c5f2e2642f2c4ff")
                .setBaseUri("https://gorest.co.in")
                .build();

        resSpec = new ResponseSpecBuilder()
                .expectStatusCode(oneOf(200, 201, 204))
                .expectBody(containsString("id"))
                .expectBody(containsString("name"))
                .expectBody(containsString("email"))
                .expectBody(containsString("gender"))
                .expectBody(containsString("status"))
                .build();

    }

    int id;
    String name;
    String gender;
    String email;
    String status;


    @Test(testName = "createUser")
    public void test1_CreateUser() {
        String json = getJsonData();


        Response response = given()
                .spec(reqSpec)
                .contentType(ContentType.JSON)
                .body(json)
                .when()
                .post("/public/v2/users")
                .then()
                .log().body()
                .spec(resSpec)
                .extract().response();

        id = response.jsonPath().get("id");
        name = response.jsonPath().get("name");
        email = response.jsonPath().get("email");
        gender = response.jsonPath().get("gender");
        status = response.jsonPath().get("status");


    }


    @Test(dependsOnMethods = "test1_CreateUser")
    public void test2_updateUser() {
        Response response = given()
                .spec(reqSpec)
                .contentType(ContentType.JSON)
                .body(getJsonData())
                .when()
                .put("/public/v2/users/" + id)
                .then()
                .log().body()
                .spec(resSpec)
                .extract().response();

        System.out.println(response.jsonPath().get("name").toString());
    }

    @Test(dependsOnMethods = "test1_CreateUser")
    public void test3_deleteUser() {
        given()
                .spec(reqSpec)
                .when()
                .delete("/public/v2/users/" + id)
                .then()
                .log().body()
                .statusCode(oneOf(200,204,201));


    }


    public String getJsonData() {
        String[] genders = {"male", "female"};
        String[] statuses = {"active", "inactive"};

        String name = RandomStringUtils.randomAlphabetic(5, 10);
        String email = RandomStringUtils.randomAlphabetic(5, 10) + "@dmail.com";
        String gender = genders[new Random().nextInt(genders.length)];
        String status = statuses[new Random().nextInt(statuses.length)];


        String jsonReel = "{" +
                "\"name\":\"" + name + "\", " +
                "\"email\":\"" + email + "\", " +
                "\"gender\":\"" + gender + "\", " +
                "\"status\":\"" + status + "\"" +
                "}";

        return jsonReel;
    }

}
