/*
 * This Java source file was generated by the Gradle 'init' task.
 */
package currency.converter;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeAll;
import static org.junit.jupiter.api.Assertions.*;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.io.PrintStream;
import java.io.File;
import java.io.FileWriter;
import java.time.LocalDate;

class AppTest {
    private final String DATABASE_LOCATION = "src/test/resources/appTest.json";

    // Building a sample database for testing
    @BeforeAll
    static void buildSampleDatabase() {
        try {
            // Creating a database JSONObject
            JSONObject database = new JSONObject();
            database.put("base", "AUD");

            // Creating the popular currency JSONArray
            JSONArray popular = new JSONArray();
            popular.add("USD");
            popular.add("EUR");
            popular.add("SGD");
            popular.add("JPY");
            database.put("popular", popular);

            // Adding rate objects for USD, EUR and SGD
            JSONArray rates = new JSONArray();

            // USD
            JSONObject USD = new JSONObject();
            USD.put("rate", "USD");
            JSONArray USD_data = new JSONArray();
            JSONObject USD_rate1 = new JSONObject();
            USD_rate1.put("date", "01-09-2022");
            USD_rate1.put("rate", 0.677);
            JSONObject USD_rate2 = new JSONObject();
            USD_rate2.put("date", "10-09-2022");
            USD_rate2.put("rate", 0.684);

            USD_data.add(USD_rate1);
            USD_data.add(USD_rate2);

            USD.put("data", USD_data);

            // EUR
            JSONObject EUR = new JSONObject();
            EUR.put("rate", "EUR");
            JSONArray EUR_data = new JSONArray();
            JSONObject EUR_rate1 = new JSONObject();
            EUR_rate1.put("date", "01-09-2022");
            EUR_rate1.put("rate", 0.681);
            JSONObject EUR_rate2 = new JSONObject();
            EUR_rate2.put("date", "10-09-2022");
            EUR_rate2.put("rate", 0.677);

            EUR_data.add(EUR_rate1);
            EUR_data.add(EUR_rate2);

            EUR.put("data", EUR_data);

            // SGD
            JSONObject SGD = new JSONObject();
            SGD.put("rate", "SGD");
            JSONArray SGD_data = new JSONArray();
            JSONObject SGD_rate = new JSONObject();
            SGD_rate.put("date", "01-09-2022");
            SGD_rate.put("rate", 0.950);

            SGD_data.add(SGD_rate);

            SGD.put("data", SGD_data);

            // put all currencies into rate array
            rates.add(USD);
            rates.add(EUR);
            rates.add(SGD);

            database.put("rates", rates);

            File file = new File(DATABASE_LOCATION);
            file.createNewFile();
            FileWriter writer = new FileWriter(file);
            writer.write(database.toJSONString());

            writer.close();

            // // Setting the database to
            // this.database = database;

        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Test
    void appConvert() {
        // Test Convert method works as expected
        String userInput = "convert USD AUD 100\nexit";
        System.setIn(new ByteArrayInputStream(userInput.getBytes()));

        String expected = "The conversion of 100.00 USD is: 146.19 AUD";
        ByteArrayOutputStream output = new ByteArrayOutputStream();
        PrintStream pStream = new PrintStream(output);
        System.setOut(pStream);
        String[] args = new String[2];
        args[0] = "test";
        args[1] = DATABASE_LOCATION;

        App.main(args);
        String[] lines = output.toString().split("\n");
        String actual = lines[lines.length - 3];

        assertEquals(expected, actual);

        // Test for invalid currency and invalid arguments
    }

    @Test
    void appDisplayMethods() {
        // Test display
        // Test summary method
        // Test help method
        String userInput = "help\nexit";
        System.setIn(new ByteArrayInputStream(userInput.getBytes()));

        String expected = "help - displays this help menu";
        ByteArrayOutputStream output = new ByteArrayOutputStream();
        PrintStream pStream = new PrintStream(output);
        System.setOut(pStream);
        String[] args = new String[1];
        args[0] = "test";

        App.main(args);
        String[] lines = output.toString().split(System.lineSeparator());
        String actual = lines[lines.length - 3];

        assertEquals(expected, actual);
    }

    @Test
    void appUpdate() {

    }

    @Test
    void appUpdatePopular() {

    }

    @Test
    void appAdd() {

    }
}
