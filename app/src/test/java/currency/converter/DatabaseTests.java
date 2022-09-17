package currency.converter;

import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.junit.jupiter.api.*;

import java.io.*;
import java.util.ArrayList;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

import static org.junit.jupiter.api.Assertions.*;

public class DatabaseTests {

    JSONObject database;

    FileReader fr;

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
            SGD.put("data", SGD_data);

            File file = new File("database.json");
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
    void testGetConversion() {
        DatabaseManager dbm = new DatabaseManager("src/main/resources/database.json");

        assertEquals(Math.round(dbm.getConversion("EUR", "AUD") * 1000) / 1000.0, 1.477);
        assertEquals(Math.round(dbm.getConversion("AUD", "EUR") * 1000) / 1000.0, 0.677);
        assertEquals(Math.round(dbm.getConversion("EUR", "USD") * 1000) / 1000.0, 1.01);

        assertEquals(dbm.getConversion("EUR", "XRP"), 0);
        assertEquals(dbm.getConversion("XRP", "USD"), 0);
    }

    @Test
    void testAddCurrency() throws IOException, ParseException {
        DatabaseManager dbm = new DatabaseManager("src/main/resources/database.json");

        JSONParser jsonParser = new JSONParser();
        JSONObject database = (JSONObject) jsonParser.parse(new FileReader("src/main/resources/database.json"));

        JSONArray rates = (JSONArray) database.get("rates");

        dbm.addCurrency("NZD");
        assertEquals(DatabaseManager.getConversionIndex("NZD", rates), 3); //NZD in index 3
    }

    @Test
    void testGetPopularCurrencies() {
        DatabaseManager dbm = new DatabaseManager("src/main/resources/database.json");

        ArrayList<String> popular = new ArrayList<String>() {{
            add("HKD");
            add("GBP");
            add("CHF");
            add("KRW");
        }};

        assertEquals(dbm.getPopularCurrencies(), popular);
    }

    @Test
    void testAddPopularCurrencies() {
        DatabaseManager dbm = new DatabaseManager("src/main/resources/database.json");

        ArrayList<String> popular = new ArrayList<String>() {
            {
                add("HKD");
                add("GBP");
                add("CHF");
                add("KRW");
            }
        };

        dbm.addPopularCurrencies(popular);

        assertEquals(dbm.getPopularCurrencies(), popular);
    }

    @Test
    void testConversionIncreased() {
        DatabaseManager dbm = new DatabaseManager("src/main/resources/database.json");

        assertTrue(dbm.conversionIncreased("AUD", "USD"));
        assertFalse(dbm.conversionIncreased("USD", "AUD"));

        assertTrue(dbm.conversionIncreased("EUR", "USD"));
        assertFalse(dbm.conversionIncreased("USD", "EUR"));

        // Not enough historical data
        assertNull(dbm.conversionIncreased("USD", "SGD"));

        // Non-existent currency
        assertNull(dbm.conversionIncreased("USD", "XRP"));
    }

    @Test
    void testCheckDate() {
        DatabaseManager dbm = new DatabaseManager("src/main/resources/database.json");

        assertEquals(dbm.checkDate("USD"), "10-09-2022");
        assertNull(dbm.checkDate("XRP"));
    }
}
