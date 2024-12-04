package botsystest.tests.exchanges;

import botsystem.core.ApplicationManager;
import botsystest.tests.core.TestBase;
import io.restassured.http.ContentType;
import io.restassured.response.Response;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.Test;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.notNullValue;

public class CreateExchangeTest extends TestBase {
    Logger logger = LoggerFactory.getLogger(CreateExchangeTest.class);
    String exchangeId;



    @Test
    public void CreateExchangePositiveTest() {
        String nameExchange = "19.11";
        Boolean isDefault = false;
        String binance = "Binance";
        String requestBody = "{\n" +
                "  \"name\": \"" + nameExchange + "\",\n" +
                "  \"apiKey\": \"" + app.apiKeyExchange + "\",\n" +
                "  \"secretKey\": \"" + app.secretExchange + "\",\n" +
                "  \"exchange\": \"" + binance + "\",\n" +
                "  \"isDefault\": " + isDefault + "\n" +
                "}";
        Response response = given()
                .header(app.AUTH, "Bearer " + app.TOKEN)
                .contentType(ContentType.JSON)
                .body(requestBody)
                .when()
                .post("exchanges")
                .then()
                .statusCode(201)
                .body("id", notNullValue())
                .body("usedId", notNullValue())
                .body("name", equalTo(nameExchange))
                .body("type", equalTo("real"))
                .body("exchange", equalTo(binance))
                .body("isDefault", equalTo(isDefault))
                .body("connect", equalTo(true))
                .body("message", equalTo("Connected successfully"))
                .extract()
                .response();
        exchangeId = response.jsonPath().getString("id");
        logger.info(response.body().asString());

    }

    @Test
    public void CreateExchangeConnectPositiveFalseTest() {
        String apiKeyInvalid = "pdK1XR6o1AMrPm0zlwp5E8HAvg0cgGb2dWH4wVnxmgc1ev9BXLfwWRFv2Erk4";
        String nameExchange = "19.11";
        Boolean isDefault = false;
        String binance = "Binance";
        String requestBody = "{\n" +
                "  \"name\": \"" + nameExchange + "\",\n" +
                "  \"apiKey\": \"" + apiKeyInvalid + "\",\n" +
                "  \"secretKey\": \"" + app.secretExchange + "\",\n" +
                "  \"exchange\": \"" + binance + "\",\n" +
                "  \"isDefault\": " + isDefault + "\n" +
                "}";
        Response response = given()
                .header(app.AUTH, "Bearer " + app.TOKEN)
                .contentType(ContentType.JSON)
                .body(requestBody)
                .when()
                .post("exchanges")
                .then()
                .statusCode(201)
                .body("id", notNullValue())
                .body("usedId", notNullValue())
                .body("name", equalTo(nameExchange))
                .body("type", equalTo("real"))
                .body("exchange", equalTo(binance))
                .body("isDefault", equalTo(isDefault))
                .body("connect", equalTo(false))
                .body("message", equalTo("Error while checking API status: {\"code\":-2008,\"msg\":\"Invalid Api-Key ID.\"}"))
                .extract()
                .response();
        exchangeId = response.jsonPath().getString("id");
        logger.info(response.body().asString());

    }

    @Test
    public void CreateExchangeConnectPositiveFalseTest2() {
        String secretKeyInvalid = "4OMiRuPGNujFLhJV6g949tWk9KMdU0aliE0n0UC1TgqDJdGEDRK9Z4q1Af";
        String nameExchange = "19.11";
        Boolean isDefault = false;
        String binance = "Binance";
        String requestBody = "{\n" +
                "  \"name\": \"" + nameExchange + "\",\n" +
                "  \"apiKey\": \"" + app.apiKeyExchange + "\",\n" +
                "  \"secretKey\": \"" + secretKeyInvalid + "\",\n" +
                "  \"exchange\": \"" + binance + "\",\n" +
                "  \"isDefault\": " + isDefault + "\n" +
                "}";
        Response response = given()
                .header(app.AUTH, "Bearer " + app.TOKEN)
                .contentType(ContentType.JSON)
                .body(requestBody)
                .when()
                .post("exchanges")
                .then()
                .statusCode(201)
                .body("id", notNullValue())
                .body("usedId", notNullValue())
                .body("name", equalTo(nameExchange))
                .body("type", equalTo("real"))
                .body("exchange", equalTo(binance))
                .body("isDefault", equalTo(isDefault))
                .body("connect", equalTo(false))
                .body("message", equalTo("Error while checking API status: {\"code\":-1022,\"msg\":\"Signature for this request is not valid.\"}"))
                .extract()
                .response();
        exchangeId = response.jsonPath().getString("id");
        logger.info(response.body().asString());

    }

    @AfterMethod
    public void postCondition(){
        app.deleteExchange(exchangeId);
    }








}
