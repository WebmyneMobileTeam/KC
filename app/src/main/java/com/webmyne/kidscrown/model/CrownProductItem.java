package com.webmyne.kidscrown.model;

/**
 * Created by sagartahelyani on 26-08-2015.
 */
public class CrownProductItem {

    public String itemName;

    public int itemQty;

    public int specificId;

    public CrownProductItem(String itemName, int itemQty, int specificId) {
        this.itemName = itemName;
        this.itemQty = itemQty;
        this.specificId = specificId;
    }
}
