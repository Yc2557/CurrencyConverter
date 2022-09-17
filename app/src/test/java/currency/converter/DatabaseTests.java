package currency.converter;

import org.junit.jupiter.api.*;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.util.List;
import java.util.ArrayList;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

import javax.xml.crypto.Data;

import static org.junit.jupiter.api.Assertions.*;

public class DatabaseTests {

    JSONObject database;

    FileReader fr;

    // Building a sample database for testing
    @BeforeAll
    void buildSampleDatabase() {
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

//            // Setting the database to
//            this.database = database;

        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Test
    void testGetConvesion() {
        DatabaseManager dbm = new DatabaseManager(true);

        assertEquals(dbm.getConversion("EUR", "AUD"), 1.468);
        assertEquals(dbm.getConversion("AUD", "EUR"), 0.681);
        assertEquals(dbm.getConversion("EUR", "USD"), 0.994);

        assertEquals(dbm.getConversion("EUR", "XRP"), 0);
        assertEquals(dbm.getConversion("XRP", "USD"), 0);
    }

    @Test
    void testAddCurrency() {
        DatabaseManager dbm = new DatabaseManager(true);


    }

    @Test
    void testGetPopularCurrencies() {
        DatabaseManager dbm = new DatabaseManager(true);

        ArrayList<String> popular = new ArrayList<String>() {{
            add("USD");
            add("EUR");
            add("SGD");
            add("JPY");
        }};

        assertEquals(dbm.getPopularCurrencies(), popular);
    }

    @Test
    void testAddPopularCurrencies() {
        DatabaseManager dbm = new DatabaseManager(true);

        ArrayList<String> popular = new ArrayList<String>() {{
            add("HKD");
            add("GBP");
            add("CHF");
            add("KRW");
        }};

        dbm.addPopularCurrencies(popular);

        assertEquals(dbm.getPopularCurrencies(), popular);
    }

    @Test
    void testConversionIncreased() {
        DatabaseManager dbm = new DatabaseManager(true);

        assertTrue(dbm.conversionIncreased("AUD", "USD"));
        assertFalse(dbm.conversionIncreased("USD", "AUD"));

        assertTrue(dbm.conversionIncreased("USD", "EUR"));
        assertFalse(dbm.conversionIncreased("USD", "EUR"));

        // Not enough historical data
        assertNull(dbm.conversionIncreased("USD", "SGD"));

        // Non-existent currency
        assertNull(dbm.conversionIncreased("USD", "XRP"));
    }

    @Test
    void testCheckDate() {
        DatabaseManager dbm = new DatabaseManager(true);

        assertEquals(dbm.checkDate("USD"), "10-09-2022");
        assertNull(dbm.checkDate("XRP"));
    }
}