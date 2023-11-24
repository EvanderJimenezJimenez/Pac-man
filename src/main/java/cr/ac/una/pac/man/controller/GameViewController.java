package cr.ac.una.pac.man.controller;

import cr.ac.una.pac.man.Algorithms;
import cr.ac.una.pac.man.GameMap;
import cr.ac.una.pac.man.Level;
import cr.ac.una.pac.man.Pacman;
import cr.ac.una.pac.man.util.AppContext;
import cr.ac.una.pac.man.util.FlowController;
import java.awt.Point;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
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
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.GridPane;

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

    private int frameDelay = 2; // Controla la velocidad de movimiento
    private int frameDelayBlinky = 5;
    private int frameDelayPinky = 5;
    private int frameDelayClyde = 5;
    private int frameDelayInky = 5;

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

    int[][] matrizAdyacentePesos;

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
    int velocityF = 0;
    int encierroF = 0;

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
    private Button btn_encierro;
    @FXML
    private Label lblTime;
    private Timeline timeline;
    private int segundos = 0;
    private int minutos = 0;
    private int horas = 0;
    @FXML
    private ImageView imgTemaNivel;

    int lostLifes = 0;
    int deadGhost = 0;
    int scoreDead = 0;

    int ptsBlinky = 0;

    boolean consecutivo = false;
    boolean velocityHelp = false;
    boolean doubleponits = false;

    int consecutiveGhostCount = 0;
    @FXML
    private Button btn_encierroF;
    @FXML
    private Button btn_velocityF;
    @FXML
    private Label lbl_encierro_bloqueado;
    @FXML
    private Label lbl_velocidad_bloqueado;

    boolean usoEncierro = false;

    String dificultad = null;
    @FXML
    private Label lbl_dificultad;

    @Override
    public void initialize(URL url, ResourceBundle rb) {

        getDificultad();

        gameMap = new GameMap();
        gameMap.cargarImagenes();

        paman = new Pacman();

        algorithms = new Algorithms();

        this.nivel = (int) AppContext.getInstance().get("Level");
        AppContext.getInstance().set("LifeBuy", false);
        inicializarVidas();
        configurarManejoDeTeclado();
        iniciarAnimacionPacman();
        imgTemaNivel.setImage(imageLevel(String.valueOf(nivel)));
        cargarMapa(nivel);
        lbl_level.setText(String.valueOf(nivel));
        lbl_dificultad.setText(dificultad);

        startTime(lblTime);
    }

    private void startTime(Label label) {
        timeline = new Timeline(new KeyFrame(Duration.seconds(1), new EventHandler<ActionEvent>() {
            public void handle(ActionEvent event) {
                segundos++;

                label.setText(String.format("%02d:%02d:%02d", horas, minutos, segundos));
                if (segundos == 60) {
                    segundos = 0;
                    minutos++;

                    if (minutos == 60) {
                        minutos = 0;
                        horas++;
                    }
                }

                GameData gameData = new GameData();//instanceo la clase GameData  
                gameData.setHoras(horas);
                gameData.setMinutos(minutos);
                gameData.setSegundos(segundos);
                AppContext.getInstance().set("tiempo", gameData);

            }
        }));
        timeline.setCycleCount(Timeline.INDEFINITE);
        timeline.play();
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
                    ptsBlinky += 10;
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

        matrizAdyacentePesos = algorithms.matrizAdyacentePesos(map);

        floydMatriz = algorithms.floydWarshall(matrizAdyacentePesos);

        System.out.println("Puntos: " + ptsBlinky);

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
                        if (inkyEnc) {
                            Timeline timeline = new Timeline(new KeyFrame(Duration.seconds(10), event -> {
                                shockInky = false;
                                inkyEnc = false;
                                frameDelayInky = 4;
                            }));
                            timeline.play();
                        } else {
                            inkyImageView.setImage(gameMap.getInkyImage());
                            shockInky = false;
                            frameDelayInky = 4;
                        }

                    }
                }

                List<Integer> shortestPath = algorithms.dijisktraShortPath(startNode, targetNode, matrizAdyacentePesos);

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
                    if (clydeEnc) {
                        Timeline timeline = new Timeline(new KeyFrame(Duration.seconds(10), event -> {
                            shockClyde = false;
                            clydeEnc = false;
                            frameDelayClyde = 5;
                        }));
                        timeline.play();
                    } else {
                        clydeImageView.setImage(gameMap.getClydeImage());
                        shockClyde = false;
                        frameDelayClyde = 5;
                    }

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
                blinkyVelocity();
            } else {
                targetNode = blinkyYHouse * 15 + blinkyXHouse;
                frameDelayBlinky = 0;

                if (blinkyX == blinkyXHouse && blinkyY == blinkyYHouse) {

                    if (blinkyEnc) {
                        Timeline timeline = new Timeline(new KeyFrame(Duration.seconds(10), event -> {
                            System.out.println("GGs");
                            shockBlinky = false;
                            blinkyEnc = false;

                            blinkyVelocity();
                        }));
                        timeline.play();
                    } else {
                        blinkyImageView.setImage(gameMap.getBlinkyImage());
                        shockBlinky = false;
                    }

                }
            }
            List<Integer> shortestPath = new ArrayList<>();

            if ("facil".equals(dificultad)) {

                shortestPath = algorithms.longestPathDijkstra(startNode, targetNode, matrizAdyacentePesos);
            } else if ("dificil".equals(dificultad)) {
         
                shortestPath = algorithms.dijisktraShortPath(startNode, targetNode, matrizAdyacentePesos);
            }
            if (shortestPath.size() == 1 && !isPoweredUp && !blinkyEnc && !shockBlinky) {
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
                    if (pinkyEnc) {
                        Timeline timeline = new Timeline(new KeyFrame(Duration.seconds(10), event -> {
                            shockPinky = false;
                            pinkyEnc = false;
                            frameDelayPinky = 4;
                        }));
                        timeline.play();
                    } else {
                        pinkyImageView.setImage(gameMap.getPinkyImage());
                        shockPinky = false;
                        frameDelayPinky = 4;
                    }

                }
            }
            List<Integer> shortestPath = new ArrayList<>();

            if ("facil".equals(dificultad)) {

                shortestPath = algorithms.longestPathDijkstra(startNode, targetNode, matrizAdyacentePesos);
            } else if ("dificil".equals(dificultad)) {

                shortestPath = algorithms.dijisktraShortPath(startNode, targetNode, matrizAdyacentePesos);
            }

            //  System.out.println("Corta: "+ shortestPath2.size());
            // System.out.println("Larga: " + shortestPath.size());
            if (shortestPath.size() == 1 && !isPoweredUp && !pinkyEnc && !shockPinky) {
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

//                defineTunel();
                ImageView cellImageView = (ImageView) algorithms.getNodeByRowColumnIndex(newPacmanY, newPacmanX, gridPaneMap);
                gridPaneMap.getChildren().remove(cellImageView);

                gridPaneMap.add(pacmanImageView, newPacmanX, newPacmanY);

            }
        }

    }

    private void movePacman() {

        // velocityActiva();
        // encierroActivo();
        encierroActivo();
//        defineTunel();
        frameCount++;

        if (frameCount >= frameDelay) {
            if (map[pacmanY][pacmanX] != 'B' && map[pacmanY][pacmanX] != 'C' && map[pacmanY][pacmanX] != 'T' && map[pacmanY][pacmanX] != 'S'
                    && map[pacmanY][pacmanX] != 'D') {
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
                        if (doubleponits) {
                            score = score + (2 * 10);
                        } else {
                            score += 10;
                        }

                        lbl_score.setText(String.valueOf(score));
                        if (algorithms.levelCompleted(map)) {
                            completeLevel();
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
                        if (doubleponits) {
                            score = score + (2 * 10);
                        } else {
                            score += 10;
                        }
                        lbl_score.setText(String.valueOf(score));
                        if (algorithms.levelCompleted(map)) {
                            completeLevel();
                        }
                        ImageView cellImageView = (ImageView) algorithms.getNodeByRowColumnIndex(newPacmanY, newPacmanX, gridPaneMap);
                        gridPaneMap.getChildren().remove(cellImageView);

                        // Reemplazar la imagen de la celda por una celda vacía
                        ImageView emptyImageView = new ImageView();
                        emptyImageView.setFitWidth(15);
                        emptyImageView.setFitHeight(15);
                        gridPaneMap.add(emptyImageView, newPacmanX, newPacmanY);

                    } else if (nextCell != 'T') {
                        map[newPacmanY][newPacmanX] = 'P';
                    }

                    gridPaneMap.getChildren().remove(pacmanImageView);
                    pacmanX = newPacmanX;
                    pacmanY = newPacmanY;
                    pacmanImageView.setFitHeight(15);
                    pacmanImageView.setFitWidth(15);
                    gridPaneMap.add(pacmanImageView, pacmanX, pacmanY);

                    houseGosht();

                }
            }
            frameCount = 0;
        }
    }

    private void handleCollision() {

        boolean lifesBuy = false;

        lifesBuy = (boolean) AppContext.getInstance().get("LifeBuy");

        if (lifesBuy) {
            lifes = 0;
            lifesBuy = false;

        }

        if (lifes == 6) {
            scoreDead = score;
            System.out.println("Puntos sin morir: " + scoreDead);
        }

        if (lifes > 0) {
            AppContext.getInstance().set("LifeBuy", false);
            lifes--;
            if (lifes == 1) {
                lifes = 0;
            }
            lostLifes++;

            algorithms.kill(lifes, imgViewLife1, imgViewLife2, imgViewLife3, imgViewLife4, imgViewLife5, imgViewLife6);
            restartGhost();
            paman.pauseGame(pinkyTimeline, inkyTimeline, blinkyTimeline, clydeTimeline, pacManTimeline);
        } else {
            appContext();
            FlowController.getInstance().goViewInWindow("GameOverView");
            restartGhost();
            paman.pauseGame(pinkyTimeline, inkyTimeline, blinkyTimeline, clydeTimeline, pacManTimeline);
        }

    }

//    public void defineTunel() {
//        map[tunnels.get(0).x][tunnels.get(0).y] = 'T';
//        map[tunnels.get(1).x][tunnels.get(1).y] = 'T';
//    }
    public void completeLevel() {

        int lev = Integer.parseInt(lbl_level.getText());
        AppContext.getInstance().set("Level", lev);
        System.out.println("Nivel de juego: " + lev);

        String time = lblTime.getText();
        if (lifes == 6) {
            scoreDead = score;
            System.out.println("Puntos sin morir: " + scoreDead);
        }

        score += (lev * 100);

        AppContext.getInstance().set("GameTime", time);
        AppContext.getInstance().set("GameScore", score);
        AppContext.getInstance().set("GameLife", lifes);
        AppContext.getInstance().set("GameLostLifes", lostLifes);
        AppContext.getInstance().set("GameDeadGhost", deadGhost);
        AppContext.getInstance().set("GameScoreDead", scoreDead);

        AppContext.getInstance().set("velocity", velocityF);
        AppContext.getInstance().set("encierro", encierroF);

        System.out.println("Dead: " + scoreDead);
        paman.pauseGame(pinkyTimeline, inkyTimeline, blinkyTimeline, clydeTimeline, pacManTimeline);
        FlowController.getInstance().goViewInWindow("LevelComplete");
        getStage().close();
        FlowController.getInstance().deleteView("GameView");

    }

    public void appContext() {
        int lev = Integer.parseInt(lbl_level.getText());
        AppContext.getInstance().set("Level", lev);
        System.out.println("Nivel de juego: " + lev);

        String time = lblTime.getText();
        if (lifes == 6) {
            scoreDead = score;
            System.out.println("Puntos sin morir: " + scoreDead);
        }

        score += (lev * 100);

        AppContext.getInstance().set("GameTime", time);
        AppContext.getInstance().set("GameScore", score);
        AppContext.getInstance().set("GameLife", lifes);
        AppContext.getInstance().set("GameLostLifes", lostLifes);
        AppContext.getInstance().set("GameDeadGhost", deadGhost);
        AppContext.getInstance().set("GameScoreDead", scoreDead);

        AppContext.getInstance().set("velocity", velocityF);
        AppContext.getInstance().set("encierro", encierroF);

    }

    private boolean checkClydeCollision() {
        return clydeX == pacmanX && clydeY == pacmanY;
    }

    private boolean checkInkyCollision() {
        return inkyX == pacmanX && inkyY == pacmanY;
    }

    public void houseGosht() {

        if (checkGhostCollision(blinkyX, blinkyY)) {

            if (isPoweredUp) {
                deadGhost++;

                if (!shockBlinky) {

                    if (consecutivo) {
                        score += 100;
                        handleConsecutiveGhostEating();
                    }
                    score += 300;
                    lbl_score.setText(String.valueOf(score));
                }

                shockBlinky = true;
                blinkyImageView.setImage(gameMap.getImage("ojos"));
                activarConsecutivo();

            } else {
                handleCollision();
            }
        }
        if (checkGhostCollision(pinkyX, pinkyY)) {
            if (isPoweredUp) {

                if (!shockPinky) {

                    if (consecutivo) {
                        score += 100;
                        handleConsecutiveGhostEating();
                    }
                    score += 300;
                    lbl_score.setText(String.valueOf(score));

                }
                deadGhost++;
                shockPinky = true;
                pinkyImageView.setImage(gameMap.getImage("ojos"));
                activarConsecutivo();
            } else {

                handleCollision();
            }
        }
        if (checkGhostCollision(inkyX, inkyY)) {
            if (isPoweredUp) {
                deadGhost++;
                if (!shockInky) {

                    if (consecutivo) {
                        score += 100;
                        handleConsecutiveGhostEating();
                    }
                    score += 300;
                    lbl_score.setText(String.valueOf(score));

                }
                shockInky = true;
                inkyImageView.setImage(gameMap.getImage("ojos"));
                activarConsecutivo();

            } else {
                handleCollision();
            }
        }
        if (checkGhostCollision(clydeX, clydeY)) {
            if (isPoweredUp) {
                if (!shockClyde) {

                    if (consecutivo) {
                        score += 100;
                        handleConsecutiveGhostEating();
                    }
                    score += 300;
                    lbl_score.setText(String.valueOf(score));

                }
                deadGhost++;
                shockClyde = true;
                clydeImageView.setImage(gameMap.getImage("ojos"));
                activarConsecutivo();
            } else {
                handleCollision();
            }
        }
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

    private void encierroActivo() {

        if (score >= ptsBlinky / 2 && !usoEncierro && lifes == 6) {
            lbl_encierro_bloqueado.setText("Disponile");
            encierro = true;

            //return encierro;
        } else {
            lbl_encierro_bloqueado.setText("Bloqueado");
            encierro = false;
            //return encierro;
        }
    }

    @FXML
    private void onAction_encierro(ActionEvent event) {

        //
        if (encierro && usoEncierro == false) {
            System.out.println("Encierro entro");
            encierroF++;
            usoEncierro = true;
            Set<Integer> selectedIndices = new HashSet<>();
            Random random = new Random();
            int count = 0;

            while (count < 2) {
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

    public Image imageLevel(String levelNumber) {
        URL imageURL = null;
        switch (levelNumber) {
            case "1":
                imageURL = getClass().getResource("/cr/ac/una/pac/man/resources/jokerr.gif");
                break;
            case "2":
                imageURL = getClass().getResource("/cr/ac/una/pac/man/resources/simpsons.gif");
                break;
            case "3":
                imageURL = getClass().getResource("/cr/ac/una/pac/man/resources/ironman.gif");
                break;
            case "4":
                imageURL = getClass().getResource("/cr/ac/una/pac/man/resources/spiderman.gif");
                break;
            case "5":
                imageURL = getClass().getResource("/cr/ac/una/pac/man/resources/batman.gif");
                break;
            case "6":
                imageURL = getClass().getResource("/cr/ac/una/pac/man/resources/superman.gif");
                break;
            case "7":
                imageURL = getClass().getResource("/cr/ac/una/pac/man/resources/deadPool.gif");
                break;
            case "8":
                imageURL = getClass().getResource("/cr/ac/una/pac/man/resources/onePiece.gif");
                break;
            case "9":
                imageURL = getClass().getResource("/cr/ac/una/pac/man/resources/naruto.gif");
                break;
            case "10":
                imageURL = getClass().getResource("/cr/ac/una/pac/man/resources/dragonBall.gif");
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

    public void blinkyVelocity() {
        if (score > (ptsBlinky / 2) + 200) {
            frameDelayBlinky = 3;

        } else {
            frameCountBlinky = 5;
        }
    }

    @FXML
    private void onAction_velocity(ActionEvent event) {

        if (velocityHelp) {
            velocityActive();
            velocityF++;
            System.out.println("PASO VELO");
            velocityHelp = false;
        }

    }

    public void velocityActive() {

        frameDelay = 0;
        doubleponits = true;
        System.out.println("Velo: " + frameDelay);
        lbl_velocidad_bloqueado.setText("Bloqueado");
        Timeline timeline = new Timeline(new KeyFrame(Duration.seconds(6), event -> {

            frameDelay = 2;
            doubleponits = false;

        }));
        timeline.play();
    }

    public void handleConsecutiveGhostEating() {
        consecutiveGhostCount++;

        if (consecutiveGhostCount == 2) {
            // Activa la habilidad especial al comer 2 fantasmas consecutivamente
            velocityHelp = true;
            lbl_velocidad_bloqueado.setText("Disponible");
            //  btn_encierro.setVisible(true);
            // Reinicia el contador de fantasmas consecutivos
            consecutiveGhostCount = 0;
        }
    }

    public void activarConsecutivo() {
        consecutivo = true;

        // Inicia el temporizador para desactivar el modo consecutivo después de 2 segundos
        Timeline timeline = new Timeline(new KeyFrame(Duration.seconds(5), event -> {
            consecutivo = false;
        }));
        timeline.play();
    }

    @FXML
    private void onAction_nextLevel(ActionEvent event) {
        completeLevel();
    }

    @FXML
    private void onAction_lifes(ActionEvent event) {
        lifes = 2;
    }

    public void getDificultad() {
        dificultad = null;

        try (BufferedReader br = new BufferedReader(new FileReader(".\\src\\main\\resources\\cr\\ac\\una\\pac\\man\\files\\dificultad.txt"))) {
            String line;
            while ((line = br.readLine()) != null) {
                // Buscar la posición de "***"
                int index = line.indexOf("***");
                if (index != -1) {

                    dificultad = line.substring(0, index).trim();
                    break;
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
