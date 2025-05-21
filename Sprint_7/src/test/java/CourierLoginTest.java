import io.qameta.allure.junit4.DisplayName;
import io.qameta.allure.Step;
import io.restassured.RestAssured;
import io.restassured.response.Response;
import org.example.Courier;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import static io.restassured.RestAssured.given;
import static org.junit.Assert.*;

public class CourierLogin {

    private Courier courier;
    private static final String BASE_URI = "https://qa-scooter.praktikum-services.ru";
    private static final String COURIER_PATH = "/api/v1/courier";
    private static final String LOGIN_PATH = "/api/v1/courier/login";

    @Before
    @Step("Подготовка тестовых данных")
    public void setup() {
        RestAssured.baseURI = BASE_URI;
        courier = new Courier(
                "ninja_" + System.currentTimeMillis(),
                "12345",
                "TestCourier"
        );

        Response createResponse = given()
                .header("Content-type", "application/json")
                .body(courier)
                .post(COURIER_PATH);

        assertEquals("Неверный статус-код при создании курьера",
                201, createResponse.statusCode());
        assertTrue("Поле 'ok' должно быть true",
                createResponse.jsonPath().getBoolean("ok"));
    }

    @After
    @Step("Очистка тестовых данных")
    public void cleanup() {
        Response loginResponse = given()
                .header("Content-type", "application/json")
                .body(new Courier(courier.getLogin(), courier.getPassword(), null))
                .post(LOGIN_PATH);

        if (loginResponse.statusCode() == 200) {
            int courierId = loginResponse.jsonPath().getInt("id");
            Response deleteResponse = given()
                    .header("Content-type", "application/json")
                    .delete(COURIER_PATH + "/" + courierId);

            assertEquals("Неверный статус-код при удалении курьера",
                    200, deleteResponse.statusCode());
        }
    }

    @Test
    @DisplayName("Позитивный тест: успешная авторизация")
    public void successfulLoginWithValidCredentials() {
        Response response = given()
                .header("Content-type", "application/json")
                .body(courier)
                .post(LOGIN_PATH);

        assertEquals("Неверный статус-код при авторизации",
                200, response.statusCode());
        assertNotNull("ID курьера не должен быть null",
                response.jsonPath().get("id"));
    }

    @Test
    @DisplayName("Негативный тест: неверный пароль")
    public void loginWithInvalidPasswordShouldFail() {
        Courier invalidCourier = new Courier(
                courier.getLogin(),
                "wrong_password",
                null
        );

        Response response = given()
                .header("Content-type", "application/json")
                .body(invalidCourier)
                .post(LOGIN_PATH);

        assertEquals("Неверный статус-код при неверном пароле",
                404, response.statusCode());
        assertEquals("Неверное сообщение об ошибке",
                "Учетная запись не найдена", response.jsonPath().getString("message"));
    }

    @Test
    @DisplayName("Негативный тест: отсутствие логина")
    public void loginWithoutLoginShouldFail() {
        Courier noLoginCourier = new Courier(null, courier.getPassword(), null);

        Response response = given()
                .header("Content-type", "application/json")
                .body(noLoginCourier)
                .post(LOGIN_PATH);

        assertEquals("Неверный статус-код при отсутствии логина",
                400, response.statusCode());
        assertEquals("Неверное сообщение об ошибке",
                "Недостаточно данных для входа", response.jsonPath().getString("message"));
    }

    @Test
    @DisplayName("Негативный тест: пустые данные")
    public void loginWithEmptyCredentialsShouldFail() {
        Response response = given()
                .header("Content-type", "application/json")
                .body(new Courier("", "", null))
                .post(LOGIN_PATH);

        assertEquals("Неверный статус-код при пустых данных",
                400, response.statusCode());
        assertEquals("Неверное сообщение об ошибке",
                "Недостаточно данных для входа", response.jsonPath().getString("message"));
    }

    @Test
    @DisplayName("Негативный тест: несуществующий пользователь")
    public void loginNonExistentCourierShouldFail() {
        Courier nonExistentCourier = new Courier(
                "nonexistent_" + System.currentTimeMillis(),
                "12345",
                null
        );

        Response response = given()
                .header("Content-type", "application/json")
                .body(nonExistentCourier)
                .post(LOGIN_PATH);

        assertEquals("Неверный статус-код для несуществующего пользователя",
                404, response.statusCode());
        assertEquals("Неверное сообщение об ошибке",
                "Учетная запись не найдена", response.jsonPath().getString("message"));
    }
}