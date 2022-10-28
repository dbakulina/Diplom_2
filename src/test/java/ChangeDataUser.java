import io.qameta.allure.junit4.DisplayName;
import org.apache.commons.lang3.RandomStringUtils;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import user.User;
import user.UserClient;
import user.UserCredentials;

public class ChangeDataUser {
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
    @DisplayName("Проверить что авторизованный пользователь может изменить любое поле данных")
    public void changeDataUserWithAutorization() {
        UserCredentials creds = UserCredentials.from(user);
        String token = userClient.login(creds)
                .extract().path("accessToken");
        String email = userClient.login(creds)
                .extract().path("user.email");
        String name = userClient.login(creds)
                .extract().path("user.name");
        userClient.getDataUser(token);
        String json = "{\"email\": \"" + RandomStringUtils.randomAlphabetic(6) + "@ru.ru\", \"name\": \"" + RandomStringUtils.randomAlphabetic(6) + "\"}";
        String newName = userClient.changeDataUser(token, json)
                .extract().body().path("user.name");
        String newEmail = userClient.changeDataUser(token, json)
                .extract().body().path("user.email");
        Assert.assertNotEquals(name, newName);
        Assert.assertNotEquals(email, newEmail);
        String jsonNew = "{\"email\": \"" + email + "\", \"name\": \"" + name + "\"}";
        userClient.changeDataUser(token, jsonNew);
    }

    @Test
    @DisplayName("Проверить что нельзя изменить данные пользователя без авторизации")
    public void changeDataUserWithoutAutorization() {
        UserCredentials creds = UserCredentials.from(user);
        userClient.login(creds);
        String json = "{\"email\": \"" + RandomStringUtils.randomAlphabetic(6) + "@ru.ru\", \"name\": \"" + RandomStringUtils.randomAlphabetic(6) + "\"}";
        userClient.changeDataUserWithoutAutorization(json)
                .statusCode(401);
        String message = userClient.changeDataUserWithoutAutorization(json)
                .extract().body().path("message");
        Assert.assertEquals("You should be authorised", message);
    }
}
