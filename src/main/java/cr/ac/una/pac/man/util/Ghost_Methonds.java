package cr.ac.una.pac.man.util;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;

/**
 * @author evand
 */
public class Ghost_Methonds {

    public void MoveNormalGhost(char[][] map) {
        int pac_Y = 0;
        int pac_X = 0;
        int YelllowGhost_Y = 0;
        int YellowGhost_X = 0;

    }

    public int[] WhereIsAElement(char[][] map, char element) {
        int[] positions = new int[2];

        Character.toUpperCase(element);

        for (int y = 0; y < map.length; y++) {
            for (int x = 0; x < map.length; x++) {
                if (map[y][x] == element) {
                    positions[0] = y;
                    positions[1] = x;
                }
            }
        }
        return positions;
    }

    //Move a element in the matrix with a order up, dowm, right, left and with a letter of element
    public void MoveElement(char element, String where, char[][] map) {
        where.toUpperCase();
        Character.toUpperCase(element);
        int positions[] = null;

        switch (where) {

            case "UP":
                positions = WhereIsAElement(map, element);
                //Determine if there is space in front of him
                if (map[positions[0] + 1][positions[1]] != 'W') {

                    map[positions[0] + 1][positions[1]] = element;
                    map[positions[0]][positions[1]] = ' ';
                    //Re-acinamos valores de la position que cambiamos
                    positions[0] = positions[0] + 1;
                } else {
                    System.out.println("Error al mover el elemento por falta de espacio");
                }
                break;
            case "DOWM":
                positions = WhereIsAElement(map, element);
                if (map[positions[0] - 1][positions[1]] != 'W') {

                    map[positions[0] - 1][positions[1]] = element;
                    map[positions[0]][positions[1]] = ' ';
                    positions[0] = positions[0] - 1;
                } else {
                    System.out.println("Error al mover el elemento por falta de espacio");
                }
                break;
            case "RIGHT":
                positions = WhereIsAElement(map, element);
                if (map[positions[0]][positions[1] + 1] != 'W') {

                    map[positions[0]][positions[1] + 1] = element;
                    map[positions[0]][positions[1]] = ' ';
                    positions[1] = positions[1] + 1;

                } else {
                    System.out.println("Error al mover el elemento por falta de espacio");
                }

                break;
            case "LEFT":
                positions = WhereIsAElement(map, element);
                if (map[positions[0]][positions[1] - 1] != 'W') {
                    map[positions[0]][positions[1] - 1] = element;
                    map[positions[0]][positions[1]] = ' ';
                    positions[1] = positions[1] - 1;
                } else {
                    System.out.println("Error al mover el elemento por falta de espacio");
                }
                break;

            default:
                throw new AssertionError();
        }

    }

    public void NormalGhostRoute(char[][] map) {
        int[] pacManPosition = WhereIsAElement(map, 'P');
        int[] ghostYellowPosition = WhereIsAElement(map, 'C');

        // Create a queue for BFS
        Queue<int[]> queue = new LinkedList<>();
        queue.add(pacManPosition);

        // Create a visited array to keep track of visited positions
        boolean[][] visited = new boolean[map.length][map[0].length];
        visited[pacManPosition[1]][pacManPosition[0]] = true;

        while (!queue.isEmpty()) {
            int[] currentPosition = queue.poll();

            if (currentPosition[0] == ghostYellowPosition[0] && currentPosition[1] == ghostYellowPosition[1]) {
                // Found the yellow ghost, exit the loop
                break;
            }

            // Find valid moves for the ghost at the current position
            List<int[]> validMoves = findValidMoves(currentPosition[0], currentPosition[1], map);

            for (int[] move : validMoves) {
                int newCol = move[0];
                int newRow = move[1];

                if (!visited[newRow][newCol]) {
                    queue.add(new int[]{newCol, newRow});
                    visited[newRow][newCol] = true;
                    MoveElement('C', getDirection(new int[]{newCol - currentPosition[0], newRow - currentPosition[1]}), map);
                }
            }
        }
    }

// Helper method to convert move to a direction string
    private String getDirection(int[] move) {
        if (move[0] == -1) {
            return "UP";
        }
        if (move[0] == 1) {
            return "DOWM";
        }
        if (move[1] == 1) {
            return "RIGHT";
        }
        if (move[1] == -1) {
            return "LEFT";
        }
        return "UP"; // Default to UP
    }

    private List<int[]> findValidMoves(int x, int y, char[][] map) {
        int[][] moves = {{-1, 0}, {1, 0}, {0, 1}, {0, -1}};
        List<int[]> validMoves = new ArrayList<>();

        for (int[] move : moves) {
            int newRow = y + move[0];
            int newCol = x + move[1];

            if (newRow >= 0 && newRow < map.length && newCol >= 0 && newCol < map[0].length
                    && map[newRow][newCol] != 'W') {
                validMoves.add(new int[]{newCol, newRow});
            }
        }

        return validMoves;
    }

}
