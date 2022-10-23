import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import user.User;
import user.UserClient;
import user.UserCredentials;
import static org.junit.Assert.assertEquals;

public class GetOrdersTest {
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
    //Проверить что авторизованный пользователь получает заказ
    @Test
    public void getOrdersWithAuth() {
        UserCredentials creds = UserCredentials.from(user);
        String token = userClient.login(creds)
                .extract().path("accessToken");
        String json = "{\"ingredients\": [\"61c0c5a71d1f82001bdaaa6d\",\"61c0c5a71d1f82001bdaaa6f\"]}";
        int total = userClient.getOrders(token)
                .extract().path("total");
        userClient.createOrder(token,json);
        userClient.getOrders(token);
        int newTotal = userClient.getOrders(token)
                .extract().path("total");
        System.out.println(total);
        assertEquals(total + 1, newTotal);
    }
    //Проверить при попытке получить заказ неавторизованным пользователем вернется ошибка
    @Test
    public void getOrdersWithoutAuth() {
        String message = userClient.getOrdersWithoutAuth()
                .extract().path("message");
        assertEquals("You should be authorised", message);
    }
}
