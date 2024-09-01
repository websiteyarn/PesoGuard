package org.example;

import java.util.Objects;

/**
 * For shopping list table from Expenses - View More
 * */
public class ShoppingList {

    private String item;
    private double quantity, price;

    public ShoppingList(String item, double quantity, double price) {
        this.item = item;
        this.price = price;
        this.quantity = quantity;
    }

    public String getItem() {
        return item;
    }

    public void setItem(String item) {
        this.item = item;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public double getQuantity() {
        return quantity;
    }

    public void setQuantity(double quantity) {
        this.quantity = quantity;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ShoppingList that = (ShoppingList) o;
        return Double.compare(that.getQuantity(), getQuantity()) == 0 && Double.compare(that.getPrice(), getPrice()) == 0 && getItem().equals(that.getItem());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getItem(), getQuantity(), getPrice());
    }
}
