package cr.ac.una.pac.man;

import java.util.Random;

/**
 *
 * @author dario
 */
public class GeneratedMap {
    
        public void mapGenerated() {

        char map[][] = new char[15][15];

        for (int i = 0; i < 15; i++) {
            for (int j = 0; j < 15; j++) {
                map[i][j] = 'W';
            }
        }

       
            handleRoutes(map);
            initialsPositions(map);

        printMap(map);

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
        // Llenar la matriz con 'W'
        for (int i = 0; i < map.length; i++) {
            for (int j = 0; j < map[i].length; j++) {
                map[i][j] = 'W';
            }
        }

        // Generar el laberinto
        generateMaze(map, 1, 1);

        // Colocar los elementos restantes (N, B, C, R, I, P) en posiciones específicas
        // ... (tu código para colocar los elementos)

    }

    private void generateMaze(char[][] map, int x, int y) {
        map[x][y] = 'S'; // Marcar la posición actual como parte del camino

        // Direcciones posibles (arriba, derecha, abajo, izquierda)
        int[] dx = { 0, 1, 0, -1 };
        int[] dy = { -1, 0, 1, 0 };

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

    //Ejemplo de matriz que debe crear
    /*

    W W W W W W W W W W W W W W W
    W S S S W W W W S W W W S W W
    W S W W S W W W S S S W W S W
    W S W S S S S S S W S S S S W
    W S S S S W W S W W W S W S W
    W S S W N N N N N N N S S S W
    W S S W N W W B W W N W W S W
    W S S S N W C R I W N W W S W
    W W S W N W W W W W N S S S W
    W S S S N N N P N N N W S S W
    W S S S W S W W W S W W W S W
    W S W S S S S S S S S S S S W
    W S W S W S S W W W S S W S W
    W S S S W S S S S S S S S S W
    W W W W W W W W W W W W W W W

*/

    private void printMap(char[][] map) {
        for (int i = 0; i < map.length; i++) {
            for (int j = 0; j < map[i].length; j++) {
                System.out.print(map[i][j] + " ");
            }
            System.out.println();
        }
    }

    
}
