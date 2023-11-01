package cr.ac.una.pac.man.controller;

import cr.ac.una.pac.man.util.AppContext;
import cr.ac.una.pac.man.util.FlowController;
import java.awt.Point;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.PriorityQueue;
import java.util.ResourceBundle;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
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

    //pacman
    private Image pacmanRight;
    private Image pacmanFeft;
    private Image pacmanUp;
    private Image pacmanDown;

    //Ghost
    private Image blinkyImage;
    private Image clydeImage;
    private Image pinkyImage;
    private Image inkyImage;

    private ImageView pacmanImageView; //pacman en el mapa

    private ImageView blinkyImageView; //pacman en el mapa

    private ImageView pinkyImageView; //pacman en el mapa

    private ImageView inkyImageView; //pacman en el mapa

    private ImageView clydeImageView; //pacman en el mapa

    //vidas
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
    private int frameCountBlinky = 0;
    private int frameCountPinky = 0;
    private int frameCountClyde = 0;

    private int frameDelay = 1; // Controla la velocidad de movimiento, ajusta según tus necesidades
    private int frameDelayBlinky = 3;
    private int frameDelayPinky = 4;
    private int frameDelayClyde = 5;

    private int pacmanSpeed = 1; // Velocidad predeterminada
    private int blinkySpeed = 0;

    private int blinkyX;
    private int blinkyY;
    private int blinkyDirectionX;
    private int blinkyDirectionY;

    private int pinkyX;
    private int pinkyY;
    private int pinkyDirectionX;
    private int pinkyDirectionY;

    private int inkyX;
    private int inkyY;
    private int inkyDirectionX;
    private int inkyDirectionY;

    private int clydeX;
    private int clydeY;
    private int clydeDirectionX;
    private int clydeDirectionY;

    private Timeline blinkyTimeline;

    private Timeline pinkyTimeline;
    private Timeline clydeTimeline;

    int[][] weightedGraph;

    int[][] floydMatriz;
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
        inkyImage = getIamge("inky");
        pinkyImage = getIamge("pinky");

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
                pacManTimeline.play();
                 blinkyTimeline.play();
                pinkyTimeline.play();
                clydeTimeline.play();

            }
        });
    }

        //System.out.println("p:" + smallPoints);
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
       // pacManTimeline.play();
    }

    @Override
    public void initialize() {
        //blinkyTimeline.play();
        
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

                if (cell == 'W') {
                    imageView.setImage(wallImage);
                } else if (cell == 'S') {
                    imageView.setImage(smallPointImage);
                    smallPoints.add(new Point(x, y));
                } else if (cell == 'B') {
                    blinkyImageView = new ImageView(blinkyImage);
                    blinkyImageView.setFitHeight(imageSize);
                    blinkyImageView.setFitWidth(imageSize);
                    gridPaneMap.add(blinkyImageView, x, y);
                    blinkyX = x; // Coordenada X inicial de Blinky
                    blinkyY = y;

                } else if (cell == 'I') {
                    inkyImageView = new ImageView(inkyImage);
                    inkyImageView.setFitHeight(imageSize);
                    inkyImageView.setFitWidth(imageSize);
                    gridPaneMap.add(inkyImageView, x, y);
                    inkyX = x; // Coordenada X inicial de inky
                    inkyY = y;

                } else if (cell == 'R') {
                    pinkyImageView = new ImageView(pinkyImage);
                    pinkyImageView.setFitHeight(imageSize);
                    pinkyImageView.setFitWidth(imageSize);
                    gridPaneMap.add(pinkyImageView, x, y);
                    pinkyX = x; // Coordenada X inicial de inky
                    pinkyY = y;

                } else if (cell == 'D') {
                    imageView.setImage(bigPointImage);
                } else if (cell == 'C') {

                    clydeImageView = new ImageView(clydeImage);
                    clydeImageView.setFitHeight(imageSize);
                    clydeImageView.setFitWidth(imageSize);
                    gridPaneMap.add(clydeImageView, x, y);
                    clydeX = x; // Coordenada X inicial de Blinky
                    clydeY = y;

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

         weightedGraph = createWeightedGraph(map);

        floydMatriz = floydWarshall(weightedGraph);

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
            case "inky":
                imageURL = getClass().getResource("/cr/ac/una/pac/man/resources/Inky.gif");
                break;
            case "pinky":
                imageURL = getClass().getResource("/cr/ac/una/pac/man/resources/Pinky.gif");
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

    private Point currentRandomPoint; // Punto aleatorio actual
    private boolean isMovingToRandomPoint = false;

    private Point selectRandomPoint() {

        int randomIndex = (int) (Math.random() * smallPoints.size());
        System.out.println("Randon: " + randomIndex);
        return smallPoints.get(randomIndex);
    }

    private void clydeMove() {
        frameCountClyde++;

        if (frameCountClyde >= frameDelayClyde) {
            if (!isMovingToRandomPoint) {
                currentRandomPoint = selectRandomPoint();
                isMovingToRandomPoint = true;
            }

            int startNode = clydeY * 15 + clydeX;
            int randomX = currentRandomPoint.x;
            int randomY = currentRandomPoint.y;
            int targetNode = randomY * 15 + randomX;

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
            }

            frameCountClyde = 0;
        }
    }

    private void blinkyMove() {
        frameCountBlinky++;

        if (frameCountBlinky >= frameDelayBlinky) {
            int startNode = blinkyY * 15 + blinkyX; 
            int targetNode = pacmanY * 15 + pacmanX;
            List<Integer> shortestPath = shortestPath(startNode, targetNode, weightedGraph);

            if (shortestPath != null && shortestPath.size() > 1) {
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

              
                if (map[anticipatedPacmanY][anticipatedPacmanX] == 'W') {
                   
                    anticipatedPacmanX -= directionX;
                    anticipatedPacmanY -= directionY;
                    break;
                }
            }

            int startNode = pinkyY * 15 + pinkyX;
            int targetNode = anticipatedPacmanY * 15 + anticipatedPacmanX;
            List<Integer> shortestPath = shortestPath(startNode, targetNode, weightedGraph);

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

    private void movePacman() {
        frameCount++;

        if (frameCount >= frameDelay) {
            if (map[pacmanY][pacmanX] != 'B' && map[pacmanY][pacmanX] != 'C') {
                map[pacmanY][pacmanX] = ' '; // celda vacía
            }

            int newPacmanX = pacmanX + directionX;
            int newPacmanY = pacmanY + directionY;

            if (newPacmanX >= 0 && newPacmanX < 15 && newPacmanY >= 0 && newPacmanY < 15) { //rango del mapa
                char nextCell = map[newPacmanY][newPacmanX];
                if (nextCell != 'W') { // no es muro
                    if (nextCell == 'S' || nextCell == 'D') { //si es un punto
                        map[newPacmanY][newPacmanX] = 'P'; //define la nueva posicion de P
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
        // verMatriz();
        return isComplete;
    }

    //matriz adyacencia 
    private int[][] createWeightedGraph(char[][] map) {
        int mapSize = map.length;
        int[][] graph = new int[mapSize * mapSize][mapSize * mapSize];

        for (int i = 0; i < mapSize * mapSize; i++) {
            for (int j = 0; j < mapSize * mapSize; j++) {
                graph[i][j] = (i == j) ? 0 : Integer.MAX_VALUE; // Inicializar con valor infinito
            }
        }

        for (int y = 0; y < mapSize; y++) {
            for (int x = 0; x < mapSize; x++) {
                if (map[y][x] != 'W') {
                    // Comprueba los vecinos arriba, abajo, izquierda y derecha
                    if (y > 0 && map[y - 1][x] != 'W') {
                        graph[y * mapSize + x][(y - 1) * mapSize + x] = 1;
                        graph[(y - 1) * mapSize + x][y * mapSize + x] = 1;
                    }
                    if (y < mapSize - 1 && map[y + 1][x] != 'W') {
                        graph[y * mapSize + x][(y + 1) * mapSize + x] = 1;
                        graph[(y + 1) * mapSize + x][y * mapSize + x] = 1;
                    }
                    if (x > 0 && map[y][x - 1] != 'W') {
                        graph[y * mapSize + x][y * mapSize + (x - 1)] = 1;
                        graph[y * mapSize + (x - 1)][y * mapSize + x] = 1;
                    }
                    if (x < mapSize - 1 && map[y][x + 1] != 'W') {
                        graph[y * mapSize + x][y * mapSize + (x + 1)] = 1;
                        graph[y * mapSize + (x + 1)][y * mapSize + x] = 1;
                    }
                }
            }
        }

        return graph;
    }

    private List<Integer> shortestPath(int start, int target, int[][] weightedGraph) { //dijsktra
        int numNodes = weightedGraph.length;
        int[] distance = new int[numNodes];
        Arrays.fill(distance, Integer.MAX_VALUE);

        PriorityQueue<Integer> queue = new PriorityQueue<>(numNodes, Comparator.comparingInt(node -> distance[node]));
        queue.add(start);
        distance[start] = 0;

        int[] previous = new int[numNodes];
        Arrays.fill(previous, -1);

        while (!queue.isEmpty()) {
            int currentNode = queue.poll();
            if (currentNode == target) {
                break;
            }

            for (int neighbor = 0; neighbor < numNodes; neighbor++) {
                int weight = weightedGraph[currentNode][neighbor];
                if (weight != Integer.MAX_VALUE) {
                    int altDistance = distance[currentNode] + weight;
                    if (altDistance < distance[neighbor]) {
                        distance[neighbor] = altDistance;
                        previous[neighbor] = currentNode;
                        queue.add(neighbor);
                    }
                }
            }
        }

        List<Integer> path = new ArrayList<>();
        int current = target;
        while (current != -1) {
            path.add(0, current);
            current = previous[current];
        }

        return path;
    }

    private List<Integer> longestPath(int start, int target, int[][] weightedGraph) {
       

     
        List<Integer> longestPath = new ArrayList<>();
        
        return longestPath;
    }

    public int[][] floydWarshall(int[][] graph) {
        int V = graph.length;
        int[][] dist = new int[V][V];

        for (int i = 0; i < V; i++) {
            for (int j = 0; j < V; j++) {
                dist[i][j] = graph[i][j];
            }
        }

        for (int k = 0; k < V; k++) {
            for (int i = 0; i < V; i++) {
                for (int j = 0; j < V; j++) {
                    if (dist[i][k] != Integer.MAX_VALUE && dist[k][j] != Integer.MAX_VALUE
                            && dist[i][k] + dist[k][j] < dist[i][j]) {
                        dist[i][j] = dist[i][k] + dist[k][j];
                    }
                }
            }
        }

//        System.out.println("Matriz floyd");
//
//        for (int i = 0; i < V; i++) {
//            for (int j = 0; j < V; j++) {
//                System.out.print(dist[i][j] + " ");
//            }
//            System.out.println();
//        }
        return dist;
    }

    public void verMatriz() {

        for (int i = 0; i < 15; i++) {
            for (int j = 0; j < 15; j++) {
                System.out.print(weightedGraph[i][j] + " ");
            }
            System.out.println();

        }
    }
}
