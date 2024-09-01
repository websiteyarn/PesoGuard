package org.example;

import javafx.application.Application;
import javafx.scene.text.Font;
import javafx.stage.Stage;

public class Main extends Application {

    @Override
    public void start(Stage stage) throws Exception {
        Font.loadFont(getClass().getResourceAsStream("Inter-Regular.ttf"), 10);
        ControllerUtil.start();
    }

    public static void main(String[] args) {
        launch();
    }

}