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

import javax.imageio.IIOParam;
import java.io.IOException;

public class mainPanelController {

    @FXML
    private Button convertCurrencyButton;

    @FXML
    private Button viewHistoryButton;

    @FXML
    private Button getSummaryButton;
    @FXML
    private Button adminPanelButton;

    @FXML
    private Button backButton;

    @FXML
    private Text welcomeText;

    private String fileName;
    private String type;

    public void initScene(String type, String fileName) {

        this.fileName = fileName;
        this.type = type;

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
        loader.setLocation(getClass().getClassLoader().getResource(filename));
        Parent root = loader.load();

        return new Scene(root);

    }

    public void backAction(ActionEvent event) throws IOException {

        Scene mainPanelView = changeScene("Login.fxml");

        Stage window = (Stage) ((Node) event.getSource()).getScene().getWindow();

        window.setScene(mainPanelView);
        window.show();
    }

    public void adminButtonAction(ActionEvent event) throws IOException {


        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(getClass().getClassLoader().getResource("adminPanel.fxml"));
        Parent root = loader.load();

        Scene mainPanelView = new Scene(root);

        Stage window = (Stage) ((Node) event.getSource()).getScene().getWindow();

        AdminPanelController controller = loader.getController();
        controller.initScene(fileName);

        window.setScene(mainPanelView);
        window.show();
    }

    public void displayButtonAction(ActionEvent event) throws IOException {


        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(getClass().getClassLoader().getResource("displayPopular.fxml"));
        Parent root = loader.load();

        Scene mainPanelView = new Scene(root);

        Stage window = (Stage) ((Node) event.getSource()).getScene().getWindow();

        DisplayPopularController controller = loader.getController();
        controller.initScene(type, fileName);

        window.setScene(mainPanelView);
        window.show();
    }

    public void convertButtonAction(ActionEvent event) throws IOException {


        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(getClass().getClassLoader().getResource("convertCurrency.fxml"));
        Parent root = loader.load();

        Scene mainPanelView = new Scene(root);

        Stage window = (Stage) ((Node) event.getSource()).getScene().getWindow();

        CurrencyConvertController controller = loader.getController();
        controller.initScene(type, fileName);

        window.setScene(mainPanelView);
        window.show();
    }

    public void summaryButtonAction(ActionEvent event) throws IOException {


        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(getClass().getClassLoader().getResource("summaryPanel.fxml"));
        Parent root = loader.load();

        Scene mainPanelView = new Scene(root);

        Stage window = (Stage) ((Node) event.getSource()).getScene().getWindow();

        summaryController controller = loader.getController();
        controller.initScene(type, fileName);

        window.setScene(mainPanelView);
        window.show();
    }
}
