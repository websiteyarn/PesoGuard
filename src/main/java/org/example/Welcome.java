package org.example;

import java.net.URL;
import java.util.ResourceBundle;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;

public class Welcome implements Initializable {
    @FXML
    private void showLogIn(ActionEvent event) {
        ControllerUtil.showLogIn();
    }

    @FXML
    private void showSignUp(ActionEvent event) {
        ControllerUtil.showSignUp();
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
    }
}
