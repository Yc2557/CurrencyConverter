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

public class CurrencyConvertController {

    @FXML
    private Label amountErrorLabel;

    @FXML
    private TextField amountField;

    @FXML
    private Button backButton;

    @FXML
    private TextField conversionField;

    @FXML
    private Button convertButton;

    @FXML
    private ComboBox<String> fromCurrencyChoice;

    @FXML
    private Text invalidMessage;

    @FXML
    private Label noCurrencyError;

    @FXML
    private Label noCurrencyError1;

    @FXML
    private Label noCurrencyLabel;

    @FXML
    private ComboBox<String> toCurrencyChoice;
    private String type;
    private String filePath;

    private CurrencyHandler handler;

    public void initScene(String type, String filename) {
        this.type = type;
        this.filePath = filename;

        if (type.equalsIgnoreCase("admin")) {
            handler = new CurrencyHandler(true, filename);
        } else {
            handler = new CurrencyHandler(false, filename);
        }
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

    @FXML
    void convert() {
        if (amountField.getText().isBlank() || amountField.getText().isBlank()) {
            invalidMessage.setFill(Color.RED);
            invalidMessage.setText("Please Ensure All Fields Are Filled.");
            return;

        } else if (toCurrencyChoice.getSelectionModel().isEmpty() || fromCurrencyChoice.getSelectionModel().isEmpty()) {
            invalidMessage.setFill(Color.RED);
            invalidMessage.setText("Please Ensure All Currencies Are Selected.");
            return;
        }

        try {
            float amount = Float.parseFloat(amountField.getText());

            double result = handler.convertCurrency(amount, fromCurrencyChoice.getSelectionModel().getSelectedItem(),
                                                    toCurrencyChoice.getSelectionModel().getSelectedItem());

            conversionField.setText(String.format("%.02f", result));
            invalidMessage.setText("");

        } catch (Exception e) {
            invalidMessage.setFill(Color.RED);
            invalidMessage.setText("Invalid Input.");
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

}

