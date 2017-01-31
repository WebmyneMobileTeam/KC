package com.webmyne.kidscrown.model;

import java.io.Serializable;

/**
 * Created by sagartahelyani on 30-01-2017.
 */

public class ProductSlab implements Serializable {

    /**
     * Columns : 1
     * ModelName : D7
     * ModelNumber : D-UL-7
     * Position : UL
     * ProductSpecID : 3
     * Rows : 1
     */

    private int Columns;
    private String ModelName;
    private String ModelNumber;
    private String Position;
    private int ProductSpecID;
    private int Rows;

    public int getColumns() {
        return Columns;
    }

    public void setColumns(int Columns) {
        this.Columns = Columns;
    }

    public String getModelName() {
        return ModelName;
    }

    public void setModelName(String ModelName) {
        this.ModelName = ModelName;
    }

    public String getModelNumber() {
        return ModelNumber;
    }

    public void setModelNumber(String ModelNumber) {
        this.ModelNumber = ModelNumber;
    }

    public String getPosition() {
        return Position;
    }

    public void setPosition(String Position) {
        this.Position = Position;
    }

    public int getProductSpecID() {
        return ProductSpecID;
    }

    public void setProductSpecID(int ProductSpecID) {
        this.ProductSpecID = ProductSpecID;
    }

    public int getRows() {
        return Rows;
    }

    public void setRows(int Rows) {
        this.Rows = Rows;
    }
}
