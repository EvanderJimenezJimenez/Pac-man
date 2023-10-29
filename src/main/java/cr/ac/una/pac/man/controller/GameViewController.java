package cr.ac.una.pac.man.controller;

import cr.ac.una.pac.man.util.FlowController;
import java.awt.Point;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.collections.ObservableList;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.GridPane;

import static javafx.scene.input.KeyCode.SPACE;
import static javafx.scene.input.KeyCode.LEFT;
import static javafx.scene.input.KeyCode.RIGHT;
import static javafx.scene.input.KeyCode.UP;
import static javafx.scene.input.KeyCode.DOWN;
import javafx.util.Duration;

/**
 * FXML Controller class
 *
 * @author dario
 */
public class GameViewController extends Controller implements Initializable {

    //map
    private Image wallImage;
    private Image bigPointImage;
    private Image smallPointImage;

    //
    private Image pacmanRight;

    //Ghost
    private Image blinkyImage;
    private Image clydeImage;

    @FXML
    private GridPane gridPaneMap;
    @FXML
    private ImageView imgViewLife1;
    @FXML
    private ImageView imgViewLife2;
    @FXML
    private ImageView imgViewLife3;
    @FXML
    private ImageView imgViewLife4;
    @FXML
    private ImageView imgViewLife5;
    @FXML
    private Label lbl_score;

    private ImageView pacmanImageView;

    Image life1;
    Image life2;
    Image life3;
    Image life4;
    Image life5;
    @FXML
    private AnchorPane anchorPane;

    private int directionX = 0;
    private int directionY = 0;

    private Timeline pacManTimeline;

    //pocisiones x,y en el grid
    private int pacmanX;
    private int pacmanY;

    char[][] map;

    private List<Point> smallPoints = new ArrayList<>();

    int score = 0;

    @Override
    public void initialize(URL url, ResourceBundle rb) {

        wallImage = getIamge("wall");
        smallPointImage = getIamge("smallPoint");
        bigPointImage = getIamge("bigPoint");
        pacmanRight = getIamge("pacmanRight");

        //ghost
        blinkyImage = getIamge("blinky");
        clydeImage = getIamge("clyde");

        life1 = getIamge("life");
        life2 = getIamge("life");
        life3 = getIamge("life");
        life4 = getIamge("life");
        life5 = getIamge("life");

        imgViewLife1.setImage(life1);
        imgViewLife2.setImage(life2);
        imgViewLife3.setImage(life3);
        imgViewLife4.setImage(life4);
        imgViewLife5.setImage(life5);

        map = new char[][]{
            {'W', 'W', 'W', 'W', 'W', 'W', 'W', 'W', 'W', 'W', 'W', 'W', 'W', 'W', 'W'},
            {'W', 'S', 'S', 'S', 'S', 'W', 'S', 'S', 'S', 'S', 'W', 'S', 'S', 'S', 'W'},
            {'W', 'W', 'S', 'W', 'S', 'W', 'S', 'W', 'W', 'W', 'W', 'S', 'W', 'W', 'W'},
            {'W', 'W', 'S', 'W', 'S', 'W', 'S', 'W', 'W', 'W', 'W', 'S', 'W', 'W', 'W'},
            {'W', 'W', 'S', 'W', 'S', 'W', 'S', 'S', 'S', 'S', 'S', 'S', 'S', 'S', 'W'},
            {'W', 'W', 'S', 'W', 'S', 'W', 'S', 'W', 'W', 'W', ' ', 'W', 'S', 'W', 'W'},
            {'W', 'W', 'S', 'W', 'S', 'W', 'S', 'W', ' ', ' ', ' ', 'W', 'S', 'W', 'W'},
            {'W', 'S', 'S', 'S', 'S', 'S', 'S', 'W', 'B', ' ', 'C', 'W', 'S', 'W', 'W'},
            {'W', 'W', 'W', 'W', 'W', 'W', 'S', 'W', ' ', ' ', ' ', 'W', 'S', 'W', 'W'},
            {'W', 'W', 'W', 'W', 'W', 'W', 'S', 'W', 'W', 'W', 'W', 'W', 'S', 'W', 'W'},
            {'W', 'S', 'S', 'S', 'S', 'S', 'S', 'S', 'S', 'S', 'S', 'S', 'S', 'S', 'W'},
            {'W', 'W', 'W', 'W', 'W', 'S', 'W', 'W', 'W', 'W', 'W', 'S', 'W', 'S', 'W'},
            {'W', 'W', 'W', 'W', 'W', 'S', 'W', 'W', 'W', 'W', 'W', 'S', 'W', 'S', 'W'},
            {'W', 'S', 'S', 'S', 'S', 'S', 'P', 'S', 'S', 'S', 'S', 'S', 'S', 'S', 'W'},
            {'W', 'W', 'W', 'W', 'W', 'W', 'W', 'W', 'W', 'W', 'W', 'W', 'W', 'W', 'W'}
        };

        double imageSize = 15.0;

        for (int y = 0; y < map.length; y++) {
            for (int x = 0; x < map[y].length; x++) {
                char cell = map[y][x];
                ImageView imageView = new ImageView();
                imageView.setFitWidth(imageSize);
                imageView.setFitHeight(imageSize);

                if (cell == 'W') {
                    imageView.setImage(wallImage);
                } else if (cell == 'S') {
                    imageView.setImage(smallPointImage);
                    smallPoints.add(new Point(x, y));
                } else if (cell == 'B') {
                    imageView.setImage(blinkyImage);
                } else if (cell == 'C') {
                    imageView.setImage(clydeImage);
                } else if (cell == 'P') {
                    pacmanImageView = new ImageView(pacmanRight);
                    pacmanImageView.setFitHeight(imageSize);
                    pacmanImageView.setFitWidth(imageSize);
                    gridPaneMap.add(pacmanImageView, x, y);
                    pacmanX = x;
                    pacmanY = y;
                }

                gridPaneMap.add(imageView, x, y);
            }
        }

        pacManTimeline = new Timeline(new KeyFrame(Duration.millis(200), event -> movePacman()));
        pacManTimeline.setCycleCount(Timeline.INDEFINITE);

        anchorPane.addEventFilter(KeyEvent.KEY_PRESSED, new EventHandler<KeyEvent>() {
            public void handle(KeyEvent keyEvent) {
                if (keyEvent.getCode() == LEFT) {
                    directionX = -1;
                    directionY = 0;
                } else if (keyEvent.getCode() == RIGHT) {
                    directionX = 1;
                    directionY = 0;
                } else if (keyEvent.getCode() == UP) {
                    directionX = 0;
                    directionY = -1;
                } else if (keyEvent.getCode() == DOWN) {
                    directionX = 0;
                    directionY = 1;
                }
                pacManTimeline.play();
            }
        });

    }

    @Override
    public void initialize() {
    }

    public Image getIamge(String imageName) {

        URL imageURL = null;
        switch (imageName) {
            case "wall":
                imageURL = getClass().getResource("/cr/ac/una/pac/man/resources/wall.png");
                break;
            case "smallPoint":
                imageURL = getClass().getResource("/cr/ac/una/pac/man/resources/smallPoint.png");
                break;
            case "bigPoint":
                imageURL = getClass().getResource("/cr/ac/una/pac/man/resources/bigPoint.png");
                break;
            case "life":
                imageURL = getClass().getResource("/cr/ac/una/pac/man/resources/life.png");
                break;
            case "pacmanRight":
                imageURL = getClass().getResource("/cr/ac/una/pac/man/resources/pacmanRight.gif");
                break;
            case "clyde":
                imageURL = getClass().getResource("/cr/ac/una/pac/man/resources/Clyde.gif");
                break;
            case "blinky":
                imageURL = getClass().getResource("/cr/ac/una/pac/man/resources/Blinky.gif");
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

    private void movePacman() {
        int newPacmanX = pacmanX + directionX;
        int newPacmanY = pacmanY + directionY;

        if (newPacmanX >= 0 && newPacmanX < 15 && newPacmanY >= 0 && newPacmanY < 15) {
            char nextCell = map[newPacmanY][newPacmanX];

            if (nextCell != 'W') {

                if (nextCell == 'S') {

                    map[newPacmanY][newPacmanX] = ' ';
                    score += 10;
                    lbl_score.setText(String.valueOf(score));
                    if (levelCompleted()) {
                         FlowController.getInstance().goViewInWindow("LevelComplete");
                        System.out.println("Hola");
                    }

                }

                ImageView cellImageView = (ImageView) getNodeByRowColumnIndex(newPacmanY, newPacmanX);
                gridPaneMap.getChildren().remove(cellImageView);

                //reemplaaz la imagen
                ImageView emptyImageView = new ImageView();
                emptyImageView.setFitWidth(15);
                emptyImageView.setFitHeight(15);
                gridPaneMap.add(emptyImageView, newPacmanX, newPacmanY);

                gridPaneMap.getChildren().remove(pacmanImageView);
                pacmanX = newPacmanX;
                pacmanY = newPacmanY;
                pacmanImageView.setFitHeight(15);
                pacmanImageView.setFitWidth(15);
                gridPaneMap.add(pacmanImageView, pacmanX, pacmanY);
            }
        }
    }

// Función para obtener una ImageView en una fila y columna específicas
    private Node getNodeByRowColumnIndex(final int row, final int column) {
        Node result = null;
        ObservableList<Node> children = gridPaneMap.getChildren();

        for (Node node : children) {
            if (GridPane.getRowIndex(node) == row && GridPane.getColumnIndex(node) == column) {
                result = node;
                break;
            }
        }

        return result;
    }

    public boolean levelCompleted() {
        boolean isComplete = true;

        if (map != null) {
            for (int y = 0; y < map.length; y++) {
                for (int x = 0; x < map[y].length; x++) {
                    if (map[y][x] == 'S') {
                        isComplete = false;
                        break;
                    }
                }
                if (!isComplete) {
                    break;
                }
            }
        }
        return isComplete;
    }

}
