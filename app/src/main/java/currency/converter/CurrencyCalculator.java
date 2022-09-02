package currency.converter;

import java.util.List;
import java.util.Map;

public class CurrencyCalculator {

    public float calculateAmount(float amount, float rate) {
        return amount * rate;
    }

    public Map<String, Float> calculateStatistic(List<Float> historyList) { //takes list of rates
        //returns map with name of statistic e.g. "mean" as key and the mean as value
        

    }
}
