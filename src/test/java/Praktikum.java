import io.restassured.RestAssured;
import io.restassured.response.Response;
import org.junit.Before;
import org.junit.Test;

import java.util.Random;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.*;

public class Praktikum {

    @Before
    public void setUp() {
        RestAssured.baseURI = "https://qa-mesto.praktikum-services.ru";
    }

    @Test
    public void registrationAndAuth() {
        Random random = new Random();
        String email = "something" + random.nextInt(10000000) + "@yandex.ru";
        String json = "{\"email\": \"" + email + "\", \"password\": \"aaa\" }";

        // Регистрация
        given()
                .header("Content-type", "application/json")
                .body(json)
                .post("/api/signup")
                .then().statusCode(201);
        // Авторизация
        Response response = given()
                .header("Content-type", "application/json")
                .body(json)
                .post("/api/signin");

        response.then().assertThat().body("token", notNullValue())
                .and().statusCode(200);
// Попытка зарегистрироваться с теми же параметрами ещё раз
        given()
                .header("Content-type", "application/json")
                .body(json)
                .post("/api/signup")
                .then()
                // Проверь, что статус ответа изменился
                .statusCode(409);
    }
}