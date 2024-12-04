package botsystest.tests.bots;

import botsystest.tests.core.TestBase;
import io.restassured.http.ContentType;
import io.restassured.response.Response;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import java.util.*;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.notNullValue;

public class CreateSuccessfulBotsTests extends TestBase {
        Logger logger = LoggerFactory.getLogger(CreateSuccessfulBotsTests.class);
        String tradingPair = "EURUSDT";
        String type = "Short";
        float deposit = 0.08F;
        boolean stopLoss = false;
        boolean takeProfit = false;
        boolean pumpDump = false;
        String indicator = "STOCHASTIC";
        int period = 7;
        String interval = "5m";
        String botId;
        String idConnectedExch;
        String idNotConnectedExch;

        @BeforeMethod
        public void preConditions() {
                idConnectedExch = app.createExchange(true);
                idNotConnectedExch = app.createExchange(true, "MyApiKey", "MyApiSecret");
        }

    @Test
    public void botCreatePositiveTest() {
            Map<String, Object> requestBody = new HashMap<>();
            requestBody.put("exchangeId", idConnectedExch);
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
                    .statusCode(201)
                    .body("id", notNullValue())
                    .body("exchangeId", equalTo(idConnectedExch))
                    .body("tradingPair", equalTo(tradingPair))
                    .body("type", equalTo(type))
                    .body("deposit", equalTo(deposit))
                    .body("stopLoss", equalTo(stopLoss))
                    .body("takeProfit", equalTo(takeProfit))
                    .body("pumpDump", equalTo(pumpDump))
                    .body("status", equalTo("stop"))
                    .body("indicators[0].indicator", equalTo(indicator))
                    .body("indicators[0].period", equalTo(period))
                    .body("indicators[0].interval", equalTo(interval))
                    .extract()
                    .response();
            botId = response.jsonPath().getString("id");
            logger.info(response.asString());
        }

        @Test
        public void botCreatePositiveDemoTest() {
                String exchangeId = app.getIdDemoExchange();
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
                        .statusCode(201)
                        .body("id", notNullValue())
                        .body("exchangeId", equalTo(exchangeId))
                        .body("tradingPair", equalTo(tradingPair))
                        .body("type", equalTo(type))
                        .body("deposit", equalTo(deposit))
                        .body("stopLoss", equalTo(stopLoss))
                        .body("takeProfit", equalTo(takeProfit))
                        .body("pumpDump", equalTo(pumpDump))
                        .body("status", equalTo("stop"))
                        .body("indicators[0].indicator", equalTo(indicator))
                        .body("indicators[0].period", equalTo(period))
                        .body("indicators[0].interval", equalTo(interval))
                        .extract()
                        .response();
                botId = response.jsonPath().getString("id");
                logger.info(response.asString());
        }



        @AfterMethod
        public void postCondition(){
            app.deleteOneBot(botId);
            app.deleteExchange(idConnectedExch);
            app.deleteExchange(idNotConnectedExch);
                logger.info("Exchange with ID " + idConnectedExch + " was deleted.");
                logger.info("Exchange with ID " + idNotConnectedExch + " was deleted.");
                logger.info("Bot with ID " + botId+ " was deleted.");
        }

    }

