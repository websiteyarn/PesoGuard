/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/javafx/FXMLController.java to edit this template
 */
package org.example;

import java.net.URL;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.NumberFormat;
import java.util.Locale;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.ToggleGroup;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;

public class AdminPanel implements Initializable {

    @FXML
    private ToggleGroup navbar;
    @FXML
    private Button btnSearch;
    @FXML
    private TextField txtSearch;
    @FXML
    private AnchorPane tableView;
    @FXML
    private TableColumn<Market, String> colName;
    @FXML
    private TableColumn<Market, Double> colPastPrice;
    @FXML
    private TableColumn<Market, Double> colPresPrice;
    @FXML
    private TableColumn<Market, String> colCat;
    @FXML
    private TableView<Market> table;
    @FXML
    private TextField updatetf;
    @FXML
    private Label pricelbl;
    @FXML
    private Button updateBtn;
    @FXML
    private Label namelbl;
    
    Connection conn;
    PreparedStatement pst;
    ResultSet rs;
            
    private String name;
    private String cat;
    private Double presPrice;
    private Double pastPrice;
    
    NumberFormat currencyFormat = NumberFormat.getCurrencyInstance(new Locale("EN", "PH"));

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        show_table();
    }    

    private void show_table(){
        ObservableList<Market> tblList = FXCollections.observableArrayList();
        colName.setCellValueFactory(new PropertyValueFactory<>("prodName"));
        colCat.setCellValueFactory(new PropertyValueFactory<>("cat"));
        colPastPrice.setCellValueFactory(new PropertyValueFactory<>("pastPrice"));
        colPresPrice.setCellValueFactory(new PropertyValueFactory<>("presPrice"));
        
        colPresPrice.setCellFactory(column ->
            new TableCell<Market, Double>() {
                @Override
                public void updateItem(Double price, boolean empty) {
                    super.updateItem(price, empty);
                    setText(empty? null : currencyFormat.format(price));
                }
        });
        
        colPastPrice.setCellFactory(column ->
            new TableCell<Market, Double>() {
                @Override
                public void updateItem(Double price, boolean empty) {
                    super.updateItem(price, empty);
                    setText(empty? null : currencyFormat.format(price));
                }
        });
        
        
        try { 
            Class.forName("com.mysql.cj.jdbc.Driver");
            conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/market_products","root","");
            
            pst = conn.prepareStatement("SELECT * FROM product_information;");
            rs = pst.executeQuery();
            
            while(rs.next()){
                String data1 = rs.getString("prodName");
                String data2 = rs.getString("category");
                Double data3 = rs.getDouble("prevAvePrice");
                Double data4 = rs.getDouble("presAvePrice");

                tblList.add(new Market(data1,data2,data3,data4));
                table.setItems(tblList); 
            }
        } catch (SQLException | ClassNotFoundException ex) {
            Logger.getLogger(MarketPrice.class.getName()).log(Level.SEVERE, null, ex);
        } 
    }
    
    @FXML
    private void textChanged(KeyEvent event) {
        ObservableList<Market> tblList = FXCollections.observableArrayList();
        try { 
            Class.forName("com.mysql.cj.jdbc.Driver");
            conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/market_products","root","");
            
            pst = conn.prepareStatement("SELECT * FROM `product_information` WHERE prodname LIKE ?;");
            pst.setString(1, "%" + txtSearch.getText() + "%");
            rs = pst.executeQuery();
            
            while(rs.next()){
                String data1 = rs.getString("prodName");
                String data2 = rs.getString("category");
                Double data3 = rs.getDouble("prevAvePrice");
                Double data4 = rs.getDouble("presAvePrice");

                tblList.add(new Market(data1,data2,data3,data4));
                table.setItems(tblList); 
            }
        } catch (SQLException | ClassNotFoundException ex) {
            Logger.getLogger(MarketPrice.class.getName()).log(Level.SEVERE, null, ex);
        }    
        
    }

    @FXML
    private void showHome(ActionEvent event) {
    }

    @FXML
    private void buttonClicked(ActionEvent event) {  
        String newPrice = updatetf.getText();
        
        if(newPrice.equals(""))
            return;
        
        try { 
            Class.forName("com.mysql.cj.jdbc.Driver");
            conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/market_products","root","");
            
            pst = conn.prepareStatement("UPDATE product_information SET prevAvePrice=?, presAvePrice=? WHERE prodName=? AND category=?;");
            pst.setString(1, presPrice.toString());
            pst.setString(2, newPrice);
            pst.setString(3, name);
            pst.setString(4, cat);
            pst.executeUpdate();
            
        } catch (SQLException | ClassNotFoundException ex) {
            Logger.getLogger(MarketPrice.class.getName()).log(Level.SEVERE, null, ex);
        } 
        show_table();
        namelbl.setText("");
        updatetf.setText("");
    }

    @FXML
    private void tableClicked(MouseEvent event) {
        if(event.getClickCount() == 1){
            if(table.getSelectionModel().getSelectedItem() != null){
                Market selectedrow = table.getSelectionModel().getSelectedItem();
                
                name = selectedrow.getProdName();
                cat = selectedrow.getCat();
                presPrice = selectedrow.getPresPrice();
                pastPrice = selectedrow.getPastPrice();
                
                namelbl.setText(name);
                pricelbl.setText(currencyFormat.format(presPrice));
            }
        }
    }
    
}
