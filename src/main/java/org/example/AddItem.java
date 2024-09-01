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

import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableRow;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.AnchorPane;

public class AddItem implements Initializable {

    @FXML
    private Button btnSearch;

    @FXML
    private AnchorPane deleteRows;

    @FXML
    private TableView<Item> tableItems;

    @FXML
    private TextField txtSearch;

    private final TableColumn<Item, String> colItem = new TableColumn<>("Name");
    private final TableColumn<Item, String> colCat = new TableColumn<>("Category");
    private final TableColumn<Item, String> colUnit = new TableColumn<>("Unit");
    private final TableColumn<Item, Number> colPrice = new TableColumn<>("Price");

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        ObservableList<Item> list = FXCollections.observableArrayList();
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            Connection conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/market_products","root","");
            
            PreparedStatement pst = conn.prepareStatement("SELECT * FROM product_information;");
            ResultSet rs = pst.executeQuery();
            
            while(rs.next()){
                String data1 = rs.getString("prodName");
                String data4 = rs.getString(("category"));
                String data2 = rs.getString("unitOfMeasure");
                Double data3 = rs.getDouble("presAvePrice");
                
                list.add(new Item(data1,data2,data3,data4));
                tableItems.setItems(list); 
            }
        } catch (SQLException | ClassNotFoundException ex) {
            Logger.getLogger(MarketPrice.class.getName()).log(Level.SEVERE, null, ex);
        } 
        
        tableItems.setItems(list);
        setupTable();
    }

    private void setupTable() {
        tableItems.getColumns().addAll(colItem, colCat, colPrice, colUnit);
        tableItems.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
        tableItems.getColumns().forEach(column -> {column.setReorderable(false); column.setResizable(false); column.setSortable(false);});
        
        colItem.prefWidthProperty().bind(tableItems.widthProperty().multiply(0.30));
        colCat.prefWidthProperty().bind(tableItems.widthProperty().multiply(0.32));
        colUnit.prefWidthProperty().bind(tableItems.widthProperty().multiply(0.20));
        colPrice.prefWidthProperty().bind(tableItems.widthProperty().multiply(0.15));
        

        colItem.setCellValueFactory(cell -> new SimpleStringProperty(cell.getValue().getName()));
        colCat.setCellValueFactory(cell -> new SimpleStringProperty(cell.getValue().getCat()));
        colUnit.setCellValueFactory(cell -> new SimpleStringProperty(cell.getValue().getUnit()));
        colPrice.setCellValueFactory(cell -> new SimpleDoubleProperty(cell.getValue().getPrice()));

        //Para magkaPeso sign yung double value
        NumberFormat currencyFormat = NumberFormat.getCurrencyInstance(new Locale("EN", "PH"));
        colPrice.setCellFactory(column ->
                new TableCell<Item, Number>() {
                    @Override
                    public void updateItem(Number price, boolean empty) {
                        super.updateItem(price, empty);
                        setText(empty? null : currencyFormat.format(price));
                    }
                });

        //Double click row
        tableItems.setRowFactory(view -> {
            TableRow<Item> row = new TableRow<>();
            row.setOnMouseClicked(event -> {
                if (event.getClickCount() == 2 && (!row.isEmpty())) {
                    Item selectedrow = tableItems.getSelectionModel().getSelectedItem();
                    String name = selectedrow.getName();
                    Double price = selectedrow.getPrice();
                    System.out.println(name + " " + price);
                            
                    for(String a : ControllerUtil.prodlist){
                        if(name.equals(a))
                            return;
                    }
                    
                    ControllerUtil.addItem(name, price);
                    ControllerUtil.showAddQuantity(row.getScene());
                }
            });
            return row;
        });
    }

    @FXML
    private void textChanged(KeyEvent event) {
        String str = txtSearch.getText();
        
        try {
            ObservableList<Item> tblList = FXCollections.observableArrayList();
            
            Class.forName("com.mysql.cj.jdbc.Driver");
            Connection conn = DriverManager.getConnection("jdbc:mysql:///market_products","root","");
            
            PreparedStatement pst = conn.prepareStatement("SELECT * FROM `product_information` WHERE prodname LIKE ?;");
            pst.setString(1, "%" + str + "%");
            ResultSet rs = pst.executeQuery();
            
            while(rs.next()){
                String data1 = rs.getString("prodName");
                String data4 = rs.getString("category");
                String data2 = rs.getString("unitOfMeasure");
                Double data3 = rs.getDouble("presAvePrice");
                
                tblList.add(new Item(data1,data2,data3,data4));
                tableItems.setItems(tblList);
            }

        } catch (ClassNotFoundException | SQLException ex) {
            Logger.getLogger(MarketPrice.class.getName()).log(Level.SEVERE, null, ex);
        } 
    }
}
