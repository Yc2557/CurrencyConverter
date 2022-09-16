package currency.converter;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.text.Text;
import javafx.stage.Stage;

import java.io.IOException;

public class LoginController {

    @FXML
    private Button loginButton;

    @FXML
    private TextField loginText;

    @FXML
    private Text invalidMessage;

    public void loginButtonAction(ActionEvent event) throws IOException {
        if (loginText.getText().equalsIgnoreCase("user")) {
            System.out.println("change to user");

            changeScene(event, "user");
        } else if (loginText.getText().equalsIgnoreCase("admin")) {
            System.out.println("change to admin");

            changeScene(event, "admin");

        } else {
            System.out.println("invalid");
            setInvalidMessage();
        }
    }

    public void setInvalidMessage() {
        invalidMessage.setText("Invalid User");
    }

    public void changeScene(ActionEvent event, String type) throws IOException {
        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(getClass().getResource("mainPanel.fxml"));
        Parent root = loader.load();
        Scene mainPanelView = new Scene(root);

        mainPanelController controller = loader.getController();
        controller.initScene(type);

        Stage window = (Stage) ((Node) event.getSource()).getScene().getWindow();

        window.setScene(mainPanelView);
        window.show();

    }
}
