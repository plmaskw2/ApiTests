package credentials_service.tests;

import com.github.javafaker.Faker;
import io.restassured.RestAssured;
import io.restassured.builder.RequestSpecBuilder;
import io.restassured.specification.RequestSpecification;
import model.User;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.*;

import static io.restassured.RestAssured.given;

public class E2ETest {

    RequestSpecification spec;
    User user;
    User userUpdated;
    String id;

    @BeforeEach
    public void setUp() {
        RestAssured.baseURI = "http://192.168.0.121";
        RestAssured.port = 8085;
        spec = new RequestSpecBuilder()
                .setContentType("application/json")
                .build();
        user = new User();
        userUpdated = new User();
        id = null;
    }

    @Test
    public void addUser() {
        user = User.builder()
                .username("newUser" + new Faker().name().username())
                .password("pass" + new Faker().internet().password())
                .build();

        //Add user
        given()
                .spec(spec)
                .basePath("users")
                .body(user)
                .when()
                .post()
                .then().log().all().statusCode(201);

        //Verify created user
        id = given()
                .spec(spec)
                .pathParam("username", user.getUsername())
                .basePath("users/username/{username}")
                .when()
                .get()
                .then().log().all()
                .body("username", Matchers.equalTo(user.getUsername()))
                .body("password", Matchers.equalTo(user.getPassword()))
                .extract().jsonPath().get("id");

        //Update user
        userUpdated = User.builder()
                .id(user.getId())
                .username("newUserUpdated" + new Faker().name().username())
                .password("passUpdated" + new Faker().internet().password())
                .build();
        given()
                .spec(spec)
                .pathParam("id", id)
                .basePath("users/{id}")
                .when()
                .body(userUpdated)
                .put()
                .then().log().all()
                .statusCode(200);

        //Verify updated user
        given()
                .spec(spec)
                .pathParam("id", id)
                .basePath("users/id/{id}")
                .when()
                .get()
                .then().log().all()
                .body("username", Matchers.equalTo(userUpdated.getUsername()))
                .body("password", Matchers.equalTo(userUpdated.getPassword()));

        //Delete user
        given()
                .spec(spec)
                .pathParam("id", id)
                .basePath("users/{id}")
                .when()
                .delete()
                .then().log().all().statusCode(204);

        //Verify deleted user
        given()
                .spec(spec)
                .pathParam("id", id)
                .basePath("users/id/{id}")
                .when()
                .get()
                .then().log().all()
                .statusCode(404);
    }
}
