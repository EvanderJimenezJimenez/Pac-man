package cr.ac.una.pac.man;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.HashSet;
import java.util.Random;
import java.util.Set;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

/**
 *
 * @author dario
 */
public class GeneratedMap {

    ObservableList<char[][]> mapList = FXCollections.observableArrayList();
    Set<char[][]> mapSet = new HashSet<>();

    public void mapGenerated() {

        char map[][] = new char[15][15];

        for (int i = 0; i < 15; i++) {
            for (int j = 0; j < 15; j++) {
                map[i][j] = 'W';
            }
        }

        handleRoutes(map);
        initialsPositions(map);
        tunnels(map, 0, 14);
        powerPellets(map, 3);
        printMap(map);
        saveMaps();

    }

    private void initialsPositions(char[][] map) {

        for (int i = 5; i < 10; i++) {
            map[i][4] = 'N';
            map[i][10] = 'N';
        }
        for (int j = 4; j < 10; j++) {
            map[5][j] = 'N';
            map[9][j] = 'N';
        }

        for (int i = 6; i < 9; i++) {
            map[i][5] = 'W';
            map[i][9] = 'W';
        }
        for (int j = 6; j < 9; j++) {
            map[6][j] = 'W';
            map[8][j] = 'W';
        }

        //personajes
        map[6][7] = 'B';
        map[7][7] = 'R';
        map[7][6] = 'C';
        map[7][8] = 'I';
        map[9][7] = 'P';
    }

    //base de mapa
    /*
    W W W W W W W W W W W W W W W 
    W W W W W W W W W W W W W W W 
    W W W W W W W W W W W W W W W 
    W W W W W W W W W W W W W W W 
    W W W W W W W W W W W W W W W 
    W W W W N N N N N N N W W W W 
    W W W W N W W B W W N W W W W 
    W W W W N W C R I W N W W W W 
    W W W W N W W W W W N W W W W 
    W W W W N N N P N N N W W W W 
    W W W W W W W W W W W W W W W 
    W W W W W W W W W W W W W W W 
    W W W W W W W W W W W W W W W 
    W W W W W W W W W W W W W W W 
    W W W W W W W W W W W W W W W
     */
    //casa de fantasmas
    /*   
         N N N N N N N  
         N W W B W W N  
         N W C R I W N  
         N W W W W W N  
         N N N P N N N 
       
     */
    public void handleRoutes(char[][] map) {

        for (int i = 0; i < map.length; i++) {
            for (int j = 0; j < map[i].length; j++) {
                map[i][j] = 'W';
            }
        }

        generateMaze(map, 1, 1);

    }

    private void generateMaze(char[][] map, int x, int y) {
        map[x][y] = 'S'; // Marcar la posición actual como parte del camino

        // Direcciones posibles (arriba, derecha, abajo, izquierda)
        int[] dx = {0, 1, 0, -1};
        int[] dy = {-1, 0, 1, 0};

        // Mezclar aleatoriamente las direcciones
        Random random = new Random();
        for (int i = 0; i < 4; i++) {
            int randomIndex = random.nextInt(4);
            int temp = dx[i];
            dx[i] = dx[randomIndex];
            dx[randomIndex] = temp;

            temp = dy[i];
            dy[i] = dy[randomIndex];
            dy[randomIndex] = temp;
        }

        // Recorrer las direcciones
        for (int i = 0; i < 4; i++) {
            int nx = x + dx[i] * 2; // Posición siguiente en x
            int ny = y + dy[i] * 2; // Posición siguiente en y

            // Verificar si la siguiente posición es válida
            if (isValid(nx, ny, map)) {
                map[x + dx[i]][y + dy[i]] = 'S'; // Marcar la pared entre la posición actual y la siguiente
                generateMaze(map, nx, ny); // Llamada recursiva para la siguiente posición
            }
        }
    }

    private boolean isValid(int x, int y, char[][] map) {
        // Verificar si la posición (x, y) es válida en el mapa
        return x > 0 && x < map.length - 1 && y > 0 && y < map[0].length - 1 && map[x][y] == 'W';
    }

    private void printMap(char[][] map) {
        for (int i = 0; i < map.length; i++) {
            for (int j = 0; j < map[i].length; j++) {
                System.out.print(map[i][j] + " ");
            }
            System.out.println();
        }
    }

    private void tunnels(char[][] map, int entranceColumn, int exitColumn) {
        //Ve que este en el rango 0-14
        if (entranceColumn < 0 || entranceColumn >= map[0].length || exitColumn < 0 || exitColumn >= map[0].length) {

            return;
        }

        int entranceRow = (int) (Math.random() * (map.length - 2)) + 1; // Evitar esquinas
        map[entranceRow][entranceColumn] = 'T';

        // Colocar 'S' a la derecha del túnel de entrada (para la columna 0)
        if (entranceColumn == 0 && entranceColumn + 1 < map[0].length) {
            map[entranceRow][entranceColumn + 1] = 'S';
        }

        int exitRow = (int) (Math.random() * (map.length - 2)) + 1; // Evitar esquinas
        map[exitRow][exitColumn] = 'T';

        //por si no hay S
        if (exitColumn == map[0].length - 1 && exitColumn - 1 >= 0) {
            map[exitRow][exitColumn - 1] = 'S';
        }
    }

    private void powerPellets(char[][] map, int numPellets) {

        if (numPellets <= 0) {
            return;
        }

        Random random = new Random();
        int pelletsPlaced = 0;

        while (pelletsPlaced < numPellets) {
            int x = random.nextInt(map.length - 2) + 1;
            int y = random.nextInt(map[0].length - 2) + 1;

            if ((x > 5 && x < 10) && (y > 4 && y < 10)) {
                continue;
            }

            if (map[x][y] == 'W') {
                map[x][y] = 'D';
                pelletsPlaced++;
            }
        }
    }

    public char[][] mapGenerated2() {

        char map[][] = new char[15][15];

        for (int i = 0; i < 15; i++) {
            for (int j = 0; j < 15; j++) {
                map[i][j] = 'W';
            }
        }

        handleRoutes(map);
        initialsPositions(map);
        tunnels(map, 0, 14);
        powerPellets(map, 3);
        printMap(map);

        return map;
    }

    public void saveMaps() {
        for (int i = 0; i < 10; i++) {
            char[][] generatedMap = mapGenerated2();

            // Verifica si el mapa generado no está ya en el set
            if (mapSet.add(generatedMap)) {
                // Si no está en el set, agrégalo al set
                mapSet.add(generatedMap);
            } else {
                // Si ya está en el set, genera un nuevo mapa
                i--;
            }
        }
        System.out.println("Set de mapas: " + mapSet.toString());

        int nivel = 1; // Empieza desde el nivel 1
        int contL = 1;
        for (char[][] generatedMap : mapSet) {
            contL = 1;
            try (BufferedWriter writer = new BufferedWriter(new FileWriter("src/main/resources/cr/ac/una/pac/man/niveles/nivel (" + nivel + ").txt"))) {
                for (char[] row : generatedMap) {
                    boolean firstChar = true;
                    for (char cell : row) {
                        if (!firstChar) {
                            writer.write(' '); // Agrega un espacio entre cada caracter, excepto antes del primer carácter
                        }
                        writer.write(cell);
                        firstChar = false;
                    }
                    if(contL < 15){
                         writer.newLine();
                    }
                   
                    contL++;
                }
                // No insertar una línea en blanco después del último mapa
            } catch (IOException e) {
                e.printStackTrace();
            }

            nivel++;
        }

        System.out.println("Mapas guardados en archivos.");
    }


}
