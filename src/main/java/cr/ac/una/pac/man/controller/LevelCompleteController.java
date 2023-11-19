package cr.ac.una.pac.man.controller;

import cr.ac.una.pac.man.Level;
import cr.ac.una.pac.man.Statistics;
import cr.ac.una.pac.man.Trophie;
import cr.ac.una.pac.man.util.AppContext;
import cr.ac.una.pac.man.util.FlowController;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
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
    @FXML
    private Label lbl_level;

    ObservableList<Level> levelList;
    ObservableList<Statistics> statisticsList;
    int score = (int) AppContext.getInstance().get("GameScore");
    ;
     int lifes = (int) AppContext.getInstance().get("GameLife");

    String time = (String) AppContext.getInstance().get("GameTime");
    int nivel = (int) AppContext.getInstance().get("Level");

    Statistics statistics;

    @Override
    public void initialize(URL url, ResourceBundle rb) {

        statistics = new Statistics();

        lbl_time.setText(time);
        lbl_score.setText(String.valueOf(score));
        lbl_level.setText(String.valueOf(nivel));

        loadLevelDataFromFile();
        getStatistics();
        levelUpdate();
        updateStatistics();

//            System.out.println("Nombre: " +  levelList.get(nivel-1).getName());
//            System.out.println("Número de nivel: " +  levelList.get(nivel-1).getLevelNumber());
//            System.out.println("Disponible: " +  levelList.get(nivel-1).isAvailable());
//            System.out.println("Completo: " +  levelList.get(nivel-1).isComplete());
//            System.out.println("Puntuación: " +  levelList.get(nivel-1).getScore());
//            System.out.println("Puntuación de vida: " +  levelList.get(nivel-1).getScoreLife());
//            System.out.println("¿Jugar?: " +  levelList.get(nivel-1).getPlay());
//            System.out.println("Tiempo: " +  levelList.get(nivel-1).getTime());
//            System.out.println("--------------");
//        
    }

    @Override
    public void initialize() {
    }

    public void updateStatistics() {

        statistics = statisticsList.get(0);
        statistics.setScore(String.valueOf(score));
        statistics.setTime(time);
        statistics.setLifes(String.valueOf(lifes));

        System.out.println("Score: " + statistics.getScore());
        System.out.println("Time: " + statistics.getTime());
        System.out.println("Lifes: " + statistics.getLifes());
        System.out.println("Ghost: " + statistics.getGhost());

     updateStisticsFile();

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

        System.out.println("Item: " + levelList.get(0).getName()
                + levelList.get(0).getLevelNumber() + levelList.get(0).isAvailable()
                + levelList.get(0).isComplete());

    }

    @FXML
    private void onAction_continue(ActionEvent event) {
        if (nivel != 10) {
            nivel += 1;
            AppContext.getInstance().set("Level", nivel);
            FlowController.getInstance().goViewInWindow("GameView");
            getStage().close();
        } else {
            FlowController.getInstance().goViewInWindow("WelcomeView");
            getStage().close();
        }
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

        System.out.println("Item: " + levelList.get(0).getName()
                + levelList.get(0).getLevelNumber() + levelList.get(0).isAvailable()
                + levelList.get(0).isComplete());
    }

    public boolean state(String state) {
        return "true".equals(state);
    }

    public void levelUpdate() {

        Level lev = levelList.get(nivel - 1);

        lev.setComplete(true);
        lev.setScore(String.valueOf(score));

        int play = Integer.parseInt(lev.getPlay());
        lev.setPlay(String.valueOf(play + 1));

        if (nivel != 10) {
            levelList.get(nivel).setAvailable(true);
        }

        updateLevelFile();

    }

    private void updateStisticsFile() {
        try {

            String filePath = ".\\src\\main\\resources\\cr\\ac\\una\\pac\\man\\files\\statistics.txt";
            FileWriter writer = new FileWriter(filePath);

            for (Statistics statistics : statisticsList) {
                String statisticsString = statistics.getScore() + "(//)" + statistics.getTime() + "(//)" + statistics.getLifes() + "(//)"
                        + statistics.getGhost() + "***";
                writer.write(statisticsString);
            }

            writer.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
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

}
