package com.example.market.models;

import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.value.ObservableValue;

import java.math.BigDecimal;
import java.util.Date;
import java.util.SimpleTimeZone;

public class Price {
    private int id;
    private String groceryName;
    private String marketName;
    private String division;
    private Date date;
    private double basePrice;
    private double preferredPrice;

    public Price(int id, String groceryName, String marketName, String division, Date date, double basePrice, double preferredPrice) {
        this.id = id;
        this.groceryName = groceryName;
        this.marketName = marketName;
        this.division = division;
        this.date = date;
        this.basePrice = basePrice;
        this.preferredPrice = preferredPrice;
    }

    // Getters and setters
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getGroceryName() {
        return groceryName;
    }

    public void setGroceryName(String groceryName) {
        this.groceryName = groceryName;
    }

    public String getMarketName() {
        return marketName;
    }

    public void setMarketName(String marketName) {
        this.marketName = marketName;
    }

    public String getDivision() {
        return division;
    }

    public void setDivision(String division) {
        this.division = division;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public double getBasePrice() {
        return basePrice;
    }

    public void setBasePrice(double basePrice) {
        this.basePrice = basePrice;
    }

    public double getPreferredPrice() {
        return preferredPrice;
    }

    public void setPreferredPrice(double preferredPrice) {
        this.preferredPrice = preferredPrice;
    }

    // toString method for debugging or printing
    @Override
    public String toString() {
        return "Price{" +
                "id=" + id +
                ", groceryName='" + groceryName + '\'' +
                ", marketName='" + marketName + '\'' +
                ", division='" + division + '\'' +
                ", date=" + date +
                ", basePrice=" + basePrice +
                ", preferredPrice=" + preferredPrice +
                '}';
    }


    public ObservableValue<String> groceryNameProperty() {
        return new SimpleStringProperty(getGroceryName());
    }

    public ObservableValue<String> marketNameProperty() {

        return new SimpleStringProperty(getMarketName());}

    public ObservableValue<String> divisionProperty() {
        return new SimpleStringProperty(getDivision());}

    public ObservableValue<String> dateProperty() {
        return new SimpleStringProperty(getDate().toString());}

    public ObservableValue<Number> basePriceProperty() {
        return new SimpleDoubleProperty(getBasePrice());}

    public ObservableValue<Number> preferredPriceProperty() {
        return new SimpleDoubleProperty(getPreferredPrice());}
}