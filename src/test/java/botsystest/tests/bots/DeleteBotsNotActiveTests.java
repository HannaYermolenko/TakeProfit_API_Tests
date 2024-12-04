package botsystest.tests.bots;

import botsystest.tests.core.TestBase;
import io.restassured.http.ContentType;
import io.restassured.response.Response;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.equalTo;

public class DeleteBotsNotActiveTests extends TestBase {
    Logger logger = LoggerFactory.getLogger(DeleteBotsNotActiveTests.class);
    String botId;

    @BeforeMethod
    public void preConditions() {
       botId = app.createBotId("ETHUSDT", "Long", 100, true, true, false, "RSI", 14, "1m", app.getIdDemoExchange());

    }

    @Test
    public void botsDeleteSuccessfulPositiveTest() {
        Response response = given()
                .header(app.AUTH, "Bearer " + app.TOKEN)
                .contentType(ContentType.JSON)
                .when()
                .delete("bots/" + botId)
                .then()
                .statusCode(200)
                .body("id", equalTo(botId))
                .extract()
                .response();
        logger.info(response.asString());
    }

}
