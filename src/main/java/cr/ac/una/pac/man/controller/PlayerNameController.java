package cr.ac.una.pac.man.controller;

import cr.ac.una.pac.man.GeneratedMap;
import cr.ac.una.pac.man.Level;
import cr.ac.una.pac.man.Statistics;
import cr.ac.una.pac.man.Trophie;
import cr.ac.una.pac.man.util.FlowController;
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
import java.util.logging.Logger;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

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
    @FXML
    private ImageView img_retroceder;

    GeneratedMap gmap;

    ObservableList<Level> levelList;

    ObservableList<Statistics> statisticsList;

    String player = null;

    Statistics statistics;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        statistics = new Statistics();

        getLevels();
        getPlayer();
        getStatistics();
        txt_PlayerName.setText(player);
    }

    public void getPlayer() {
        player = null; // Asegúrate de inicializar la variable antes de usarla

        try (BufferedReader br = new BufferedReader(new FileReader(".\\src\\main\\resources\\cr\\ac\\una\\pac\\man\\files\\player.txt"))) {
            String line;
            while ((line = br.readLine()) != null) {
                // Buscar la posición de "***"
                int index = line.indexOf("***");
                if (index != -1) {
                    // Extraer el nombre antes de "***"
                    player = line.substring(0, index).trim();
                    break; // Rompe el bucle después de encontrar la primera ocurrencia
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public boolean levelsComplete() {

        boolean complete = true;

        for (Level lev : levelList) {

            if (!lev.isComplete()) {
                complete = false;
            }

        }
        return complete;
    }

    @Override
    public void initialize() {

        gmap = new GeneratedMap();
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

    private void getLevels() {
        List<cr.ac.una.pac.man.Level> levels = new ArrayList<>();

        try (BufferedReader br = new BufferedReader(new FileReader(".\\src\\main\\resources\\cr\\ac\\una\\pac\\man\\files\\completedlevels.txt"))) {
            String line;
            while ((line = br.readLine()) != null) {
                String[] levelData = line.split("\\*\\*\\*");

                for (String levelString : levelData) {

                    String[] parts = levelString.split("\\(//\\)");

                    if (parts.length >= 8) {
                        String name = parts[0];
                        String levelNumber = parts[1];

                        boolean available = state(parts[2]);
                        boolean complete = state(parts[3]);

                        String score = parts[4];

                        String scoreLife = parts[5];

                        String play = parts[6];

                        String time = parts[7];

                        cr.ac.una.pac.man.Level level = new cr.ac.una.pac.man.Level(name, levelNumber, available, complete, score, scoreLife, play, time);
                        levels.add(level);
                    }
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        levelList = FXCollections.observableArrayList(levels);

    }

    private void addPalyer(String nombre, String esp) {
        try {

            File playerName = new File(".\\src\\main\\resources\\cr\\ac\\una\\pac\\man\\files\\player.txt");

            FileWriter player = new FileWriter(playerName);
            player.write(nombre + esp);
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

            Trophie trofeo = trophiesList.get(index);

            trofeo.setComplete(true);

            updateTrophiesFile();

            // trophiesAvailable();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void updateTrophiesFile() {
        try {

            String filePath = ".\\src\\main\\resources\\cr\\ac\\una\\pac\\man\\files\\trophies.txt";
            FileWriter writer = new FileWriter(filePath);

            for (Trophie trofeo : trophiesList) {
                String trofeoString = trofeo.getName() + "(//)" + trofeo.getScore() + "(//)" + trofeo.isComplete() + "***";
                writer.write(trofeoString);
            }

            writer.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void trophiesAvailable() {

        for (int i = 0; i < 6; i++) {

            switch (i) {
                case 0:
                    if (levelsComplete() || trophiesList.get(0).isComplete()) {
                        vbox_1.setDisable(false);
                        vbox_1.setOpacity(100);
                        marcarCompletado(i);
                    }
                    break;
                case 1:
                    if (Integer.parseInt(trophiesList.get(i).getScore()) > 5 ||  trophiesList.get(1).isComplete()) {
                        vbox_2.setDisable(false);
                        vbox_2.setOpacity(100);
                        marcarCompletado(i);
                    }
                    break;
                case 2:
                    if (Integer.parseInt(trophiesList.get(i).getScore()) >= 3 ||  trophiesList.get(2).isComplete()) {
                        vbox_3.setDisable(false);
                        vbox_3.setOpacity(100);
                        marcarCompletado(i);
                    }
                    break;
                case 3:
                    if (Integer.parseInt(trophiesList.get(i).getScore()) >= 5 ||  trophiesList.get(3).isComplete()) {
                        vbox_4.setDisable(false);
                        vbox_4.setOpacity(100);
                        marcarCompletado(i);
                    }
                    break;
                case 4:
                    if (Integer.parseInt(trophiesList.get(i).getScore()) >= 5 ||  trophiesList.get(4).isComplete()) {
                        vbox_5.setDisable(false);
                        vbox_5.setOpacity(100);
                        marcarCompletado(i);

                    }
                    break;
                case 5:
                    if (Integer.parseInt(trophiesList.get(i).getScore()) >= 10 ||  trophiesList.get(5).isComplete()) {
                        vbox_6.setDisable(false);
                        vbox_6.setOpacity(100);
                        marcarCompletado(i);
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

    private void updateLevelFile() {
        try {

            String filePath = ".\\src\\main\\resources\\cr\\ac\\una\\pac\\man\\files\\completedlevels.txt";
            FileWriter writer = new FileWriter(filePath);

            for (Level level : levelList) {
                String trofeoString = level.getName() + "(//)" + level.getLevelNumber() + "(//)" + level.isAvailable() + "(//)"
                        + level.isComplete() + "(//)" + level.getScore() + "(//)" + level.getScoreLife()
                        + "(//)" + level.getPlay() + "(//)" + level.getTime() + "***";
                writer.write(trofeoString);
            }

            writer.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void getStatistics() {

        List<Statistics> statiscstics = new ArrayList<>();

        try (BufferedReader br = new BufferedReader(new FileReader(".\\src\\main\\resources\\cr\\ac\\una\\pac\\man\\files\\statistics.txt"))) {
            String line;
            while ((line = br.readLine()) != null) {
                String[] statisticsData = line.split("\\*\\*\\*");

                for (String statisticsString : statisticsData) {

                    String[] parts = statisticsString.split("\\(//\\)");

                    if (parts.length >= 3) {

                        String score = parts[0];

                        String time = parts[1];

                        String lifes = parts[2];

                        String ghost = parts[3];

                        Statistics statistics = new Statistics(score, time, lifes, ghost);
                        statiscstics.add(statistics);
                    }
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        statisticsList = FXCollections.observableArrayList(statiscstics);

    }

    private void updateStisticsFile(Statistics statis) {
        try {

            String filePath = ".\\src\\main\\resources\\cr\\ac\\una\\pac\\man\\files\\statistics.txt";
            FileWriter writer = new FileWriter(filePath);

            String statisticsString = statis.getScore() + "(//)" + statis.getTime() + "(//)" + statis.getLifes() + "(//)"
                    + statis.getGhost() + "***";
            writer.write(statisticsString);

            writer.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void onAction_savePlayer(ActionEvent event) {

        if (txt_PlayerName.getText().length() > 0) {
            addPalyer( txt_PlayerName.getText(),"***");
            //FlowController.getInstance().goMain();
            getStage().close();
            getStage().close();
        
        } else {
            new Mensaje().show(Alert.AlertType.ERROR, "Nombre Jugador", "Ingresa el nombre del jugador.");
        }

    }

    @FXML
    private void onAction_deleteData(ActionEvent event) {
        deleteInfoFiles();
    }

    private void deleteInfoFiles() {

        if (new Mensaje().showConfirmation("Eliminar info", getStage(), "Desea eliminar los datos.")) {
            String level1 = levelList.get(0).getName();

        for(Level lev : levelList){
           // System.out.println(lev.getName());
            if(lev.getName()!= level1){
                System.out.println(lev.getName());
                lev.setAvailable(false);
            }
            lev.setComplete(false);
            lev.setPlay(String.valueOf(0));
            lev.setScoreLife(String.valueOf(0));
            lev.setScore(String.valueOf(0));
            lev.setTime("00:00:00");
        }
        updateLevelFile();
        for (Trophie trophie : trophiesList) {
            trophie.setScore(String.valueOf(0));
            trophie.setComplete(false);
        }
        updateTrophiesFile();
           statistics = statisticsList.get(0);
            statistics.setScore(String.valueOf(0));
            statistics.setLifes(String.valueOf(0));
            statistics.setGhost(String.valueOf(0));
            statistics.setTime("00:00:00");
       
        updateStisticsFile(statisticsList.get(0));
             addPalyer("","");
             getStage().close();
            new Mensaje().show(Alert.AlertType.INFORMATION, "Eliminar info", "Datos eliminados correctamente.");
        }

    }

//gmap.mapGenerated();
    @FXML
    private void onMouseAtras(MouseEvent event) {

            getStage().close();
    

    }

}
