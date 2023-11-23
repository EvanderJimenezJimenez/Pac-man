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
import java.time.Duration;
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

    public int score = (int) AppContext.getInstance().get("GameScore");
    public int deadGhost = (int) AppContext.getInstance().get("GameDeadGhost");
    public int lifes = (int) AppContext.getInstance().get("GameLife");
    public int scporeDead = (int) AppContext.getInstance().get("GameScoreDead");
    public String time = (String) AppContext.getInstance().get("GameTime");
    public int nivel = (int) AppContext.getInstance().get("Level");

    public int velocity = (int) AppContext.getInstance().get("velocity");
    public int encierro = (int) AppContext.getInstance().get("encierro");

    Statistics statistics;

    ObservableList<Trophie> trophiesList;

    @Override
    public void initialize(URL url, ResourceBundle rb) {

        this.score = (int) AppContext.getInstance().get("GameScore");

//        AppContext.getInstance().delete("Level");
//        AppContext.getInstance().delete("GameTime");
//        AppContext.getInstance().delete("GameScoreDead");
//        AppContext.getInstance().delete("GameScore");
//        AppContext.getInstance().delete("GameDeadGhost");
        System.out.println("Score: " + score);
        System.out.println("DeadG: " + deadGhost);
        System.out.println("lifes: " + lifes);
        System.out.println("sore dead: " + scporeDead);
        System.out.println("level: " + nivel);
        System.out.println("Time: " + time);

        statistics = new Statistics();

        lbl_time.setText(time);
        lbl_score.setText(String.valueOf(score));

        loadLevelDataFromFile();
        levelUpdate();

        getStatistics();
        updateStatistics();

        getTrophies();
        updateDataTrophies();

        lbl_level.setText(String.valueOf(nivel));

    }

    @Override
    public void initialize() {
    }

    public void updateStatistics() {
        statistics = statisticsList.get(0);

        int vidas = Integer.parseInt(statistics.getLifes());
        int ghostD = Integer.parseInt(statistics.getGhost());

        int scoreG = Integer.parseInt(statistics.getScore()) + score;
        statistics.setScore(String.valueOf(scoreG));

        int deadG = Integer.parseInt(statistics.getGhost());
        statistics.setLifes(String.valueOf(deadG + deadGhost));

        String tiempoExistenteStr = statistics.getTime();
        LocalTime tiempoExistente = LocalTime.parse(tiempoExistenteStr, DateTimeFormatter.ofPattern("HH:mm:ss"));
        LocalTime tiempoNuevo = LocalTime.parse(time, DateTimeFormatter.ofPattern("HH:mm:ss"));
        LocalTime tiempoSumado = tiempoExistente.plusHours(tiempoNuevo.getHour())
                .plusMinutes(tiempoNuevo.getMinute())
                .plusSeconds(tiempoNuevo.getSecond());

        statistics.setTime(tiempoSumado.format(DateTimeFormatter.ofPattern("HH:mm:ss")));

        // Suma las vidas actuales con las nuevas
        int totalLifes = vidas + (6 - lifes);

        // Actualiza las estadísticas con el nuevo total de vidas
        statistics.setLifes(String.valueOf(totalLifes));

        statistics.setGhost(String.valueOf(deadG + deadGhost));

        updateStisticsFile(statistics);
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

    @FXML
    private void onAction_continue(ActionEvent event) {

        if (nivel != 10) {
            nivel += 1;
            AppContext.getInstance().set("Level", nivel);
            FlowController.getInstance().goViewInWindow("GameView");
            getStage().close();
            FlowController.getInstance().deleteView("LevelComplete");
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

        //tbhh
        String tiempoExistenteStr = lev.getTime();
        LocalTime tiempoExistente = "00:00:00".equals(tiempoExistenteStr) ? LocalTime.MIN : LocalTime.parse(tiempoExistenteStr, DateTimeFormatter.ofPattern("HH:mm:ss"));
        LocalTime tiempoNuevo = LocalTime.parse(time, DateTimeFormatter.ofPattern("HH:mm:ss"));

        if (tiempoExistente.equals(LocalTime.MIN) || tiempoNuevo.isBefore(tiempoExistente)) {

            lev.setTime(tiempoNuevo.format(DateTimeFormatter.ofPattern("HH:mm:ss")));
        } else {

            lev.setTime(tiempoExistente.format(DateTimeFormatter.ofPattern("HH:mm:ss")));
        }

        int scoreD = Integer.parseInt(lev.getScoreLife());

        if (scoreD < scporeDead) {
            lev.setScoreLife(String.valueOf(scporeDead));
        }

        updateLevelFile();

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

    private void getTrophies() {
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

    }

    private void updateDataTrophies() {

       
        if (lifes == 6) {
            int scoreLifes = Integer.parseInt(trophiesList.get(2).getScore()) +1;
            System.out.println("Lifes txt "+ scoreLifes);
            trophiesList.get(2).setScore(String.valueOf(scoreLifes++));
        }
        if (deadGhost != 0) {
            int dGhost = Integer.parseInt(trophiesList.get(1).getScore());

            trophiesList.get(1).setScore(String.valueOf(dGhost + deadGhost));
        }

        if (encierro != 0) {
            int encierroT = Integer.parseInt(trophiesList.get(3).getScore());

            trophiesList.get(3).setScore(String.valueOf(encierroT + encierro));
        }

        if (velocity != 0) {
            int velocidadT = Integer.parseInt(trophiesList.get(4).getScore());
            trophiesList.get(4).setScore(String.valueOf(velocidadT + velocity));
        }

        updateTrophiesFile();
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

}
