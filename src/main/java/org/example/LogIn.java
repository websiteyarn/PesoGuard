package org.example;

import java.io.IOException;
import java.net.URL;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.TilePane;

public class LogIn implements Initializable {

    @FXML
    private Button btnLogIn;

    @FXML
    private ImageView btnReturn;

    @FXML
    private PasswordField tf_password;

    @FXML
    private TextField tf_username;
    @FXML
    private BorderPane scenePane;

    @FXML
    void showMain(ActionEvent event) throws IOException {
        //ControllerUtil.showHome(); //Change to show home later
        String username = tf_username.getText();
        String password = tf_password.getText();

        if(username.equals("admin") && password.equals("111111")){
            FXMLLoader loader = new FXMLLoader(ControllerUtil.class.getResource("/org/example/AdminPanel.fxml"));
            Scene scene = new Scene(loader.load());
            ControllerUtil.getStage().setScene(scene);
            ControllerUtil.getStage().show();
            return;
        }

            
                    
        try {

            String uid="";

            Class.forName("com.mysql.cj.jdbc.Driver");
            Connection conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/market_products","root","");
            PreparedStatement pst = conn.prepareStatement("SELECT * FROM `user_information` WHERE username=? AND password=?;");
            pst.setString(1, username);
            pst.setString(2, password);
            ResultSet rs = pst.executeQuery();

            while(rs.next()){
                uid = rs.getString("username");
            }

            if(uid.equals("")){
                TilePane r = new TilePane();
                Alert a = new Alert(AlertType.NONE);
                a.setContentText("Account does not exist.");
                a.setAlertType(AlertType.WARNING);
                a.show();
            }
            else{
                ControllerUtil.setUID(username);
                ControllerUtil.showHome();

            }

        } catch (ClassNotFoundException | SQLException ex) {
            Logger.getLogger(LogIn.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        btnReturn.setOnMouseClicked(mouseEvent -> ControllerUtil.showWelcome());
    }

}
