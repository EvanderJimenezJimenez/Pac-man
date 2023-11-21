package cr.ac.una.pac.man.controller;

import cr.ac.una.pac.man.Trophie;
import cr.ac.una.pac.man.util.Mensaje;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

/**
 * FXML Controller class
 *
 * @author dario
 */
public class PlayerNameController extends Controller implements Initializable {

    @FXML
    private TextField txt_PlayerName;
    @FXML
    private VBox vbox_1;
    @FXML
    private VBox vbox_2;
    @FXML
    private VBox vbox_3;
    @FXML
    private VBox vbox_4;
    @FXML
    private VBox vbox_5;
    @FXML
    private VBox vbox_6;
    @FXML
    private Button btn_deleteData;

    ObservableList<Trophie> trophiesList;
    @FXML
    private HBox hbox_0;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
    }

    @Override
    public void initialize() {

        loadTrophiesDataFromFile();
        trophiesDisable();
        System.out.println("Trofeo: " + trophiesList.get(0).getName()
                + trophiesList.get(0).getScore()
                + trophiesList.get(0).isComplete());
        trophiesAvailable();
        System.out.println("Trofeo: " + trophiesList.get(0).getName()
                + trophiesList.get(0).getScore()
                + trophiesList.get(0).isComplete());

    }

    private void addPalyer() {
        try {
            String nombre = txt_PlayerName.getText();

            String contenido;

            File playerName = new File(".\\src\\main\\resources\\cr\\ac\\una\\pac\\man\\files\\player.txt");

            FileWriter player = new FileWriter(playerName);
            player.write(nombre + "***");
            player.close();
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    public void trophiesDisable() {
        vbox_1.setDisable(true);
        vbox_1.setOpacity(0);

        vbox_2.setDisable(true);
        vbox_2.setOpacity(0);

        vbox_3.setDisable(true);
        vbox_3.setOpacity(0);

        vbox_4.setDisable(true);
        vbox_4.setOpacity(0);

        vbox_5.setDisable(true);
        vbox_5.setOpacity(0);

        vbox_6.setDisable(true);
        vbox_6.setOpacity(0);

    }

public void marcarCompletado(int index) {
    try {
        // Obtén el trofeo correspondiente
        Trophie trofeo = trophiesList.get(index);

        // Modifica el estado del trofeo
        trofeo.setComplete(true);

        // Actualiza el archivo de trofeos con la nueva información
        updateTrophiesFile(index);

        // Actualiza la interfaz según sea necesario
        // trophiesAvailable();
    } catch (Exception e) {
        e.printStackTrace();
    }
}



private void updateTrophiesFile(int trofeoIndex) {
    try {
        // Lee el contenido actual del archivo trophies.txt
        List<String> lines = Files.readAllLines(Paths.get(".\\src\\main\\resources\\cr\\ac\\una\\pac\\man\\files\\trophies.txt"));

        // Verifica si el índice está dentro de los límites del archivo
        if (trofeoIndex >= 0 && trofeoIndex < lines.size()) {
            // Obtiene la línea correspondiente al trofeo
            String trofeoLine = lines.get(trofeoIndex);

            // Separa los campos del trofeo
            String[] trofeoData = trofeoLine.split("\\(//\\)");

            // Modifica el estado del trofeo en el archivo
            System.out.println(trofeoData[2]);
            trofeoData[2] = "true";
            System.out.println(trofeoData[2]);

            // Une los campos del trofeo con el formato correcto
            String trofeoUpdatedLine = String.join("(//)", trofeoData);

            // Actualiza solo la línea del trofeo en la lista
            lines.set(trofeoIndex, trofeoUpdatedLine);

            // Escribe las líneas actualizadas de vuelta al archivo trophies.txt
            //Files.write(Paths.get(".\\src\\main\\resources\\cr\\ac\\una\\pac\\man\\files\\trophies.txt"), lines);
        } else {
            System.out.println("Índice de trofeo fuera de límites.");
        }
    } catch (IOException ex) {
        ex.printStackTrace();
    }
}





    
    public void trophiesAvailable() {

        for (int i = 0; i < 6; i++) {

            switch (i) {
                case 0:
                    if (Integer.parseInt(trophiesList.get(i).getScore()) != 10) {
                        vbox_1.setDisable(false);
                        vbox_1.setOpacity(100);
                        marcarCompletado(i);
                    }
                    break;
                case 1:
                    if (Integer.parseInt(trophiesList.get(i).getScore()) >=5) {
                        vbox_2.setDisable(false);
                        vbox_2.setOpacity(100);
                        marcarCompletado(i);
                    }
                    break;
                case 2:
                    if (Integer.parseInt(trophiesList.get(i).getScore()) >= 3) {
                        vbox_3.setDisable(false);
                        vbox_3.setOpacity(100);
                        marcarCompletado(i);
                    }
                    break;
                case 3:
                    if (Integer.parseInt(trophiesList.get(i).getScore()) >= 5) {
                        vbox_4.setDisable(false);
                        vbox_4.setOpacity(100);
                    }
                    break;
                case 4:
                    if (Integer.parseInt(trophiesList.get(i).getScore()) >= 0) {
                        vbox_5.setDisable(false);
                        vbox_5.setOpacity(100);
                        
                    }
                    break;
                case 5:
                    if (Integer.parseInt(trophiesList.get(i).getScore()) >= 10) {
                        vbox_6.setDisable(false);
                        vbox_6.setOpacity(100);
                    }
                    break;
                    

            }

        }

    }

    private void loadTrophiesDataFromFile() {
        List<Trophie> trophies = new ArrayList<>();

        try (BufferedReader br = new BufferedReader(new FileReader(".\\src\\main\\resources\\cr\\ac\\una\\pac\\man\\files\\trophies.txt"))) {
            String line;
            while ((line = br.readLine()) != null) {
                String[] trophieData = line.split("\\*\\*\\*");

                for (String trophieString : trophieData) {

                    String[] parts = trophieString.split("\\(//\\)");

                    if (parts.length >= 3) {
                        String name = parts[0];
                        String score = parts[1];

                        boolean complete = state(parts[2]);

                        Trophie troph = new Trophie(name, score, complete);
                        trophies.add(troph);
                    }
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        trophiesList = FXCollections.observableArrayList(trophies);

        System.out.println("Trofeo: " + trophiesList.get(0).getName()
                + trophiesList.get(0).getScore()
                + trophiesList.get(0).isComplete());

    }

    public boolean state(String state) {
        return "true".equals(state);
    }

    @FXML
    private void onAction_savePlayer(ActionEvent event) {

        if (txt_PlayerName.getText().length() > 0) {
            addPalyer();
            getStage().close();

        } else {
            new Mensaje().show(Alert.AlertType.ERROR, "Nombre Jugador", "Ingresa el nombre del jugador.");
        }

    }

    @FXML
    private void onAction_deleteData(ActionEvent event) {
    }

}
