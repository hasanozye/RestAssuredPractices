package localJsonServer;

import org.testng.annotations.Test;

import static io.restassured.RestAssured.*;
import static org.hamcrest.Matchers.*;

public class LocalTestDB {

    @Test
    public void test01_getAllPosts() {
        given()
                .when()
                .get("http://localhost:3000/posts")
                .then()
                .log().all()
                .body(containsString("author"))
                .statusCode(200)

        ;
    }
}
