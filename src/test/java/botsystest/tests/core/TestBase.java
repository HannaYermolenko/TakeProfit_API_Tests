package botsystest.tests.core;

import botsystem.core.ApplicationManager;
//import ch.qos.logback.classic.Logger;
import io.restassured.RestAssured;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.testng.annotations.BeforeMethod;

import static io.restassured.RestAssured.given;

public class TestBase {
    Logger logger = LoggerFactory.getLogger(TestBase.class);

    protected final ApplicationManager app = new ApplicationManager();

    @BeforeMethod
    public void init() {
        //https://takeprofit.tech
        RestAssured.baseURI = "https://takeprofit.tech/api/";
        RestAssured.basePath = "v1";


        app.TOKEN = app.getTOKEN();
        //  logger.info(TOKEN);
    }


}







