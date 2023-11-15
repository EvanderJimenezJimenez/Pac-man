package cr.ac.una.pac.man.controller;

import cr.ac.una.pac.man.util.FlowController;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;

/**
 * FXML Controller class
 *
 * @author dario
 */
public class PlayerViewController extends Controller implements Initializable {

    @FXML
    private ImageView img_retroceder;

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
    }    

    @Override
    public void initialize() {}

    @FXML
    private void onActionDelete(ActionEvent event) {
    }

    @FXML
    private void onMouseAtras(MouseEvent event) {
        Stage currentStage = (Stage) img_retroceder.getScene().getWindow();
        currentStage.close();
        FlowController.getInstance().goMain();
    }
    
}
