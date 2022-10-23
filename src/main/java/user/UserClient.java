package user;

import config.Config;
import io.restassured.response.ValidatableResponse;

import static io.restassured.RestAssured.given;


public class UserClient {
    private final String ROOT = "/auth/register";
    private final String LOGIN= "/auth/login ";
    private final String USER= "/auth/user ";
    private final String ORDERS= "/orders ";
    private final String INGREDIENTS= "/ingredients ";

    public ValidatableResponse create(User user) {
        return given().log().all()
                .header("Content-Type", "application/json")
                .baseUri(Config.BASE_URL)
                .body(user)
                .when()
                .post(ROOT)
                .then().log().all();
    }
    //DELETE https://stellarburgers.nomoreparties.site/api/auth/user
    public ValidatableResponse delete(User user) {
        UserCredentials creds = UserCredentials.from(user);
        String token = login(creds)
                .extract().path("accessToken");
        return given().log().all()
                .header("Content-Type", "application/json")
                .baseUri(Config.BASE_URL)
                .body(user)
                .auth().oauth2(token.substring(7))
                .when()
                .delete(USER)
                .then().log().all();
    }
    public ValidatableResponse deleteChange(String token) {

        return given().log().all()
                .header("Content-Type", "application/json")
                .baseUri(Config.BASE_URL)

                .auth().oauth2(token.substring(7))
                .when()
                .delete(USER)
                .then().log().all();
    }
        public ValidatableResponse login(UserCredentials creds) {
        return given().log().all()
                .header("Content-Type", "application/json")
                .baseUri(Config.BASE_URL)
                .body(creds)
                .when()
                .post(LOGIN)
                .then().log().all();
    }
    public ValidatableResponse getDataUser(String token) {
        return given().log().all()
                .header("Content-Type", "application/json")
                .baseUri(Config.BASE_URL)
                .auth().oauth2(token.substring(7))
                .get(USER)
                .then().log().all();
    }
    public ValidatableResponse changeDataUser(String token, String json) {
        return  given().log().all()
                .baseUri(Config.BASE_URL)
                //.auth().oauth2(token.substring(7))
                .header("Content-Type", "application/json")
                .auth().oauth2(token.substring(7))
                .body(json)
                .when()
                .patch(USER)
                .then().log().all();
    }
    public ValidatableResponse changeDataUserWithoutAutorization(String json) {
        return  given().log().all()
                .baseUri(Config.BASE_URL)
                .header("Content-Type", "application/json")
                .body(json)
                .when()
                .patch(USER)
                .then().log().all();
    }
    public ValidatableResponse createOrder (String token, String json) {
        return given().log().all()
                .header("Content-Type", "application/json")
                .body(json)
                .baseUri(Config.BASE_URL)
                .auth().oauth2(token.substring(7))
                .post(ORDERS)
                .then().log().all();
    }
    public ValidatableResponse createOrderWithoutAutorization (String json) {
        return given().log().all()
                .header("Content-Type", "application/json")
                .body(json)
                .baseUri(Config.BASE_URL)
                .post(ORDERS)
                .then().log().all();
    }
    public ValidatableResponse getIngredients() {
        return given().log().all()
                .header("Content-Type", "application/json")
                .baseUri(Config.BASE_URL)
                .get(INGREDIENTS)
                .then().log().all();
    }
    public ValidatableResponse getOrders (String token) {
        return given().log().all()
                .header("Content-Type", "application/json")
                .baseUri(Config.BASE_URL)
                .auth().oauth2(token.substring(7))
                .get(ORDERS)
                .then().log().all();
    }
    public ValidatableResponse getOrdersWithoutAuth () {
        return given().log().all()
                .header("Content-Type", "application/json")
                .baseUri(Config.BASE_URL)
                .get(ORDERS)
                .then().log().all();
    }
}




