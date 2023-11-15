package cr.ac.una.pac.man.controller;

import com.jfoenix.controls.JFXComboBox;
import cr.ac.una.pac.man.Level;
import cr.ac.una.pac.man.util.AppContext;
import cr.ac.una.pac.man.util.FlowController;
import cr.ac.una.pac.man.util.Mensaje;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.ResourceBundle;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;

/**
 * FXML Controller class
 *
 * @author dario
 */
public class ChooseLevelController extends Controller implements Initializable {

    @FXML
    private JFXComboBox<String> cbx_level;
    @FXML
    private Label lbl_name;
    @FXML
    private ImageView img_level;

    ObservableList<Level> levelList;

    List<String> opcionesColores = Arrays.asList(
            "1-The Joker",
            "2-Los Simpsons",
            "3-IronMan",
            "4-Spider Man",
            "5-Batman",
            "6-Superman",
            "7-DeadPool",
            "8-One Piece",
            "9-Naruto Shippuden",
            "10-Dragon Ball Z"
    );

    int index = 0;
    @FXML
    private Button btn_play;
    @FXML
    private ImageView img_retroceder;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        cbx_level.setItems(FXCollections.observableList(opcionesColores));

        cbx_level.getSelectionModel().select(0);

        loadLevelDataFromFile();
        setDataLevel(levelList.get(index));
        
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

                        Level level = new Level(name, levelNumber, available, complete, score,scoreLife,play,time);
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

    public void setDataLevel(Level levelData) {
        lbl_name.setText(levelData.getName());
        img_level.setImage(imageLevel(levelData.getLevelNumber()));
        System.out.println("Level: " + levelData.getLevelNumber());
        newSelectCbx(Integer.valueOf(levelData.getLevelNumber()));
        
        if(levelData.isAvailable()){
            btn_play.setText("Jugar");
        }else{
            btn_play.setText("Bloqueado");
        }

    }

    public void newSelectCbx(Integer ind) {
        ind -= 1;
        cbx_level.getSelectionModel().select(ind);

    }

    public Image imageLevel(String levelNumber) {

        URL imageURL = null;
        switch (levelNumber) {
            case "1":
                imageURL = getClass().getResource("/cr/ac/una/pac/man/images/levels/joker.png");
                break;
            case "2":
                imageURL = getClass().getResource("/cr/ac/una/pac/man/images/levels/simpsons.jpg");
                break;
            case "3":
                imageURL = getClass().getResource("/cr/ac/una/pac/man/images/levels/ironman.png");
                break;
            case "4":
                imageURL = getClass().getResource("/cr/ac/una/pac/man/images/levels/spiderman.png");
                break;
            case "5":
                imageURL = getClass().getResource("/cr/ac/una/pac/man/images/levels/batman.png");
                break;
            case "6":
                imageURL = getClass().getResource("/cr/ac/una/pac/man/images/levels/superman.jpg");
                break;
            case "7":
                imageURL = getClass().getResource("/cr/ac/una/pac/man/images/levels/deadpool.png");
                break;
            case "8":
                imageURL = getClass().getResource("/cr/ac/una/pac/man/images/levels/onepiece.png");
                break;
            case "9":
                imageURL = getClass().getResource("/cr/ac/una/pac/man/images/levels/naruto.jpg");
                break;
            case "10":
                imageURL = getClass().getResource("/cr/ac/una/pac/man/images/levels/dragonball.jpg");
                break;
            default:
                throw new AssertionError();
        }

        if (imageURL != null) {
            return new Image(imageURL.toExternalForm());
        } else {
            System.out.println("F");
            return null;
        }
    }

    @Override
    public void initialize() {
    }

    boolean available() {

        if (levelList.get(index).isAvailable()) {
            return true;
        } else {
            return false;
        }

    }

    @FXML
    private void onAction_back(ActionEvent event) {
        if (index > 0) {
            index--;
            setDataLevel(levelList.get(index));
        }
    }

    @FXML
    private void onAction_next(ActionEvent event) {

        if (index < levelList.size() - 1) {
            index++;
            setDataLevel(levelList.get(index));
        }

    }

    @FXML
    private void onAction_play(ActionEvent event) {
        
        int level = cbx_level.getSelectionModel().getSelectedIndex();
System.out.println("Level: " + level);
        if (level == 0) {
            level = 1;
        }else{
            level++;
        }

        if (available()) {
             System.out.println("Level: " + level);
            AppContext.getInstance().set("Level", level);
            //FlowController.getInstance().goLoadingView("GameView");
           
            FlowController.getInstance().goViewInWindow("GameView");
            getStage().close();
        } else {
            new Mensaje().show(Alert.AlertType.ERROR, "No disponible", "Nivel aun sin desbloquear.");
        }
    }

    @FXML
    private void onAction_cbx(ActionEvent event) {

        String name = cbx_level.getSelectionModel().getSelectedItem();
        System.out.println("Name: " + name);

        Level lev = levelList.filtered(level -> level.getName().equals(name)).get(0);

        setDataLevel(lev);

    }

    @FXML
    private void onMouseAtras(MouseEvent event) {
        Stage currentStage = (Stage) img_retroceder.getScene().getWindow();
        currentStage.close();
        FlowController.getInstance().goMain();
    }

}
