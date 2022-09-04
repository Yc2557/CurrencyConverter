package currency.converter;

import java.util.List;
import java.util.ArrayList;

import java.io.FileReader;
import java.io.IOException;
import java.util.Iterator;
import java.io.FileNotFoundException;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.*;

public class DatabaseManager {

    private boolean isAdmin;
    //private JSONParser jsonParser; <-- maybe?

    public DatabaseManager(boolean isAdmin) {
        this.isAdmin = isAdmin;
    }

    public float getRate(String curr1, String curr2) {
        try {
            // Parsing the .json database to a JSONObject
            JSONParser jsonParser = new JSONParser();
            JSONObject database = (JSONObject) jsonParser.parse(new
                    FileReader("database.json"));

            // Extracting the rates object from the database
            JSONObject rates = (JSONObject) database.get("rates");

            // Extracting the arrays for the respective currencies
            // which contain all the historical & present rates
            JSONArray arrayCurr1 = (JSONArray) rates.get(curr1);
            JSONArray arrayCurr2 = (JSONArray) rates.get(curr2);

            // Extracting the most recent exchange rates of the two currencies
            JSONObject curr1Obj = (JSONObject) arrayCurr1.get(arrayCurr1.size()-1);
            JSONObject curr2Obj = (JSONObject) arrayCurr2.get(arrayCurr2.size()-1);

            // Extracting the most recent rates for each of the currencies
            float rate1 = (float) curr1Obj.get("rate");
            float rate2 = (float) curr2Obj.get("rate");

            // Calculating & returning the finalised exchange rate between
            // the two currencies
            return rate2/rate1;

            // NOTE: must include error cases; i.e. passing through a non-existent
            // currency
        } catch (FileNotFoundException e) {
            System.out.println("File not found!");
            throw new RuntimeException(e);
        } catch (IOException e) {
            throw new RuntimeException(e);
        } catch (ParseException e) {
            throw new RuntimeException(e);
        }
    }

    public float getPastConversion(String curr1, String curr2, String startDate,
                                   String endDate) {
        return 0.000F;
    }

    public void addRate(String curr1, String curr2, float amount, String date) {

    }

    public List<String> getPopularCurrencies() {
        return null;
    }

    public boolean rateIncreased(String curr) {
        try {
            JSONParser jsonParser = new JSONParser();
            JSONObject database = (JSONObject) jsonParser.parse(new
                    FileReader("database.json"));

            JSONObject rates = (JSONObject) database.get("rates");
            JSONArray currArray = (JSONArray) rates.get(curr);
            // Checking if there is at least one other timestamp to compare with
            if (currArray.size() < 2) {
                return false;
            }

            // Getting the currency objects for the different timestamps
            JSONObject currObj1 = (JSONObject) currArray.get(currArray.size()-1);
            JSONObject currObj2 = (JSONObject) currArray.get(currArray.size()-2);

            // Extracting the rates at the current & previous timestamps
            float rate1 = (float) currObj1.get("rate");
            float rate2 = (float) currObj2.get("rate");

            // Returning whether the rate has increased or not
            return rate1-rate2>0;

        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        } catch (IOException e) {
            throw new RuntimeException(e);
        } catch (ParseException e) {
            throw new RuntimeException(e);
        }
    }
}
