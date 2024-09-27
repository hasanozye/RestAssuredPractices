package zippo;

import io.restassured.filter.log.LogDetail;
import org.testng.Assert;
import org.testng.annotations.*;
import org.testng.annotations.Test;
import io.restassured.builder.RequestSpecBuilder;
import io.restassured.builder.ResponseSpecBuilder;
import io.restassured.http.ContentType;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;
import io.restassured.specification.ResponseSpecification;
import org.apache.commons.lang3.RandomStringUtils;
import org.testng.annotations.Test;

import java.util.*;
import java.util.stream.Collectors;

import static io.restassured.RestAssured.*;
import static org.hamcrest.Matchers.*;

public class Zippo2 {

    /*
    1.
     */

    RequestSpecification requestSpecification;
    ResponseSpecification responseSpecification;

    @BeforeClass
    public void beforeClass() {
        requestSpecification = new RequestSpecBuilder()
                .setBaseUri("https://api.zippopotam.us/")
                //.setBasePath("/TR/")
                .log(LogDetail.URI)
                .build();

        responseSpecification = new ResponseSpecBuilder()
                .expectStatusCode(200)
                .log(LogDetail.BODY)
                .build();
    }

    @Test
    public void test1_GetData0608() {
        given()
                .spec(requestSpecification)
                .when()
                .get("/TR/06080")
                .then()
                .spec(responseSpecification)
                .statusCode(200)
        //.log().body()

        ;
    }

    @Test
    public void test2_getDataAndAssert() {
        given()
                .spec(requestSpecification)
                .when()
                .get("/tr/06080")
                .then()
                .spec(responseSpecification)
                .body("'post code'", is(not(empty())))
                .body("country", equalTo("Turkey"))
                .body("'country abbreviation'", equalTo("TR"))
                .body("places[2].'place name'", equalTo("Sokullu Mah."))
                .body("places", hasSize(18))
        ;

    }

    @Test
    public void test3_getDataAllStatesAreAnkara() {

        // json'daki places'in size'i 18 dir
        given()
                .spec(requestSpecification)
                .when()
                .get("/tr/06080")
                .then()
                .spec(responseSpecification)
                .body("places.findAll{it.state == 'Ankara'}", hasSize(18))
                .body("places.findAll{it.state != 'Ankara'}", hasSize(0))
        ;

    }

    // TR ve 06080 yerine pathParam kullaniniz.
    @Test
    public void test3_getDataUsePathParam() {
        // json'daki places'in size'i 18 dir

        String country = "TR";
        String postCode = "06080";

        given()
                .spec(requestSpecification)
                .pathParams("ulke", country)
                .pathParams("postaKodu", postCode)
                .when()
                .get("/{ulke}/{postaKodu}")
                .then()
                .spec(responseSpecification)
                .body("places.findAll{it.state == 'Ankara'}", hasSize(18))
                .body("places.findAll{it.state != 'Ankara'}", hasSize(0))
        ;

    }

    @Test
    public void test4_getDataExtractPlaceName() {
        Response response = given()
                .spec(requestSpecification)
                .when()
                .get("/TR/06080")
                .then()
                .spec(responseSpecification)
                .extract().response();

        String countryResponse = response.then()
                .extract()
                .path("country");
        Assert.assertEquals(countryResponse, "Turkey");

        String placeNameResponse = response.then()
                .extract()
                .jsonPath()
                .get("places[2].'place name'");
        Assert.assertEquals(placeNameResponse, "Sokullu Mah.");
        response.prettyPrint();


        String country = given()
                .spec(requestSpecification)
                .when()
                .get("/TR/06080")
                .then()
                .spec(responseSpecification)
                .extract().path("country");
        Assert.assertEquals(country, "Turkey");


        String placeName = given()
                .spec(requestSpecification)
                .when()
                .get("/TR/06080")
                .then()
                .spec(responseSpecification)
                .extract().jsonPath().get("places[2].'place name'");
        Assert.assertEquals(placeName, "Sokullu Mah.");
    }

    public static void main(String args[]) {
        ArrayList<Integer> list = new ArrayList<>(Arrays.asList(2, 4, 6));
        int evenNumCount = (int) list.stream().filter(n -> n % 2 == 0).count();
        Assert.assertEquals(evenNumCount, list.size());

        int oddNumCount = (int) list.stream().filter(n -> n % 2 != 0).count();
        Assert.assertEquals(oddNumCount, list.size());

    }


}
