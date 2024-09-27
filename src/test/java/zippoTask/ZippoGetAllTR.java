package zippoTask;

import io.restassured.RestAssured;
import io.restassured.response.Response;
import org.testng.annotations.Test;
import static io.restassured.RestAssured.given;
import static org.testng.Assert.assertEquals;

public class ZippoGetAllTR {

    @Test
    public void getZipCodesInRange() {
        // Set the base URI
        RestAssured.baseURI = "https://api.zippopotam.us/TR";

        // Loop through zip codes from 01000 to 02000
        for (int zipCode = 1000; zipCode <= 2000; zipCode++) {
            String zipCodeString = String.format("%05d", zipCode);  // Format zip code to 5 digits

            // Make the GET request
            Response response = given()
                    .when()
                    .get("/" + zipCodeString)
                    .then()
                    .extract().response();

            // Check if the response is 200 OK
            if (response.statusCode() == 200) {
                // Deserialize the response to TurkeyDatabase POJO
                TurkeyDatabase data = response.getBody().as(TurkeyDatabase.class);

                // Log the details of the response
                System.out.println("Zip Code: " + zipCodeString);
                System.out.println("Response: " + data.toString());
            } else {
                // Handle the case where no data is found (404)
                System.out.println("No data found for zip code: " + zipCodeString);
            }
        }
    }
}
