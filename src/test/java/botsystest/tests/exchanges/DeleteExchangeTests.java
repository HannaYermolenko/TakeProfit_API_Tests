package botsystest.tests.exchanges;

import botsystest.tests.core.TestBase;
import io.restassured.http.ContentType;
import io.restassured.response.Response;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.testng.annotations.Test;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.equalTo;

public class DeleteExchangeTests extends TestBase {
    Logger logger = LoggerFactory.getLogger(DeleteExchangeTests.class);

    @Test
    public void testDeleteExchangePositive() {
        String exchId = app.createExchange(false);
        Response response = given()
                .header(app.AUTH, "Bearer " + app.TOKEN)
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
}
