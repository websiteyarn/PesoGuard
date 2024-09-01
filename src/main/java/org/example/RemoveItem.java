package org.example;

import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

public class RemoveItem {

    @FXML
    private Button btnNo;

    @FXML
    private Button btnYes;
    
    Connection conn;
    PreparedStatement pst;
    ResultSet rs;  
    @FXML
    private AnchorPane scenePane;

    @FXML
    private void buttonClicked(ActionEvent event) throws IOException {
        
        if(event.getSource() == btnNo){
            Stage stage = ControllerUtil.getStage();
            Stage currentStage = (Stage) scenePane.getScene().getWindow();
            currentStage.close();
        }
        if(event.getSource() == btnYes){
            System.out.println(ControllerUtil.getRemoveItem());
            System.out.println(ControllerUtil.getshoplistID());
                    
            try {
                String prodname = ControllerUtil.getRemoveItem();
                String shoplistID = ControllerUtil.getshoplistID();
                
                Class.forName("com.mysql.cj.jdbc.Driver");
                conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/market_products","root","");
                
                pst = conn.prepareStatement("DELETE FROM `shopping_list` WHERE shoplistID=? AND prodName=?;");
                pst.setString(1, shoplistID);
                pst.setString(2, prodname);

                pst.executeUpdate();

            } catch (ClassNotFoundException ex) {
                Logger.getLogger(RemoveItem.class.getName()).log(Level.SEVERE, null, ex);
            } catch (SQLException ex) {
                Logger.getLogger(RemoveItem.class.getName()).log(Level.SEVERE, null, ex);
            }
                FXMLLoader loader = new FXMLLoader(ControllerUtil.class.getResource("/org/example/Home.fxml"));
                Scene scene = new Scene(loader.load());
                Stage stage = ControllerUtil.getStage();
                Home controller = loader.getController();
                controller.setreTable("true");
                Stage currentStage = (Stage) scenePane.getScene().getWindow();
                currentStage.close();
                stage.setScene(scene);
                stage.show();
        }
    }
}
