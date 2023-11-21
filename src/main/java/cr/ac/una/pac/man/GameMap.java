package cr.ac.una.pac.man;

import cr.ac.una.pac.man.controller.GameViewController;
import java.awt.Point;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import javafx.scene.image.Image;

/**
 *
 * @author dario
 */
public class GameMap {

    // Variables relacionadas con el mapa
    private char[][] map;
    private List<Point> smallPoints = new ArrayList<>();
    

    //map
    private Image wallImage;
    private Image smallPointImage;
    private Image bigPointImage;
    private Image lifeImage;

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

    //vidas
    Image life1;
    Image life2;
    Image life3;
    Image life4;
    Image life5;
    Image life6;

    public GameMap() {

    }

    public void cargarImagenes() {
        //map
        wallImage = getImage("wall");
        smallPointImage = getImage("smallPoint");
        bigPointImage = getImage("bigPoint");

        //pacman
        pacmanRight = getImage("pacmanRight");
        pacmanFeft = getImage("pacmanLeft");
        pacmanUp = getImage("pacmanUp");
        pacmanDown = getImage("pacmanDown");

        //ghost
        blinkyImage = getImage("blinky");
        clydeImage = getImage("clyde");
        inkyImage = getImage("inky");
        pinkyImage = getImage("pinky");

        //life
        life1 = getImage("life");
        life2 = getImage("life");
        life3 = getImage("life");
        life4 = getImage("life");
        life5 = getImage("life");
        life6 = getImage("life");
    }

    public Image getImage(String imageName) {

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
            case "blue":
                imageURL = getClass().getResource("/cr/ac/una/pac/man/resources/blueghost.gif");
                break;
                case "ojos":
                imageURL = getClass().getResource("/cr/ac/una/pac/man/resources/ojos.png");
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

    public void cargarMapa(int nivel) {

        int cantidadFilas = 15, cantidadColumnas = 15;
        map = new char[cantidadFilas][cantidadColumnas];
        //gridPaneMap.getChildren().clear();
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
    }

    public char[][] getMap() {
        return map;
    }

    public void setMap(char[][] map) {
        this.map = map;
    }

    public List<Point> getSmallPoints() {
        return smallPoints;
    }

    public void setSmallPoints(List<Point> smallPoints) {
        this.smallPoints = smallPoints;
    }

    public Image getWallImage() {
        return wallImage;
    }

    public void setWallImage(Image wallImage) {
        this.wallImage = wallImage;
    }

    public Image getSmallPointImage() {
        return smallPointImage;
    }

    public void setSmallPointImage(Image smallPointImage) {
        this.smallPointImage = smallPointImage;
    }

    public Image getBigPointImage() {
        return bigPointImage;
    }

    public void setBigPointImage(Image bigPointImage) {
        this.bigPointImage = bigPointImage;
    }

    public Image getLifeImage() {
        return lifeImage;
    }

    public void setLifeImage(Image lifeImage) {
        this.lifeImage = lifeImage;
    }

    public Image getPacmanRight() {
        return pacmanRight;
    }

    public void setPacmanRight(Image pacmanRight) {
        this.pacmanRight = pacmanRight;
    }

    public Image getPacmanFeft() {
        return pacmanFeft;
    }

    public void setPacmanFeft(Image pacmanFeft) {
        this.pacmanFeft = pacmanFeft;
    }

    public Image getPacmanUp() {
        return pacmanUp;
    }

    public void setPacmanUp(Image pacmanUp) {
        this.pacmanUp = pacmanUp;
    }

    public Image getPacmanDown() {
        return pacmanDown;
    }

    public void setPacmanDown(Image pacmanDown) {
        this.pacmanDown = pacmanDown;
    }

    public Image getBlinkyImage() {
        return blinkyImage;
    }

    public void setBlinkyImage(Image blinkyImage) {
        this.blinkyImage = blinkyImage;
    }

    public Image getClydeImage() {
        return clydeImage;
    }

    public void setClydeImage(Image clydeImage) {
        this.clydeImage = clydeImage;
    }

    public Image getPinkyImage() {
        return pinkyImage;
    }

    public void setPinkyImage(Image pinkyImage) {
        this.pinkyImage = pinkyImage;
    }

    public Image getInkyImage() {
        return inkyImage;
    }

    public void setInkyImage(Image inkyImage) {
        this.inkyImage = inkyImage;
    }

    public Image getLife1() {
        return life1;
    }

    public void setLife1(Image life1) {
        this.life1 = life1;
    }

    public Image getLife2() {
        return life2;
    }

    public void setLife2(Image life2) {
        this.life2 = life2;
    }

    public Image getLife3() {
        return life3;
    }

    public void setLife3(Image life3) {
        this.life3 = life3;
    }

    public Image getLife4() {
        return life4;
    }

    public void setLife4(Image life4) {
        this.life4 = life4;
    }

    public Image getLife5() {
        return life5;
    }

    public void setLife5(Image life5) {
        this.life5 = life5;
    }

    public Image getLife6() {
        return life6;
    }

    public void setLife6(Image life6) {
        this.life6 = life6;
    }

}
