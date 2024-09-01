package org.example;

import java.net.URL;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.NumberFormat;
import java.time.format.DateTimeFormatter;
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
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.ToggleButton;
import javafx.scene.control.ToggleGroup;
import javafx.scene.image.ImageView;

public class ExpensesViewMore implements Initializable {
    private String week;
    private Double sum;
    private String shoplistID;
    private Double budget;
    private Double expenses;
    
    Connection conn;
    PreparedStatement pst;
    ResultSet rs; 

    @FXML private ToggleButton btnExpenses, btnHome, btnMarketPrice;

    @FXML private ImageView btnReturn;

    @FXML private ComboBox<String> comboSort;

    @FXML private Label lblBalance, lblBudget, lblDate, lblExpense;

    @FXML private ToggleGroup navbar;

    @FXML private TableView<ShoppingList> tableList;

    private final TableColumn<ShoppingList, String> colItem = new TableColumn<>("Name");
    private final TableColumn<ShoppingList, Number> colQuantity = new TableColumn<>("Quantity");
    private final TableColumn<ShoppingList, Number> colPrice = new TableColumn<>("Price");
    private final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MM/dd/yyyy");
    private final NumberFormat currencyFormat = NumberFormat.getCurrencyInstance(new Locale("EN", "PH"));
    private ObservableList<ShoppingList> list = FXCollections.observableArrayList();
    private Expense selectedExpense;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        navbar.selectedToggleProperty().addListener((obsVal, oldVal, newVal) -> {if (newVal == null) oldVal.setSelected(true);});
        btnReturn.setOnMouseClicked(mouseEvent -> ControllerUtil.showExpenses());
        getShoplistID();
        setupTable();

        ObservableList<ShoppingList> list = FXCollections.observableArrayList();
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            pst = conn.prepareStatement("SELECT * FROM `shopping_list` WHERE shoplistID=?;");
            pst.setString(1, shoplistID);
            rs = pst.executeQuery();
            
            while(rs.next()){
                String data1 = rs.getString("prodName");
                Double data2 = Double.parseDouble(rs.getString("qty"));
                Double data3 = rs.getDouble("total");
                list.add(new ShoppingList(data1,data2,data3));
            }
            tableList.setItems(list);
        } catch (SQLException ex) { 
            Logger.getLogger(ExpensesViewMore.class.getName()).log(Level.SEVERE, null, ex);
        } catch (ClassNotFoundException ex) {
            Logger.getLogger(ExpensesViewMore.class.getName()).log(Level.SEVERE, null, ex);
        }

        //ComboBox for Sort by
        ObservableList<String> sort = FXCollections.observableArrayList("Descending Price", "Ascending Price");
        comboSort.setItems(sort);
        comboSort.getSelectionModel().selectFirst();
        
        String week[] = ControllerUtil.getWeek().split(" ");
        lblDate.setText(week[1]);
    }

    public void setSelectedExpense(String week) {
        this.week = week;
        lblDate.setText(week); 
        getShoplistID();
    }

    private void setupTable() {
        tableList.getColumns().addAll(colItem, colQuantity, colPrice);
        tableList.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
        tableList.getColumns().forEach(column -> {column.setReorderable(false); column.setResizable(false); column.setSortable(false);});
        colItem.prefWidthProperty().bind(tableList.widthProperty().multiply(0.33));
        colQuantity.prefWidthProperty().bind(tableList.widthProperty().multiply(0.32));
        colPrice.prefWidthProperty().bind(tableList.widthProperty().multiply(0.32));

        colItem.setCellValueFactory(cell -> new SimpleStringProperty(cell.getValue().getItem()));
        colQuantity.setCellValueFactory(cell -> new SimpleDoubleProperty(cell.getValue().getQuantity()));
        colPrice.setCellValueFactory(cell -> new SimpleDoubleProperty(cell.getValue().getPrice()));
        
        colItem.setSortable(true);
        colQuantity.setSortable(true);
        colPrice.setSortable(true);

        NumberFormat currencyFormat = NumberFormat.getCurrencyInstance(new Locale("EN", "PH"));
        colPrice.setCellFactory(column ->
            new TableCell<ShoppingList, Number>() {
                @Override
                public void updateItem(Number price, boolean empty) {
                    super.updateItem(price, empty);
                    setText(empty? null : currencyFormat.format(price));
                }
        });
    }

    private void getShoplistID(){
        System.out.println(">"+ControllerUtil.getUID());
        System.out.println(">"+ControllerUtil.getWeek());
        try {        
            Class.forName("com.mysql.cj.jdbc.Driver");
            conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/market_products","root","");
            
            pst = conn.prepareStatement("SELECT * FROM user_budget WHERE username=? AND week=?;");
            pst.setString(1, ControllerUtil.getUID());
            pst.setString(2, ControllerUtil.getWeek());
            rs = pst.executeQuery();
            
            while(rs.next()){
                shoplistID = rs.getString("shoplistID");
                budget = rs.getDouble("budget");
                expenses = rs.getDouble("expenses");
            }
                    
            if(shoplistID == null){
                JOptionPane.showMessageDialog(null, "null");
                return;
            }
               
            lblBudget.setText(currencyFormat.format(budget));
            lblExpense.setText(currencyFormat.format(expenses));
            lblBalance.setText(currencyFormat.format(budget-expenses));
        } catch (ClassNotFoundException | SQLException ex) {
            Logger.getLogger(Home.class.getName()).log(Level.SEVERE, null, ex);
        }
        
    }
    @FXML
    void showHome(ActionEvent event) {
        ControllerUtil.showHome();
    }

    @FXML
    void showMarketPrice(ActionEvent event) {
        ControllerUtil.showMarketPrice();
    }

}
