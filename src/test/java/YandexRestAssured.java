import org.junit.Test;

import java.util.Base64;

import static io.restassured.RestAssured.given;
import static java.nio.charset.StandardCharsets.UTF_8;

public class YandexRestAssured {

    private static final String LOGIN = "popk3n";
    private static final String PASSWORD = "wAMVqoC";
    private static final String CLIENT_ID = "d5670b5da3d14e818c5a80142379aaea";
    private static final String CLIENT_SECRET = "a3764926351941c79fcecbc332d8b62e";

    private final String ENCODED_LOG_PASS = Base64.getEncoder()
            .encodeToString(
                    String.format("%s:%s", CLIENT_ID, CLIENT_SECRET).getBytes(UTF_8)
            );

    @Test
    public void test_files_in_disk() {

/*        given()
                .queryParams(
                        "response_type", "code",
                        "client_id", CLIENT_ID,
                        "force_confirm", "yes"
                )
                .log().all()
                .when()
                .post("https://oauth.yandex.ru/authorize")
                .then()
                .log().all()
                .statusCode(200);*/

        // Авторизация
        String accessToken = given()
                .contentType("application/x-www-form-urlencoded")
                .header("Authorization", "Basic " + ENCODED_LOG_PASS)
                .params(
                        "grant_type", "authorization_code",
                        "code", 3280430
                )
                .when()
                .post("https://oauth.yandex.ru/token")
                .then()
                .log().all()
                .statusCode(200)
                .extract().
                path("access_token");

        System.out.println("accessToken = " + accessToken);

        // Получение списка всех файлов на Диске

//        Response response = get("https://cloud-api.yandex.net/v1/disk/resources/files").andReturn();
//        response.prettyPrint();

        given()
                .header("Authentication", "OAuth " + accessToken)
                .when()
                .get("https://oauth.yandex.ru/token")
                .then()
                .log().all()
                .statusCode(200);

        // Разлогин

    }

}
