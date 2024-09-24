package localJsonServer;

import static io.restassured.RestAssured.*;
import static org.hamcrest.Matchers.*;

import io.restassured.http.ContentType;
import org.apache.commons.lang3.RandomStringUtils;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;

import java.util.List;

public class Odev {


    @BeforeTest
    public void beforeTest() {
        baseURI = "http://localhost:3000";
    }


    @Test(invocationCount = 10)
    public void test1() {
        // 10 adet user kaydi yapacak
        String email = RandomStringUtils.randomAlphabetic(5, 10) + "@mail.com";
        String password = RandomStringUtils.randomAlphabetic(5, 10);
        String username = RandomStringUtils.randomAlphabetic(5, 10);
        String data = "{\n" +
                "        \"email\": \"" + email + "\",\n" +
                "        \"password\": \"" + password + "\",\n" +
                "        \"username\": \"" + username + "\"\n" +
                "    }";
        given()
                .contentType(ContentType.JSON)
                .body(data)
                .when()
                .post("/users")
                .then()
                .log().body()
                .statusCode(201);
    }

    @Test(priority = 1, dependsOnMethods = "test1")
    public void test2() {
// tüm username'ler içindeki en uzun username'i yazdıracak.
        String enUzunDeger = "";
        List<String> usernameList = given()
                .when()
                .get("/users")
                .then()
                .statusCode(200)
                .extract().path("username");


        for (String s : usernameList) {
            if (s.length()>enUzunDeger.length()){
                enUzunDeger = s;
            }
        }

        System.out.println(usernameList);
        System.out.println(enUzunDeger);

    }
}
