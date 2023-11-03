package cr.ac.una.pac.man.controller;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXTextField;
import cr.ac.una.pac.man.util.AppContext;
import cr.ac.una.pac.man.util.FlowController;
import cr.ac.una.pac.man.util.Mensaje;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
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

    private TextField txt_PlayerName;
    @FXML
    private JFXTextField txfPlayerName;//Joshua Cambio
    @FXML
    private JFXButton btnGuardar;
    @FXML
    private JFXButton btnCargarPartida;

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
    }    

    @Override
    public void initialize() {}
    
    private void addPlayer() {
         FileWriter player = null; // Declaración de FileWriter

        try {
            String nombre = txfPlayerName.getText();
            String contenido;

            File playerName = new File(".\\src\\main\\resources\\cr\\ac\\una\\pac\\man\\files\\player.txt");

            // Abre el archivo en modo de añadir (append)
            player = new FileWriter(playerName, true);
            player.write(nombre + "***" + System.lineSeparator());
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                if (player != null) {
                    player.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    @FXML
    private void onAction_savePlayer(ActionEvent event) {
        
            FileWriter player = null;
        
        if (txfPlayerName.getText() == null || txfPlayerName.getText().isBlank()) {  
                new Mensaje().showModal(Alert.AlertType.ERROR, "Validación de usuario", getStage(), "Debe digitar un nombre.");

        }else{    
        try {
            String nombre = txfPlayerName.getText();
            File playerName = new File(".\\src\\main\\resources\\cr\\ac\\una\\pac\\man\\files\\player.txt");

            // Verificar si el nombre ya existe en el archivo
            if (nameExistsInFile(nombre, playerName)) {
                new Mensaje().showModal(Alert.AlertType.ERROR, "Validación de usuario", getStage(), "El nombre ya existe, por favor ingrese otro nombre.");
            } else {
                addPlayer();
                nombreAppContext();
                //FlowController.getInstance().goViewInWindow("GameView");
                FlowController.getInstance().goViewInWindow("ChooseLevel");
                getStage().close();  
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                if (player != null) {
                    player.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }    
        }
        
    }
    
    private boolean nameExistsInFile(String nombre, File file) throws IOException {
        BufferedReader reader = new BufferedReader(new FileReader(file));
        String line;
        while ((line = reader.readLine()) != null) {
            if (line.startsWith(nombre + "***")) {
                reader.close();
                return true;
            }
        }
        reader.close();
        return false;
    }

    private void nombreAppContext(){
    AppContext.getInstance().set("NamePlayer",txfPlayerName.getText());
    }
    @FXML
    private void onActionBtnCargarPartida(ActionEvent event) {
        nombreAppContext();
    }
    
}
