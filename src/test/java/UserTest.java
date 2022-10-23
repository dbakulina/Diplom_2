import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import user.User;
import user.UserClient;

import static org.junit.Assert.assertEquals;

public class UserTest {
    User user;
    UserClient userClient;
    @Before
    public void setup() {
        user = User.getRandomUser();
        userClient = new UserClient();
    }
    //создать уникального пользователя;
    @Test
    public void userCreateTest() {
        userClient.create(user)
                .assertThat()
                .statusCode(200);
        userClient.delete(user);
    }
    //создать пользователя, который уже зарегистрирован;
    @Test
    public void userCreateAlreadyExistsTest() {
        userClient.create(user);
        String userAlreadyExists = userClient.create(user)
                .assertThat()
                .statusCode(403)
        .extract().path("message");
        assertEquals("User already exists", userAlreadyExists);
        userClient.delete(user);
    }
    //создать пользователя и не заполнить одно из обязательных полей.
    @Test
    public void userCreateWithoutPasswordTest() {
        User userWithoutPassword = User.getWithoutPassword();
        String expected = userClient.create(userWithoutPassword)
                .assertThat()
                .statusCode(403)
                .extract().path("message");
        assertEquals("Email, password and name are required fields", expected);
    }
}
