package cr.ac.una.pac.man.controller;

import cr.ac.una.pac.man.Level;
import cr.ac.una.pac.man.Statistics;
import cr.ac.una.pac.man.util.FlowController;
import java.io.BufferedReader;
import java.io.FileReader;
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
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;

/**
 * FXML Controller class
 *
 * @author dario
 */
public class StatisticsViewController extends Controller implements Initializable {

    @FXML
    private ImageView img_retroceder;

    ObservableList<Statistics> statisticsList;
    @FXML
    private Label lbl_generalTime;
    @FXML
    private Label lbl_generalScore;
    @FXML
    private Label lbl_generalGhost;
    @FXML
    private Label lbl_generalLifes;

    Statistics statistics;
    @FXML
    private TableColumn<Statistics, String> columnLevel;
    @FXML
    private TableColumn<Statistics, String> columnScore;
    @FXML
    private TableColumn<Statistics, String> columnScoresp;
    @FXML
    private TableColumn<Statistics, String> columnPLays;
    @FXML
    private TableColumn<Statistics, String> columnTime;

    ObservableList<Level> levelList;
    @FXML
    private TableView<Level> tbv_levels;
    @FXML
    private Button btn_top10;

    @Override
    public void initialize(URL url, ResourceBundle rb) {

        columnLevel.setCellValueFactory(new PropertyValueFactory<>("name"));
        columnScore.setCellValueFactory(new PropertyValueFactory<>("score"));
        columnScoresp.setCellValueFactory(new PropertyValueFactory<>("scoreLife"));
        columnPLays.setCellValueFactory(new PropertyValueFactory<>("play"));
        columnTime.setCellValueFactory(new PropertyValueFactory<>("time"));

        statistics = new Statistics();
        
        //tbv_levels.

        getStatistics();
        loadGeneralStatistics();
        loadLevelDataFromFile();

    }

    @Override
    public void initialize() {
    }

    @FXML
    private void onMouseAtras(MouseEvent event) {
        Stage currentStage = (Stage) img_retroceder.getScene().getWindow();
        currentStage.close();
        FlowController.getInstance().goMain();
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

        tbv_levels.setItems(levelList);

        System.out.println("Item: " + levelList.get(0).getName()
                + levelList.get(0).getLevelNumber() + levelList.get(0).isAvailable()
                + levelList.get(0).isComplete());
    }

    public boolean state(String state) {
        return "true".equals(state);
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

    public void loadGeneralStatistics() {

        statistics = statisticsList.get(0);

        lbl_generalScore.setText(statistics.getScore());
        lbl_generalTime.setText(statistics.getTime());
        lbl_generalLifes.setText(statistics.getLifes());
        lbl_generalGhost.setText(statistics.getGhost());

    }

    @FXML
    private void onAction_top(ActionEvent event) {
        System.out.println("Lsds");
        FlowController.getInstance().goViewInWindow("TopPlayersView");
        getStage().close();
         FlowController.getInstance().deleteView("StatisticsView");
    }

}
