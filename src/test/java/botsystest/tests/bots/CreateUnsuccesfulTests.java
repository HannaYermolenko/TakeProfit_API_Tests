package botsystest.tests.bots;

import botsystest.tests.core.TestBase;
import io.restassured.http.ContentType;
import io.restassured.response.Response;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import static io.restassured.RestAssured.given;

public class CreateUnsuccesfulTests extends TestBase {
    Logger logger = LoggerFactory.getLogger(CreateUnsuccesfulTests.class);
    String tradingPair = "EURUSDT";
    String type = "Short";
    float deposit = 0.08F;
    boolean stopLoss = false;
    boolean takeProfit = false;
    boolean pumpDump = false;
    String indicator = "STOCHASTIC";
    int period = 7;
    String interval = "5m";
    String idConnectedExch;
    String idNotConnectedExch;

    @BeforeMethod
    public void preConditions() {
        idConnectedExch = app.createExchange(true);
        idNotConnectedExch = app.createExchange(true, "MyApiKey", "MyApiSecret");
    }


    @Test
    public void botCreateNegativeNotConnectExchTest() {
        String exchangeId = app.getIdNotConnectedExchange()[0];
        Map<String, Object> requestBody = new HashMap<>();
        requestBody.put("exchangeId", exchangeId);
        requestBody.put("tradingPair", tradingPair);
        requestBody.put("type", type);
        requestBody.put("deposit", deposit);
        requestBody.put("stopLoss", stopLoss);
        requestBody.put("takeProfit", takeProfit);
        requestBody.put("pumpDump", pumpDump);

        Map<String, Object> indicatorMap = new HashMap<>();
        indicatorMap.put("indicator", indicator);
        indicatorMap.put("period", period);
        indicatorMap.put("interval", interval);
        requestBody.put("indicators", Collections.singletonList(indicatorMap));


        Response response = given()
                .header("Authorization", "Bearer " + app.TOKEN)
                .contentType(ContentType.JSON)
                .body(requestBody)
                .when()
                .post("/bots")
                .then()
                .statusCode(403)
                .extract()
                .response();
        logger.info(response.asString());
    }

    @AfterMethod
    public void postCondition(){
        app.deleteExchange(idConnectedExch);
        app.deleteExchange(idNotConnectedExch);
        logger.info("Exchange with ID " + idConnectedExch + " was deleted.");
        logger.info("Exchange with ID " + idNotConnectedExch + " was deleted.");

    }

}
