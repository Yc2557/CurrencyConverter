package currency.converter;

import java.io.*;
import java.util.ArrayList;
import java.util.Map;
import java.util.HashMap;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.*;

public class DatabaseManager {

    private final String FILE_NAME;
    // private JSONParser jsonParser; <-- maybe?

    public DatabaseManager(String fileName) {
        this.FILE_NAME = fileName;
    }

    public double getConversion(String fromCurr, String toCurr) {
        try {
            // Parsing the .json database to a JSONObject
            JSONParser jsonParser = new JSONParser();
            JSONObject database = (JSONObject) jsonParser.parse(new FileReader(FILE_NAME));

            // Extracting the rates array from the database
            JSONArray rates = (JSONArray) database.get("rates");

            // Checking that currency exists
            if (!currencyExists(fromCurr, rates) || !currencyExists(toCurr, rates)) {
                return 0;
            }

            if (fromCurr.equals("AUD")) {
                JSONObject currObject = (JSONObject) rates.get(getConversionIndex(toCurr, rates));
                JSONArray currData = (JSONArray) currObject.get("data");
                JSONObject currRateObject = (JSONObject) currData.get(currData.size() - 1);

                return (double) currRateObject.get("rate");
            } else if (toCurr.equals("AUD")) {
                JSONObject currObject = (JSONObject) rates.get(getConversionIndex(fromCurr, rates));
                JSONArray currData = (JSONArray) currObject.get("data");
                JSONObject currRateObject = (JSONObject) currData.get(currData.size() - 1);

                // Taking the inverse because the rates are stored FROM the AUD in the database
                return (1 / (double) currRateObject.get("rate"));

            } else {
                // Extracting the specific currency objects from the database
                JSONObject fromCurrObject = (JSONObject) rates.get(getConversionIndex(fromCurr, rates));
                JSONObject toCurrObject = (JSONObject) rates.get(getConversionIndex(toCurr, rates));

                // Extracting the arrays for the respective currencies
                // which contain all the historical & present rates
                JSONArray fromCurrData = (JSONArray) fromCurrObject.get("data");
                JSONArray toCurrData = (JSONArray) toCurrObject.get("data");

                // Extracting the most recent exchange rates of the two currencies
                JSONObject fromCurrRateObject = (JSONObject) fromCurrData.get(fromCurrData.size() - 1);
                JSONObject toCurrRateObject = (JSONObject) toCurrData.get(toCurrData.size() - 1);

                // Extracting the most recent rates for each of the currencies
                double rate1 = (double) fromCurrRateObject.get("rate");
                double rate2 = (double) toCurrRateObject.get("rate");

                // Calculating & returning the finalised exchange rate between
                // the two currencies (rate = to/from)
                return rate2 / rate1;
            }

            // NOTE: must include error cases; i.e. passing through a non-existent
            // currency
        } catch (FileNotFoundException e) {
            System.out.println("File not found!");
            throw new RuntimeException(e);
        } catch (IOException | ParseException e) {
            throw new RuntimeException(e);
        }
    }

    public HashMap<String, Float> getPastConversion(String cur1, String cur2, String startDate,
            String endDate) {

        HashMap<String, Float> pastRates = new HashMap<>();

        try {
            JSONParser jsonParser = new JSONParser();
            JSONObject database = (JSONObject) jsonParser.parse(new FileReader(FILE_NAME));

            if (cur1.equals("AUD") || cur2.equals("AUD")) {

                String referenceCur;
                if (cur1.equals("AUD")) {
                    referenceCur = cur2;
                } else {
                    referenceCur = cur1;
                }

                JSONArray ratesArray = (JSONArray) database.get("rates");
                JSONObject cur = (JSONObject) ratesArray.get(getConversionIndex(referenceCur, ratesArray));
                JSONArray currency1Rates = (JSONArray) cur.get("data");

                for (Object currency1Rate : currency1Rates) {

                    JSONObject dateAndRate = (JSONObject) currency1Rate;
                    String conversionDate = (String) dateAndRate.get("date");

                    // If conversionDate is between start and end date

                    if (Boolean.TRUE.equals(isBefore(startDate, conversionDate))
                            && Boolean.TRUE.equals(isBefore(conversionDate, endDate))) {

                        String rateTransform = String.valueOf(dateAndRate.get("rate"));
                        Float actualRate = Float.parseFloat(rateTransform);
                        if (cur1.equals("AUD")) {
                            actualRate = 1/actualRate;
                            pastRates.put(conversionDate, actualRate);
                        } else {
                            pastRates.put(conversionDate, actualRate);
                        }
                    } else if (Boolean.TRUE.equals(isBefore(endDate, conversionDate))) {
                        break;
                    }
                }

            } else {
                // Neither currency is AUD, so must find both, and convert through
                // Each could have different dates, these must be lined up/
                // Find both, place into hashmaps
                // use the size() of longer one as a reference for the returner map
                // (divide by cur1 )* cur2 /

                JSONArray ratesArray = (JSONArray) database.get("rates");
                JSONObject cur = (JSONObject) ratesArray.get(getConversionIndex(cur1, ratesArray));
                JSONArray currencyArray = (JSONArray) cur.get("data");

                HashMap<String, Float> currency1Map = new HashMap<>();
                HashMap<String, Float> currency2Map = new HashMap<>();

                // Fill conversion hashmap for curr1
                for (Object o : currencyArray) {

                    JSONObject dateAndRate = (JSONObject) o;
                    String conversionDate = (String) dateAndRate.get("date");

                    // If conversionDate is between start and end date

                    if (Boolean.TRUE.equals(isBefore(startDate, conversionDate))
                            && Boolean.TRUE.equals(isBefore(conversionDate, endDate))) {

                        String rateTransform = String.valueOf(dateAndRate.get("rate"));
                        Float actualRate = Float.parseFloat(rateTransform);
                        currency1Map.put(conversionDate, actualRate);

                    } else if (Boolean.TRUE.equals(isBefore(endDate, conversionDate))) {
                        break;
                    }
                }
                // Reuse vars
                cur = (JSONObject) ratesArray.get(getConversionIndex(cur2, ratesArray));
                currencyArray = (JSONArray) cur.get("data");

                // fill hashmap for curr2
                for (Object o : currencyArray) {

                    JSONObject dateAndRate = (JSONObject) o;
                    String conversionDate = (String) dateAndRate.get("date");

                    // If conversionDate is between start and end date
                    if (Boolean.TRUE.equals(isBefore(startDate, conversionDate))
                            && Boolean.TRUE.equals(isBefore(conversionDate, endDate))) {

                        String rateTransform = String.valueOf(dateAndRate.get("rate"));
                        Float actualRate = Float.parseFloat(rateTransform);
                        currency2Map.put(conversionDate, actualRate);

                    } else if (Boolean.TRUE.equals(isBefore(endDate, conversionDate))) {
                        break;
                    }
                }

                // For each entry in map 1, find any dates in map2 that are after that entry
                // and correct the rate for all dates prior to that one/
                for (Map.Entry<String, Float> entry : currency1Map.entrySet()) {
                    String key1 = entry.getKey();
                    Float value1 = entry.getValue();

                    Float actualConversionRate = 0f;
                    for (Map.Entry<String, Float> map2 : currency2Map.entrySet()) {
                        String key2 = map2.getKey();
                        Float value2 = map2.getValue();
                        if (isBefore(key2, key1)) {
                            actualConversionRate = value1 / value2;
                        }
                    }
                    currency1Map.put(key1, actualConversionRate);
                }
                return currency1Map;
            }

        } catch (IOException | ParseException e) {
            throw new RuntimeException(e);
        }

        return pastRates;
    }

    public void addConversion(String curr, float amount, String date) {

        try {
            JSONParser jsonParser = new JSONParser();
            JSONObject database = (JSONObject) jsonParser.parse(new FileReader(FILE_NAME));

            // Get the rates array, relevant currency array
            JSONArray rates = (JSONArray) database.get("rates");
            JSONObject currObject = (JSONObject) rates.get(getConversionIndex(curr, rates));

            // Get the date:rate array from that currency
            JSONArray currArray = (JSONArray) currObject.get("data");

            // Create a new currency obj, populate it
            JSONObject newCur = new JSONObject();
            newCur.put("date", date);
            newCur.put("rate", amount);

            currArray.add(newCur);

            FileWriter writer = new FileWriter(FILE_NAME);
            writer.write(database.toJSONString());
            writer.flush();
            writer.close();

        } catch (FileNotFoundException e) {
            System.out.println("File not found!");
            throw new RuntimeException(e);
        } catch (IOException | ParseException e) {
            throw new RuntimeException(e);
        }
    }

    public void addCurrency(String curr) {
        try {
            JSONParser jsonParser = new JSONParser();
            JSONObject database = (JSONObject) jsonParser.parse(new FileReader(FILE_NAME));
            JSONArray rates = (JSONArray) database.get("rates");

            // Creating the new currency object
            JSONObject currency = new JSONObject();
            JSONArray data = new JSONArray();
            currency.put("rate", curr);
            currency.put("data", data);
            rates.add(currency);
            database.put("rates", rates);

            FileWriter writer = new FileWriter(FILE_NAME);
            writer.write(database.toJSONString());
            writer.flush();
            writer.close();

        } catch (IOException | ParseException e) {
            throw new RuntimeException(e);
        }
    }

    public ArrayList<String> getPopularCurrencies() {
        try {
            JSONParser jsonParser = new JSONParser();
            JSONObject database = (JSONObject) jsonParser.parse(new FileReader(FILE_NAME));
            JSONArray popular = (JSONArray) database.get("popular");

            ArrayList<String> list = new ArrayList<>();
            // noinspection ForLoopReplaceableByForEach
            for (int i = 0; i < popular.size(); i++) {
                list.add(popular.get(i).toString());
            }

            return list;

        } catch (IOException | ParseException e) {
            throw new RuntimeException(e);
        }
    }
    /*
     * Will be set by the admin (create new method)
     * 
     */

    // Receiving the four most popular currencies from the admin
    // and updating the database
    public boolean addPopularCurrencies(ArrayList<String> currencies) {
        try {
            JSONParser jsonParser = new JSONParser();
            JSONObject database = (JSONObject) jsonParser.parse(new FileReader(FILE_NAME));
            JSONArray popular = new JSONArray();

            popular.addAll(currencies);

            database.put("popular", popular);

            PrintWriter pw = new PrintWriter(FILE_NAME);
            pw.write(database.toJSONString());

            pw.flush();
            pw.close();

            return true;

        } catch (IOException | ParseException e) {
            throw new RuntimeException(e);
        }

        // Needs error checking return false;
    }

    public Boolean conversionIncreased(String fromCurr, String toCurr) {
        try {
            JSONParser jsonParser = new JSONParser();
            JSONObject database = (JSONObject) jsonParser.parse(new FileReader(FILE_NAME));

            JSONArray rates = (JSONArray) database.get("rates");

            // Checking that currency exists
            if (!currencyExists(fromCurr, rates) || !currencyExists(toCurr, rates)) {
                return null;
            }

            if (fromCurr.equals("AUD")) {
                JSONObject currObject = (JSONObject) rates.get(getConversionIndex(toCurr, rates));
                JSONArray currArray = (JSONArray) currObject.get("data");

                // Checking if there is historical data to compare to
                if (currArray.size() < 2) {
                    return null;
                }

                JSONObject currObj1 = (JSONObject) currArray.get(currArray.size() - 1);
                JSONObject currObj2 = (JSONObject) currArray.get(currArray.size() - 2);

                double rate1 = (double) currObj1.get("rate");
                double rate2 = (double) currObj2.get("rate");

                return (rate1 - rate2) > 0;

            } else if (toCurr.equals("AUD")) {
                JSONObject currObject = (JSONObject) rates.get(getConversionIndex(fromCurr, rates));
                JSONArray currArray = (JSONArray) currObject.get("data");

                // Checking if there is historical data to compare to
                if (currArray.size() < 2) {
                    return null;
                }

                JSONObject currObj1 = (JSONObject) currArray.get(currArray.size() - 1);
                JSONObject currObj2 = (JSONObject) currArray.get(currArray.size() - 2);

                // Getting inverse of stored as it is database stores the values
                // for every currency FROM AUD
                double rate1 = 1 / (double) currObj1.get("rate");
                double rate2 = 1 / (double) currObj2.get("rate");

                return (rate1 - rate2) > 0;
            } else {
                // Get rate and check with previous
                // Get inverse of stored
                JSONObject toCurrObject = (JSONObject) rates.get(getConversionIndex(toCurr, rates));
                JSONArray toCurrArray = (JSONArray) toCurrObject.get("data");
                JSONObject fromCurrObject = (JSONObject) rates.get(getConversionIndex(fromCurr, rates));
                JSONArray fromCurrArray = (JSONArray) fromCurrObject.get("data");

                // Checking if there is historical data to compare to
                if (toCurrArray.size() < 2 || fromCurrArray.size() < 2) {
                    return null;
                }

                JSONObject toCurrObj1 = (JSONObject) toCurrArray.get(toCurrArray.size() - 1);
                JSONObject toCurrObj2 = (JSONObject) toCurrArray.get(toCurrArray.size() - 2);
                JSONObject fromCurrObj1 = (JSONObject) fromCurrArray.get(fromCurrArray.size() - 1);
                JSONObject fromCurrObj2 = (JSONObject) fromCurrArray.get(fromCurrArray.size() - 2);

                double rate1 = (double) toCurrObj1.get("rate") / (double) fromCurrObj1.get("rate");
                double rate2 = (double) toCurrObj2.get("rate") / (double) fromCurrObj2.get("rate");

                return (rate1 - rate2) > 0;
            }
        } catch (IOException | ParseException e) {
            throw new RuntimeException(e);
        }
    }

    // Returns the most recent date which the passed currency was updated
    public String checkDate(String curr) {
        try {
            JSONParser jsonParser = new JSONParser();
            JSONObject database = (JSONObject) jsonParser.parse(new FileReader(FILE_NAME));

            JSONArray rates = (JSONArray) database.get("rates");

            // Checking that currency exists
            if (!currencyExists(curr, rates)) {
                return null;
            }

            JSONObject currObject = (JSONObject) rates.get(getConversionIndex(curr, rates));
            JSONArray currArray = (JSONArray) currObject.get("data");
            JSONObject currPresentRate = (JSONObject) currArray.get(currArray.size() - 1);

            return (String) currPresentRate.get("date");

        } catch (IOException | ParseException e) {
            throw new RuntimeException(e);
        }
    }

    // Helper function to find the index of a rate object in the array
    public static int getConversionIndex(String curr, JSONArray array) {
        for (int i = 0; i < array.size(); i++) {
            JSONObject c = (JSONObject) array.get(i);
            if (curr.equals(c.get("rate").toString())) {
                return i;
            }
        }
        return -1;
    }

    // if date1 before date2 : true, if equal: null
    public static Boolean isBefore(String d1, String d2) {

        String[] date1 = d1.split("-");
        String[] date2 = d2.split("-");

        int year1 = Integer.parseInt(date1[2]);
        int year2 = Integer.parseInt(date2[2]);

        if (year1 > year2) {
            return false;
        } else if (year1 < year2) {
            return true;
        } else {

            int month1 = Integer.parseInt(date1[1]);
            int month2 = Integer.parseInt(date2[1]);

            if (month1 > month2) {
                return false;
            } else if (month1 < month2) {
                return true;
            } else {
                int day1 = Integer.parseInt(date1[0]);
                int day2 = Integer.parseInt(date2[0]);

                if (day1 > day2) {
                    return false;
                } else if (day1 <= day2) {
                    return true;
                }
            }
        }
        return null;
    }

    public static Integer dateToInt(String d) {
        String[] dividedDate = d.split("-");
        String strDate = dividedDate[2] + dividedDate[1] + dividedDate[0];
        return Integer.parseInt(strDate);
    }

    public static String dateToString(Integer i) {
        String date = i.toString();
        String year = date.substring(0, 4);
        String month = date.substring(5, 6);
        String day = date.substring(7, 8);

        date = year + "-" + month + "-" + day;
        return date;
    }

    public static Boolean currencyExists(String curr, JSONArray rates) {
        // Checking that currency exists
        if (curr.equalsIgnoreCase("AUD")) {
            return true;
        } else {
            int currConvIndex = getConversionIndex(curr, rates);

            if (currConvIndex == -1) {
                System.out.println("Currency " + curr + " does not exist!");
                return false;
            }
        }
        return true;
    }

}