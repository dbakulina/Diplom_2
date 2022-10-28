import io.qameta.allure.junit4.DisplayName;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import user.User;
import user.UserClient;
import user.UserCredentials;
import static org.junit.Assert.assertEquals;

public class CreateOrderTest {
    User user;
    UserClient userClient;

    @Before
    public void setup() {
        user = User.getRandomUser();
        userClient = new UserClient();
        userClient.create(user);
    }

    @After
    public void delete() {
        userClient.delete(user);
    }

    @Test
    @DisplayName("Проверить что можно получить данные об ингредиентах")
    public void getIngredients() {
        userClient.getIngredients();
    }

    @Test
    @DisplayName("Проверить что можно создать заказ с авторизацией и с ингредиентами")
    public void createOrderWithAutorizationWithIngredients() {
        UserCredentials creds = UserCredentials.from(user);
        String token = userClient.login(creds)
                .extract().path("accessToken");
        String json = "{\"ingredients\": [\"61c0c5a71d1f82001bdaaa6d\",\"61c0c5a71d1f82001bdaaa6f\"]}";
        userClient.createOrder(token, json)
                .statusCode(200);
        String name = userClient.createOrder(token, json)
                .extract().path("name");
        assertEquals("Флюоресцентный бессмертный бургер", name);
    }

    @Test
    @DisplayName("Проверить что можно создать заказ без авторизации и с ингредиентами")
    public void createOrderWithoutAutorizationWithIngredients() {
        String json = "{\"ingredients\": [\"61c0c5a71d1f82001bdaaa6d\",\"61c0c5a71d1f82001bdaaa6f\"]}";
        userClient.createOrderWithoutAutorization(json)
                .statusCode(200);
        String name = userClient.createOrderWithoutAutorization(json)
                .extract().path("name");
        assertEquals("Флюоресцентный бессмертный бургер", name);
    }

    @Test
    @DisplayName("Проверить что нельзя создать заказ с авторизацией и без ингредиентов")
    public void createOrderWithAutorizationWithoutIngredients() {
        UserCredentials creds = UserCredentials.from(user);
        String token = userClient.login(creds)
                .extract().path("accessToken");
        String json = "{\"ingredients\": []}";
        userClient.createOrder(token, json)
                .statusCode(400);
        String massage = userClient.createOrder(token, json)
                .extract().path("message");
        assertEquals("Ingredient ids must be provided", massage);
    }

    @Test
    @DisplayName("Проверить что нельзя создать заказ без авторизации и без ингредиентов")
    public void createOrderWithoutAutorizationWithoutIngredients() {
        String json = "{\"ingredients\": []}";
        userClient.createOrderWithoutAutorization(json)
                .statusCode(400);
        String massage = userClient.createOrderWithoutAutorization(json)
                .extract().path("message");
        assertEquals("Ingredient ids must be provided", massage);
    }

    @Test
    @DisplayName("Проверить что если передан невалидный хеш ингредиента, вернётся код ответа 500")
    public void createOrderWrongIngredients() {
        UserCredentials creds = UserCredentials.from(user);
        String token = userClient.login(creds)
                .extract().path("accessToken");
        String json = "{\"ingredients\": [\"рыба\",\"мясо\"]}";
        userClient.createOrder(token, json)
                .statusCode(500);
    }
}
