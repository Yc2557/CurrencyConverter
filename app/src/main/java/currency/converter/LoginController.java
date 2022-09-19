package currency.converter;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import javafx.event.ActionEvent;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;

public class LoginController {

    @FXML
    private TextField loginText;

    @FXML
    private TextField filePathText;

    @FXML
    private Text invalidMessage;

    @FXML
    private Text invalidPathText;

    private String filePath = "src/main/resources/database.json";


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

    public void setInvalidPathMessage() {
        invalidPathText.setText("Invalid Filepath");
    }

    public void changeScene(ActionEvent event, String type) throws IOException {
        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(getClass().getClassLoader().getResource("mainPanel.fxml"));
        Parent root = loader.load();
        Scene mainPanelView = new Scene(root);

        mainPanelController controller = loader.getController();
        controller.initScene(type, filePath);

        Stage window = (Stage) ((Node) event.getSource()).getScene().getWindow();

        window.setScene(mainPanelView);
        window.show();

    }

    public void setFilePathAction(ActionEvent event) {
        try {
            JSONParser jsonParser = new JSONParser();
            JSONObject database = (JSONObject) jsonParser.parse(new FileReader(filePathText.getText()));

            this.filePath = filePathText.getText();
        } catch (ParseException | IOException e) {
            setInvalidPathMessage();
        }
    }
}
