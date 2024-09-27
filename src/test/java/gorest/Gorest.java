package gorest;

import static io.restassured.RestAssured.*;
import static org.hamcrest.Matchers.*;

import io.restassured.builder.RequestSpecBuilder;
import io.restassured.builder.ResponseBuilder;
import io.restassured.builder.ResponseSpecBuilder;
import io.restassured.http.ContentType;
import io.restassured.specification.RequestSpecification;
import io.restassured.specification.ResponseSpecification;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;

import java.util.Collections;
import java.util.List;

public class Gorest {

    //Test 1: get all users

    @BeforeTest
    public void beforeTest() {
        baseURI = "https://gorest.co.in/public/v2";
    }

    @Test
    public void test1_getAllUsers() {
        given()
                .when()
                .get("/users")
                .then()
                .log().body()
                .statusCode(oneOf(200, 201, 204))

        ;

    }

    @Test
    public void test2_getFirstName() {
        String obj = given()
                .when()
                .get("/users")
                .then()
                //.log().body()
                .statusCode(oneOf(200, 201, 204))
                .extract().path("name[0]");
        System.out.println("obj = " + obj);
    }

    // /users ile gelen tüm isimleri list'e atın, sıralayın ve konsoal yazdirin
    @Test
    public void test3_getAllNamesThenSortThenWriteToConsol() {
        List<String> namesList = given()
                .when()
                .get("/users")
                .then()
                .statusCode(oneOf(200))
                .extract().path("name");

        Collections.sort(namesList);
        System.out.println(namesList);

    }

    @Test
    public void test3_getAllFemaleNamesThenSortThenWriteToConsol() {
        List<String> namesList = given()
                .when()
                .get("/users")
                .then()
                .statusCode(oneOf(200))
                .body(not(empty()))
                .extract().path("findAll{it.gender=='female'}.name");

        //Collections.sort(namesList);
        System.out.println(namesList);

    }

    @Test
    public void test3_getAllNamesWihJsonPath() {
        List<String> namesList = given()
                .when()
                .get("/users")
                .then()
                .statusCode(oneOf(200))
                .body(not(empty()))
                .extract().jsonPath().getList("name");

        Collections.sort(namesList);
        System.out.println(namesList);

    }

    // Specifications -> spec
    @Test
    public void test4_genel() {

        RequestSpecification reqSpec = new RequestSpecBuilder()
                .setContentType(ContentType.JSON)
                //.setBaseUri()
                .build();

        ResponseSpecification resSpec = new ResponseSpecBuilder()
                .expectContentType(ContentType.JSON)
                .expectStatusCode(oneOf(200, 201, 204))
                .build();
        given()
                .spec(reqSpec)
                .when()
                .get("/users")
                .then()
                .spec(resSpec);
        ;
    }

    @Test
    public void getAUser() {
        get("https://gorest.co.in/public/v2/users/6940853")
                .then()
                .statusCode(200)
                .log().body()

        ;
    }

    @Test
    public void getAUserInAClass() {
        User user = get("https://gorest.co.in/public/v2/users/6940853")
                .then()
                .statusCode(200)
                //.log().body()
                .extract().as(User.class);
        System.out.println(user);
        System.out.println(user.getName());
        System.out.println(user.getEmail());
    }

}
