import org.apache.commons.lang3.RandomStringUtils;
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
    //Проверить что авторизованный пользователь может изменить любое поле данных
    @Test
    public void changeDataUserWithAutorization() {
        UserCredentials creds = UserCredentials.from(user);
        String token = userClient.login(creds)
                .extract().path("accessToken");
        String email = userClient.login(creds)
                .extract().path("user.email");
        String name = userClient.login(creds)
                .extract().path("user.name");
        String password = userClient.login(creds)
                .extract().path("user.password");
        System.out.println(token);
        userClient.getDataUser(token);
        String json = "{\"email\": \"" + RandomStringUtils.randomAlphabetic(6) + "@ru.ru\", \"name\": \""+ RandomStringUtils.randomAlphabetic(6) +"\"}";
        String newName = userClient.changeDataUser(token,json)
                .extract().body().path("user.name");
        String newEmail = userClient.changeDataUser(token,json)
                .extract().body().path("user.email");
        Assert.assertNotEquals(name, newName);
        Assert.assertNotEquals(email, newEmail);
        userClient.deleteChange(token);
    }
    //проверить что нельзя изменить данные пользователя без авторизации
    @Test
    public void changeDataUserWithoutAutorization() {
        UserCredentials creds = UserCredentials.from(user);
        userClient.login(creds);
        String json = "{\"email\": \"" + RandomStringUtils.randomAlphabetic(6) + "@ru.ru\", \"name\": \""+ RandomStringUtils.randomAlphabetic(6) +"\"}";
        userClient.changeDataUserWithoutAutorization(json)
                .statusCode(401);
        String message = userClient.changeDataUserWithoutAutorization(json)
                .extract().body().path("message");
        Assert.assertEquals("You should be authorised", message);
        userClient.delete(user);
    }
}
