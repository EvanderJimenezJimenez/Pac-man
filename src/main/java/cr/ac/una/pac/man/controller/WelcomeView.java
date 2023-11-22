package cr.ac.una.pac.man.controller;

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

    @Override
    public void initialize(URL url, ResourceBundle rb) {

        if (archivoNoVacio()) {
            getStatistics();
            getPlayers();
            getPlayer();
            playerTopUpdate(player);
            System.out.println("Actualiza");
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
        // Obtener el puntaje del jugador actual
        int currentScore = Integer.parseInt(statisticsList.get(0).getScore());

        // Verificar si el jugador ya está en la lista
        boolean playerExists = palyersList.stream().anyMatch(player -> player.getName().equals(playerName));

        if (palyersList.size() < 10) {
            // Si la lista aún no tiene 10 elementos, verificar si el jugador ya está presente
            if (playerExists) {
                // El jugador ya está en la lista, y su puntaje no es suficiente para estar en el top, pero actualizamos sus datos
                TopPlayers existingPlayer = palyersList.stream()
                        .filter(player -> player.getName().equals(playerName))
                        .findFirst().orElse(null);

                if (existingPlayer != null && currentScore > Integer.parseInt(existingPlayer.getScore())) {
                    existingPlayer.setScore(String.valueOf(currentScore));
                    // Aquí puedes agregar la lógica para actualizar el archivo de top players si es necesario
                }
            } else {
                // El jugador no está en la lista, agregarlo a la lista
                palyersList.add(new TopPlayers(playerName, String.valueOf(currentScore)));
                // Aquí puedes agregar la lógica para actualizar el archivo de top players si es necesario
            }
        } else {
            // La lista ya tiene 10 elementos, verificar si el puntaje actual es mejor que alguno en la lista
            int indexToReplace = -1;

            for (int i = 0; i < palyersList.size(); i++) {
                int playerScore = Integer.parseInt(palyersList.get(i).getScore());
                if (currentScore > playerScore) {
                    indexToReplace = i;
                    break;  // Encontramos un puntaje más bajo en la lista, podemos salir del bucle
                }
            }

            if (indexToReplace != -1) {
                // Reemplazar el puntaje en la posición indexToReplace con el nuevo puntaje
                palyersList.set(indexToReplace, new TopPlayers(playerName, String.valueOf(currentScore)));
                // Aquí puedes agregar la lógica para actualizar el archivo de top players si es necesario
            } else if (playerExists) {
                // El jugador ya está en la lista, y su puntaje no es suficiente para estar en el top, pero actualizamos sus datos
                TopPlayers existingPlayer = palyersList.stream()
                        .filter(player -> player.getName().equals(playerName))
                        .findFirst().orElse(null);

                if (existingPlayer != null && currentScore > Integer.parseInt(existingPlayer.getScore())) {
                    existingPlayer.setScore(String.valueOf(currentScore));
                    // Aquí puedes agregar la lógica para actualizar el archivo de top players si es necesario
                }
            }
        }

        updateTopsFile();
    }

    private void updateTopsFile() {
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
        FlowController.getInstance().goViewInWindow("PlayerName");
        getStage().close();
        FlowController.getInstance().deleteView("WelcomeView");
    }

    @FXML
    private void onAction_settings(ActionEvent event) {
        FlowController.getInstance().goLoadingView("SettingView");
        getStage().close();
        FlowController.getInstance().deleteView("WelcomeView");
    }

    @FXML
    private void onAction_exit(ActionEvent event) {
        getStage().close();
    }

    @FXML
    private void onAction_Estadisticas(ActionEvent event) {
        FlowController.getInstance().goViewInWindow("StatisticsView");
        getStage().close();
        FlowController.getInstance().deleteView("WelcomeView");
    }

}
