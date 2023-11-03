/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package cr.ac.una.pac.man.controller;

import java.net.URL;
import java.util.ResourceBundle;
import javafx.fxml.Initializable;

/**
 *
 * @author Joshua Cambronero
 */
class GameData extends Controller implements Initializable {

    public String getLabelScore() {
        return labelScore;
    }

    public void setLabelScore(String labelScore) {
        this.labelScore = labelScore;
    }

    public int getLabelLevel() {
        return labelLevel;
    }

    public void setLabelLevel(int labelLevel) {
        this.labelLevel = labelLevel;
    }
     String labelScore; 
     int labelLevel; 

    @Override
    public void initialize() {
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        //Todo
    }
}
