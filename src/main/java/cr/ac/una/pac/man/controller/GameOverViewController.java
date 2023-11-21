package cr.ac.una.pac.man.controller;

import cr.ac.una.pac.man.Statistics;
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
    
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        
        getStatistics();
        lbl_score.setText(statisticsList.get(0).getScore());

        // TODO
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
    private void onAction_exit(ActionEvent event) {
        
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
