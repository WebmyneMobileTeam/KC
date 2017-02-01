package com.webmyne.kidscrown.helper;

import android.content.Context;

import com.webmyne.kidscrown.model.PriceSlab;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Created by Sagar on 01-02-2017.
 */

public class GetPriceSlab {

    private ArrayList<PriceSlab> prices;
    private ArrayList<PriceSlab> tempPrices;
    private List<Integer> maxies;
    private PriceSlab priceSlab;
    private boolean isPass = false;

    public GetPriceSlab(Context context) {
        DatabaseHandler handler = new DatabaseHandler(context);
        prices = new ArrayList<>();
        prices.addAll(handler.getPriceSlab());
        tempPrices = new ArrayList<>();
        maxies = new ArrayList<>();
    }

    public PriceSlab getRelevantPrice(int productId, int qty) {

        for (int i = 0; i < prices.size(); i++) {
            if (prices.get(i).getProductID() == productId) {
                tempPrices.add(prices.get(i));
            }
        }

        for (int j = 0; j < tempPrices.size(); j++) {
            maxies.add(tempPrices.get(j).getMaxQty());
            if (qty >= tempPrices.get(j).getMinQty() && qty <= tempPrices.get(j).getMaxQty()) {
                priceSlab = tempPrices.get(j);
                isPass = true;
                break;
            } else {
                isPass = false;
            }
        }

        if (!isPass) {
            int tempPos = 0;
            int max = Collections.max(maxies);
            tempPos = maxies.indexOf(Integer.valueOf(max));
            priceSlab = tempPrices.get(tempPos);
        }

        return priceSlab;
    }

}
