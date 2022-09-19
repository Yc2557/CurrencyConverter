package currency.converter;

import com.google.common.base.Functions;
import com.google.common.collect.Lists;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.ComboBox;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;
import javafx.stage.Stage;

import java.io.IOException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class summaryController {

    @FXML
    private ListView<String> dateList;

    @FXML
    private TextField endField;

    @FXML
    private ComboBox<String> fromCurrencyChoice;

    @FXML
    private Text maxText;

    @FXML
    private Text meanText;

    @FXML
    private Text medText;

    @FXML
    private Text minText;

    @FXML
    private Text invalidText;

    @FXML
    private ListView<String> rateList;

    @FXML
    private Text sdText;

    @FXML
    private TextField startField;

    @FXML
    private ComboBox<String> toCurrencyChoice;

    private CurrencyHandler handler;

    private CurrencyCalculator currCalc;

    private String type;
    private String filePath;

    public void initScene(String type, String filename) {
        this.type = type;
        this.filePath = filename;

        if (type.equalsIgnoreCase("admin")) {
            handler = new CurrencyHandler(true, filename);
        } else {
            handler = new CurrencyHandler(false, filename);
        }

        currCalc = new CurrencyCalculator();

    }

    public void getSummary() {

        if (fromCurrencyChoice.getSelectionModel().isEmpty() || toCurrencyChoice.getSelectionModel().isEmpty()) {
            invalidText.setFill(Color.RED);
            invalidText.setText("Please Select All Currencies.");
            return;
        }

        LocalDate startDate;
        LocalDate endDate;

        try {
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy");
            startDate = LocalDate.parse(startField.getText(), formatter);
            endDate = LocalDate.parse(endField.getText(), formatter);
        } catch (Exception e) {
            invalidText.setFill(Color.RED);
            invalidText.setText("Invalid Date");
            return;
        }


        try {
            HashMap<String, Float> result = handler.printConversionHistory(fromCurrencyChoice.getSelectionModel().getSelectedItem(),
                    toCurrencyChoice.getSelectionModel().getSelectedItem(), startDate, endDate);

            if (result == null) {
                invalidText.setFill(Color.RED);
                invalidText.setText("Could Not Get Summary.");
                return;
            }

            invalidText.setFill(Color.BLACK);
            invalidText.setText("Successfully Gotten Summary.");

            List<Float> listOfRates = new ArrayList<>(result.values());
            Map<String, Float> statMap = currCalc.calculateStatistic(listOfRates);

            List<String> ratesString = new ArrayList<>();

            for (float rate: listOfRates) {
                ratesString.add(String.format("%.03f", rate));
            }
            dateList.getItems().clear();
            dateList.setItems(FXCollections.observableArrayList(result.keySet()));
            rateList.setItems(FXCollections.observableArrayList(ratesString));

            meanText.setText(String.format("%.04f", statMap.get("average")));
            sdText.setText(String.format("%.04f", statMap.get("sd")));
            medText.setText(String.format("%.04f", statMap.get("median")));
            minText.setText(String.format("%.04f", statMap.get("min")));
            maxText.setText(String.format("%.04f", statMap.get("max")));

        } catch (Exception e) {
            invalidText.setFill(Color.RED);
            invalidText.setText("Invalid Input or Not Enough Data for Currency.");
        }

    }

    public Scene changeScene(String filename) throws IOException {
        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(getClass().getClassLoader().getResource(filename));
        Parent root = loader.load();
        mainPanelController controller = loader.getController();
        controller.initScene(type, filePath);

        return new Scene(root);

    }

    public void backAction(ActionEvent event) throws IOException {

        Scene mainPanelView = changeScene("mainPanel.fxml");

        Stage window = (Stage) ((Node) event.getSource()).getScene().getWindow();

        window.setScene(mainPanelView);
        window.show();
    }

    public void populateFromCurrencyChoice() {
        ObservableList<String> list = FXCollections.observableArrayList(handler.getCurrencies());

        fromCurrencyChoice.getItems().clear();

        if (toCurrencyChoice.getSelectionModel().isEmpty()) {
            fromCurrencyChoice.setItems(list);
        } else {
            list.remove(toCurrencyChoice.getSelectionModel().getSelectedItem());
            fromCurrencyChoice.setItems(list);
        }
    }

    public void populatetoCurrencyChoice() {
        ObservableList<String> list = FXCollections.observableArrayList(handler.getCurrencies());

        toCurrencyChoice.getItems().clear();

        if (fromCurrencyChoice.getSelectionModel().isEmpty()) {
            toCurrencyChoice.setItems(list);
        } else {
            list.remove(fromCurrencyChoice.getSelectionModel().getSelectedItem());
            toCurrencyChoice.setItems(list);
        }
    }

}
