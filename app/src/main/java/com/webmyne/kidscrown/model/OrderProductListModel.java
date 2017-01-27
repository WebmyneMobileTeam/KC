package com.webmyne.kidscrown.model;

/**
 * Created by vatsaldesai on 25-01-2017.
 */

public class OrderProductListModel {

    /**
     * Price : 1.2678967543233E7
     * ProductID : 9223372036854775807
     * ProductName : String content
     * ProductNumber : String content
     * Quantity : 9223372036854775807
     */

    private double Price;
    private int ProductID;
    private String ProductName;
    private String ProductNumber;
    private int Quantity;

    public double getPrice() {
        return Price;
    }

    public void setPrice(double Price) {
        this.Price = Price;
    }

    public int getProductID() {
        return ProductID;
    }

    public void setProductID(int ProductID) {
        this.ProductID = ProductID;
    }

    public String getProductName() {
        return ProductName;
    }

    public void setProductName(String ProductName) {
        this.ProductName = ProductName;
    }

    public String getProductNumber() {
        return ProductNumber;
    }

    public void setProductNumber(String ProductNumber) {
        this.ProductNumber = ProductNumber;
    }

    public int getQuantity() {
        return Quantity;
    }

    public void setQuantity(int Quantity) {
        this.Quantity = Quantity;
    }
}
