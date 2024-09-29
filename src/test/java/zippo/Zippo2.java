package zippo;

import io.restassured.RestAssured;
import io.restassured.filter.log.LogDetail;
import io.restassured.response.ValidatableResponse;
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
import zippo.pojoClasses.Location;
import zippo.pojoClasses.Place;

import java.io.FileWriter;
import java.io.IOException;
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
        RestAssured.baseURI = "https://api.zippopotam.us";

        requestSpecification = new RequestSpecBuilder()
                .setBaseUri("https://api.zippopotam.us")
//                .setBasePath("/TR/")
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

    @Test
    public void test5_getDataExtractPlaceName() {
        Response response1 = given()
                .spec(requestSpecification)
                .when()
                .get("/TR/06080")
                .then()
                .spec(responseSpecification)
                .extract().response();

//        List<String> places = response1.then().extract().path("places.'place name'");
        List<String> places = response1.then().extract().jsonPath().getList("places.'place name'");


        Assert.assertEquals(18, places.size());
    }

    @Test
    public void test6_getDataToPojo() {
        Response response = given()
                .spec(requestSpecification)
                .when()
                .get("/TR/06080")
                .then()
                .spec(responseSpecification)
                .extract().response();

        Location location = response.then().extract().as(Location.class);
        System.out.println(location);
        //System.out.println(location.getPlaces());
        //System.out.println(location.getPlaces().get(0).getPlaceName());

    }


    @Test
    public void test7_getDataToPojo() throws IOException {
        FileWriter fileWriter = new FileWriter("Places.txt");
        for (int i = 6070; i < 6090; i++) {
            String postCode = getPostaKodu(i);
            Response response = given()
                    .spec(requestSpecification)
                    .pathParams("postaKodu", postCode)
                    .when()
                    .get("/TR/{postaKodu}")
                    .then()
                    .extract().response();
            Location location = response.then().extract().as(Location.class);
            if (location.getPlaces() != null) {
                for (Place place : location.getPlaces()) {
                    String str = location.getCountry() + "\t" +
                            place.getState() + "\t" +
                            place.getPlaceName() + "\n";

                    fileWriter.write(str);
                }
            }
        }
        fileWriter.close();
    }

    public String getPostaKodu(int num) {
        String code = String.valueOf(num);

        for (int i = code.length(); i < 5; i++) {
            code = "0".concat(code);
        }
        return code;
    }


    public static void main(String args[]) {
        ArrayList<Integer> list = new ArrayList<>(Arrays.asList(2, 4, 6));
        int evenNumCount = (int) list.stream().filter(n -> n % 2 == 0).count();
        Assert.assertEquals(evenNumCount, list.size());

        int oddNumCount = (int) list.stream().filter(n -> n % 2 != 0).count();
        Assert.assertEquals(oddNumCount, list.size());

    }


}
