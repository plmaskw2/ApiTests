package credentials_service.tests;

import com.github.javafaker.Faker;
import data.DataProvider;
import io.restassured.RestAssured;
import io.restassured.builder.RequestSpecBuilder;
import io.restassured.specification.RequestSpecification;
import model.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class AddTest {

    RequestSpecification spec = new RequestSpecBuilder().build();
    User user;

    @BeforeEach
    public void setup() {
        spec
                .baseUri("http://192.168.0.121")
                .port(8085)
                .basePath("/users")
                .contentType("application/json");
        user = DataProvider.getUser();
    }

    @Test
    public void addUser() {
        RestAssured.given()
                .spec(spec)
                .when()
                .body(user)
                .post()
                .then()
                .statusCode(201);
    }
}
