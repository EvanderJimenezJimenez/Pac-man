package cr.ac.una.pac.man.controller;

import cr.ac.una.pac.man.GeneratedMap;
import cr.ac.una.pac.man.Statistics;
import cr.ac.una.pac.man.TopPlayers;
import cr.ac.una.pac.man.util.FlowController;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.ResourceBundle;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;

/**
 * @author evand
 */
public class WelcomeView extends Controller implements Initializable {

    ObservableList<Statistics> statisticsList;
    ObservableList<TopPlayers> palyersList;

    String player = null;

    GeneratedMap gen;

    @Override
    public void initialize(URL url, ResourceBundle rb) {

        gen = new GeneratedMap();

        if (archivoNoVacio()) {
            getStatistics();
            getPlayers();
            getPlayer();
            playerTopUpdate(player);
            System.out.println("Actualiza");
        } else {
            System.out.println("Genera mapas");
            gen.mapGenerated();
        }

    }

    @Override
    public void initialize() {
    }

    public void getStatistics() {

        List<Statistics> statiscstics = new ArrayList<>();

        try (BufferedReader br = new BufferedReader(new FileReader(".\\src\\main\\resources\\cr\\ac\\una\\pac\\man\\files\\statistics.txt"))) {
            String line;
            while ((line = br.readLine()) != null) {
                String[] statisticsData = line.split("\\*\\*\\*");

                for (String statisticsString : statisticsData) {

                    String[] parts = statisticsString.split("\\(//\\)");

                    if (parts.length >= 3) {

                        String score = parts[0];

                        String time = parts[1];

                        String lifes = parts[2];

                        String ghost = parts[3];

                        Statistics statistics = new Statistics(score, time, lifes, ghost);
                        statiscstics.add(statistics);
                    }
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        statisticsList = FXCollections.observableArrayList(statiscstics);

    }

    public void getPlayer() {
        player = null; // Asegúrate de inicializar la variable antes de usarla

        try (BufferedReader br = new BufferedReader(new FileReader(".\\src\\main\\resources\\cr\\ac\\una\\pac\\man\\files\\player.txt"))) {
            String line;
            while ((line = br.readLine()) != null) {
                // Buscar la posición de "***"
                int index = line.indexOf("***");
                if (index != -1) {
                    // Extraer el nombre antes de "***"
                    player = line.substring(0, index).trim();
                    break; // Rompe el bucle después de encontrar la primera ocurrencia
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void getPlayers() {

        List<TopPlayers> players = new ArrayList<>();

        try (BufferedReader br = new BufferedReader(new FileReader(".\\src\\main\\resources\\cr\\ac\\una\\pac\\man\\files\\topplayers.txt"))) {
            String line;
            while ((line = br.readLine()) != null) {
                String[] statisticsData = line.split("\\*\\*\\*");

                for (String statisticsString : statisticsData) {

                    String[] parts = statisticsString.split("\\(//\\)");

                    if (parts.length >= 1) {

                        String name = parts[0];

                        String score = parts[1];

                        TopPlayers player = new TopPlayers(name, score);
                        players.add(player);
                    }
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        palyersList = FXCollections.observableArrayList(players);

    }

    private boolean archivoNoVacio() {
        File player = new File(".\\src\\main\\resources\\cr\\ac\\una\\pac\\man\\files\\player.txt");
        return player.exists() && player.length() > 0;
    }

    public void playerTopUpdate(String playerName) {
        int currentScore = Integer.parseInt(statisticsList.get(0).getScore());

        boolean playerExists = false;
        TopPlayers existingPlayer = null;

        palyersList.sort((player1, player2) -> {

            int score1 = Integer.parseInt(player1.getScore());
            int score2 = Integer.parseInt(player2.getScore());
            return Integer.compare(score2, score1);
        });

        for (TopPlayers player : palyersList) {
            if (player.getName().equals(playerName)) {
                playerExists = true;
                existingPlayer = player;
                break;
            }
        }

        if (!playerExists) {
            //agrega 
            palyersList.add(new TopPlayers(playerName, String.valueOf(currentScore)));

        } else {

            if (existingPlayer != null && currentScore > Integer.parseInt(existingPlayer.getScore())) {
                existingPlayer.setScore(String.valueOf(currentScore));
            }
        }

        palyersList.sort((player1, player2) -> {

            int score1 = Integer.parseInt(player1.getScore());
            int score2 = Integer.parseInt(player2.getScore());
            return Integer.compare(score2, score1);
        });

        while (palyersList.size() > 10) {
            palyersList.remove(palyersList.size() - 1);
        }

        updateTopsFile(palyersList);
    }

    private void updateTopsFile(ObservableList<TopPlayers> palyersList) {
        try {

            String filePath = ".\\src\\main\\resources\\cr\\ac\\una\\pac\\man\\files\\topplayers.txt";
            FileWriter writer = new FileWriter(filePath);

            for (TopPlayers tops : palyersList) {
                String trofeoString = tops.getName() + "(//)" + tops.getScore() + "***";
                writer.write(trofeoString);
            }

            writer.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void onAction_start(ActionEvent event) {

        if (archivoNoVacio()) {
            System.out.println("Hoal1");
            FlowController.getInstance().goViewInWindow("ChooseLevel");
            getStage().close();

        } else {
            System.out.println("hola2");
            FlowController.getInstance().goViewInWindowModal("PlayerName", getStage(), true);
            getStage().close();

        }

    }

    @FXML
    private void onAction_player(ActionEvent event) {
        FlowController.getInstance().goViewInWindowModal("PlayerName", getStage(), Boolean.FALSE);

    }

    @FXML
    private void onAction_settings(ActionEvent event) {
        FlowController.getInstance().goLoadingView("SettingView");
    }

    @FXML
    private void onAction_exit(ActionEvent event) {
        getStage().close();
    }

    @FXML
    private void onAction_Estadisticas(ActionEvent event) {
        FlowController.getInstance().goViewInWindowModal("StatisticsView", getStage(), Boolean.FALSE);
    }

}
