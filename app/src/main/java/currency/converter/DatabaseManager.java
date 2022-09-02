package currency.converter;

public class DatabaseManager {

    private boolean isAdmin;

    public DatabaseManager(boolean isAdmin) {
        this.isAdmin = isAdmin;
    }

    public float getConversion(String curr1, String curr2) {
        return 0.000F;
    }

    public float getPastConversion(String curr1, String curr2, String startDate,
                                   String endDate) {
        return 0.000F;
    }

    public void addRate(String curr1, String curr2, float amount, String date) {

    }
}
