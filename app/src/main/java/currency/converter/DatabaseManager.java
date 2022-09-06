package currency.converter;

import java.util.List;
import java.util.ArrayList;
import java.util.Map;
import java.util.HashMap;

import java.io.FileReader;
import java.io.IOException;
import java.util.Iterator;
import java.io.FileNotFoundException;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.*;

import org.json.simple.JSONObject;

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

            // Extracting the rates array from the database
            JSONArray rates = (JSONArray) database.get("rates");

            // Extracting the specific currency objects from the database
            JSONObject curr1Object = (JSONObject) rates.get(getRateIndex(curr1, rates));
            JSONObject curr2Object = (JSONObject) rates.get(getRateIndex(curr2, rates));

            // Extracting the arrays for the respective currencies
            // which contain all the historical & present rates
            JSONArray curr1Data = (JSONArray) curr1Object.get("data");
            JSONArray curr2Data = (JSONArray) curr2Object.get("data");

            // Extracting the most recent exchange rates of the two currencies
            JSONObject curr1Obj = (JSONObject) curr1Data.get(curr1Data.size()-1);
            JSONObject curr2Obj = (JSONObject) curr2Data.get(curr2Data.size()-1);

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

        try {
            JSONParser jsonParser = new JSONParser();
            JSONObject database = (JSONObject) jsonParser.parse(new
                    FileReader("database.json"));

            //Get the rates array, relevant currency array
            JSONArray rates = (JSONArray) database.get("rates");
            JSONObject currObject = (JSONObject) rates.get(getRateIndex(curr1, rates));

            //Get the date:rate array from that currency
            JSONArray currArray = (JSONArray) currObject.get("data");

            //Create a new currency obj, populate it
            JSONObject newCur = (JSONObject) new JSONObject();
            newCur.put("date", date);
            newCur.put("rate", amount);

            currArray.put(newCur);

        } catch (FileNotFoundException e) {
            System.out.println("File not found!");
            throw new RuntimeException(e);
        } catch (IOException e) {
            throw new RuntimeException(e);
        } catch (ParseException e) {
            throw new RuntimeException(e);
        }
    }

    public List<String> getPopularCurrencies() {
        return null;
    }

    public boolean rateIncreased(String curr) {
        try {
            JSONParser jsonParser = new JSONParser();
            JSONObject database = (JSONObject) jsonParser.parse(new
                    FileReader("database.json"));

            JSONArray rates = (JSONArray) database.get("rates");
            JSONObject currObject = (JSONObject) rates.get(getRateIndex(curr, rates));
            JSONArray currArray = (JSONArray) currObject.get("data");

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

    public String checkDate(String curr1, String curr2) {
        return null;
    }

    public HashMap<String, Float> getConversionHistory(String curr1,
                                                       String curr2,
                                                       String startDate,
                                                       String endDate){
        return null;
    }

    // Helper function to find the index of a rate object in the array
    public static int getRateIndex(String curr, JSONArray array) {
        for (int i=0; i<array.size(); i++) {
            if (curr.equals(array.get(i))) {
                return i;
            }
        }
        return -1;
    }
}
