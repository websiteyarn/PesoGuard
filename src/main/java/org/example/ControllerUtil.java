package org.example;

import java.io.IOException;
import java.util.ArrayList;

import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Modality;
import javafx.stage.Stage;

public final class ControllerUtil {

    public static ArrayList<String> prodlist = new ArrayList<>();
    private static String week;
    private static String shoplistID;
    private static String removeitem;
    private static String chosenitem;
    private static Double price;
    private static String username;
    private static final Stage STAGE = new Stage();
    private static final Image ICON = new Image(ControllerUtil.class.getResourceAsStream("/org/example/icon_appdev.png"));

    public static Stage getStage() {
        return STAGE;
    }

    public static void start() {
        STAGE.setResizable(false);
        STAGE.setTitle("PesoGuard");
        STAGE.getIcons().add(ICON);
        showWelcome();
    }

    public static void showHome() {
        try {
            FXMLLoader loader = new FXMLLoader(ControllerUtil.class.getResource("/org/example/Home.fxml"));
            Scene scene = new Scene(loader.load());
            //scene.getStylesheets().add(ControllerUtil.class.getResource("/org/example/css/style.css").toExternalForm());
            STAGE.setScene(scene);
            STAGE.show();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public static void showAddItem() {
        try {
            Stage popup = new Stage();
            popup.initModality(Modality.APPLICATION_MODAL);
            popup.initOwner(STAGE);
            popup.setResizable(false);
            popup.setTitle("Add Items");
            popup.getIcons().add(ICON);
            FXMLLoader loader = new FXMLLoader(ControllerUtil.class.getResource("/org/example/AddItem.fxml"));
            Scene scene = new Scene(loader.load());
            popup.setScene(scene);
            popup.show();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public static void showEditBudget() {
        try {
            Stage popup = new Stage();
            popup.initModality(Modality.APPLICATION_MODAL);
            popup.initOwner(STAGE);
            popup.setResizable(false);
            popup.setTitle("Edit Budget");
            popup.getIcons().add(ICON);
            FXMLLoader loader = new FXMLLoader(ControllerUtil.class.getResource("/org/example/editBudget.fxml"));
            Scene scene = new Scene(loader.load());
            popup.setScene(scene);
            popup.show();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public static void showRemoveItem() {
        try {
            Stage popup = new Stage();
            popup.initModality(Modality.APPLICATION_MODAL);
            popup.initOwner(STAGE);
            popup.setResizable(false);
            popup.setTitle("Remove Item");
            popup.getIcons().add(ICON);
            FXMLLoader loader = new FXMLLoader(ControllerUtil.class.getResource("/org/example/RemoveItem.fxml"));
            Scene scene = new Scene(loader.load());
            popup.setScene(scene);
            popup.show();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
    
    public static void setRemoveItem(String name){
        removeitem = name;
    }
    public static String getRemoveItem(){
        return removeitem;
    }

    
    public static void setshoplistID(String id){
        shoplistID = id;
    }
    public static String getshoplistID(){
        return shoplistID;
    }
    
    public static void showAddQuantity(Scene parent) {
        try {
            Stage popup = new Stage();
            popup.initModality(Modality.APPLICATION_MODAL);
            popup.initOwner(parent.getWindow());
            popup.setResizable(false);
            popup.setTitle("Enter Item Quantity");
            popup.getIcons().add(ICON);
            FXMLLoader loader = new FXMLLoader(ControllerUtil.class.getResource("/org/example/AddQuantity.fxml"));
            Scene scene = new Scene(loader.load());
            popup.setScene(scene);
            popup.show();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public static void showExpenses() {
        try {
            FXMLLoader loader = new FXMLLoader(ControllerUtil.class.getResource("/org/example/Expenses.fxml"));
            Scene scene = new Scene(loader.load());
            //scene.getStylesheets().add(ControllerUtil.class.getResource("/org/example/css/style.css").toExternalForm());
            STAGE.setScene(scene);
            STAGE.show();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public static void showViewMore() { // Expense expense
        try {
            FXMLLoader loader = new FXMLLoader(ControllerUtil.class.getResource("/org/example/Expenses-ViewMore.fxml"));
            Scene scene = new Scene(loader.load());
            STAGE.setScene(scene);
            STAGE.show();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public static void showMarketPrice() {
        try {
            FXMLLoader loader = new FXMLLoader(ControllerUtil.class.getResource("/org/example/MarketPrice.fxml"));
            Scene scene = new Scene(loader.load());
            STAGE.setScene(scene);
            STAGE.show();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public static void showSignUp() {
        try {
            FXMLLoader loader = new FXMLLoader(ControllerUtil.class.getResource("/org/example/SignUp.fxml"));
            Scene scene = new Scene(loader.load());
            STAGE.setScene(scene);
            STAGE.show();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public static void showLogIn() {
        try {
            FXMLLoader loader = new FXMLLoader(ControllerUtil.class.getResource("/org/example/LogIn.fxml"));
            Scene scene = new Scene(loader.load());
            STAGE.setScene(scene);
            STAGE.show();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public static void showWelcome() {
        try {
            FXMLLoader loader = new FXMLLoader(ControllerUtil.class.getResource("/org/example/Welcome.fxml"));
            Scene scene = new Scene(loader.load());
            STAGE.setScene(scene);
            STAGE.show();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public static void exit() {
        STAGE.close();
    }

    public static void setUID(String uid){
        username = uid;
        System.out.println("2UID: " + username);
    }
    
    public static String getUID(){
        return username;
    }
    
    public static void addItem(String x, Double y){
        chosenitem = x;
        price = y;
    }
    
    public static String getChosenItem(){
        return chosenitem;
    }
    
    public static Double getPrice(){
        return price;
    }
    
    
    public static void setWeek(String w){
        week = w;
    }
    
    public static String getWeek(){
        return week;
    }
}
