package org.example;

import java.net.URL;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.image.ImageView;
import javafx.scene.layout.TilePane;

public class SignUp implements Initializable {

    @FXML
    private Button btnLogin;

    @FXML
    private ImageView btnReturn;

    @FXML
    private Button btnSignUp;

    @FXML
    private TextField tf_dob;

    @FXML
    private TextField tf_name;

    @FXML
    private PasswordField tf_password;

    @FXML
    private TextField tf_username;
    
    private ArrayList<String> UIDList = new ArrayList<>();

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        btnReturn.setOnMouseClicked(mouseEvent -> ControllerUtil.showWelcome());
        
        btnReturn.setOnMouseClicked(mouseEvent -> ControllerUtil.showWelcome());
        
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            Connection conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/market_products","root","");

            PreparedStatement pst = conn.prepareStatement("SELECT * FROM user_information;");
            ResultSet rs = pst.executeQuery();
            
            while(rs.next()){
                String uid = rs.getString("username");
                UIDList.add(uid);
            }
        } catch (ClassNotFoundException | SQLException ex) {
            Logger.getLogger(SignUp.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        for(String data: UIDList){
            System.out.println(data);
        }
    }

    @FXML
    void showLogIn(ActionEvent event) {
        ControllerUtil.showLogIn();
    }

    @FXML
    private void buttonClicked(ActionEvent event) {
        if(event.getSource() == btnSignUp){
            String name = tf_username.getText();
            String bdate = tf_dob.getText();
            String username = tf_username.getText();
            String password = tf_password.getText();

            if(name.equals("") || bdate.equals("") || username.equals("") || password.equals("")){
                TilePane r = new TilePane();
                Alert a = new Alert(Alert.AlertType.NONE);
                a.setContentText("Fill-out all fields.");
                a.setAlertType(Alert.AlertType.WARNING);
                a.show();
            }
            else{
                // add acc to db
                for(String data: UIDList){
                    if(username.equals(data)){
                        TilePane r = new TilePane();
                        Alert a = new Alert(Alert.AlertType.NONE);
                        a.setContentText("Account already exists.");
                        a.setAlertType(Alert.AlertType.WARNING);
                        a.show();
                        return;
                    }
                }
                createAcc(name,bdate,username,password);
                tf_name.setText("");
                tf_dob.setText("");
                tf_username.setText("");
                tf_password.setText("");
                
                TilePane r = new TilePane();
                Alert a = new Alert(Alert.AlertType.NONE);
                a.setContentText("Account created succesfully.");
                a.setAlertType(Alert.AlertType.INFORMATION);
                a.show();
            }
        }
    }

    private void createAcc(String name, String bdate, String username, String password){
        try {
            Connection conn;
            PreparedStatement pst;
            Class.forName("com.mysql.cj.jdbc.Driver");
            conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/market_products","root","");
            
            pst = conn.prepareStatement("INSERT into `user_information` (name,birthdate,username,password) VALUES (?,?,?,?);");
            pst.setString(1, name);
            pst.setString(2, bdate);
            pst.setString(3, username);
            pst.setString(4, password);
            pst.executeUpdate();
            
            pst = conn.prepareStatement("INSERT into user_budget (username,week,budget,expenses) VALUES (?,\"Week 1\",\"0\",\"0\");");
            pst.setString(1, username);
            pst.executeUpdate();
            
        } catch (ClassNotFoundException | SQLException ex) {
            Logger.getLogger(SignUp.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

}
