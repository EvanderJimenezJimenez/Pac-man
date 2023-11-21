package cr.ac.una.pac.man.controller;

import cr.ac.una.pac.man.util.AppContext;
import cr.ac.una.pac.man.util.FlowController;
import java.net.URL;
import java.util.ResourceBundle;
import java.util.Timer;
import java.util.TimerTask;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;

/**
 * @author evand
 */
public class LoadingView extends Controller implements Initializable {

    long duracionEnMilisegundos = 200; // 5 segundos
    double largo = 0;
    
    String nextPage = (String) AppContext.getInstance().get("NextPage");


    @FXML
    private BorderPane root;
    @FXML
    private AnchorPane LoadingBar;
    @FXML
    private AnchorPane pnBar;
    @FXML
    private Label lbPercentage;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        System.out.println(nextPage);
        Loading();
    }

    public void Loading() {
        Timer timer = new Timer();

        timer.scheduleAtFixedRate(new TimerTask() {
            int seg = 0;

            @Override
            public void run() {
                
                seg++;

                Platform.runLater(() -> {
                    lbPercentage.setText(seg + "%");
                    AvanceBar(seg);
                });

                if (seg >= 100) {
                    timer.cancel();
                }
            }
        }, 50, 10); //1000 100
    }

    public void AvanceBar(int seg) {
        largo = largo + 2;
        if (seg == 100) {
            System.out.println("Antes");
            FlowController.getInstance().goViewInWindow(nextPage);
            getStage().close();
            System.out.println("Despues");

        } else {
            pnBar.setPrefWidth(largo);
        }
    }

    @Override
    public void initialize() {
    }
}
