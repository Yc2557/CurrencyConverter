package currency.converter;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.text.Text;
import javafx.stage.Stage;

import java.io.IOException;

public class mainPanelController {

    @FXML
    private Button convertCurrencyButton;

    @FXML
    private Button viewHistoryButton;

    @FXML
    private Button viewPopularButton;
    @FXML
    private Button adminPanelButton;

    @FXML
    private Button backButton;

    @FXML
    private Text welcomeText;

    public void initScene(String type) {
        if (type.equalsIgnoreCase("user")) {
            welcomeText.setText("Welcome, User!");
            adminPanelButton.setVisible(false);
        } else {
            welcomeText.setText("Welcome, Admin!");
            adminPanelButton.setDisable(false);
        }
    }

    public Scene changeScene(String filename) throws IOException {
        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(getClass().getResource(filename));
        Parent root = loader.load();

        return new Scene(root);

    }

    public void backAction(ActionEvent event) throws IOException {

        Scene mainPanelView = changeScene("login.fxml");

        Stage window = (Stage) ((Node) event.getSource()).getScene().getWindow();

        window.setScene(mainPanelView);
        window.show();
    }

    public void adminButtonAction(ActionEvent event) throws IOException {

        Scene mainPanelView = changeScene("adminPanel.fxml");

        Stage window = (Stage) ((Node) event.getSource()).getScene().getWindow();

        window.setScene(mainPanelView);
        window.show();
    }
}
