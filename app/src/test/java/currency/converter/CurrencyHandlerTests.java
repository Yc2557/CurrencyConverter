package currency.converter;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.io.FileWriter;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import static org.junit.jupiter.api.Assertions.*;

public class CurrencyHandlerTests {
    private final CurrencyHandler currencyHandler = new CurrencyHandler(true, "src/test/resources/CurrencyHandlerTest.json");

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

//          JPY
            JSONObject JPY = new JSONObject();
            JPY.put("rate", "JPY");
            JSONArray JPY_data = new JSONArray();
            JSONObject JPY_rate = new JSONObject();
            JPY_rate.put("date", "01-09-2022");
            JPY_rate.put("rate", 0.550);

            JPY_data.add(JPY_rate);
            JPY.put("data", JPY_data);

            //put all currencies into rate array
            rates.add(USD);
            rates.add(EUR);
            rates.add(SGD);
            rates.add(JPY);

            database.put("rates", rates);

            File file = new File("src/test/resources/CurrencyHandlerTest.json");
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
    public void displayPopularTest() {

        String[][] result = currencyHandler.displayPopular();

        assertEquals("-", result[0][0]);
        assertEquals("-", result[1][1]);
        assertEquals("0.99 (D)", result[0][1]);
        assertEquals("0.80 (D)", result[0][3]);
        assertEquals("0.71 (D)", result[2][1]);

    }

    @Test
    public void addCurrencyTest() {
        boolean result1 = currencyHandler.addCurrency("NZD");
        assertTrue(result1);
        boolean result2 = currencyHandler.addCurrency("NZD");
        assertFalse(result2);
    }

    //collateHistoryResults tests are done in CalculatorTests

    @Test
    public void updateConversionTest() { //to from???
        assertFalse(currencyHandler.updateCurrency("USD", "SGD", 1.5f, LocalDate.now()));
        assertTrue(currencyHandler.updateCurrency("USD", "AUD", 1.5f, LocalDate.now()));

        assertEquals(150.00, Math.round(currencyHandler.convertCurrency(100, "USD", "AUD")* 1000) / 1000.0);
    }

    @Test
    public void getCurrenciesTest() { //to from???
        List<String> expected = new ArrayList<>();
        expected.add("AUD");
        expected.add("USD");
        expected.add("EUR");
        expected.add("SGD");
        expected.add("JPY");

        assertEquals(expected, currencyHandler.getCurrencies());
    }

    @Test
    public void convertCurrencyTest() {
        assertEquals(1.90, currencyHandler.convertCurrency(2f, "AUD", "SGD"));
        assertEquals(2.11, Math.round(currencyHandler.convertCurrency(2f, "SGD", "AUD")*100) / 100.0);

        assertEquals(1.44, Math.round(currencyHandler.convertCurrency(2f, "SGD", "USD")*100) / 100.0);
        assertEquals(2.78, Math.round(currencyHandler.convertCurrency(2f, "USD", "SGD")*100) / 100.0);
    }

    @Test
    public void getPopularCurrenciesTest() {
        List<String> expected = new ArrayList<>();
        expected.add("USD");
        expected.add("EUR");
        expected.add("SGD");
        expected.add("JPY");

        assertEquals(expected, currencyHandler.getPopularCurrencies());
    }

    @Test
    public void updatePopularTest() {
        assertTrue(currencyHandler.updatePopular("USD", "JPY", "SGD", "EUR"));
        assertFalse(currencyHandler.updatePopular("USD", "JPY", "SGD", "ABD"));
    }

    @Test
    public void printConversionHistoryTest() {
        String start = "01-09-2022";
        String end = "03-09-2022";

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy");
        LocalDate startDate = LocalDate.parse(start, formatter);
        LocalDate endDate = LocalDate.parse(end, formatter);

        assertNotNull(currencyHandler.printConversionHistory("AUD","SGD",startDate, endDate));
        assertNull(currencyHandler.printConversionHistory("ASD","SGD",startDate, endDate));
    }
}
