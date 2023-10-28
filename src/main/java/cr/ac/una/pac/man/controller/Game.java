package cr.ac.una.pac.man.controller;

import java.net.URL;
import java.util.ResourceBundle;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;

/**
 * @author evand
 */
public class Game extends Controller implements Initializable {

    private int currentRow = 0;
    private int currentColumn = 0;

    @FXML
    private BorderPane root;
    @FXML
    private GridPane Game_space; // Matriz 16 x 16 para ser el tablero de juego
    @FXML
    private ImageView imgPacman;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        root.requestFocus();
    }

    @Override
    public void initialize() {
    }

    private void handleKeyPress(KeyEvent event) {

    }

    @FXML
    private void OnKeyPressedRoot(KeyEvent event) {
        KeyCode keyPressed = event.getCode();
        switch (keyPressed) {
            case UP:
                if (currentRow > 0) {
                    System.out.println("Hi");
                    Game_space.getChildren().remove(imgPacman);
                    Game_space.add(imgPacman, currentColumn, currentRow - 1);
                    currentRow--;
                }
                break;
            case DOWN:
                if (currentRow < Game_space.getRowCount() - 1) {
                    System.out.println("Hi");
                    Game_space.getChildren().remove(imgPacman);
                    Game_space.add(imgPacman, currentColumn, currentRow + 1);
                    currentRow++;
                }
                break;
            case LEFT:
                if (currentColumn > 0) {
                    System.out.println("Hi");
                    Game_space.getChildren().remove(imgPacman);
                    Game_space.add(imgPacman, currentColumn - 1, currentRow);
                    currentColumn--;
                }
                break;
            case RIGHT:
                if (currentColumn < Game_space.getColumnCount() - 1) {
                    System.out.println("Hi");
                    Game_space.getChildren().remove(imgPacman);
                    Game_space.add(imgPacman, currentColumn + 1, currentRow);
                    currentColumn++;
                }
                break;
            default:
                break;
        }
    }

}
