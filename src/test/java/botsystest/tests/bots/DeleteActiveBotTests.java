package botsystest.tests.bots;

import botsystest.tests.core.TestBase;
import io.restassured.http.ContentType;
import io.restassured.response.Response;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.equalTo;

public class DeleteActiveBotTests extends TestBase {
    Logger logger = LoggerFactory.getLogger(DeleteActiveBotTests.class);
    String idConnectedExch;
    String idRunningBot;

    @BeforeMethod
    public void preConditions() {
        idConnectedExch = app.createExchange(true);
        idRunningBot = app.createBotId("ETHUSDT", "Long", 5, true, true, false, "RSI", 14, "1m", idConnectedExch);

    }

    @Test
    public void botsDeleteRunningNegativeTest() {
        String respAfterStartBot = app.changeBotStatus(idRunningBot,"Start");
        logger.info(respAfterStartBot);
        Response response = given()
                .header(app.AUTH, "Bearer " + app.TOKEN)
                .contentType(ContentType.JSON)
                .when()
                .delete("bots/" + idRunningBot)
                .then()
                .statusCode(400)
                .body("message", equalTo("Cannot delete running bot"))
                .extract()
                .response();
        logger.info(response.asString());

    }


    @AfterMethod
    public void postCondition(){
        app.changeBotStatus(idRunningBot,"Stop");
        app.deleteOneBot(idRunningBot);
        app.deleteExchange(idConnectedExch);
    }
}
