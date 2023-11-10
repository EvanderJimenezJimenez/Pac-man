package cr.ac.una.pac.man;

import javafx.animation.Timeline;
import javafx.scene.image.ImageView;
import javafx.scene.layout.GridPane;

/**
 *
 * @author dario
 */
public class Pacman {

    
    
    public void pauseGame(Timeline pinkyTimeline, Timeline inkyTimeline, Timeline blinkyTimeline, Timeline clydeTimeline, Timeline pacManTimeline) {
        pinkyTimeline.pause();
        inkyTimeline.pause();
        blinkyTimeline.pause();
        clydeTimeline.pause();

        pacManTimeline.pause();

    }

    public void restartGhost(GridPane gridPaneMap,
            ImageView blinkyImageView, int blinkyXHouse, int blinkyYHouse,
            ImageView pinkyImageView, int pinkyXHouse, int pinkyYHouse,
            ImageView inkyImageView, int inkyXHouse, int inkyYHouse,
            ImageView clydeImageView, int clydeXHouse, int clydeYHouse,
            ImageView pacmanImageView, int pacmanXHouse, int pacmanYHouse) {

        // Blinky
        gridPaneMap.getChildren().remove(blinkyImageView);
        gridPaneMap.add(blinkyImageView, blinkyXHouse, blinkyYHouse);

        // Pinky
        gridPaneMap.getChildren().remove(pinkyImageView);
        gridPaneMap.add(pinkyImageView, pinkyXHouse, pinkyYHouse);

        // Inky
        gridPaneMap.getChildren().remove(inkyImageView);
        gridPaneMap.add(inkyImageView, inkyXHouse, inkyYHouse);

        // Clyde
        gridPaneMap.getChildren().remove(clydeImageView);
        gridPaneMap.add(clydeImageView, clydeXHouse, clydeYHouse);

        // Pacman
        gridPaneMap.getChildren().remove(pacmanImageView);
        gridPaneMap.add(pacmanImageView, pacmanXHouse, pacmanYHouse);
    }

}
