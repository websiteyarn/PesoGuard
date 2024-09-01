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

import javax.swing.JOptionPane;

import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.ToggleButton;
import javafx.scene.control.ToggleGroup;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.TilePane;

public class Home implements Initializable {
    
    private static String username;
    private static String shoplistID;
    private static Double budget;
    private static Double expenses;
    private static Double sum;
    private static String week;

    @FXML 
    private Button btnAddItems, btnBudget, btnDeleteAll;

    @FXML 
    private ToggleButton btnExpenses, btnHome, btnMarketPrice;

    @FXML 
    private Button btnWeek;

    @FXML 
    private ComboBox<String> comboMonth, comboSort;

    @FXML 
    private Label lblBalance, lblBudget, lblExpense, lblWeek;

    @FXML 
    private ToggleGroup navbar;

    @FXML 
    private TableView<ShoppingList> tableList;

    private final TableColumn<ShoppingList, String> colItem = new TableColumn<>("Name");
    private final TableColumn<ShoppingList, Number> colQuantity = new TableColumn<>("Quantity");
    private final TableColumn<ShoppingList, Number> colPrice = new TableColumn<>("Price");
    private final NumberFormat currencyFormat = NumberFormat.getCurrencyInstance(new Locale("EN", "PH"));

    Connection conn;
    PreparedStatement pst;
    ResultSet rs;    
                
    
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        setUID(ControllerUtil.getUID());
        navbar.selectedToggleProperty().addListener((obsVal, oldVal, newVal) -> {if (newVal == null) oldVal.setSelected(true);});
        btnBudget.setOnAction(actionEvent -> ControllerUtil.showEditBudget());
        
        tableList.getColumns().addAll(colItem, colQuantity, colPrice);
        tableList.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
        tableList.getColumns().forEach(column -> {column.setReorderable(false); column.setResizable(false); column.setSortable(false);});
        colItem.prefWidthProperty().bind(tableList.widthProperty().multiply(0.33));
        colQuantity.prefWidthProperty().bind(tableList.widthProperty().multiply(0.32));
        colPrice.prefWidthProperty().bind(tableList.widthProperty().multiply(0.32));
        colItem.setSortable(true);
        colQuantity.setSortable(true);
        colPrice.setSortable(true);

        //Para maspecify yung format ng value na asa cell
        colItem.setCellValueFactory(cell -> new SimpleStringProperty(cell.getValue().getItem()));
        colQuantity.setCellValueFactory(cell -> new SimpleDoubleProperty(cell.getValue().getQuantity()));
        colPrice.setCellValueFactory(cell -> new SimpleDoubleProperty(cell.getValue().getPrice()));

        colPrice.setCellFactory(column ->
            new TableCell<ShoppingList, Number>() {
                @Override
                public void updateItem(Number price, boolean empty) {
                    super.updateItem(price, empty);
                    setText(empty? null : currencyFormat.format(price));
                }
        });
        ObservableList<String> sort = FXCollections.observableArrayList("Name", "Price");
        comboSort.setItems(sort);
    }

    @FXML
    private void showAddItems(ActionEvent event) {
        ControllerUtil.showAddItem();
    }

    @FXML
    private void showExpenses(ActionEvent event) {
        ControllerUtil.showExpenses();
    }

    @FXML
    private void showMarketPrice(ActionEvent event) {
        ControllerUtil.showMarketPrice();
    }

    public void setLblBudget(String budget) {
        lblBudget.setText(currencyFormat.format(Double.valueOf(budget)));
    }
    
    public void setreTable(String n){
        setupTable();
    }
    
    public void setUID(String name){
        username = name;
        System.out.println("UID: " + username);
        getShoplistID();
    }
    
    public void getShoplistID(){
        try {        
            Class.forName("com.mysql.cj.jdbc.Driver");
            conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/market_products","root","");
            
            pst = conn.prepareStatement("SELECT max(shoplistID) as shoplistid FROM user_budget WHERE username=?;");
            pst.setString(1, this.username);
            rs = pst.executeQuery();
            
            // get shoplistID = most recent week
            while(rs.next()){
                shoplistID = rs.getString("shoplistid");
            }
            
            System.out.println("id: " + shoplistID);
            ControllerUtil.setshoplistID(shoplistID);
                    
            if(shoplistID == null){
                JOptionPane.showMessageDialog(null, "null");
                return;
            }
               
            // get budget 
            pst = conn.prepareStatement("SELECT * FROM `user_budget` WHERE shoplistID=?;");
            pst.setString(1, shoplistID);
            rs = pst.executeQuery();
            
            while(rs.next()){
                budget = Double.parseDouble(rs.getString("budget"));
                week = rs.getString("week");
            }
            System.out.println("budget: " + budget);
            
        } catch (ClassNotFoundException | SQLException ex) {
            Logger.getLogger(Home.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        setupTable();
    }
    
    private void setupTable() {
        ControllerUtil.prodlist.clear();
        ObservableList<ShoppingList> list = FXCollections.observableArrayList();
        sum=0d;
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/market_products","root","");
            pst = conn.prepareStatement("SELECT * FROM `shopping_list` WHERE shoplistID=?;");
            pst.setString(1, shoplistID);
            rs = pst.executeQuery();
            
            while(rs.next()){
                String data1 = rs.getString("prodName");
                Double data2 = Double.parseDouble(rs.getString("qty"));
                Double data3 = rs.getDouble("total");
                list.add(new ShoppingList(data1,data2,data3));
                sum += data3;
                ControllerUtil.prodlist.add(data1);
            }
            tableList.setItems(list);
            lblWeek.setText(new String((week.split(" "))[1]));
            lblBudget.setText(currencyFormat.format(budget));
            lblExpense.setText(currencyFormat.format(sum));
            lblBalance.setText(currencyFormat.format(budget-sum));
            
            if((budget-sum) < 0){
                TilePane r = new TilePane();
                Alert a = new Alert(Alert.AlertType.NONE);
                a.setContentText("Balance is below 0.");
                a.setAlertType(Alert.AlertType.WARNING);
                a.show();
            }
            
            pst = conn.prepareStatement("UPDATE user_budget SET expenses=? WHERE shoplistID=?;");
            pst.setString(1, sum.toString());
            pst.setString(2, shoplistID);
            pst.executeUpdate();
        } catch (SQLException | ClassNotFoundException ex) {
            Logger.getLogger(Home.class.getName()).log(Level.SEVERE, null, ex);
        } 
    }

    @FXML
    private void tableClicked(MouseEvent event) {
        if(event.getClickCount() > 1){
            if(tableList.getSelectionModel().getSelectedItem() != null){
                ShoppingList selectedrow = tableList.getSelectionModel().getSelectedItem();
                
                String name = selectedrow.getItem();
                Double qty  = selectedrow.getQuantity();
                Double price = selectedrow.getPrice();
                
                System.out.println(name + " " + qty + " " + price);
                
                ControllerUtil.setRemoveItem(name);
                ControllerUtil.showRemoveItem();
            }
        }
    }

    @FXML
    private void buttonClicked(ActionEvent event) {
        if(event.getSource() == btnDeleteAll){
            try {
                // System.out.println("hi");
                String shoplistID = ControllerUtil.getshoplistID();
                
                Class.forName("com.mysql.cj.jdbc.Driver");
                
                pst = conn.prepareStatement("DELETE FROM `shopping_list` WHERE shoplistID=?;");
                pst.setString(1,shoplistID);
                pst.executeUpdate();
                
            } catch (ClassNotFoundException ex) {
                Logger.getLogger(Home.class.getName()).log(Level.SEVERE, null, ex);
            } catch (SQLException ex) {
                Logger.getLogger(Home.class.getName()).log(Level.SEVERE, null, ex);
            }

            setupTable();
        }
        
        if(event.getSource() == btnWeek){

            int splitWeek = Integer.parseInt(week.split(" ")[1]) + 1;
            String newWeek = "Week "+splitWeek;
            System.out.println(newWeek);
            
            try {
                Class.forName("com.mysql.cj.jdbc.Driver");
                
                pst = conn.prepareStatement("INSERT into user_budget (username,week,budget,expenses) VALUES (?,?,\"0\",\"0\");");
                pst.setString(1, username);
                pst.setString(2, newWeek);
                pst.executeUpdate();
            } catch (ClassNotFoundException | SQLException ex) {
                Logger.getLogger(Home.class.getName()).log(Level.SEVERE, null, ex);
            }
            ControllerUtil.showHome();
        }
    }

    @FXML
    private void comboClicked(ActionEvent event) {
        System.out.println(comboSort.getSelectionModel().getSelectedItem().toString());
        String val = comboSort.getSelectionModel().getSelectedItem().toString();

        
        ObservableList<ShoppingList> list = FXCollections.observableArrayList();
        if(val.equals("Name")){
            try {
                Class.forName("com.mysql.cj.jdbc.Driver");

                pst = conn.prepareStatement("SELECT * FROM `shopping_list` WHERE shoplistID=? order by prodName;");
                pst.setString(1, shoplistID);
                rs = pst.executeQuery();

                while(rs.next()){
                    String data1 = rs.getString("prodName");
                    Double data2 = Double.parseDouble(rs.getString("qty"));
                    Double data3 = rs.getDouble("total");
                    list.add(new ShoppingList(data1,data2,data3));
                }
                tableList.setItems(list);
            } catch (ClassNotFoundException | SQLException ex) {
                Logger.getLogger(Home.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        else{
            try {
                Class.forName("com.mysql.cj.jdbc.Driver");

                pst = conn.prepareStatement("SELECT * FROM `shopping_list` WHERE shoplistID=? order by total;");
                pst.setString(1, shoplistID);
                rs = pst.executeQuery();

                while(rs.next()){
                    String data1 = rs.getString("prodName");
                    Double data2 = Double.parseDouble(rs.getString("qty"));
                    Double data3 = rs.getDouble("total");
                    list.add(new ShoppingList(data1,data2,data3));
                }
                tableList.setItems(list);
            } catch (ClassNotFoundException | SQLException ex) {
                Logger.getLogger(Home.class.getName()).log(Level.SEVERE, null, ex);
            }            
        }

    }
}
