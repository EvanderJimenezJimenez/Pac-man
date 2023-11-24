package cr.ac.una.pac.man.controller;

import com.jfoenix.controls.JFXTextField;
import cr.ac.una.pac.man.Level;
import cr.ac.una.pac.man.Statistics;
import cr.ac.una.pac.man.Trophie;
import cr.ac.una.pac.man.util.Mensaje;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.net.URL;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Label;

/**
 * FXML Controller class
 *
 * @author dario
 */
public class SettingViewController extends Controller implements Initializable {

    @FXML
    private Label lbl_score;
    @FXML
    private JFXTextField txt_score;

    ObservableList<Level> levelList;
    ObservableList<Statistics> statisticsList;
    ObservableList<Trophie> trophiesList;

    Statistics statistics;
    @FXML
    private Label lbl_dificultad;

    String dificultad = null;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
        statistics = new Statistics();
        getStatistics();
        loadTrophiesDataFromFile();
        loadLevelDataFromFile();

        getificultad();

        lbl_dificultad.setText(dificultad);

        lbl_score.setText(statisticsList.get(0).getScore());

    }

    @Override
    public void initialize() {
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

    private void loadLevelDataFromFile() {
        List<Level> levels = new ArrayList<>();

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

                        Level level = new Level(name, levelNumber, available, complete, score, scoreLife, play, time);
                        levels.add(level);
                    }
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        levelList = FXCollections.observableArrayList(levels);

    }

    public boolean state(String state) {
        return "true".equals(state);
    }

    //actualizar datos
    public void levelUpdate() {

        for (Level lev : levelList) {
            lev.setComplete(true);
            lev.setAvailable(true);
        }

        updateLevelFile();
    }

    public void trophieUpdate() {

        for (Trophie lev : trophiesList) {
            lev.setComplete(true);
        }

        updateTrophiesFile();
    }

    public void StatisticsUpdate() {
        statistics = statisticsList.get(0);

        if (txt_score.getText().length() > 0) {
            statistics.setScore(txt_score.getText());
            lbl_score.setText(txt_score.getText());
            updateStisticsFile(statistics);
        } else {

        }
    }

    //actualizar archivos
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

    public void getificultad() {
        dificultad = null; // Asegúrate de inicializar la variable antes de usarla

        try (BufferedReader br = new BufferedReader(new FileReader(".\\src\\main\\resources\\cr\\ac\\una\\pac\\man\\files\\dificultad.txt"))) {
            String line;
            while ((line = br.readLine()) != null) {
                // Buscar la posición de "***"
                int index = line.indexOf("***");
                if (index != -1) {
                    // Extraer el nombre antes de "***"
                    dificultad = line.substring(0, index).trim();
                    break; // Rompe el bucle después de encontrar la primera ocurrencia
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void actualizarDificultad(String nombre) {
        try {

            File playerName = new File(".\\src\\main\\resources\\cr\\ac\\una\\pac\\man\\files\\dificultad.txt");

            FileWriter player = new FileWriter(playerName);
            player.write(nombre + "***");
            player.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void onAction_facil(ActionEvent event) {
        lbl_dificultad.setText("facil");
        actualizarDificultad(lbl_dificultad.getText());
        getificultad();
    }

    @FXML
    private void onAction_medio(ActionEvent event) {
        lbl_dificultad.setText("medio");
        actualizarDificultad(lbl_dificultad.getText());
        getificultad();
    }

    @FXML
    private void onAction_dificil(ActionEvent event) {
        lbl_dificultad.setText("dificil");
        actualizarDificultad(lbl_dificultad.getText());
        getificultad();
    }

    @FXML
    private void onAction_activarNiveles(ActionEvent event) {
        levelUpdate();
        new Mensaje().show(Alert.AlertType.INFORMATION, "Niveles", "Todos los niveles han sido debloqueados.");
    }

    @FXML
    private void onAction_activarTrofeos(ActionEvent event) {
        trophieUpdate();
        new Mensaje().show(Alert.AlertType.INFORMATION, "Trofeos", "Todos los trofeos han sido desbloqueados.");

    }

    @FXML
    private void onAction_salir(ActionEvent event) {
        getStage().close();
    }

    @FXML
    private void onActionGuardarScore(ActionEvent event) {
        StatisticsUpdate();
        new Mensaje().show(Alert.AlertType.INFORMATION, "Puntuacion actualizada", "Actualizada correctamente.");
    }

}
