package botsystest.tests.timestream;

import botsystest.tests.core.TestBase;
import io.restassured.http.ContentType;
import io.restassured.response.Response;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import java.util.List;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.equalTo;
import static org.testng.Assert.assertTrue;

public class CheckStreamAfterChangeBot extends TestBase {
    Logger logger = LoggerFactory.getLogger(CheckStreamAfterChangeBot.class);
    String exchangeId;
    String myBotId;

    @BeforeMethod
    public void beforeMethod() {
        exchangeId = app.createExchange(false);
        myBotId = app.createBotId("ETHUSDT", "Long", 5, false, false, false, "RSI", 14, "1m", exchangeId);

    }

    @Test
    public void CheckStreamAfterChangeBotPositiveTest() {
        Response respAfterStartBot = given()
                .header(app.AUTH, "Bearer " + app.TOKEN)
                .contentType(ContentType.JSON)
                .body("{ \"status\": \"Start\" }")
                .when()
                .patch("bots/" + myBotId + "/status")
                .then()
                .statusCode(200)
                .body("status", equalTo("Start"))
                .extract()
                .response();

        logger.info(respAfterStartBot.asString());

        String requestBodyForUpdate = "{\n" +
                "  \"deposit\": 5,\n" +
                "  \"stopLoss\": true,\n" +
                "  \"takeProfit\": true,\n" +
                "  \"pumpDump\": true,\n" +
                "  \"indicators\": [\n" +
                "    {\n" +
                "      \"indicator\": \"EMA\",\n" +
                "      \"period\": 14,\n" +
                "      \"interval\": \"1m\"\n" +
                "    }\n" +
                "  ]\n" +
                "}";

        Response respAfterUpdateBot = given()
                .header(app.AUTH, "Bearer " + app.TOKEN)
                .contentType(ContentType.JSON)
                .body(requestBodyForUpdate)
                .when()
                .patch("bots/" + myBotId)
                .then()
                .statusCode(200)
                .body("status", equalTo("Stop"))
                .extract()
                .response();

        logger.info(respAfterUpdateBot.asString());

        Response response = given()
                .header("Authorization", "Bearer " + app.TOKEN)
                .contentType("application/json")
                .when()
                .get("bots/active") // URL для получения активных ботов
                .then()
                .statusCode(200)  // Ожидаемый статус код 200
                .extract()
                .response();

        List<String> botIds = response.jsonPath().getList("botId");
        assertTrue(!botIds.contains(myBotId), "Bot with ID " + myBotId + " should not be found in the active bots list after stopping it.");
    }

    @AfterMethod
    public void postCondition(){
        app.deleteOneBot(myBotId);
        app.deleteExchange(exchangeId);
    }
}
