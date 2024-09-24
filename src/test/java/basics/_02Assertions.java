package basics;

import org.hamcrest.Matchers;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import javax.xml.crypto.Data;

import static io.restassured.RestAssured.*;
import static org.hamcrest.Matchers.*;

public class _02Assertions {

    @BeforeTest
    public void beforeTest() {
        baseURI = "https://reqres.in";
    }

    @Test
    public void test01() {
        given()
                .pathParams("id", 2)
                .get("/api/users/{id}")
                .then()
                .log().body()
                // status code 200 olmalı
                .statusCode(200)

                .body("data.first_name", equalTo("Janet"))
                .body("data.last_name", equalTo("Weaver"))
                .body("data", hasKey("id"))
                .body("support", hasKey("url"))

                // response body json "contributions" içermeli
                .body(containsString("contributions"))

                // data id si 2 veya daha küçük olmalı
                .body("data.id", lessThanOrEqualTo(2))
        ;
    }

    @Test(dataProvider = "getIds")
    public void test02_dataProvider(int id) {
        given()
                .pathParams("id", id)
                .get("/api/users/{id}")
                .then()
                .log().body()
                .statusCode(200)

        ;
    }

    @DataProvider
    public Object[][] getIds() {
        return new Object[][]{
                {1},
                {2},
                {3}
        };
    }


}
