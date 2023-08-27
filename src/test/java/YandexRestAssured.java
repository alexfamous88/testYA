import io.restassured.RestAssured;
import io.restassured.response.Response;
import org.junit.Test;

public class YandexRestAssured {

    public static final String LOGIN = "popk3n";
    public static final String PASSWORD = "wAMVqoC";

    @Test
    public void testFilesInDisk() {

        // Авторизация


        // Получение списка всех файлов на Диске

        Response response = RestAssured.get("https://cloud-api.yandex.net/v1/disk/resources/files").andReturn();
        response.prettyPrint();

        // Разлогин

    }

}
