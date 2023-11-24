module cr.ac.una.pac.man {
   requires javafx.controls;
    requires javafx.fxml;
    requires java.logging;
    requires com.jfoenix;
    requires java.base;
    
    requires javafx.graphics;
    requires  java.desktop;
    
    requires org.jgrapht.core;


    opens cr.ac.una.pac.man to javafx.fxml,javafx.graphics;
    opens cr.ac.una.pac.man.controller to javafx.fxml,javafx.controls,com.jfoenix;
    
    exports cr.ac.una.pac.man.controller;
    
    exports cr.ac.una.pac.man;
}
