package currency.converter;

import java.util.List;
import java.time.LocalDate;
import java.util.ArrayList;

import java.util.Map.Entry;
import java.util.HashMap;
import java.util.Map;
import java.util.HashMap;

public class CurrencyHandler {
    private final CurrencyCalculator currCalc;
    private final DatabaseManager DBM;
    private boolean isAdmin;

    public CurrencyHandler(boolean admin) {
        this.currCalc = new CurrencyCalculator();
        this.isAdmin = admin;
        this.DBM = new DatabaseManager("src/main/resources/database.json");
    }

    public float convertCurrency(float amount, String currCurrency, String newCurrency) {
        float rate = DBM.getConversion(currCurrency, newCurrency); // Need to test whether the rate is as expected

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
                    float conversion = DBM.getConversion(fromCurrency, toCurrency);
                    boolean upDirection = DBM.conversionIncreased(fromCurrency, toCurrency);
                    if (upDirection) {
                        String data = String.format("{0} (↑)", conversion);
                        display[i][j] = data;
                    } else {
                        String data = String.format("{0} (↓)", conversion);
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
        if (DBM.addPopularCurrencies(currencies)) {
            return true;
        } else {
            return false;
        }
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

    public void updateCurrency(String curr1, String curr2, float newRate, LocalDate date) {
        String recentDate = DBM.checkDate(curr1);
        if (!recentDate.equals(date.toString())) {
            // can convert date to a string if needed, left as LocalDate
            DBM.addConversion(curr1, curr2, newRate, date.toString()); // Made this for testing
        }
    }

    public void printConversionHistory(String curr1, String curr2, LocalDate startDate, LocalDate endDate) {
        HashMap<String, Float> conversionRates = DBM.getPastConversion(curr1, curr2, startDate.toString(),
                endDate.toString());

        List<Float> listOfRates = new ArrayList<>(conversionRates.values());
        Map<String, Float> statMap = currCalc.calculateStatistic(listOfRates);

        System.out.println("Conversion Rate History of " + curr1 + " to " + curr2);
        for (String key : conversionRates.keySet()) {
            System.out.println(key + ": " + conversionRates.get(key));
        }

        System.out.println("Statistics");
        for (String key : statMap.keySet()) {
            System.out.println(key + ": " + statMap.get(key));
        }
    }
}