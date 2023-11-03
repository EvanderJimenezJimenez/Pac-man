package cr.ac.una.pac.man.controller;

import cr.ac.una.pac.man.util.AppContext;
import cr.ac.una.pac.man.util.FlowController;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Pos;
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
    @FXML
    private Label lbl_level;
    
    GameData gameData = (GameData) AppContext.getInstance().get("GameData");

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
        lbl_time.setAlignment(Pos.CENTER);
        lbl_level.setAlignment(Pos.CENTER);
        lbl_score.setAlignment(Pos.CENTER);
       // GameViewController gameData = (GameData) AppContext.getInstance().get("GameData");
        
        lbl_score.setText (gameData.getLabelScore() + " pts");
        lbl_level.setText( String.valueOf(gameData.getLabelLevel()));
       
    }

    @Override
    public void initialize() {
    }
    int nivel = (int) AppContext.getInstance().get("Level");
    
    @FXML
    private void onAction_continue(ActionEvent event) {
        if ( gameData.getLabelLevel() <= 10) {
             int nivel = gameData.getLabelLevel();
             int Chulada = (nivel+1);//problema
            gameData.setLabelLevel(Chulada);
            System.out.println(Chulada);
            AppContext.getInstance().set("GameData", gameData);
            
            FlowController.getInstance().goViewInWindow("GameView");
            getStage().close();
        } else {
            FlowController.getInstance().goViewInWindow("WelcomeView");
            getStage().close();
        }
    }

}
