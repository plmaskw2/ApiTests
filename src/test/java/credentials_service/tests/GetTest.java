package credentials_service.tests;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.sun.tools.xjc.reader.xmlschema.bindinfo.BIConversion;
import io.restassured.RestAssured;
import io.restassured.builder.RequestSpecBuilder;
import io.restassured.path.json.JsonPath;
import io.restassured.specification.RequestSpecification;
import model.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.lang.reflect.Type;
import java.util.List;

public class GetTest {

    RequestSpecification spec = new RequestSpecBuilder().build();

    @BeforeEach
    public void setup() {
        spec
                .baseUri("http://192.168.0.121")
                .port(8085)
                .contentType("application/json");
    }

    @Test
    public void getUsers() {
        Type listType = new TypeToken<List<User>>() {}.getType();

        String usersJson = RestAssured
                .given()
                .spec(spec)
                .basePath("/users")
                .when()
                .get()
                .then()
                .log()
                .all()
                .statusCode(200)
                .extract().body().asString();

        List<User> users = new Gson().fromJson(usersJson, listType);

        System.out.println(users);
    }
    @Test
    public void getUserByName() {
        User user = User.builder()
                .username("NoExistingUser")
                .build();

        RestAssured
                .given()
                .spec(spec)
                .pathParam("username", user.getUsername())
                .basePath("/users/username/{username}")
                .when()
                .get()
                .then()
                .statusCode(404);
    }

    @Test
    public void getUserById() {
        int id = 1;

        RestAssured
                .given()
                .spec(spec)
                .pathParam("id", id)
                .basePath("/users/id/{id}")
                .when()
                .get()
                .then()
                .statusCode(200);
    }
}
