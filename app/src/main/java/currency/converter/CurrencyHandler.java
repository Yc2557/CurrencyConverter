package currency.converter;

import java.util.List;
import java.time.LocalDate;
import java.util.ArrayList;

public class CurrencyHandler {
    private boolean isAdmin;
    private CurrencyCalculator currCalc;
    private DatabaseManager DBM;

    public CurrencyHandler(boolean admin) {
        this.isAdmin = admin;
        this.currCalc = new CurrencyCalculator();
        this.DBM = new DatabaseManager();
    }

    public float convertCurrency(float amount, String currCurrency, String newCurrency) {
        float rate = DBM.getConversion(currCurrency, newCurrency); // Need to test whether the rate is as expected

        return rate * amount;
    }

    public String[][] displayPopular() {
        ArrayList<String> popularCurrencies = DBM.getPopularCurrencies();
        String[][] display = new String[4][4];

        for (int i = 0; i < popularCurrencies.size(); i++) {
            String fromCurrency = popularCurrencies.get(i);
            for (int j = 0; j < popularCurrencies.size() j++) {
                String toCurrency = popularCurrencies.get(j);
                if (i == j) {
                    display[i][j] = "-";
                }
                else {
                    float conversion = DBM.getConversion(fromCurrency, toCurrency);
                    boolean upDirection = DBM.checkDirection(fromCurrency, toCurrency);
                    if (upDirection) {
                        String data = String.format("{0} (↑)", conversion);
                    } else {
                        String data = String.format("{0} (↓)", conversion);
                    }
                }
            }
        }

        return display;
    }

    public void updateCurrency(String curr1, String curr2, float newRate, LocalDate date) {
        LocalDate recentDate = DBM.checkDate(curr1, curr2);
        if (!recentDate.equals(date)) {
            //can convert date to a string if needed, left as LocalDate
            DBM.addRate(curr1, curr2, newRate, date);
        }
    }
}