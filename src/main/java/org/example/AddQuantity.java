package org.example;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

public class AddQuantity {
    
    @FXML
    private Button btnDone;
    
    @FXML
    private AnchorPane scenePane;
    @FXML
    private TextField txtQty;

    @FXML
    private void buttonClicked(ActionEvent event) {
        
        if(txtQty.getText().isEmpty())
            return;
                            
        try {
            
            String prodname = ControllerUtil.getChosenItem();
            Double qty = Double.parseDouble(txtQty.getText());
            Double price = ControllerUtil.getPrice();
            Double total = price*qty;
            
            Class.forName("com.mysql.cj.jdbc.Driver");
            Connection conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/market_products","root","");
            
            PreparedStatement pst = conn.prepareStatement("INSERT INTO `shopping_list` (shoplistID,prodName,qty,price,total) VALUES (?,?,?,?,?);");
            pst.setString(1, ControllerUtil.getshoplistID());
            pst.setString(2, prodname);
            pst.setString(3, txtQty.getText());
            pst.setString(4, price.toString());
            pst.setString(5, total.toString());
            
            pst.executeUpdate();
            ControllerUtil.showHome();
            Stage currentStage = (Stage)((Node) event.getSource()).getScene().getWindow();
            currentStage.close();

        } catch (ClassNotFoundException | SQLException ex) {
            Logger.getLogger(AddQuantity.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}
