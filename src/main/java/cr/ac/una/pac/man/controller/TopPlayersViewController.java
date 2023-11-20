package cr.ac.una.pac.man.controller;

import cr.ac.una.pac.man.Level;
import cr.ac.una.pac.man.TopPlayers;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;

/**
 * FXML Controller class
 *
 * @author dario
 */
public class TopPlayersViewController extends Controller implements Initializable {

 ObservableList<TopPlayers> topPlayersList;
    @FXML
    private TableView<TopPlayers> tbv_top;
    @FXML
    private TableColumn<TopPlayers, String> columnName;
    @FXML
    private TableColumn<TopPlayers, String> columnScore;
 
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        
        columnName.setCellValueFactory(new PropertyValueFactory<>("name"));
        columnScore.setCellValueFactory(new PropertyValueFactory<>("score"));
        
        getDataTops();
        
        // TODO
    }    

    @Override
    public void initialize() {}
    
    public void getDataTops() {

        List<TopPlayers> topsp = new ArrayList<>();

        try (BufferedReader br = new BufferedReader(new FileReader(".\\src\\main\\resources\\cr\\ac\\una\\pac\\man\\files\\topplayers.txt"))) {
            String line;
            while ((line = br.readLine()) != null) {
                String[] topData = line.split("\\*\\*\\*");

                for (String topString : topData) {

                    String[] parts = topString.split("\\(//\\)");

                    if (parts.length >= 1) {

                        String name = parts[0];

                        String score = parts[1];



                       TopPlayers tops = new TopPlayers(name, score);
                        topsp.add(tops);
                    }
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        topPlayersList = FXCollections.observableArrayList(topsp);
        tbv_top.setItems(topPlayersList);
    }
    
}
