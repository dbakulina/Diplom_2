import io.qameta.allure.junit4.DisplayName;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import user.User;
import user.UserClient;
import user.UserCredentials;

import static org.junit.Assert.*;

public class LoginTest {
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
    @DisplayName("Проверить что можно залогиниться под существующим пользователем")
    public void userloginTest() {
        UserCredentials creds = UserCredentials.from(user);
        Boolean isOk = userClient.login(creds).extract().path("success");
        assertTrue(isOk);
    }

    @Test
    @DisplayName("Проверить что нельзя залогиниться с неверным логином и паролем")
    public void userloginTestWrongLoginPassword() {
        UserCredentials creds = UserCredentials.getWrongLoginPassword(user);
        String massage = userClient.login(creds).extract().path("message");
        assertEquals("email or password are incorrect", massage);
    }
}
