package botsystest.tests.core;

import org.testng.annotations.Test;

import static io.restassured.RestAssured.given;

public class TestHelper extends TestBase {

    @Test
    public void HelperTests() {
        //get information about user
        //  app.getUserInfo();
        System.out.println("--------------");
        //Print get all exchange with connect flag "true"
//        for (int i = 0; i < app.getIdConnectedExchange().length; i++) {
//            System.out.println(app.getIdConnectedExchange()[i]);
//        }
       // System.out.println(app.getAllExchanges().length);

        //delete All exchanges
//        for (int i = 0; i < app.getAllExchanges().length; i++) {
//
//            app.deleteExchange(app.getAllExchanges()[i]);
//        }
            //delete all bots
//        app.deleteAllBotsExceptOne("674308e10c04b02d5a4ffe31");
//        System.out.println(app.getAllBots().length);
//        System.out.println(app.getIdConnectedExchange().length);



       int x = app.getAllBots().length;
        System.out.println(x);






    }
}
