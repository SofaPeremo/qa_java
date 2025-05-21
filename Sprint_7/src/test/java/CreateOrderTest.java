import io.qameta.allure.junit4.DisplayName;
import io.qameta.allure.Step;
import io.restassured.RestAssured;
import io.restassured.response.Response;
import org.example.Order;
import org.example.Track;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;

import java.util.Arrays;
import java.util.List;
import java.util.Collection;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.*;

@RunWith(Parameterized.class)
public class CreateOrderTest {
    private static final String BASE_URI = "https://qa-scooter.praktikum-services.ru";
    private static final String CREATE_ORDER_PATH = "/api/v1/orders";
    private static final String CANCEL_ORDER_PATH = "/api/v1/orders/cancel";

    private final List<String> color;
    private Integer trackNumber;

    public CreateOrderTest(List<String> color) {
        this.color = color;
    }

    @Parameterized.Parameters(name = "Цвет самоката: {0}")
    public static Collection<Object[]> getColors() {
        return Arrays.asList(new Object[][]{
                {Arrays.asList("BLACK")},
                {Arrays.asList("GREY")},
                {Arrays.asList("BLACK", "GREY")},
                {null}
        });
    }

    @Before
    @Step("Настройка тестового окружения")
    public void setup() {
        RestAssured.baseURI = BASE_URI;
    }

    @After
    @Step("Отмена созданного заказа")
    public void cleanup() {
        if (trackNumber != null) {
            given()
                    .contentType("application/json")
                    .body(new Track(trackNumber))  // Используем объект вместо строки
                    .when()
                    .put(CANCEL_ORDER_PATH)
                    .then()
                    .statusCode(200)
                    .body("ok", equalTo(true));
        }
    }

    @Step("Создание нового заказа с цветом: {colors}")
    private void createNewOrder(List<String> colors) {
        Order order = new Order(
                "Naruto",
                "Uchiha",
                "Konoha, 142 apt.",
                4,
                "+7 800 355 35 35",
                5,
                "2020-06-06",
                "Saske, come back to Konoha",
                colors
        );

        Response response = given()
                .contentType("application/json")
                .body(order)
                .when()
                .post(CREATE_ORDER_PATH)
                .then()
                .statusCode(201)
                .body("track", notNullValue())
                .extract().response();

        trackNumber = response.jsonPath().getInt("track");
    }

    @Test
    @DisplayName("Проверка создания заказа с различными вариантами цвета самоката")
    public void shouldCreateOrderWithDifferentColorOptions() {
        createNewOrder(color);
    }
}