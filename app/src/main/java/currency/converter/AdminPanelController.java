package currency.converter;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;
import javafx.stage.Stage;

import java.io.IOException;
import java.time.LocalDate;

public class AdminPanelController {

    @FXML
    public Button backButton;
    @FXML
    private Button addCurrencyButton;

    @FXML
    private ListView<String> popularList;

    @FXML
    private TextField addCurrencyText;

    @FXML
    private Button changePopularButton;

    @FXML
    private ComboBox<String> popularNumChoice1;

    @FXML
    private ComboBox<String> popularNumChoice2;

    @FXML
    private ComboBox<String> popularNumChoice3;

    @FXML
    private ComboBox<String> popularNumChoice4;

    @FXML
    private ComboBox<String> fromCurrencyChoice;

    @FXML
    private ComboBox<String> toCurrencyChoice;

    @FXML
    private TextField updateRateText;

    @FXML
    private Text updateRateResult;

    @FXML
    private Text changePopularText;

    @FXML
    private Text addCurrencyResult;


    private CurrencyHandler handler;

    private String filePath;

    public void initScene(String fileName) {
        this.filePath = fileName;
        handler = new CurrencyHandler(true, fileName);
        updateList();
    }

    public void updateList() {
        ObservableList<String> list = FXCollections.observableArrayList(handler.getPopularCurrencies());

        popularList.getItems().clear();
        popularList.setItems(list);
    }

    public Scene changeScene(String filename) throws IOException {
        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(getClass().getClassLoader().getResource(filename));
        Parent root = loader.load();
        mainPanelController controller = loader.getController();
        controller.initScene("admin", filePath);

        return new Scene(root);

    }

    public void backAction(ActionEvent event) throws IOException {

        Scene mainPanelView = changeScene("mainPanel.fxml");

        Stage window = (Stage) ((Node) event.getSource()).getScene().getWindow();

        window.setScene(mainPanelView);
        window.show();
    }

    public void addCurrencyAction(ActionEvent event) {

        if (addCurrencyText.getText().isBlank()) {
            addCurrencyResult.setFill(Color.RED);
            addCurrencyResult.setText("Please Enter Something.");
            return;
        }
        if (handler.addCurrency(addCurrencyText.getText())){
            addCurrencyResult.setFill(Color.BLACK);
            addCurrencyResult.setText("Successfully Add New Currency.");
        } else {
            addCurrencyResult.setFill(Color.RED);
            addCurrencyResult.setText("Currency Already Exists.");
        }


    }

    public void updateRateAction() {

        if (fromCurrencyChoice.getSelectionModel().isEmpty() || toCurrencyChoice.getSelectionModel().isEmpty()) {
            updateRateResult.setFill(Color.RED);
            updateRateResult.setText("Please Select All Currencies.");
            return;
        }

        float run;
        try {
            run = Float.parseFloat(updateRateText.getText());
        } catch (Exception e) {
            updateRateResult.setFill(Color.RED);
            updateRateResult.setText("Please Enter Valid Number.");
            return;
        }
        boolean result = handler.updateCurrency(fromCurrencyChoice.getSelectionModel().getSelectedItem(),
                toCurrencyChoice.getSelectionModel().getSelectedItem(),
                run,
                LocalDate.now());

        if (!result) {
            updateRateResult.setFill(Color.RED);
            updateRateResult.setText("One Currency Must Be AUD");
            return;
        }

        updateRateResult.setFill(Color.BLACK);
        updateRateResult.setText("Successfully Updated Rate.");
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

    public void populatePopularChoice(ComboBox<String> c1, ComboBox<String> c2, ComboBox<String> c3, ComboBox<String> c4) {
        ObservableList<String> list = FXCollections.observableArrayList(handler.getCurrencies());

        c1.getItems().clear();
        if (c2.getSelectionModel().isEmpty() && c3.getSelectionModel().isEmpty() && c4.getSelectionModel().isEmpty()) {
            c1.setItems(list);
        } else {
            if (!c2.getSelectionModel().isEmpty()) {
                list.remove(c2.getSelectionModel().getSelectedItem());
            }
            if (!c3.getSelectionModel().isEmpty()) {
                list.remove(c3.getSelectionModel().getSelectedItem());
            }
            if (!c4.getSelectionModel().isEmpty()) {
                list.remove(c4.getSelectionModel().getSelectedItem());
            }

            c1.setItems(list);
        }
    }

    public void popularNumChoice1Action() {
        populatePopularChoice(popularNumChoice1, popularNumChoice2, popularNumChoice3, popularNumChoice4);
    }

    public void popularNumChoice2Action() {
        populatePopularChoice(popularNumChoice2, popularNumChoice1, popularNumChoice3, popularNumChoice4);
    }

    public void popularNumChoice3Action() {
        populatePopularChoice(popularNumChoice3, popularNumChoice2, popularNumChoice1, popularNumChoice4);
    }

    public void popularNumChoice4Action() {
        populatePopularChoice(popularNumChoice4, popularNumChoice2, popularNumChoice3, popularNumChoice1);
    }

    public void updatePopularAction() {

        if (popularNumChoice1.getSelectionModel().isEmpty() || popularNumChoice2.getSelectionModel().isEmpty() ||
                popularNumChoice3.getSelectionModel().isEmpty() || popularNumChoice4.getSelectionModel().isEmpty()) {
            changePopularText.setFill(Color.RED);
            changePopularText.setText("Please Select All Currencies.");

            return;
        }
        handler.updatePopular(popularNumChoice1.getSelectionModel().getSelectedItem(),
                popularNumChoice2.getSelectionModel().getSelectedItem(),
                popularNumChoice3.getSelectionModel().getSelectedItem(),
                popularNumChoice4.getSelectionModel().getSelectedItem());

        changePopularText.setFill(Color.BLACK);
        changePopularText.setText("Successfully Set New Popular.");

        updateList();
    }
}

