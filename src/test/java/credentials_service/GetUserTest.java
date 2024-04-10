package credentials_service;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import io.restassured.RestAssured;
import io.restassured.builder.RequestSpecBuilder;
import io.restassured.specification.RequestSpecification;
import model.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.lang.reflect.Type;
import java.util.List;
import java.util.stream.Collectors;

import static io.restassured.RestAssured.given;

public class GetUserTest {

    RequestSpecification spec;

    @BeforeEach
    public void setUp() {
        RestAssured.baseURI = "http://192.168.0.115";
        RestAssured.port = 8085;
        spec = new RequestSpecBuilder()
                .setContentType("application/json")
                .build();
    }

    @Test
    public void getUser() {
        User user;
        String username = "user4604";

        String model = given().log().all()
                .pathParam("username", username)
                .basePath("users/username/{username}")
                .contentType("application/json")
                .when().get().getBody().asString();

        user = new Gson().fromJson(model, User.class);
        System.out.println(user);
    }

    @Test
    public void getUsers() {
        Type listType = new TypeToken<List<User>>() {
        }.getType();

        String model = given().log().all()
                .basePath("users")
                .contentType("application/json")
                .when().get().getBody().asString();

        List<User> userList = new Gson().fromJson(model, listType);

        List<String> ids = userList.stream().map(user -> user.getId()).collect(Collectors.toList());
        ids.stream().forEach(id ->
                given()
                        .spec(spec)
                        .pathParam("id", id)
                        .basePath("users/{id}")
                        .when()
                        .delete()
                        .then().log().all().statusCode(204)
        );
    }
}
