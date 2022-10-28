import io.qameta.allure.Step;
import io.qameta.allure.junit4.DisplayName;
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
    @After
    public void delete() {
        userClient.delete(user);
    }

    @Test
    @DisplayName("Проверить что можно создать уникального пользователя")
    public void userCreateTest() {
        userClient.create(user)
                .assertThat()
                .statusCode(200);
    }

    @Test
    @DisplayName("Проверить что нельзя создать пользователя который уже зарегистрирован")
    public void userCreateAlreadyExistsTest() {
        userClient.create(user);
        String userAlreadyExists = userClient.create(user)
                .assertThat()
                .statusCode(403)
                .extract().path("message");
        assertEquals("User already exists", userAlreadyExists);
    }

    @Test
    @DisplayName("Проверить что нельзя создать пользователя и не заполнить одно из обязательных полей")
    public void userCreateWithoutPasswordTest() {
        userClient.create(user);
        User userWithoutPassword = User.getWithoutPassword();
        String expected = userClient.create(userWithoutPassword)
                .assertThat()
                .statusCode(403)
                .extract().path("message");
        assertEquals("Email, password and name are required fields", expected);
    }
}
