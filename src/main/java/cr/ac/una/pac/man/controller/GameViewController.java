package cr.ac.una.pac.man.controller;

import cr.ac.una.pac.man.util.AppContext;
import cr.ac.una.pac.man.util.FlowController;
import java.awt.Point;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
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

    //map
    private Image wallImage;
    private Image bigPointImage;
    private Image smallPointImage;

    //
    private Image pacmanRight;
    private Image pacmanFeft;
    private Image pacmanUp;
    private Image pacmanDown;

    //Ghost
    private Image blinkyImage;
    private Image clydeImage;

    private ImageView pacmanImageView;

    Image life1;
    Image life2;
    Image life3;
    Image life4;
    Image life5;
    Image life6;
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

    private int frameCount = 0;

    private int frameDelay = 0; // Controla la velocidad de movimiento, ajusta según tus necesidades

    private int pacmanSpeed = 1; // Velocidad predeterminada
    @FXML
    private ImageView imgViewLife6;
    public int nivel;
    private boolean finJuego = false;
    @FXML
    private Label lbl_level;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        this.nivel = (int) AppContext.getInstance().get("Level");
        cargarImagenes();
        inicializarVidas();
        configurarManejoDeTeclado();
        iniciarAnimacionPacman();
        cargarMapa(nivel + 1);
        lbl_level.setText(String.valueOf(nivel + 1));
    }

    public void setnivelActual(int nivelActual) {
        this.nivel = nivelActual;
    }

    public int getNivelActual() {
        return nivel;
    }

    public int getNivelAnterior() {
        return nivel - 1;
    }

    public boolean isFinjuego() {
        return this.finJuego;
    }

    private void cargarImagenes() {
        wallImage = getIamge("wall");
        smallPointImage = getIamge("smallPoint");
        bigPointImage = getIamge("bigPoint");

        //pacman
        pacmanRight = getIamge("pacmanRight");
        pacmanFeft = getIamge("pacmanLeft");
        pacmanUp = getIamge("pacmanUp");
        pacmanDown = getIamge("pacmanDown");

        //ghost
        blinkyImage = getIamge("blinky");
        clydeImage = getIamge("clyde");

        life1 = getIamge("life");
        life2 = getIamge("life");
        life3 = getIamge("life");
        life4 = getIamge("life");
        life5 = getIamge("life");
        life6 = getIamge("life");

    }

    private void inicializarVidas() {
        imgViewLife1.setImage(life1);
        imgViewLife2.setImage(life2);
        imgViewLife3.setImage(life3);
        imgViewLife4.setImage(life4);
        imgViewLife5.setImage(life5);
        imgViewLife6.setImage(life6);
    }

    private void configurarManejoDeTeclado() {
        anchorPane.addEventFilter(KeyEvent.KEY_PRESSED, new EventHandler<KeyEvent>() {
            public void handle(KeyEvent keyEvent) {
                if (keyEvent.getCode() == LEFT) {
                    directionX = -1;
                    directionY = 0;
                    pacmanImageView.setImage(pacmanFeft);
                } else if (keyEvent.getCode() == RIGHT) {
                    directionX = 1;
                    directionY = 0;
                    pacmanImageView.setImage(pacmanRight);
                } else if (keyEvent.getCode() == UP) {
                    directionX = 0;
                    directionY = -1;
                    pacmanImageView.setImage(pacmanUp);
                } else if (keyEvent.getCode() == DOWN) {
                    directionX = 0;
                    directionY = 1;
                    pacmanImageView.setImage(pacmanDown);
                }
            }
        });
    }

    private void iniciarAnimacionPacman() {
        pacManTimeline = new Timeline(new KeyFrame(Duration.millis(200), event -> movePacman()));
        pacManTimeline.setCycleCount(Timeline.INDEFINITE);
        pacManTimeline.play();
    }

    @Override
    public void initialize() {
        
    }

    private void cargarMapa(int nivel) {
        double imageSize = 15.0;
        int cantidadFilas = 15, cantidadColumnas = 15;
        map = new char[cantidadFilas][cantidadColumnas];
        gridPaneMap.getChildren().clear();
        try {
            BufferedReader br = new BufferedReader(new FileReader("src\\main\\resources\\cr\\ac\\una\\pac\\man\\niveles\\nivel (" + nivel + ").txt"));
            String linea;
            int fila = 0;

            while ((linea = br.readLine()) != null) {
                String[] elementos = linea.split(" ");

                for (int columna = 0; columna < cantidadColumnas; columna++) {
                    map[fila][columna] = elementos[columna].charAt(0);
                }

                fila++;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        for (int y = 0; y < map.length; y++) {
            for (int x = 0; x < map[y].length; x++) {
                char cell = map[y][x];
                ImageView imageView = new ImageView();
                imageView.setFitWidth(imageSize);
                imageView.setFitHeight(imageSize);

                switch (cell) {
                    case 'W':
                        imageView.setImage(wallImage);
                        break;
                    case 'S':
                        imageView.setImage(smallPointImage);
                        smallPoints.add(new Point(x, y));
                        break;
                    case 'B':
                        imageView.setImage(blinkyImage);
                        break;
                    case 'D':
                        imageView.setImage(bigPointImage);
                        break;
                    case 'C':
                        imageView.setImage(clydeImage);
                        break;
                    case 'P':
                        pacmanImageView = new ImageView(pacmanRight);
                        pacmanImageView.setFitHeight(imageSize);
                        pacmanImageView.setFitWidth(imageSize);
                        gridPaneMap.add(pacmanImageView, x, y);
                        pacmanX = x;
                        pacmanY = y;
                        break;
                    default:
                        break;
                }

                gridPaneMap.add(imageView, x, y);
            }
        }
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
            case "pacmanLeft":
                imageURL = getClass().getResource("/cr/ac/una/pac/man/resources/pacmanLeft.gif");
                break;
            case "pacmanUp":
                imageURL = getClass().getResource("/cr/ac/una/pac/man/resources/pacmanUp.gif");
                break;
            case "pacmanDown":
                imageURL = getClass().getResource("/cr/ac/una/pac/man/resources/pacmanDown.gif");
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
        frameCount++;

        if (frameCount >= frameDelay) {
            int newPacmanX = pacmanX + directionX;
            int newPacmanY = pacmanY + directionY;

            if (newPacmanX >= 0 && newPacmanX < 15 && newPacmanY >= 0 && newPacmanY < 15) {
                char nextCell = map[newPacmanY][newPacmanX];
                if (nextCell != 'W') {
                    if (nextCell == 'S' || nextCell == 'D') {
                        map[newPacmanY][newPacmanX] = ' ';
                        score += 10;
                        lbl_score.setText(String.valueOf(score));
                        if (levelCompleted()) {
                            FlowController.getInstance().goViewInWindow("LevelComplete");
                            getStage().close();
                            FlowController.getInstance().deleteView("GameView");
                            System.out.println("Hola");
                        }

                        ImageView cellImageView = (ImageView) getNodeByRowColumnIndex(newPacmanY, newPacmanX);
                        gridPaneMap.getChildren().remove(cellImageView);

                        // Reemplazar la imagen de la celda por una celda vacía
                        ImageView emptyImageView = new ImageView();
                        emptyImageView.setFitWidth(15);
                        emptyImageView.setFitHeight(15);
                        gridPaneMap.add(emptyImageView, newPacmanX, newPacmanY);
                    }

                    gridPaneMap.getChildren().remove(pacmanImageView);
                    pacmanX = newPacmanX;
                    pacmanY = newPacmanY;
                    pacmanImageView.setFitHeight(15);
                    pacmanImageView.setFitWidth(15);
                    gridPaneMap.add(pacmanImageView, pacmanX, pacmanY);
                }
            }
            frameCount = 0;
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
                    if (map[y][x] == 'S' || map[y][x] == 'D') {
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
