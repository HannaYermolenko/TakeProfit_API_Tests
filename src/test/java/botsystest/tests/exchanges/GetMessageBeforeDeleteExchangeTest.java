package botsystest.tests.exchanges;

import botsystest.tests.core.TestBase;
import io.restassured.http.ContentType;
import io.restassured.response.Response;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.testng.annotations.Test;

import java.util.List;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.equalTo;
import static org.testng.Assert.assertTrue;

public class GetMessageBeforeDeleteExchangeTest extends TestBase {
    Logger logger = LoggerFactory.getLogger(GetMessageBeforeDeleteExchangeTest.class);


    @Test
    public void testGetExchangeBeforePositiveDeleteNoBots() {
      String idExchangeWithoutBots = app.createExchange(false);
        Response response = given()
                .header(app.AUTH, "Bearer " + app.TOKEN)
                .contentType(ContentType.JSON)
                .when()
                .get("exchanges/" + idExchangeWithoutBots+ "/preliminary")
                .then()
                .statusCode(200)
                .body("message", equalTo("This exchange does not have bots, deletion is safe"))
                .extract()
                .response();
        app.deleteExchange(idExchangeWithoutBots);
        logger.info(response.asString());

    }

    @Test
    public void testGetExchangeBeforeDeletePositiveWithBots() {
        String idExchangeWithBots = app.createExchange(true);
        String idBot = app.createBotId("DOGEUSDT", "Long", 5, true, true, true,
                "RSI", 14, "1m", idExchangeWithBots);

        Response response = given()
                .header(app.AUTH, "Bearer " + app.TOKEN)
                .contentType(ContentType.JSON)
                .when()
                .get("exchanges/" + idExchangeWithBots + "/preliminary")
                .then()
                .statusCode(200)
                .body("message", equalTo("After deleting the exchange, the bots will be deactivated."))
                .extract()
                .response();
        List<String> botIds = response.jsonPath().getList("bots.id");
        assertTrue(botIds.contains(idBot), "Bot ID not found in the response!");

        app.deleteExchange(idExchangeWithBots);
        app.deleteOneBot(idBot);
        logger.info(response.asString());

    }

}
