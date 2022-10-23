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
    //логин под существующим пользователем
    @Test
    public void userloginTest() {
        UserCredentials creds = UserCredentials.from(user);
        Boolean isOk = userClient.login(creds)
                .extract().path("success");
        assertTrue(isOk);
    }
    //логин с неверным логином и паролем
    @Test
    public void userloginTestWrongLoginPassword() {
        UserCredentials creds = UserCredentials.getWrongLoginPassword(user);
        String massage = userClient.login(creds)
                .extract().path("message");
        assertEquals("email or password are incorrect",massage);
    }
}
