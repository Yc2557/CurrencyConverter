package currency.converter;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;
import javafx.scene.text.Text;
import javafx.stage.Stage;

import java.io.IOException;
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class DisplayPopularController {

    @FXML
    private Button backButton;

    @FXML
    private Text from1;

    @FXML
    private Text from2;

    @FXML
    private Text from3;

    @FXML
    private Text from4;

    @FXML
    private ListView<String> listview1;

    @FXML
    private ListView<String> listview2;

    @FXML
    private ListView<String> listview3;

    @FXML
    private ListView<String> listview4;

    @FXML
    private Text to1;

    @FXML
    private Text to2;

    @FXML
    private Text to3;

    @FXML
    private Text to4;

    private CurrencyHandler handler;

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

        populateTable();
    }

    public void populateTable() {
        List<String> popList = handler.getPopularCurrencies();
        String[][] arr = handler.displayPopular();

        to1.setText(popList.get(0));
        to2.setText(popList.get(1));
        to3.setText(popList.get(2));
        to4.setText(popList.get(3));

        from1.setText(popList.get(0));
        from2.setText(popList.get(1));
        from3.setText(popList.get(2));
        from4.setText(popList.get(3));

        listview1.getItems().clear();
        listview2.getItems().clear();
        listview3.getItems().clear();
        listview4.getItems().clear();

        System.out.println(getColumn(arr, 2));
        System.out.println(getColumn(arr, 3));


        listview1.setItems(FXCollections.observableArrayList(getColumn(arr, 0)));
        listview2.setItems(FXCollections.observableArrayList(getColumn(arr, 1)));
        listview3.setItems(FXCollections.observableArrayList(getColumn(arr, 2)));
        listview4.setItems(FXCollections.observableArrayList(getColumn(arr, 3)));
    }

    public List<String> getColumn(String[][] arr, int index) {
        List<String> column = new ArrayList<>();
        for(int i = 0; i < arr[0].length; i++){
            column.add(arr[i][index]);
        }
        return column;
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

