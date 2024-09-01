package org.example;

public class Item {

    private String name;
    private String unit;
    private double price;
    private String cat;

    public Item(String name, String unit, double price, String cat) {
        this.name = name;
        this.unit = unit;
        this.price = price;
        this.cat = cat;
    }

    public String getName() {
        return name;
    }

    public String getUnit() {
        return unit;
    }

    public double getPrice() {
        return price;
    }

    public String getCat() {
        return cat;
    }
    
}
