package botsystem.core;

import io.github.cdimascio.dotenv.Dotenv;
import io.restassured.http.ContentType;
import io.restassured.response.Response;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.*;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.containsString;
import static org.hamcrest.Matchers.equalTo;

public class ApplicationManager {
    private static final Dotenv dotenv = Dotenv.configure()
            .filename(".env")
            .ignoreIfMalformed()
            .ignoreIfMissing()
            .load();

    public final String AUTH = "Authorization";
    public String TOKEN;
    Logger logger = LoggerFactory.getLogger(ApplicationManager.class);

    public final String emailGetToken = System.getProperty("email", dotenv.get("EMAIL_GET_TOKEN"));
    public final String passwordGetToken = System.getProperty("password", dotenv.get("PASSWORD_GET_TOKEN"));
    public final String apiKeyExchange = System.getProperty("apiKeyExchange", dotenv.get("API_KEY_EXCHANGE"));
    public final String secretExchange = System.getProperty("secretExchange", dotenv.get("SECRET_EXCHANGE"));


    public String getTOKEN() {
        UserRequest userRequest = UserRequest.builder()
                .email(emailGetToken)
                .password(passwordGetToken)
                .build();
        String responseToken = given()
                .contentType(ContentType.JSON)
                .body(userRequest)
                .when()
                .post("login")
                .then()
                .assertThat()
                .statusCode(200)
                .body(containsString("token"))
                .extract().path("token");
        return responseToken;
    }

    public String getUserInfo() {
        Response response = given()
                .header(AUTH, "Bearer " + TOKEN)
                .contentType(ContentType.JSON)
                .when()
                .get("users")
                .then()
                .statusCode(200)
                .extract()
                .response();
        String userEmail = response.jsonPath().getString("email");
        System.out.println("Email: " + userEmail);
        String fullUserInfo = response.asString();
        System.out.println("Full info: " + fullUserInfo);
        return fullUserInfo;
    }

    public String[] getIdConnectedExchange() {
        Response response = given()
                .header(AUTH, "Bearer " + TOKEN)
                .contentType(ContentType.JSON)
                .when()
                .get("exchanges")
                .then()
                .statusCode(200)
                .extract()
                .response();
        List<Map<String, Object>> exchanges = response.jsonPath().getList("");
        List<String> connectedIds = new ArrayList<>();
        for (Map<String, Object> exchange : exchanges) {
            if (Boolean.TRUE.equals(exchange.get("connect"))) {
                connectedIds.add((String) exchange.get("id"));
            }
        }
        return connectedIds.toArray(new String[0]);
    }

    public String[] getIdNotConnectedExchange() {
        Response response = given()
                .header(AUTH, "Bearer " + TOKEN)
                .contentType(ContentType.JSON)
                .when()
                .get("exchanges")
                .then()
                .statusCode(200)
                .extract()
                .response();
        List<Map<String, Object>> exchanges = response.jsonPath().getList("");
        List<String> connectedIds = new ArrayList<>();
        for (Map<String, Object> exchange : exchanges) {
            if (Boolean.FALSE.equals(exchange.get("connect"))) {
                connectedIds.add((String) exchange.get("id"));

            }
        }
        return connectedIds.toArray(new String[0]);

    }

    public String createExchange(Boolean flag) {
        String nameExchange = "NewExchange";
        Boolean isDefault = flag;
        String binance = "Binance";
        String requestBody = "{\n" +
                "  \"name\": \"" + nameExchange + "\",\n" +
                "  \"apiKey\": \"" + apiKeyExchange + "\",\n" +
                "  \"secretKey\": \"" + secretExchange + "\",\n" +
                "  \"exchange\": \"" + binance + "\",\n" +
                "  \"isDefault\": " + isDefault + "\n" +
                "}";
        Response response = given()
                .header(AUTH, "Bearer " + TOKEN)
                .contentType(ContentType.JSON)
                .body(requestBody)
                .when()
                .post("exchanges")
                .then()
                .statusCode(201)
                .extract()
                .response();
        return response.jsonPath().getString("id");

    }

    public String createExchange(Boolean flag, String myApiKey, String mySecretKey) {
        String nameExchange = "NewExchange";
        Boolean isDefault = flag;
        String binance = "Binance";
        String requestBody = "{\n" +
                "  \"name\": \"" + nameExchange + "\",\n" +
                "  \"apiKey\": \"" + myApiKey + "\",\n" +
                "  \"secretKey\": \"" + mySecretKey + "\",\n" +
                "  \"exchange\": \"" + binance + "\",\n" +
                "  \"isDefault\": " + isDefault + "\n" +
                "}";
        Response response = given()
                .header(AUTH, "Bearer " + TOKEN)
                .contentType(ContentType.JSON)
                .body(requestBody)
                .when()
                .post("exchanges")
                .then()
                .statusCode(201)
                .extract()
                .response();
        return response.jsonPath().getString("id");

    }

    public String getIdDemoExchange() {
        Response response = given()
                .header(AUTH, "Bearer " + TOKEN)
                .contentType(ContentType.JSON)
                .when()
                .get("exchanges")
                .then()
                .statusCode(200)
                .extract()
                .response();
        List<Map<String, Object>> exchanges = response.jsonPath().getList("");
        for (Map<String, Object> exchange : exchanges) {
            if ("demo".equals(exchange.get("type"))) {
                return (String) exchange.get("id");
            }
        }
        throw new RuntimeException("Demo exchange not found");
    }

    public String[] getUserPaar() {
        String exchId = createExchange(false);
        Response response = given()
                .header(AUTH, "Bearer " + TOKEN)
                .contentType(ContentType.JSON)
                .when()
                .get("symbols/" + exchId)
                .then()
                .statusCode(200)
                .extract()
                .response();
        List<Map<String, Object>> paarInfs = response.jsonPath().getList("");
        List<String> paars = new ArrayList<>();
        for (Map<String, Object> paarInf : paarInfs) {
            paars.add((String) paarInf.get("symbol"));
        }
        return paars.toArray(new String[0]);
    }

    public void deleteExchange(String exchId) {
        Response response = given()
                .header(AUTH, "Bearer " + TOKEN)
                .contentType(ContentType.JSON)
                .when()
                .delete("exchanges/" + exchId)
                .then()
                .statusCode(200)
                .body("id", equalTo(exchId))
                .extract()
                .response();
        logger.info(response.asString());
    }

    public String[] getAllBots() {
        Response response = given()
                .header(AUTH, "Bearer " + TOKEN)
                .contentType(ContentType.JSON)
                .when()
                .get("bots")
                .then()
                .statusCode(200)
                .extract()
                .response();
        List<Map<String, Object>> allBots = response.jsonPath().getList("");
        List<String> bootsId = new ArrayList<>();
        for (Map<String, Object> botId : allBots) {
            bootsId.add((String) botId.get("id"));
        }

        return bootsId.toArray(new String[0]);

    }

    public String createBotId(String tradingPair, String type, double deposit, boolean stopLoss,
                              boolean takeProfit, boolean pumpDump, String indicator,
                              int period, String interval, String exchangeId) {

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
                .header("Authorization", "Bearer " + TOKEN)
                .contentType(ContentType.JSON)
                .body(requestBody)
                .when()
                .post("/bots")
                .then()
                .statusCode(201)
                .extract()
                .response();

        return response.jsonPath().getString("id");
    }

    public void deleteOneBot(String botId) {
        Response response = given()
                .header(AUTH, "Bearer " + TOKEN)
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

    public String[] getAllExchanges() {
        Response response = given()
                .header(AUTH, "Bearer " + TOKEN)
                .contentType(ContentType.JSON)
                .when()
                .get("exchanges")
                .then()
                .statusCode(200)
                .extract()
                .response();
        List<Map<String, Object>> allExch = response.jsonPath().getList("");
        List<String> exchIds = new ArrayList<>();
        for (Map<String, Object> exch : allExch) {
            if ("real".equals(exch.get("type"))) {
                exchIds.add((String) exch.get("id"));
            }
        }
        return exchIds.toArray(new String[0]);
    }

    public void deleteAllBotsExceptOne(String myBot) {
        while (true) {
            String[] allBots = getAllBots();
            boolean botDeleted = false;
            for (String botId : allBots) {
                if (!botId.equals(myBot)) {
                    deleteOneBot(botId);
                    botDeleted = true;
                    break;
                }
            }
            if (!botDeleted) {
                break;
            }
        }
    }

    public void createBotsFromAvailablePairs(
            int numberOfBots,
            String type,
            double deposit,
            boolean stopLoss,
            boolean takeProfit,
            boolean pumpDump,
            String indicator,
            int period,
            String interval,
            String exchangeId
    ) {
        String[] userPaar = getUserPaar();
        int availablePairs = userPaar.length;
        int botsToCreate = Math.min(numberOfBots, availablePairs);
        System.out.println("Quantity of bots: " + botsToCreate);
        List<String> botsIds = new ArrayList<>();
        for (int i = 0; i < botsToCreate; i++) {
            String pair = userPaar[i];
            String botId = createBotId(
                    pair,
                    type,
                    deposit,
                    stopLoss,
                    takeProfit,
                    pumpDump,
                    indicator,
                    period,
                    interval,
                    exchangeId
            );
            botsIds.add(botId);
        }
        System.out.println("\nId bots:");
        for (String id : botsIds) {
            System.out.println(id);
        }
    }

    public String changeBotStatus(String botId, String status) {
        if (!status.equals("Start") && !status.equals("Stop")) {
            throw new IllegalArgumentException("Invalid status: " + status + ". Allowed values are 'Start' or 'Stop'.");
        }
        logger.info("Changing bot status. Bot ID: {}, Status: {}", botId, status);
        Response response = given()
                .header(AUTH, "Bearer " + TOKEN)
                .contentType(ContentType.JSON)
                .body("{ \"status\": \"" + status + "\" }")
                .when()
                .patch( "bots/" + botId + "/status")
                .then()
                .statusCode(200)
                .body("status", equalTo(status))
                .extract()
                .response();
        logger.info("Response for bot ID {}: {}", botId, response.asString());
        String botIdFromResponse = response.jsonPath().getString("id");
        String statusFromResponse = response.jsonPath().getString("status");
        return String.format("%s\n%s", botIdFromResponse, statusFromResponse);
    }


}
