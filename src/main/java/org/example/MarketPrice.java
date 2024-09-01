package org.example;

import java.net.URL;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.DecimalFormat;
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
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.ToggleButton;
import javafx.scene.control.ToggleGroup;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyEvent;

public class MarketPrice implements Initializable {

    @FXML private ToggleButton btnExpenses, btnHome, btnMarketPrice;

    @FXML private Button btnSearch;

    @FXML private ToggleGroup navbar;

    @FXML
    private TextField txtSearch;

    @FXML
    private TableView<Market> tableView;
    @FXML
    private TableColumn<Market, String> colName;
    @FXML
    private TableColumn<Market, String> colCat;
    @FXML
    private TableColumn<Market, Double> colPastPrice;
    @FXML
    private TableColumn<Market, Double> colPresPrice;
    @FXML
    private TableColumn<Market, String> colUnit;
    @FXML
    private TableColumn<Market, ImageView> colStatus;
    
    DecimalFormat deci = new DecimalFormat("0.00");
    
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        navbar.selectedToggleProperty().addListener((obsVal, oldVal, newVal) -> {if (newVal == null) oldVal.setSelected(true);});
        setupTable();
    }

    @FXML
    void showExpenses(ActionEvent event) {
        ControllerUtil.showExpenses();
    }

    @FXML
    void showHome(ActionEvent event) {
        ControllerUtil.showHome();
    }

    private void setupTable(){
        ObservableList<Market> tblList = FXCollections.observableArrayList();
        colName.setCellValueFactory(new PropertyValueFactory<>("prodName"));
        colCat.setCellValueFactory(new PropertyValueFactory<>("cat"));
        colPastPrice.setCellValueFactory(new PropertyValueFactory<>("pastPrice"));
        colPresPrice.setCellValueFactory(new PropertyValueFactory<>("presPrice"));
        colUnit.setCellValueFactory(new PropertyValueFactory<>("unit"));
        colStatus.setCellValueFactory(new PropertyValueFactory<>("status"));
        NumberFormat currencyFormat = NumberFormat.getCurrencyInstance(new Locale("EN", "PH"));
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
            Connection conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/market_products","root","");
            
            PreparedStatement pst = conn.prepareStatement("SELECT * FROM product_information;");
            ResultSet rs = pst.executeQuery();
            
            while(rs.next()){
                String data1 = rs.getString("prodName");
                String data2 = rs.getString("category");
                Double data3 = rs.getDouble("prevAvePrice");
                Double data4 = rs.getDouble("presAvePrice");
                String data5 = rs.getString("unitOfMeasure");

                ImageView img;
                if(data3 < data4){
                    img = new ImageView(new Image(getClass().getResourceAsStream("up.png")));
                    img.setFitHeight(20);
                    img.setFitWidth(20);
                }
                else if(data3 > data4){
                    img = new ImageView(new Image(getClass().getResourceAsStream("down.png")));
                    img.setFitHeight(20);
                    img.setFitWidth(20);
                }
                else{
                    img = new ImageView(new Image(getClass().getResourceAsStream("nochange.png")));
                    img.setFitHeight(5);
                    img.setFitWidth(15);
                }
                tblList.add(new Market(data1,data2,data3,data4,data5, img));
                tableView.setItems(tblList); 
            }
        } catch (SQLException | ClassNotFoundException ex) {
            Logger.getLogger(MarketPrice.class.getName()).log(Level.SEVERE, null, ex);
        } 
    }

    @FXML
    private void textChanged(KeyEvent event) {
        //System.out.println(event.getCharacter());
        
        String str = txtSearch.getText();
        
        try {
            ObservableList<Market> tblList = FXCollections.observableArrayList();
            
            Class.forName("com.mysql.cj.jdbc.Driver");
            Connection conn = DriverManager.getConnection("jdbc:mysql:///market_products","root","");
            
            PreparedStatement pst = conn.prepareStatement("SELECT * FROM `product_information` WHERE prodname LIKE ?;");
            pst.setString(1, "%" + str + "%");
            ResultSet rs = pst.executeQuery();
            
            while(rs.next()){
                String data1 = rs.getString("prodName");
                String data2 = rs.getString("category");
                Double data3 = rs.getDouble("prevAvePrice");
                Double data4 = rs.getDouble("presAvePrice");
                String data5 = rs.getString("unitOfMeasure");

                ImageView img;
                if(data3 < data4){
                    img = new ImageView(new Image(getClass().getResourceAsStream("up.png")));
                    img.setFitHeight(20);
                    img.setFitWidth(20);
                }
                else if(data3 > data4){
                    img = new ImageView(new Image(getClass().getResourceAsStream("down.png")));
                    img.setFitHeight(20);
                    img.setFitWidth(20);
                }
                else{
                    img = new ImageView(new Image(getClass().getResourceAsStream("nochange.png")));
                    img.setFitHeight(5);
                    img.setFitWidth(15);
                }
                //ImageView img = new ImageView(new Image(getClass().getResourceAsStream("return_appdev.png")));
                tblList.add(new Market(data1,data2,data3,data4,data5, img));
                tableView.setItems(tblList); 
            }
            
        } catch (ClassNotFoundException | SQLException ex) {
            Logger.getLogger(MarketPrice.class.getName()).log(Level.SEVERE, null, ex);
        } 
    }
}
