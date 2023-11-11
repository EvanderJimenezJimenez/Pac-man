package cr.ac.una.pac.man.controller;

import cr.ac.una.pac.man.Algorithms;
import cr.ac.una.pac.man.GameMap;
import cr.ac.una.pac.man.Pacman;
import cr.ac.una.pac.man.util.AppContext;
import cr.ac.una.pac.man.util.FlowController;
import java.awt.Point;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Random;
import java.util.ResourceBundle;
import java.util.Set;
import java.util.Timer;
import java.util.TimerTask;

import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.event.ActionEvent;

import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.GridPane;

import static javafx.scene.input.KeyCode.LEFT;
import static javafx.scene.input.KeyCode.RIGHT;
import static javafx.scene.input.KeyCode.UP;
import static javafx.scene.input.KeyCode.DOWN;
import javafx.scene.layout.Background;
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

    private ImageView pacmanImageView; //pacman en el mapa

    private ImageView blinkyImageView; //pacman en el mapa

    private ImageView pinkyImageView; //pacman en el mapa

    private ImageView inkyImageView; //pacman en el mapa

    private ImageView clydeImageView; //pacman en el mapa

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

    //a base
    private int frameCountInitial = 0;
    private int frameDelayInitial = 4;

    private int frameCount = 0;
    private int frameCountBlinky = 0;
    private int frameCountPinky = 0;
    private int frameCountClyde = 0;
    private int frameCountInky = 0;

    private int frameDelay = 1; // Controla la velocidad de movimiento, ajusta según tus necesidades
    private int frameDelayBlinky = 5;
    private int frameDelayPinky = 4;
    private int frameDelayClyde = 4;
    private int frameDelayInky = 5;

    private int pacmanSpeed = 1; // Velocidad predeterminada
    private int blinkySpeed = 0;

    private int blinkyX;
    private int blinkyY;

    private int pinkyX;
    private int pinkyY;

    private int inkyX;
    private int inkyY;

    private int clydeX;
    private int clydeY;

    //house
    private int blinkyXHouse;
    private int blinkyYHouse;

    private int pinkyXHouse;
    private int pinkyYHouse;

    private int inkyXHouse;
    private int inkyYHouse;

    private int clydeXHouse;
    private int clydeYHouse;

    private int pacmanXHouse;
    private int pacmanYHouse;

    private Timeline blinkyTimeline;

    private Timeline pinkyTimeline;
    private Timeline clydeTimeline;

    private Timeline inkyTimeline;

    int[][] weightedGraph;

    int[][] floydMatriz;
    @FXML
    private ImageView imgViewLife6;
    public int nivel;

    private boolean finJuego = false;
    @FXML
    private Label lbl_level;

    private Point currentRandomPoint;
    private boolean isMovingToRandomPoint = false;

    private boolean inkyMovingToRandomPoint = false;
    private Point inkyTargetPoint;
    private Timer inkyTimer;

    boolean blinkyPacman;

    int lifes = 6;

    GameMap gameMap;
    Algorithms algorithms;
    Pacman paman;

    private boolean isPoweredUp = false;

    private Timeline ghostTimeline;

    boolean shockBlinky = false;
    boolean shockPinky = false;
    boolean shockInky = false;
    boolean shockClyde = false;

    private List<Point> tunnels = new ArrayList<>();

    boolean blinkyEnc = false;
    boolean pinkyEnc = false;
    boolean clydeEnc = false;
    boolean inkyEnc = false;
    boolean encierro = false;
    @FXML
    private Button btn_encierro;
    @FXML
    private Button btn_velocidad;

    @Override
    public void initialize(URL url, ResourceBundle rb) {

        gameMap = new GameMap();
        gameMap.cargarImagenes();

        paman = new Pacman();

        algorithms = new Algorithms();

        this.nivel = (int) AppContext.getInstance().get("Level");
        inicializarVidas();
        configurarManejoDeTeclado();
        iniciarAnimacionPacman();

        cargarMapa(nivel);
        lbl_level.setText(String.valueOf(nivel));

        for (int i = 0; i < 15; i++) {
            for (int j = 0; j < 15; j++) {
                System.out.print(map[i][j] + " ");
            }
            System.out.println();  // Nueva línea después de cada fila
        }
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

    private void inicializarVidas() {
        imgViewLife1.setImage(gameMap.getLife1());
        imgViewLife2.setImage(gameMap.getLife2());
        imgViewLife3.setImage(gameMap.getLife3());
        imgViewLife4.setImage(gameMap.getLife4());
        imgViewLife5.setImage(gameMap.getLife5());
        imgViewLife6.setImage(gameMap.getLife6());
    }

    private void configurarManejoDeTeclado() {
        anchorPane.addEventFilter(KeyEvent.KEY_PRESSED, new EventHandler<KeyEvent>() {
            public void handle(KeyEvent keyEvent) {
                if (keyEvent.getCode() == LEFT) {
                    directionX = -1;
                    directionY = 0;
                    pacmanImageView.setImage(gameMap.getPacmanFeft());
                } else if (keyEvent.getCode() == RIGHT) {
                    directionX = 1;
                    directionY = 0;
                    pacmanImageView.setImage(gameMap.getPacmanRight());
                } else if (keyEvent.getCode() == UP) {
                    directionX = 0;
                    directionY = -1;
                    pacmanImageView.setImage(gameMap.getPacmanUp());
                } else if (keyEvent.getCode() == DOWN) {
                    directionX = 0;
                    directionY = 1;
                    pacmanImageView.setImage(gameMap.getPacmanDown());
                }
                pacManTimeline.play();

                blinkyTimeline.play();
                pinkyTimeline.play();
                clydeTimeline.play();
                inkyTimeline.play();
                {

                }

            }
        });
    }

    //verMatriz();
    private void iniciarAnimacionPacman() {

        pacManTimeline = new Timeline(new KeyFrame(Duration.millis(200), event -> movePacman()));
        pacManTimeline.setCycleCount(Timeline.INDEFINITE);

        blinkyTimeline = new Timeline(new KeyFrame(Duration.millis(200), event -> blinkyMove()));
        blinkyTimeline.setCycleCount(Timeline.INDEFINITE);

        pinkyTimeline = new Timeline(new KeyFrame(Duration.millis(200), event -> pinkyMove()));
        pinkyTimeline.setCycleCount(Timeline.INDEFINITE);

        clydeTimeline = new Timeline(new KeyFrame(Duration.millis(200), event -> clydeMove()));
        clydeTimeline.setCycleCount(Timeline.INDEFINITE);

        pacManTimeline = new Timeline(new KeyFrame(Duration.millis(200), event -> movePacman()));
        pacManTimeline.setCycleCount(Timeline.INDEFINITE);

        inkyTimeline = new Timeline(new KeyFrame(Duration.millis(200), event -> inkymove()));
        inkyTimeline.setCycleCount(Timeline.INDEFINITE);

        // pacManTimeline.play();
    }

    @Override
    public void initialize() {
        //blinkyTimeline.play();

        inkyTimer = new Timer();

        inkyTimer.scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
                inkyTargetPoint = algorithms.selectRandomPointInky(smallPoints, pacmanX, pacmanY, blinkyX, blinkyY, clydeX, clydeY, pinkyX, pinkyY);
                inkyMovingToRandomPoint = true;
            }
        }, 0, 5000);

    }

    private void cargarMapa(int nivel) {
        double imageSize = 15.0;

        gridPaneMap.getChildren().clear();

        gameMap.cargarMapa(nivel);

        map = gameMap.getMap();

        for (int y = 0; y < map.length; y++) {
            for (int x = 0; x < map[y].length; x++) {
                char cell = map[y][x];
                ImageView imageView = new ImageView();
                imageView.setFitWidth(imageSize);
                imageView.setFitHeight(imageSize);

                if (cell == 'W') {
                    imageView.setImage(gameMap.getWallImage());
                } else if (cell == 'S') {
                    imageView.setImage(gameMap.getSmallPointImage());
                    smallPoints.add(new Point(x, y));
                } else if (cell == 'B') {
                    blinkyImageView = new ImageView(gameMap.getBlinkyImage());
                    blinkyImageView.setFitHeight(imageSize);
                    blinkyImageView.setFitWidth(imageSize);
                    gridPaneMap.add(blinkyImageView, x, y);
                    blinkyX = x;
                    blinkyY = y;
                    blinkyXHouse = x;
                    blinkyYHouse = y;

                } else if (cell == 'I') {
                    inkyImageView = new ImageView(gameMap.getInkyImage());
                    inkyImageView.setFitHeight(imageSize);
                    inkyImageView.setFitWidth(imageSize);
                    gridPaneMap.add(inkyImageView, x, y);
                    inkyX = x;
                    inkyY = y;
                    inkyXHouse = x;
                    inkyYHouse = y;

                } else if (cell == 'R') {
                    pinkyImageView = new ImageView(gameMap.getPinkyImage());
                    pinkyImageView.setFitHeight(imageSize);
                    pinkyImageView.setFitWidth(imageSize);
                    gridPaneMap.add(pinkyImageView, x, y);
                    pinkyX = x;
                    pinkyY = y;
                    pinkyXHouse = x;
                    pinkyYHouse = y;

                } else if (cell == 'D') {
                    imageView.setImage(gameMap.getBigPointImage());
                } else if (cell == 'C') {

                    clydeImageView = new ImageView(gameMap.getClydeImage());
                    clydeImageView.setFitHeight(imageSize);
                    clydeImageView.setFitWidth(imageSize);
                    gridPaneMap.add(clydeImageView, x, y);
                    clydeX = x;
                    clydeY = y;
                    clydeXHouse = x;
                    clydeYHouse = y;

                } else if (cell == 'P') {
                    pacmanImageView = new ImageView(gameMap.getPacmanFeft());
                    pacmanImageView.setFitHeight(imageSize);
                    pacmanImageView.setFitWidth(imageSize);
                    gridPaneMap.add(pacmanImageView, x, y);
                    pacmanX = x;
                    pacmanY = y;
                    pacmanXHouse = x;
                    pacmanYHouse = y;
                }
                if (cell == 'T') {
                    if (tunnels.isEmpty()) {
                        tunnels.add(new Point(x, y));
                    } else {
                        tunnels.add(new Point(x, y));
                    }
                }

                gridPaneMap.add(imageView, x, y);
            }
        }

        weightedGraph = algorithms.createWeightedGraph(map);

        floydMatriz = algorithms.floydWarshall(weightedGraph);

    }

    private void inkymove() {
        frameCountInky++;

        if (frameCountInky >= frameDelayInky) {
            if (inkyMovingToRandomPoint) {
                int startNode = inkyY * 15 + inkyX;
                int targetNode = 0;

                if (!shockInky && !inkyEnc) {
                    targetNode = inkyTargetPoint.y * 15 + inkyTargetPoint.x;
                } else {
                    targetNode = inkyYHouse * 15 + inkyXHouse;
                    frameDelayInky = 0;
                    if (inkyX == inkyXHouse && inkyY == inkyYHouse) {
                        Timeline timeline = new Timeline(new KeyFrame(Duration.seconds(10), event -> {
                            shockInky = false;
                            inkyEnc = false;
                            frameDelayInky = 4;
                        }));
                        timeline.play();
                    }
                }

                List<Integer> shortestPath = algorithms.shortestPath(startNode, targetNode, weightedGraph);

                if (shortestPath != null && shortestPath.size() > 1) {
                    int nextNode = shortestPath.get(1);
                    int nextX = nextNode % 15;
                    int nextY = nextNode / 15;

                    inkyX = nextX;
                    inkyY = nextY;

                    if (checkInkyCollision() && !isPoweredUp && !inkyEnc) {
                        //reinicio;
                        handleCollision();
                    }
                    gridPaneMap.getChildren().remove(inkyImageView);
                    gridPaneMap.add(inkyImageView, inkyX, inkyY);

                }
            }
            frameCountInky = 0;
        }
    }

    private void clydeMove() {
        frameCountClyde++;

        if (frameCountClyde >= frameDelayClyde) {
            if (!isMovingToRandomPoint) {
                currentRandomPoint = algorithms.selectRandomPoint(smallPoints);
                isMovingToRandomPoint = true;
            }

            int startNode = clydeY * 15 + clydeX;
            int randomX = currentRandomPoint.x;
            int randomY = currentRandomPoint.y;
            int targetNode = 0;
            if (!shockClyde && !clydeEnc) {
                targetNode = randomY * 15 + randomX;
            } else {
                targetNode = clydeYHouse * 15 + clydeXHouse;
                frameDelayClyde = 0;
                if (clydeX == clydeXHouse && clydeY == clydeYHouse) {
                    Timeline timeline = new Timeline(new KeyFrame(Duration.seconds(10), event -> {
                        shockClyde = false;
                        clydeEnc = false;
                        frameDelayClyde = 5;
                    }));
                    timeline.play();
                }
            }

            int shortestDistance = floydMatriz[startNode][targetNode];

            if (shortestDistance != Integer.MAX_VALUE) {
                int nextX = clydeX;
                int nextY = clydeY;

                for (int dx = -1; dx <= 1; dx++) {
                    for (int dy = -1; dy <= 1; dy++) {
                        int newX = clydeX + dx;
                        int newY = clydeY + dy;
                        int neighborNode = newY * 15 + newX;

                        if (newX >= 0 && newX < 15 && newY >= 0 && newY < 15
                                && floydMatriz[neighborNode][targetNode] < shortestDistance) {
                            shortestDistance = floydMatriz[neighborNode][targetNode];
                            nextX = newX;
                            nextY = newY;
                        }
                    }
                }

                // Si Clyde ha llegado al punto aleatorio, cambia la bandera de movimiento
                if (nextX == randomX && nextY == randomY) {
                    isMovingToRandomPoint = false;
                }

                // Actualiza la posición de Clyde
                gridPaneMap.getChildren().remove(clydeImageView);
                clydeX = nextX;
                clydeY = nextY;
                gridPaneMap.add(clydeImageView, clydeX, clydeY);

                if (checkClydeCollision() && !isPoweredUp && clydeEnc) {
                    handleCollision();
                }
            }

            frameCountClyde = 0;
        }

    }

    private void blinkyMove() {
        frameCountBlinky++;

        if (frameCountBlinky >= frameDelayBlinky) {
            int startNode = blinkyY * 15 + blinkyX;
            int targetNode = 0;

            if (!shockBlinky && !blinkyEnc) {
                targetNode = pacmanY * 15 + pacmanX;
            } else {
                targetNode = blinkyYHouse * 15 + blinkyXHouse;
                frameDelayBlinky = 0;
                if (blinkyX == blinkyXHouse && blinkyY == blinkyYHouse) {
                    Timeline timeline = new Timeline(new KeyFrame(Duration.seconds(10), event -> {
                        shockBlinky = false;
                        blinkyEnc = false;
                        frameDelayBlinky = 3;
                    }));
                    timeline.play();
                }
            }
            List<Integer> shortestPath = algorithms.shortestPath(startNode, targetNode, weightedGraph);

            System.out.println("BB: " + shortestPath.size());
            if (shortestPath.size() == 1 && !isPoweredUp && !blinkyEnc) {
                handleCollision();
            }
            if (shortestPath != null && shortestPath.size() > 1 && lifes > 1) {
                // Obtiene el siguiente nodo en el camino más corto
                int nextNode = shortestPath.get(1);
                int nextX = nextNode % 15; // Convierte el nodo en coordenadas X
                int nextY = nextNode / 15; // Convierte el nodo en coordenadas Y

                // Actualiza la posición de Blinky
                blinkyX = nextX;
                blinkyY = nextY;

                gridPaneMap.getChildren().remove(blinkyImageView);
                gridPaneMap.add(blinkyImageView, blinkyX, blinkyY);
            }

            frameCountBlinky = 0;
        }
    }

    private void pinkyMove() {
        frameCountPinky++;

        if (frameCountPinky >= frameDelayPinky) {
            // Calcula la posición anticipada de Pac-Man
            int anticipatedPacmanX = pacmanX;
            int anticipatedPacmanY = pacmanY;
            int stepsToAnticipate = 4; // Anticipa 4 pasos por delante

            for (int i = 0; i < stepsToAnticipate; i++) {
                anticipatedPacmanX += directionX;
                anticipatedPacmanY += directionY;

                if (map[anticipatedPacmanY][anticipatedPacmanX] == 'W' || map[anticipatedPacmanY][anticipatedPacmanX] == 'T') {

                    anticipatedPacmanX -= directionX;
                    anticipatedPacmanY -= directionY;
                    break;
                }
            }

            int startNode = pinkyY * 15 + pinkyX;
            int targetNode = 0;
            if (!shockPinky && !pinkyEnc) {
                targetNode = anticipatedPacmanY * 15 + anticipatedPacmanX;
            } else {
                targetNode = pinkyYHouse * 15 + pinkyXHouse;
                frameDelayPinky = 0;
                if (pinkyX == pinkyXHouse && pinkyY == pinkyYHouse) {
                    Timeline timeline = new Timeline(new KeyFrame(Duration.seconds(10), event -> {
                        shockPinky = false;
                        pinkyEnc = false;
                        frameDelayPinky = 4;
                    }));
                    timeline.play();
                }
            }

            List<Integer> shortestPath = algorithms.shortestPath(startNode, targetNode, weightedGraph);

            if (shortestPath.size() == 1 && !isPoweredUp && !pinkyEnc) {
                handleCollision();
            }
            if (shortestPath != null && shortestPath.size() > 1) {

                int nextNode = shortestPath.get(1);
                int nextX = nextNode % 15;
                int nextY = nextNode / 15;

                // Actualiza la posición de Pinky
                pinkyX = nextX;
                pinkyY = nextY;

                gridPaneMap.getChildren().remove(pinkyImageView);
                gridPaneMap.add(pinkyImageView, pinkyX, pinkyY);
            }

            frameCountPinky = 0;
        }
    }

    public void tunnels() {

        int newPacmanX = pacmanX + directionX;
        int newPacmanY = pacmanY + directionY;

//        if(newPacmanX == 0){
//            newPacmanX = 14;
//            System.out.println("0");
//        }
//              if(newPacmanX == 14){
//            newPacmanX = 0;
//            System.out.println("14");
//        }
        if (newPacmanX >= 0 && newPacmanX <= 15 && newPacmanY >= 0 && newPacmanY <= 15) { // Verifica si está dentro de los límites del mapa
            char nextCell = map[newPacmanY][newPacmanX];
            if (nextCell == 'T') {
                // Identifica la ubicación del túnel actual
                Point t1 = tunnels.get(0);
                Point t2 = tunnels.get(1);

                Point currentTunnelPoint = new Point(newPacmanX, newPacmanY);

                if (currentTunnelPoint.equals(t1)) {
                    // Está en el túnel 1, entonces salta al túnel 2
                    pacmanX = t2.x;
                    pacmanY = t2.y;
                } else if (currentTunnelPoint.equals(t2)) {
                    // Está en el túnel 2, entonces salta al túnel 1
                    pacmanX = t1.x;
                    pacmanY = t1.y;
                }

                ImageView cellImageView = (ImageView) algorithms.getNodeByRowColumnIndex(newPacmanY, newPacmanX, gridPaneMap);
                gridPaneMap.getChildren().remove(cellImageView);

                gridPaneMap.add(pacmanImageView, newPacmanX, newPacmanY);

            }
        }

    }

    private void movePacman() {
        frameCount++;

        if (frameCount >= frameDelay) {
            if (map[pacmanY][pacmanX] != 'B' && map[pacmanY][pacmanX] != 'C' && map[pacmanY][pacmanX] != 'T') {
                map[pacmanY][pacmanX] = ' '; // celda vacía
            }

            int newPacmanX = pacmanX + directionX;
            int newPacmanY = pacmanY + directionY;

            if (newPacmanX >= 0 && newPacmanX < 15 && newPacmanY >= 0 && newPacmanY < 15) { //rango del mapa
                char nextCell = map[newPacmanY][newPacmanX];
                if (nextCell != 'W') { // no es muro
                    //tunnel
                    tunnels();
                    if (nextCell == 'S') { //si es un punto
                        map[newPacmanY][newPacmanX] = 'P'; //define la nueva posicion de P
                        score += 10;
                        lbl_score.setText(String.valueOf(score));
                        if (algorithms.levelCompleted(map)) {
                            FlowController.getInstance().goViewInWindow("LevelComplete");
                            getStage().close();
                            FlowController.getInstance().deleteView("GameView");

                        }

                        ImageView cellImageView = (ImageView) algorithms.getNodeByRowColumnIndex(newPacmanY, newPacmanX, gridPaneMap);
                        gridPaneMap.getChildren().remove(cellImageView);

                        // Reemplazar la imagen de la celda por una celda vacía
                        ImageView emptyImageView = new ImageView();
                        emptyImageView.setFitWidth(15);
                        emptyImageView.setFitHeight(15);
                        gridPaneMap.add(emptyImageView, newPacmanX, newPacmanY);
                    } else if (nextCell == 'D') {

                        changeGhost();
                        isPoweredUp = true;

                        map[newPacmanY][newPacmanX] = 'P'; //define la nueva posicion de P
                        score += 10;
                        lbl_score.setText(String.valueOf(score));
                        if (algorithms.levelCompleted(map)) {
                            FlowController.getInstance().goViewInWindow("LevelComplete");
                            getStage().close();
                            FlowController.getInstance().deleteView("GameView");

                        }

                        ImageView cellImageView = (ImageView) algorithms.getNodeByRowColumnIndex(newPacmanY, newPacmanX, gridPaneMap);
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

                    if (checkGhostCollision(blinkyX, blinkyY)) {
                        if (isPoweredUp) {
                            shockBlinky = true;
                            blinkyImageView.setImage(gameMap.getImage("ojos"));
                        } else {

                            handleCollision();
                        }
                    }
                    if (checkGhostCollision(pinkyX, pinkyY)) {
                        if (isPoweredUp) {
                            shockPinky = true;
                            pinkyImageView.setImage(gameMap.getImage("ojos"));
                        } else {

                            handleCollision();
                        }
                    }
                    if (checkGhostCollision(inkyX, inkyY)) {
                        if (isPoweredUp) {
                            shockInky = true;
                            inkyImageView.setImage(gameMap.getImage("ojos"));
                        } else {
                            handleCollision();
                        }
                    }
                    if (checkGhostCollision(clydeX, clydeY)) {
                        if (isPoweredUp) {
                            shockClyde = true;
                            clydeImageView.setImage(gameMap.getImage("ojos"));
                        } else {
                            handleCollision();
                        }
                    }

                }
            }
            frameCount = 0;
        }
    }

    private void handleCollision() {
        if (lifes > 0) {
            lifes--;
            algorithms.kill(lifes, imgViewLife1, imgViewLife2, imgViewLife3, imgViewLife4, imgViewLife5, imgViewLife6);
            restartGhost();
            paman.pauseGame(pinkyTimeline, inkyTimeline, blinkyTimeline, clydeTimeline, pacManTimeline);
        }else{
            FlowController.getInstance().goViewInWindow("GameOverView");
            
        }

    }

    private boolean checkClydeCollision() {
        return clydeX == pacmanX && clydeY == pacmanY;
    }

    private boolean checkInkyCollision() {
        return inkyX == pacmanX && inkyY == pacmanY;
    }

    public void restartGhost() {

        //blinky
        blinkyX = blinkyXHouse;
        blinkyY = blinkyYHouse;
        //pinky
        pinkyX = pinkyXHouse;
        pinkyY = pinkyYHouse;
        //inky
        inkyX = inkyXHouse;
        inkyY = inkyYHouse;
        //clyde
        clydeX = clydeXHouse;
        clydeY = clydeYHouse;
        //pacman
        pacmanX = pacmanXHouse;
        pacmanY = pacmanYHouse;
        paman.restartGhost(gridPaneMap, blinkyImageView, blinkyXHouse, blinkyYHouse, pinkyImageView, pinkyXHouse, pinkyYHouse, inkyImageView, inkyXHouse, inkyYHouse, clydeImageView, clydeXHouse, clydeYHouse, pacmanImageView, pacmanXHouse, pacmanYHouse);

    }

    public void changeGhost() {
        blinkyImageView.setImage(gameMap.getImage("blue"));
        pinkyImageView.setImage(gameMap.getImage("blue"));
        clydeImageView.setImage(gameMap.getImage("blue"));
        inkyImageView.setImage(gameMap.getImage("blue"));

        Timeline timeline = new Timeline(new KeyFrame(Duration.seconds(10), event -> {
            initialGhost();
            isPoweredUp = false;
            System.out.println("Función ejecutada después de 10 segundos");
        }));

        timeline.play();
    }

    public void initialGhost() {
        blinkyImageView.setImage(gameMap.getBlinkyImage());
        inkyImageView.setImage(gameMap.getInkyImage());
        pinkyImageView.setImage(gameMap.getPinkyImage());
        clydeImageView.setImage(gameMap.getClydeImage());
    }

    private boolean checkGhostCollision(int ghostX, int ghostY) {
        return pacmanX == ghostX && pacmanY == ghostY;
    }

    @FXML
    private void onAction_encierro(ActionEvent event) {

        if (!encierro) {
           btn_encierro.setStyle("-fx-background-color: green;");
            Set<Integer> selectedIndices = new HashSet<>();
            Random random = new Random();
            int count = 0;

            while (count < 2) {
                System.out.println("-w-");
                int randomGhost = random.nextInt(4);
                System.out.println(randomGhost);

                if (selectedIndices.add(randomGhost)) {

                    switch (randomGhost) {
                        case 0:
                            blinkyEnc = true;
                            break;
                        case 1:
                            pinkyEnc = true;
                            break;
                        case 2:
                            clydeEnc = true;
                            break;
                        case 3:
                            inkyEnc = true;
                            break;
                    }
                    count++;
                }
            }
        }
    }

    @FXML
    private void onAction_velocidad(ActionEvent event) {
    }
}
