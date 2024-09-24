package basics;

import org.testng.Assert;
import org.testng.annotations.Test;

import java.util.concurrent.TimeUnit;

import static io.restassured.RestAssured.*;

public class _01Basics {

    /* rest-assured BDD mantığına göre yazılım vardır.
    given() -> ön şartlar
    when() -> işlemler
    then() -> assertions

    RestAssured methodlarının statik importu gerekli

     */

    @Test
    public void text01_BasicUsage() {
        given()             // ön veriler, requirementler, headers, cookies, body,...
                .when()     // yapılan işlem, GET, POST, DELETE, PUT
                .then();    // assertions, status code, Json path assertions

    }

    @Test
    public void test02_get() {
        given()
                .when()
                .get("https://reqres.in/api/users?page=2") // GET methodu ile bu adrese request gönderdik
                .then()
        //.log().body()
        //.log().all()
        //.log().headers()
        //.log().cookies()
        ;
    }

    @Test
    public void test03_statusCode() {
        String url = "https://reqres.in/api/users?page=2";
        given()
                .get(url)
                .then()
                .statusCode(200)
        ;
        //Assert.assertEquals(200,url);
    }

    @Test
    public void test04_ResponseTime() {
        String url = "https://reqres.in/api/users?page=2";
        long time = given()
                .get(url)
                .timeIn(TimeUnit.MILLISECONDS);
        System.out.println(time);
    }

    @Test
    public void test05_pathParams() {
        given()
                .pathParams("page", 1)
                .pathParams("link", "api")
                .get("https://reqres.in/{link}/users?page={page}")
                .then()
                .log().body()
                .statusCode(200)

        ;
    }

    @Test
    public void test07_baseUri() {

        baseURI = "https://reqres.in";       //baseURL tanimi içindir

        given()
                .get("https://reqres.in/api/users?page=1")
                .then()
                .statusCode(200)
        ;

        /*
        baseURI tanimli ise
        GET, POST, ... 'da http ya da https yoksa baseURI kullanilir
         */

        given()
                .get("/api/users?page=1")// kesinlikle kesmeyle başlaması daha iyidir. BEST PRACTICE.
                .then()
                .statusCode(200)

        ;


    }
}
