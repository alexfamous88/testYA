import io.restassured.response.ValidatableResponse;
import org.junit.Test;
import static io.restassured.RestAssured.given;

public class YandexRestAssured {

    private static final String LOGIN = "popk3n";
    private static final String PASSWORD = "wAMVqoC";
    private static final String TOKEN = "y0_AgAAAAAxFeqxAADLWwAAAADrDoFtiIFFLOAmTeWNeklSafL49a5ZUXc";
    private static final String RESPONSE = "https://cloud-api.yandex.net/v1/disk/resources/files";


    @Test
    public void test_FilesInDisk() {

        ValidatableResponse getAllFiles = given()
                .header("Authorization", "OAuth " + TOKEN)
                .when()
                .get(RESPONSE)
                .then()
                .log().all()
                .statusCode(200);
    }
}