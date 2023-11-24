package cr.ac.una.pac.man;

import java.awt.Point;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashSet;
import java.util.List;
import java.util.PriorityQueue;
import java.util.Set;
import javafx.collections.ObservableList;
import javafx.scene.Node;
import javafx.scene.image.ImageView;
import javafx.scene.layout.GridPane;

/**
 *
 * @author dario
 */
public class Algorithms {

    public Algorithms() {
    }

    public boolean levelCompleted(char map[][]) {
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
        // verMatriz();
        return isComplete;
    }

    //matriz adyacencia 
    public int[][] matrizAdyacentePesos2(char[][] map) {
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
                        graph[y * mapSize + x][(y + 1) * mapSize + x] = 2;
                        graph[(y + 1) * mapSize + x][y * mapSize + x] = 2;
                    }
                    if (x > 0 && map[y][x - 1] != 'W') {
                        graph[y * mapSize + x][y * mapSize + (x - 1)] = 3;
                        graph[y * mapSize + (x - 1)][y * mapSize + x] = 3;
                    }
                    if (x < mapSize - 1 && map[y][x + 1] != 'W') {
                        graph[y * mapSize + x][y * mapSize + (x + 1)] = 4;
                        graph[y * mapSize + (x + 1)][y * mapSize + x] = 4;
                    }
                }
            }
        }

        return graph;
    }

    public int[][] matrizAdyacentePesos(char[][] map) {
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
                    // Asignar un peso de 1 a todas las aristas no bloqueadas
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

    public List<Integer> longestPathDijkstra(int start, int target, int[][] matrizAdyacentePesos) {
        int numNodes = matrizAdyacentePesos.length;
        int[] distance = new int[numNodes];
        Arrays.fill(distance, Integer.MIN_VALUE); // Inicializar con valor negativo grande

        PriorityQueue<Integer> queue = new PriorityQueue<>(numNodes, Comparator.comparingInt(node -> -distance[node])); // Invertir el orden de la cola de prioridad
        queue.add(start);
        distance[start] = 0;

        int[] previous = new int[numNodes];
        Arrays.fill(previous, -1);

        while (!queue.isEmpty()) {
            System.out.println("Queue size: " + queue.size());
            int currentNode = queue.poll();
            System.out.println("Processing node: " + currentNode);

            if (currentNode == target) {
                System.out.println("Target reached!");
                break;
            }

            for (int neighbor = 0; neighbor < numNodes; neighbor++) {
                int weight = matrizAdyacentePesos[currentNode][neighbor];
                if (weight != Integer.MAX_VALUE) {
                    int altDistance = distance[currentNode] + weight;
                    if (altDistance < distance[neighbor]) {
                        distance[neighbor] = altDistance;
                        previous[neighbor] = currentNode;
                        queue.add(neighbor);
                        System.out.println("Updated distance to node " + neighbor + ": " + altDistance);
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

        return dist;
    }

    // Función para obtener una ImageView en una fila y columna específicas
    public Node getNodeByRowColumnIndex(final int row, final int column, GridPane gridPaneMap) {
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

    public Point selectRandomPoint(List<Point> smallPoints) {

        int randomIndex = (int) (Math.random() * smallPoints.size());

        return smallPoints.get(randomIndex);
    }

    public Point selectRandomPointInky(List<Point> smallPoints, int pacmanX, int pacmanY, int blinkyX, int blinkyY, int clydeX, int clydeY, int pinkyX, int pinkyY) {
        int randomChoice = (int) (Math.random() * 5);
        Point randomPoint;

        if (randomChoice == 0) {
            int randomIndex = (int) (Math.random() * smallPoints.size());
            randomPoint = smallPoints.get(randomIndex);
        } else if (randomChoice == 1) {

            randomPoint = new Point(pacmanX, pacmanY);
        } else if (randomChoice == 2) {

            randomPoint = new Point(blinkyX, blinkyY);
        } else if (randomChoice == 3) {

            randomPoint = new Point(clydeX, clydeY);
        } else {
            randomPoint = new Point(pinkyX, pinkyY);
        }

        return randomPoint;
    }

    //
    public void kill(int lifes, ImageView imgViewLife1, ImageView imgViewLife2,
            ImageView imgViewLife3, ImageView imgViewLife4, ImageView imgViewLife5, ImageView imgViewLife6) {

        switch (lifes) {

            case 0:
                imgViewLife1.setImage(null);
                break;
            case 1:
                imgViewLife2.setImage(null);
                break;
            case 2:
                imgViewLife3.setImage(null);
                break;
            case 3:
                imgViewLife4.setImage(null);
                break;
            case 4:
                imgViewLife5.setImage(null);
                break;
            case 5:
                imgViewLife6.setImage(null);
                break;

            default:
                throw new AssertionError();
        }

    }

    public List<Integer> dijisktraShortPath(int start, int target, int[][] matrizAdyacentePesos) { //dijsktra
        int numNodes = matrizAdyacentePesos.length;
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
                int weight = matrizAdyacentePesos[currentNode][neighbor];
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

}
