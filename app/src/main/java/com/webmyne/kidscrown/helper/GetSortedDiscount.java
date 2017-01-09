package com.webmyne.kidscrown.helper;

import android.content.Context;
import android.util.Log;

import com.webmyne.kidscrown.model.DiscountModel;

import java.util.ArrayList;

/**
 * Created by sagartahelyani on 02-01-2017.
 */

public class GetSortedDiscount {

    private DatabaseHandler handler;
    private Context context;
    private ArrayList<DiscountModel> discountModels;
    private boolean isOffer = false;

    public GetSortedDiscount(Context context) {
        this.context = context;
        handler = new DatabaseHandler(context);
        discountModels = handler.getOffers();
    }

    public DiscountModel getOffer(String productId) {
        int index = -1;

        for (int i = 0; i < discountModels.size(); i++) {

            if (discountModels.get(i).ProductID.equals(productId)) {
                index = i;
                break;
            }
        }
        if (index == -1) {
            return null;
        } else {
            return discountModels.get(index);
        }
    }

    public boolean isOffer() {
        for (int i = 0; i < discountModels.size(); i++) {
            Log.e("offer", discountModels.get(i).DiscountPercentage);
            if (!discountModels.get(i).DiscountPercentage.equals("0.00")) {
                isOffer = true;
            }
        }
        return isOffer;
    }
}
