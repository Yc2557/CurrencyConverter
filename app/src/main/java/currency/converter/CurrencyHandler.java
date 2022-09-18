package currency.converter;

import java.util.List;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;

import java.util.Map.Entry;
import java.util.HashMap;
import java.util.Map;
import java.util.HashMap;

public class CurrencyHandler {
    private final CurrencyCalculator currCalc;
    private final DatabaseManager DBM;

    public CurrencyHandler(boolean admin, String filename) {
        this.currCalc = new CurrencyCalculator();
        this.DBM = new DatabaseManager(filename);
    }

    public double convertCurrency(float amount, String currCurrency, String newCurrency) {
        double rate = DBM.getConversion(currCurrency, newCurrency); // Need to test whether the rate is as expected

        return rate * amount;
    }

    public String[][] displayPopular() {
        List<String> popularCurrencies = DBM.getPopularCurrencies();
        String[][] display = new String[4][4];

        for (int i = 0; i < popularCurrencies.size(); i++) {
            String fromCurrency = popularCurrencies.get(i);
            for (int j = 0; j < popularCurrencies.size(); j++) {
                String toCurrency = popularCurrencies.get(j);
                if (i == j) {
                    display[i][j] = "-";
                } else {
                    double conversion = DBM.getConversion(fromCurrency, toCurrency);
                    Boolean upDirection = DBM.conversionIncreased(fromCurrency, toCurrency);

                    if (upDirection == null) {
                        display[i][j] = "-";
                    }
                    if (Boolean.TRUE.equals(upDirection)) {
                        String data = String.format("%.2f (U)", conversion);
                        display[i][j] = data;
                    } else {
                        String data = String.format("%.2f (D)", conversion);
                        display[i][j] = data;
                    }
                }
            }
        }

        return display;
    }

    public List<String> getPopularCurrencies() {
        return DBM.getPopularCurrencies();
    }

    public boolean updatePopular(String curr1, String curr2, String curr3, String curr4) {
        ArrayList<String> currencies = new ArrayList<String>() {
            {
                add(curr1);
                add(curr2);
                add(curr3);
                add(curr4);
            }
        };
        return DBM.addPopularCurrencies(currencies);
    }

    public Boolean addCurrency(String currency) {
        this.DBM.addCurrency(currency); // add new currency to the json

        return Boolean.TRUE;
    }

    public Map<String, Float> collateHistoryResults(Map<String, Float> currency, float amount) {
        // get list of rates from map
        List<Float> listOfRates = new ArrayList<>(currency.values());

        // might need to do prints here
        return currCalc.calculateStatistic(listOfRates);
    }

    public boolean updateCurrency(String fromCurr, String toCurr, float newRate, LocalDate date) {
        if (!fromCurr.equals("AUD") && !toCurr.equals("AUD")) { //one currency must be AUD
            System.out.println("At least one currency must be AUD!");
            return false;
        }

        int currNum = 0;
        String recentDate;
        if (fromCurr.equals("AUD")) { //
            currNum = 2;
            recentDate = DBM.checkDate(toCurr);
        } else {
            currNum = 1;
            recentDate = DBM.checkDate(fromCurr);
        }

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy");
        String dateFormatted = date.format(formatter);

        if (!recentDate.equals(date.toString())) {
            if (currNum == 1) {
                DBM.addConversion(fromCurr, 1/newRate, dateFormatted);
            } else {
                DBM.addConversion(toCurr, newRate, dateFormatted);
            }
        }

        return true;
    }

    public void printConversionHistory(String curr1, String curr2, LocalDate startDate, LocalDate endDate) {

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy");
        String startDateFormatted = startDate.format(formatter);
        String endDateFormatted = endDate.format(formatter);

        HashMap<String, Float> conversionRates = DBM.getPastConversion(curr1, curr2, startDateFormatted,
                endDateFormatted);

        List<Float> listOfRates = new ArrayList<>(conversionRates.values());
        Map<String, Float> statMap = currCalc.calculateStatistic(listOfRates);

        System.out.println("\nConversion Rate History of " + curr1 + " to " + curr2);
        for (String key : conversionRates.keySet()) {
            System.out.println(key + ": " + conversionRates.get(key));
        }
        System.out.println("");

        System.out.println("Statistics");
        for (String key : statMap.keySet()) {
            System.out.println(key + ": " + statMap.get(key));
        }
    }
}