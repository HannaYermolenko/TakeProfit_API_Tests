package botsystest.tests.traidingBinance;

import botsystem.utils.DataProviders;
import botsystest.tests.core.TestBase;
import io.restassured.http.ContentType;
import io.restassured.response.Response;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.testng.annotations.Test;

import static io.restassured.RestAssured.given;

public class TraidingFromBinanceTest extends TestBase {
    Logger logger = LoggerFactory.getLogger(TraidingFromBinanceTest.class);

    @Test
    public void CandlesPositiveTest() {
        String requestBody = "{\n" +
                "  \"interval\": \"1m\",\n" +
                "  \"period\": 10\n" +
                "}";

        Response response = given()
                .header(app.AUTH, "Bearer " + app.TOKEN)
                .contentType(ContentType.JSON)
                .body(requestBody) // Include the request body
                .when()
                .post("candles/EURUSDT")
                .then()
                .statusCode(200)
                .extract()
                .response();
      logger.info(response.getBody().asString());
    }


    @Test(dataProvider = "botData", dataProviderClass = DataProviders.class)
    public void CandlesCSVPositiveTest(String interval, String period, String para) {
        String requestBody = "{\n" +
                "  \"interval\": \"" + interval + "\",\n" +
                "  \"period\": " + period + "\n" +
                "}";
        Response response = given()
                .header(app.AUTH, "Bearer " + app.TOKEN)
                .contentType(ContentType.JSON)
                .body(requestBody) // Include the request body
                .when()
                .post("candles/" + para)
                .then()
                .statusCode(200)
                .extract()
                .response();
        logger.info(response.getBody().asString());
    }

}
