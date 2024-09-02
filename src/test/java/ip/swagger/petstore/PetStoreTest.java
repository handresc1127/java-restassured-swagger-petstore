package ip.swagger.petstore;

import com.tngtech.java.junit.dataprovider.DataProvider;
import com.tngtech.java.junit.dataprovider.DataProviderRunner;
import com.tngtech.java.junit.dataprovider.UseDataProvider;
import io.restassured.module.jsv.JsonSchemaValidator;
import org.apache.http.HttpStatus;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;

import static io.restassured.RestAssured.enableLoggingOfRequestAndResponseIfValidationFails;
import static io.restassured.RestAssured.given;
import static org.hamcrest.CoreMatchers.*;


@RunWith(DataProviderRunner.class)
public class PetStoreTest {

    @BeforeClass
    public static void beforeAll(){
        enableLoggingOfRequestAndResponseIfValidationFails();
    }


    @DataProvider
    public static Object[][] petList() {
        return new Object[][] {
                { 1, "Cat" },
                { 2, "Cat"},
                { 3, "Cat" }
        };
    }

    @Test
    @UseDataProvider("petList")
    public void findPetById(int id, String category) {
        given().
                accept("application/json").
                when().
                get("/api/v3/pet/"+id).
                then().
                    statusCode(HttpStatus.SC_OK)
                .body("id", equalTo(id))
                .body("category.id", equalTo(2))
                .body("category.name", equalTo(category+"s"))
                .body("name", equalTo(category + " "+ id))
                .body("photoUrls", hasItems("url1", "url2"))
                .body("tags.id", hasItems(1, 2))
                .body("tags.name", hasItems(anyOf(equalTo("tag1"), equalTo("tag2"), equalTo("tag3"), equalTo("tag4"))))
                .body("status", anyOf(equalTo("available"), equalTo("pending")))
                .body(JsonSchemaValidator.matchesJsonSchemaInClasspath("schema.json"));
    }

    @Test
    public void findPetsByStatus() {
        given()
                .accept("application/json")
                .queryParam("status", "available")
                .when()
                .get("/api/v3/pet/findByStatus")
                .then()
                .statusCode(HttpStatus.SC_OK)
                .body("id", hasItems(1, 2, 4, 7, 8, 9, 10))
                .body("category.name", everyItem(anyOf(is("Cats"), is("Dogs"), is("Lions"), is("Rabbits")))) // Ensure each category name is one of the specified values
                .body("status", everyItem(equalTo("available")))
                .log().all()
        ;
    }

    @Test
    public void addNewPet() {
        String newPetJson = "{"
                + "\"id\": 10,"
                + "\"name\": \"Henry Test\","
                + "\"category\": {"
                + "\"id\": 2,"
                + "\"name\": \"Cats\""
                + "},"
                + "\"photoUrls\": [\"string\"],"
                + "\"tags\": [{"
                + "\"id\": 0,"
                + "\"name\": \"string\""
                + "}],"
                + "\"status\": \"available\""
                + "}";

        given()
                .accept("application/json")
                .contentType("application/json")
                .body(newPetJson)
                .when()
                .post("/api/v3/pet")
                .then()
                .statusCode(HttpStatus.SC_OK)
                .body("id", equalTo(10))
                .body("name", equalTo("Henry Test"))
                .body("category.id", equalTo(2))
                .body("category.name", equalTo("Cats"))
                .body("photoUrls", hasItems("string"))
                .body("tags.id", hasItems(0))
                .body("tags.name", hasItems("string"))
                .body("status", equalTo("available"));
    }


    @Test
    public void updateANewPet() {
        String newPetJson = "{"
                + "\"id\": 11,"
                + "\"name\": \"Henry Test\","
                + "\"category\": {"
                + "\"id\": 2,"
                + "\"name\": \"Cats\""
                + "},"
                + "\"photoUrls\": [\"string\"],"
                + "\"tags\": [{"
                + "\"id\": 0,"
                + "\"name\": \"string\""
                + "}],"
                + "\"status\": \"pending\""
                + "}";

        // Create a new pet
        given()
                .accept("application/json")
                .contentType("application/json")
                .body(newPetJson)
                .when()
                .post("/api/v3/pet")
                .then()
                .statusCode(HttpStatus.SC_OK);

        String updatePetJson = "{"
                + "\"id\": 11,"
                + "\"name\": \"Henry Updated\","
                + "\"category\": {"
                + "\"id\": 2,"
                + "\"name\": \"Cats\""
                + "},"
                + "\"photoUrls\": [\"string\"],"
                + "\"tags\": [{"
                + "\"id\": 0,"
                + "\"name\": \"tagUpdated\""
                + "}],"
                + "\"status\": \"available\""
                + "}";

        // Update the existing pet
        given()
                .accept("application/json")
                .contentType("application/json")
                .body(updatePetJson)
                .when()
                .put("/api/v3/pet")
                .then()
                .statusCode(HttpStatus.SC_OK);

        // Validate the updated pet
        given()
                .accept("application/json")
                .when()
                .get("/api/v3/pet/" + 11)
                .then()
                .statusCode(HttpStatus.SC_OK)
                .body("id", equalTo(11))
                .body("category.id", equalTo(2))
                .body("category.name", equalTo("Cats"))
                .body("name", equalTo("Henry Updated"))
                .body("photoUrls", hasItem("string"))
                .body("tags.id", hasItem(0))
                .body("tags.name", hasItem("tagUpdated"))
                .body("status", equalTo("available"))
                .body(JsonSchemaValidator.matchesJsonSchemaInClasspath("schema.json"));
    }


    @Test
    public void deletePet() {
        String petJson = "{"
                + "\"id\": 12,"
                + "\"name\": \"Henry to Delete\","
                + "\"category\": {"
                + "\"id\": 2,"
                + "\"name\": \"Cats\""
                + "},"
                + "\"photoUrls\": [\"string\"],"
                + "\"tags\": [{"
                + "\"id\": 0,"
                + "\"name\": \"string\""
                + "}],"
                + "\"status\": \"pending\""
                + "}";

        // Create a pet to be deleted
        given()
                .accept("application/json")
                .contentType("application/json")
                .body(petJson)
                .when()
                .post("/api/v3/pet")
                .then()
                .statusCode(HttpStatus.SC_OK);

        // Delete the created pet
        given()
                .accept("application/json")
                .when()
                .delete("/api/v3/pet/" + 12)
                .then()
                .statusCode(HttpStatus.SC_OK); // Ensure delete request was successful

        // Verify that the pet has been deleted
        given()
                .accept("application/json")
                .when()
                .get("/api/v3/pet/" + 12)
                .then()
                .statusCode(HttpStatus.SC_NOT_FOUND); // Ensure the pet no longer exists
    }

}
