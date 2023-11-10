package cr.ac.una.pac.man.controller;

import cr.ac.una.pac.man.util.AppContext;
import cr.ac.una.pac.man.util.FlowController;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;

/**
 * FXML Controller class
 *
 * @author dario
 */
public class LevelCompleteController extends Controller implements Initializable {

    @FXML
    private Label lbl_score;
    @FXML
    private Label lbl_time;

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
    }

    @Override
    public void initialize() {
    }
    int nivel = (int) AppContext.getInstance().get("Level");

    @FXML
    private void onAction_continue(ActionEvent event) {
        if (nivel != 10) {
            nivel += 1;
            AppContext.getInstance().set("Level", nivel);
            System.out.println("Level lc: " + nivel);
            FlowController.getInstance().goViewInWindow("GameView");
            getStage().close();
        } else {
            FlowController.getInstance().goViewInWindow("WelcomeView");
            getStage().close();
        }
    }

}
