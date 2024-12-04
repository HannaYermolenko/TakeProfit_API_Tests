package botsystest.tests.exchanges;

import botsystest.tests.core.TestBase;
import io.restassured.http.ContentType;
import io.restassured.response.Response;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.testng.annotations.Test;

import static io.restassured.RestAssured.given;

public class GetExchangesTests extends TestBase {
    Logger logger = LoggerFactory.getLogger(GetExchangesTests.class);

    @Test
    public void GetExchangeSuccessfulPositiveTest() {
        Response response = given()
                .header(app.AUTH, "Bearer " + app.TOKEN)
                .contentType(ContentType.JSON)
                .when()
                .get("exchanges")
                .then()
                .statusCode(200)
                .extract()
                .response();
        logger.info(response.asString());
    }


    @Test
    public void GetOneExchangeSuccessfulPositiveTest() {
        String exchConnId = app.getIdDemoExchange();
        Response response = given()
                .header(app.AUTH, "Bearer " + app.TOKEN)
                .contentType(ContentType.JSON)
                .when()
                .get("exchanges/" + exchConnId)
                .then()
                .statusCode(200)
                .extract()
                .response();
        logger.info(response.body().asString());
    }
}
