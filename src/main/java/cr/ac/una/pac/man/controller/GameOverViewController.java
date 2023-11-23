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
import javafx.scene.control.Label;

/**
 * FXML Controller class
 *
 * @author dario
 */
public class GameOverViewController extends Controller implements Initializable {

    @FXML
    private Label lbl_score;

    ObservableList<Statistics> statisticsList;

    ObservableList<Level> levelList;

//    public int score = (int) AppContext.getInstance().get("GameScore");
//    public int deadGhost = (int) AppContext.getInstance().get("GameDeadGhost");
//    public int lifes = (int) AppContext.getInstance().get("GameLife");
//    public int scporeDead = (int) AppContext.getInstance().get("GameScoreDead");
//    public String time = (String) AppContext.getInstance().get("GameTime");
//    public int nivel = (int) AppContext.getInstance().get("Level");
//
//    public int velocity = (int) AppContext.getInstance().get("velocity");
//    public int encierro = (int) AppContext.getInstance().get("encierro");

    Statistics statistics;

    ObservableList<Trophie> trophiesList;

    @Override
    public void initialize(URL url, ResourceBundle rb) {

        getStatistics();
       // getTrophies();
        //loadLevelDataFromFile();
        lbl_score.setText(statisticsList.get(0).getScore());

        // TODO
    }

    @Override
    public void initialize() {
    }

    //estadisticas
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
    //trofeos

    

    @FXML
    private void onAction_exit(ActionEvent event) {

        //levelUpdate();
        //updateDataTrophies();
       // updateStatistics();
        
        FlowController.getInstance().goViewInWindow("WelcomeView");
        getStage().close();
        FlowController.getInstance().deleteView("GameOverView");
        FlowController.getInstance().deleteView("GameView");

    }

    @FXML
    private void onAction_continue(ActionEvent event) {
        if (Integer.parseInt(statisticsList.get(0).getScore()) >= 1500);

        int score = Integer.parseInt(statisticsList.get(0).getScore());

        score -= 1500;

        statisticsList.get(0).setScore(String.valueOf(score));

        updateStisticsFile(statisticsList.get(0));

        AppContext.getInstance().set("LifeBuy", true);
        getStage().close();
        FlowController.getInstance().deleteView("GameOverView");

    }

}
