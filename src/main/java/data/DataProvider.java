package data;

import com.github.javafaker.Faker;
import model.User;

public class DataProvider {

    public static User getUser() {
        return User.builder()
                .username(new Faker().name().username())
                .password(new Faker().crypto().md5())
                .build();
    }
}
