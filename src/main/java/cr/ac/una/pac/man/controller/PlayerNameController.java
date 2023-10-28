package cr.ac.una.pac.man.controller;

import cr.ac.una.pac.man.util.Mensaje;
import java.io.File;
import java.io.FileWriter;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.TextField;

/**
 * FXML Controller class
 *
 * @author dario
 */
public class PlayerNameController extends Controller implements Initializable {

    @FXML
    private TextField txt_PlayerName;

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
    }    

    @Override
    public void initialize() {}
    
            private void addPalyer() {
        try {
            String nombre = txt_PlayerName.getText();

            String contenido;

            File playerName = new File(".\\src\\main\\resources\\cr\\ac\\una\\pac\\man\\files\\player\\player.txt");

            FileWriter player = new FileWriter(playerName);
            player.write(nombre + "***");
            player.close();
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    @FXML
    private void onAction_savePlayer(ActionEvent event) {
        
        if(txt_PlayerName.getText().length() > 0){
            addPalyer();
            getStage().close();
            
        }else{
            new Mensaje().show(Alert.AlertType.ERROR, "Nombre Jugador", "Ingresa el nombre del jugador.");
        }
        
    }
    
}
