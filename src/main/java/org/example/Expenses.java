package org.example;

import java.sql.*;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;

import java.net.URL;
import java.text.NumberFormat;
import java.time.format.DateTimeFormatter;
import java.util.Locale;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;

public class Expenses implements Initializable {
    
    private Double totalExpenses;
     private Double totalBudget;

    @FXML private ToggleButton btnExpenses, btnHome, btnMarketPrice;

    @FXML private ComboBox<String> comboSort;

    @FXML private Label lblSavedMoney, lblBudget, lblExpense;

    @FXML private ToggleGroup navbar;

    @FXML private TableView<Expense> tableExpense;

    //private final TableColumn<Expense, LocalDate> colDate = new TableColumn<>("Date");
    private final TableColumn<Expense, String> colDate = new TableColumn<>("Week");
    private final TableColumn<Expense, Number> colTotalExpenses = new TableColumn<>("Total Expenses"); //Need na number to, di ko rin alam bakit XD
    private final ObservableList<Expense> list= FXCollections.observableArrayList();//Sample List para lng matanggal yung error vasdvnkadv
    private final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MM/dd/yyyy");
    NumberFormat currencyFormat = NumberFormat.getCurrencyInstance(new Locale("EN", "PH"));

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        //Make sure that there's always a selected button
        navbar.selectedToggleProperty().addListener((obsVal, oldVal, newVal) -> {if (newVal == null) oldVal.setSelected(true);});


        //ComboBox for Sort by
        ObservableList<String> sort = FXCollections.observableArrayList("Descending Expenses", "Ascending Expenses", "Latest", "Oldest");
        comboSort.setItems(sort);
        comboSort.getSelectionModel().selectFirst();

        totalExpenses = 0d;
        totalBudget = 0d;
        
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            Connection conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/market_products","root","");
            
            PreparedStatement pst = conn.prepareStatement("SELECT * FROM `user_budget` WHERE username = ?;");
            pst.setString(1, ControllerUtil.getUID());
            ResultSet rs = pst.executeQuery();
            
            while(rs.next()){
                String week = rs.getString("week");
                Double expenses = rs.getDouble("expenses");
                Double budget = rs.getDouble("budget");
                list.add(new Expense(week,expenses));
                totalExpenses += expenses;
                totalBudget += budget;
            }

        lblBudget.setText(currencyFormat.format(totalBudget));
        lblExpense.setText(currencyFormat.format(totalExpenses));
        lblSavedMoney.setText(currencyFormat.format(totalBudget-totalExpenses));
            
        } catch (ClassNotFoundException | SQLException ex) {
            Logger.getLogger(Expenses.class.getName()).log(Level.SEVERE, null, ex);
        }

        //Table Setup
        setupTable(); 
        tableExpense.setItems(list); //lagay laman sa table
        //LocalDate date = calendar.getValue();
    }

    @FXML
    void showHome(ActionEvent event) {
        ControllerUtil.showHome();
    }

    @FXML
    void showMarketPrice(ActionEvent event) {
        ControllerUtil.showMarketPrice();
    }

    private void setupTable() {
        tableExpense.getColumns().addAll(colDate, colTotalExpenses);
        tableExpense.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
        tableExpense.getColumns().forEach(column -> {column.setReorderable(false); column.setResizable(false); column.setSortable(false);});
        colDate.prefWidthProperty().bind(tableExpense.widthProperty().multiply(0.48));
        colTotalExpenses.prefWidthProperty().bind(tableExpense.widthProperty().multiply(0.49));
        colDate.setSortable(true);
        colTotalExpenses.setSortable(true);
        
        colDate.setCellValueFactory(cell -> new SimpleStringProperty(cell.getValue().getWeek()));
        colTotalExpenses.setCellValueFactory(cell -> new SimpleDoubleProperty(cell.getValue().getExpenses()));

        NumberFormat currencyFormat = NumberFormat.getCurrencyInstance(new Locale("EN", "PH"));
        colTotalExpenses.setCellFactory(column ->
            new TableCell<Expense, Number>() {
                @Override
                public void updateItem(Number price, boolean empty) {
                    super.updateItem(price, empty);
                    setText(empty? null : currencyFormat.format(price));
                }
        });

        //Double click row
        tableExpense.setRowFactory(view -> {
            TableRow<Expense> row = new TableRow<>();
            row.setOnMouseClicked(event -> {
                if (event.getClickCount() == 2 && (!(row.isEmpty()))) {
                    Expense selectedrow = tableExpense.getSelectionModel().getSelectedItem();

                    String week = selectedrow.getWeek();
                    ControllerUtil.setWeek(week);
                    ControllerUtil.showViewMore();
                    
                }
            });
            return row;
        });
    }

    private void setupSort () {
        tableExpense.getSortOrder().addListener(new ListChangeListener<TableColumn<Expense, ?>>() {
            @Override
            public void onChanged(Change<? extends TableColumn<Expense, ?>> change) {
                while(change.next()) {
                    if (change.wasPermutated()) {
                        TableColumn<Expense, ?> first = change.getList().get(0);
                        String tableColumnName = first.getText();
                        ComboBox<String> box = comboSort;
                        tableExpense.getSortOrder().get(0).textProperty().bindBidirectional(box.valueProperty());
                    }
                }
            }
        });
    }

}
