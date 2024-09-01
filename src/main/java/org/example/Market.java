package org.example;

import javafx.scene.image.ImageView;

public class Market {

    private String prodName;
    private String cat;
    private Double pastPrice;
    private Double presPrice;
    private String unit;
    private ImageView status;

    public Market(String prodName, String cat, Double pastPrice, Double presPrice, String unit, ImageView status) {
        this.prodName = prodName;
        this.cat = cat;
        this.pastPrice = pastPrice;
        this.presPrice = presPrice;
        this.unit = unit;
        this.status = status;
    }
    
    public Market(String prodName, String cat, Double pastPrice, Double presPrice) {
        this.prodName = prodName;
        this.cat = cat;
        this.pastPrice = pastPrice;
        this.presPrice = presPrice;
    }

    public String getProdName() {
        return prodName;
    }

    public String getCat() {
        return cat;
    }

    public Double getPastPrice() {
        return pastPrice;
    }

    public Double getPresPrice() {
        return presPrice;
    }

    public String getUnit() {
        return unit;
    }

    public ImageView getStatus() {
        return status;
    }

}
