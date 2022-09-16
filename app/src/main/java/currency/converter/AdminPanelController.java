package currency.converter;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.io.IOException;
import java.time.LocalDate;

public class AdminPanelController {

    @FXML
    private Button addCurrencyButton;

    @FXML
    private TextField addCurrencyText;

    @FXML
    private Button changePopularButton;

    @FXML
    private ChoiceBox<?> changeToChoice;

    @FXML
    private ChoiceBox<?> fromCurrencyChoice;

    @FXML
    private ChoiceBox<?> popularNumChoice;

    @FXML
    private ChoiceBox<?> toCurrencyChoice;

    @FXML
    private Button updateRateButton;

    @FXML
    private TextField updateRateText;

    private CurrencyHandler handler;

    public AdminPanelController() {
        handler = new CurrencyHandler(true);
    }


    public Scene changeScene(String filename) throws IOException {
        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(getClass().getResource(filename));
        Parent root = loader.load();

        return new Scene(root);

    }

    public void backAction(ActionEvent event) throws IOException {

        Scene mainPanelView = changeScene("mainPanel.fxml");

        Stage window = (Stage) ((Node) event.getSource()).getScene().getWindow();

        window.setScene(mainPanelView);
        window.show();
    }

    public void addCurrencyAction(ActionEvent event) {
        handler.addCurrency(addCurrencyText.getText());
    }

    public void updateRateAction(ActionEvent event) {
        handler.updateCurrency(fromCurrencyChoice.getSelectionModel().getSelectedItem().toString(),
                toCurrencyChoice.getSelectionModel().getSelectedItem().toString(),
                Float.parseFloat(updateRateText.getText()),
                LocalDate.now());
    }
}

