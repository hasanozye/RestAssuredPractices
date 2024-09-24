package localJsonServer;

import io.restassured.http.ContentType;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;

import java.util.List;
import java.util.Random;

import static io.restassured.RestAssured.*;
import static org.hamcrest.Matchers.*;

public class LocalTestDB {

    @BeforeTest
    public void beforeTest() {
        baseURI = "http://localhost:3000";
    }

    @Test
    public void test01_getAllUsers() {
        given()
                .when()
                .get("/users")
                .then()
                .log().all()
                .statusCode(200)
                .body("id", hasItems("1"))

        ;

        // hasItem -> tekDeger assert
        // hasItems -> array içinde olması beklenene değerler
    }

    //extract a value
    @Test
    public void test01_getAllUsers_extractData() {
        String id = given()
                .when()
                .get("/users")
                .then()
                .log().all()
                .statusCode(200)
                .extract().path("id[0]");
        System.out.println("id[0] : " + id);
    }

    @Test
    public void postData02() {
        String json = """
                {
                      "email": "hasanozyerLOL@email.3x4com",
                      "password": "hasanozyerpasswordXv464XX",
                      "username": "hasanusernameXX23Xxac"
                    }
                    """;
        String id = given()
                .contentType(ContentType.JSON)
                .body(json)
                .when()
                .post("/users")
                .then()
                .log().body()
                .statusCode(201)
                .extract().path("id");
        System.out.println("id: " + id);

        String jsonForUpdate = """
                {
                      "email": "hasanozyerLOL@email.34com",
                      "password": "hasanozyerpasswordXv4XX",
                      "username": "hasanusernameXXXxac"
                    }
                    """;


        given()
                .contentType(ContentType.JSON)
                .body(jsonForUpdate)
                .when()
                .put("/users/" + id)
                .then()
                .log().body()
                .statusCode(200)

        ;

        given()
                .when()
                .delete("/users/" + id)
                .then()
                .statusCode(200)

        ;


    }

    @Test(invocationCount = 5)
    public void test01_getAllUsers_extractList() {
        List<Integer> list = given()
                .when()
                .get("/users")
                .then()
                //.log().all()
                .statusCode(200)
                .extract().path("id");          // extract response'un içindeki veriye ulaşmak için
        System.out.println("ids = " + list);      // path istediğiiz verinin yolu
    }

    @Test
    public void test02_storeAUser() {
        String data = """
                {
                      "email": "hasanozyerLOL@email.com",
                      "password": "hasanozyerpasswordXXX",
                      "username": "hasanusernameXXX",
                      "id": 1
                    }
                    """;
        given()
                .contentType(ContentType.JSON) // request body content type
                .body(data)
                .when()
                .post("/users")
                .then()
                .log().body()
                .statusCode(201)
                .contentType(ContentType.JSON) // response body content type
        ;
    }

    public static void main(String[] args) {
        System.out.println(getRandomString(20, 30));
    }

    // getRandomString(3, 5) -> Ab5c
    public static String getRandomString(int min, int max) {
        String str = "abcdefABCDEF12345 ";
        String rndStr = "";
        int last = min + new Random().nextInt(max - min);
        for (int i = 0; i < last; i++) {
            rndStr += str.charAt(new Random().nextInt(str.length()));
        }
        return rndStr;
    }


}
