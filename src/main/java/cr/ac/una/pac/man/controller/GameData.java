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

     String labelScore; 
     int labelLevel;
     int time;//Cambio Joshua
     int horas;//Cambio Joshua
     int minutos;//Cambio Joshua
     int segundos;//Cambio Joshua
    
    
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
    ////////////Cambio Joshua/////////////////
    public int getTime() {
        return time;
    }

    public void setTime(int time) {
        this.time = time;
    }

    public int getHoras() {
        return horas;
    }

    public void setHoras(int horas) {
        this.horas = horas;
    }

    public int getMinutos() {
        return minutos;
    }

    public void setMinutos(int minutos) {
        this.minutos = minutos;
    }

    public int getSegundos() {
        return segundos;
    }

    public void setSegundos(int segundos) {
        this.segundos = segundos;
    }
    
    
    

    @Override
    public void initialize() {
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        //Todo
    }
    /////////////////////////////////////////////////////////////////////////
}
