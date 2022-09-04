package currency.converter;

import java.util.*;

public class CurrencyCalculator {

    private final HashMap<String, Float> statMap = new HashMap<>();

    private float calculateSD(List<Float> list) {
        float powerSum1 = 0;
        float powerSum2 = 0;
        float sd = 0;

        for (int i = 0; i < list.size(); i++) {
            powerSum1 += list.get(i);
            powerSum2 += Math.pow(list.get(i), 2);
            sd = (float) (Math.sqrt(i * powerSum2 - Math.pow(powerSum1, 2))/i);
        }

        return sd;
    }

    private float calculateMean(List<Float> list) {
        float sum = 0;

        for (Float aFloat : list) {
            sum += aFloat;
        }

        return sum/list.size();
    }

    private float calculateMedian(List<Float> list) {
        Collections.sort(list);

        if (list.size() % 2 == 1) {
            return list.get((list.size() + 1)/2 - 1);
        } else {
            float lower = list.get(list.size() / 2 - 1);
            float upper = list.get(list.size() / 2);

            return (lower + upper) / 2f;
        }

    }


    public float calculateAmount(float amount, float rate) {
        return amount * rate;
    }

    public Map<String, Float> calculateStatistic(List<Float> historyList) { //takes list of rates
        //returns map with name of statistic e.g. "mean" as key and the mean as value
        statMap.put("average", calculateMean(historyList));
        statMap.put("median", calculateMedian(historyList));
        statMap.put("max", Collections.max(historyList));
        statMap.put("min", Collections.min(historyList));
        statMap.put("sd", calculateSD(historyList));

        return statMap;
    }
}
