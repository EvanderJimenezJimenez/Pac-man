package cr.ac.una.pac.man.controller;

import cr.ac.una.pac.man.util.FlowController;
import java.io.File;
import java.io.FileWriter;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;

/**
 * @author evand
 */
public class WelcomeView extends Controller implements Initializable {

    @Override
    public void initialize(URL url, ResourceBundle rb) {

    }

    @Override
    public void initialize() {
    }

    private boolean archivoNoVacio() {
        File player = new File(".\\src\\main\\resources\\cr\\ac\\una\\cubeproject\\files\\player\\player.txt");
        return player.exists() && player.length() > 0;
    }

    @FXML
    private void onAction_start(ActionEvent event) {

        if (archivoNoVacio()) {
             FlowController.getInstance().goViewInWindow("GameView");
        getStage().close();
            
        } else {
             FlowController.getInstance().goViewInWindow("PlayerNameView");
        getStage().close();
        }

       
    }

    @FXML
    private void onAction_player(ActionEvent event) {
        FlowController.getInstance().goViewInWindow("PalyerView");
        getStage().close();
    }

    @FXML
    private void onAction_settings(ActionEvent event) {
        FlowController.getInstance().goViewInWindow("SettingView");
        getStage().close();
    }

    @FXML
    private void onAction_exit(ActionEvent event) {
        getStage().close();
    }

}
