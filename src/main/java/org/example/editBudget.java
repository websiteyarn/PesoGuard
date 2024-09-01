package org.example;

import java.net.URL;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

public class editBudget implements Initializable {

    @FXML private Button btnDone;

    @FXML private TextField txtBudget;
    @FXML
    private AnchorPane scenePane;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        btnDone.setOnAction(actionEvent -> {
            if(txtBudget.getText().isEmpty())
                return;
            try {
                Class.forName("com.mysql.cj.jdbc.Driver");
                Connection conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/market_products","root","");
                
                PreparedStatement pst = conn.prepareStatement("UPDATE user_budget SET budget=? WHERE shoplistID=?;");
                pst.setString(1, txtBudget.getText());
                pst.setString(2, ControllerUtil.getshoplistID());
                pst.executeUpdate();
                ControllerUtil.showHome();
                Stage currentStage = (Stage) ((Node) actionEvent.getSource()).getScene().getWindow();
                currentStage.close();
                
            } catch (ClassNotFoundException | SQLException ex) {
                Logger.getLogger(editBudget.class.getName()).log(Level.SEVERE, null, ex);
            }
        });
    }

}
