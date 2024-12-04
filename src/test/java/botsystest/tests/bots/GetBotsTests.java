package botsystest.tests.bots;

import botsystest.tests.core.TestBase;
import io.restassured.http.ContentType;
import io.restassured.response.Response;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.testng.annotations.Test;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.*;

public class GetBotsTests extends TestBase {
    Logger logger = LoggerFactory.getLogger(GetBotsTests.class);

    @Test
    public void getMyBotsPositiveTest() {
        Response response = given()
                .header(app.AUTH, "Bearer " + app.TOKEN)
                .contentType(ContentType.JSON)
                .when()
                .get("bots")
                .then()
                .statusCode(200)
                .body("size()", greaterThanOrEqualTo(0))
                .extract()
                .response();
        logger.info(response.body().asString());
    }

    @Test
    public void getOneBotPositiveTest() {
        String botId = app.createBotId(
                "ETHUSDT", "Long", 100, true, true, false, "RSI", 14, "1m", app.getIdDemoExchange()
        );

        try {
            Response response = given()
                    .header(app.AUTH, "Bearer " + app.TOKEN)
                    .contentType(ContentType.JSON)
                    .when()
                    .get("bots/" + botId)
                    .then()
                    .statusCode(200)
                    .body("id", notNullValue())
                    .body("exchangeId", equalTo(app.getIdDemoExchange()))
                    .body("tradingPair", equalTo("ETHUSDT"))
                    .body("type", equalTo("Long"))
                    .body("deposit", equalTo(100.0F))
                    .body("stopLoss", equalTo(true))
                    .body("takeProfit", equalTo(true))
                    .body("pumpDump", equalTo(false))
                    .body("status", equalTo("stop"))
                    .body("createdAt", notNullValue())
                    .extract()
                    .response();

            logger.info("Bot details: " + response.body().asString());

        } finally {

            app.deleteOneBot(botId);
            logger.info("Bot with ID " + botId + " was deleted.");
        }
    }

}
