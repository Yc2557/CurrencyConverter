package currency.converter;

import java.util.List;
import java.util.ArrayList;
import java.util.Map;
import java.util.HashMap;

import java.io.FileReader;
import java.io.IOException;
import java.util.Iterator;
import java.io.FileNotFoundException;
import java.util.Date;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.*;

public class DatabaseManager {

    private boolean isAdmin;
    //private JSONParser jsonParser; <-- maybe?

    public DatabaseManager(boolean isAdmin) {
        this.isAdmin = isAdmin;
    }

    public float getConversion(String curr1, String curr2) {
        try {
            // Parsing the .json database to a JSONObject
            JSONParser jsonParser = new JSONParser();
            JSONObject database = (JSONObject) jsonParser.parse(new
                    FileReader("database.json"));

            // Extracting the rates array from the database
            JSONArray rates = (JSONArray) database.get("rates");

            // Extracting the specific currency objects from the database
            JSONObject curr1Object = (JSONObject) rates.get(getConversionIndex(curr1, rates));
            JSONObject curr2Object = (JSONObject) rates.get(getConversionIndex(curr2, rates));

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

    public HashMap<String, Float> getPastConversion(String cur1, String cur2, String startDate,
                                  String endDate) {

        HashMap<String, Float> pastRates = new HashMap<String, Float>();

        try {
            JSONParser jsonParser = new JSONParser();
            JSONObject database = (JSONObject) jsonParser.parse(String.valueOf(new
                    FileReader("database.json")));


            if (cur1.equals("AUD") || cur2.equals("AUD")) {

                String referenceCur;
                if (cur1.equals("AUD")) {
                    referenceCur = cur2;
                } else if (cur2.equals("AUD")) {
                    referenceCur = cur1;
                }

                JSONArray ratesArray = (JSONArray) database.get("rates");
                JSONObject cur = (JSONObject) ratesArray.get(getConversionIndex(referenceCur, ratesArray));
                JSONArray currency1Rates = (JSONArray) cur.get("data");

                for (int i = 0; i < currency1Rates.size(); i++) {

                    JSONObject dateAndRate = (JSONObject) currency1Rates.get(i);
                    String conversionDate = (String) dateAndRate.get("date");

                    //If conversionDate is between start and end date
                    if (isBefore(startDate, conversionDate) && isBefore(conversionDate,endDate)) {

                        String rateTransform = (String) dateAndRate.get("rate");
                        Float actualRate = Float.parseFloat(rateTransform);
                        pastRates.put(conversionDate, actualRate);

                    } else if (isBefore(endDate, conversionDate)) {
                        break;
                    }
                }

            } else {
                //Neither currency is AUD, so must find both, and convert through
                //Each could have different dates, these must be lined up/
                //Find both, place into hashmaps
                //use the size() of longer one as a reference for the returner map
                //(divide by cur1 )* cur2 /

                JSONArray ratesArray = (JSONArray) database.get("rates");
                JSONObject cur = (JSONObject) ratesArray.get(getConversionIndex(cur1, ratesArray));
                JSONArray currencyArray = (JSONArray) cur.get("data");

                HashMap<String, Float> currency1Map = new HashMap<String, Float>();
                HashMap<String, Float> currency2Map = new HashMap<String, Float>();

                //Fill conversion hashmap for curr1
                for (int i = 0; i < currencyArray.size(); i++) {

                    JSONObject dateAndRate = (JSONObject) currencyArray.get(i);
                    String conversionDate = (String) dateAndRate.get("date");

                    //If conversionDate is between start and end date
                    if (isBefore(startDate, conversionDate) && isBefore(conversionDate, endDate)) {

                        String rateTransform = (String) dateAndRate.get("rate");
                        Float actualRate = Float.parseFloat(rateTransform);
                        currency1Map.put(conversionDate, actualRate);

                    } else if (isBefore(endDate, conversionDate)) {
                        break;
                    }
                }
                //Reuse vars
                cur = (JSONObject) ratesArray.get(getConversionIndex(cur2, ratesArray));
                currencyArray = (JSONArray) cur.get("data");
                //fill hashmap for curr2
                for (int i = 0; i < currencyArray.size(); i++) {

                    JSONObject dateAndRate = (JSONObject) currencyArray.get(i);
                    String conversionDate = (String) dateAndRate.get("date");

                    //If conversionDate is between start and end date
                    if (isBefore(startDate, conversionDate) && isBefore(conversionDate, endDate)) {

                        String rateTransform = (String) dateAndRate.get("rate");
                        Float actualRate = Float.parseFloat(rateTransform);
                        currency1Map.put(conversionDate, actualRate);

                    } else if (isBefore(endDate, conversionDate)) {
                        break;
                    }
                }

                //Pick the larger hashmap to work on
                if (currency1Map.size() > currency2Map.size()) {
                    ;
                } else {
                    HashMap<String, Float> tempMap = currency1Map;
                    currency1Map = currency2Map;
                    currency2Map = tempMap;
                }

                //For each entry in map 1, find any dates in map2 that are after that entry
                // and correct the rate for all dates prior to that one/
                for (Map.Entry<String, Float> entry : currency1Map.entrySet()) {
                    String key1 = entry.getKey();
                    Float value1 = entry.getValue();

                    for (Map.Entry<String, Float> map2 : currency2Map.entrySet()) {
                        String key2 = map2.getKey();
                        Float value2 = map2.getValue();
                        if (!isBefore(key1, key2)) {
                            value1 = value2/value1;
                        }
                    }
                }
                return currency1Map;
            }

        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        } catch (IOException e) {
            throw new RuntimeException(e);
        } catch (ParseException e) {
            throw new RuntimeException(e);
        }


        return pastRates;
    }

    public void addConversion(String curr1, String curr2, float amount, String date) {

        try {
            JSONParser jsonParser = new JSONParser();
            JSONObject database = (JSONObject) jsonParser.parse(new
                    FileReader("database.json"));

            //Get the rates array, relevant currency array
            JSONArray rates = (JSONArray) database.get("rates");
            JSONObject currObject = (JSONObject) rates.get(getConversionIndex(curr1, rates));

            //Get the date:rate array from that currency
            JSONArray currArray = (JSONArray) currObject.get("data");

            //Create a new currency obj, populate it
            JSONObject newCur = (JSONObject) new JSONObject();
            newCur.put("date", date);
            newCur.put("rate", amount);

            currArray.add(newCur);

        } catch (FileNotFoundException e) {
            System.out.println("File not found!");
            throw new RuntimeException(e);
        } catch (IOException e) {
            throw new RuntimeException(e);
        } catch (ParseException e) {
            throw new RuntimeException(e);
        }
    }

    // MICHAEL
    public ArrayList<String> getPopularCurrencies() {
        try {
            JSONParser jsonParser = new JSONParser();
            JSONObject database = (JSONObject) jsonParser.parse(String.valueOf(new
                    FileReader("database.json")));
            JSONArray popular = (JSONArray) database.get("popular");

            return (ArrayList<String>) popular;

        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        } catch (IOException e) {
            throw new RuntimeException(e);
        } catch (ParseException e) {
            throw new RuntimeException(e);
        }
    }
        /*
        Will be set by the admin (create new method)

         */

    // Receiving the four most popular currencies from the admin
    // and updating the database
    public void addPopularCurrencies(ArrayList<String> currencies) {
        try {
            JSONParser jsonParser = new JSONParser();
            JSONObject database = (JSONObject) jsonParser.parse(String.valueOf(new
                    FileReader("database.json")));
            JSONArray popular = new JSONArray();

            for (String curr : currencies) {
                popular.add(curr);
            }

            database.put("popular", popular);

        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        } catch (IOException e) {
            throw new RuntimeException(e);
        } catch (ParseException e) {
            throw new RuntimeException(e);
        }
    }

    public boolean conversionIncreased(String curr) {
        try {
            JSONParser jsonParser = new JSONParser();
            JSONObject database = (JSONObject) jsonParser.parse(new
                    FileReader("database.json"));

            JSONArray rates = (JSONArray) database.get("rates");
            JSONObject currObject = (JSONObject) rates.get(getConversionIndex(curr, rates));
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

    // Returns the most recent date which the passed currency was updated
    public String checkDate(String curr) {
        try {
            JSONParser jsonParser = new JSONParser();
            JSONObject database = (JSONObject) jsonParser.parse(new
                    FileReader("database.json"));

            JSONArray rates = (JSONArray) database.get("rates");
            JSONObject currObject = (JSONObject) rates.get(getConversionIndex(curr, rates));
            JSONArray currArray = (JSONArray) currObject.get("data");
            JSONObject currPresentRate = (JSONObject) currArray.get(currArray.size()-1);

            return (String) currPresentRate.get("date");

        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        } catch (IOException e) {
            throw new RuntimeException(e);
        } catch (ParseException e) {
            throw new RuntimeException(e);
        }
    }

    public HashMap<String, Float> getConversionHistory(String curr1,
                                                       String curr2,
                                                       String startDate,
                                                       String endDate){
        return null;
    }

    // Helper function to find the index of a rate object in the array
    public static int getConversionIndex(String curr, JSONArray array) {
        for (int i=0; i<array.size(); i++) {
            if (curr.equals(array.get(i))) {
                return i;
            }
        }
        return -1;
    }

    //if date1 before date2 : true, if equal: null
    public static Boolean isBefore(String d1, String d2) {

        String[] date1 = d1.split("-");
        String[] date2 = d2.split("-");

        Integer year1 = Integer.parseInt(date1[2]);
        Integer year2 = Integer.parseInt(date2[2]);

        if (year1 > year2) {
            return false;
        } else if (year1 < year2) {
            return true;
        } else {

            Integer month1 = Integer.parseInt(date1[1]);
            Integer month2 = Integer.parseInt(date2[1]);

            if (month1 > month2) {
                return false;
            } else if (month1 < month2) {
                return true;
            } else {
                Integer day1 = Integer.parseInt(date1[0]);
                Integer day2 = Integer.parseInt(date2[0]);

                if (day1 > day2) {
                    return false;
                } else if (day1 < day2) {
                    return true;
                } else {
                    return null;
                }
            }
        }
    }

    public static Integer dateToInt(String d) {
        String[] dividedDate = d.split("-");
        String strDate = dividedDate[2] + dividedDate[1] + dividedDate[0];
        Integer intDate = Integer.parseInt(strDate);
        return intDate;
    }

    public static String dateToString(Integer i) {
        String date = i.toString();
        String year = date.substring(0,4);
        String month = date.substring(5,6);
        String day = date.substring(7,8);

        date = year + "-" + month + "-" + day;
        return date;
    }


}
