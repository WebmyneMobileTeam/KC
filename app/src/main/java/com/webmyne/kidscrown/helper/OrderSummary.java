package com.webmyne.kidscrown.helper;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.webmyne.kidscrown.R;
import com.webmyne.kidscrown.model.OrderModel;
import com.webmyne.kidscrown.model.OrderProductModel;

import java.text.DecimalFormat;

/**
 * Created by sagartahelyani on 05-01-2017.
 */

public class OrderSummary extends LinearLayout {

    private Context context;
    private LayoutInflater inflater;
    private View parentView;
    private TextView txtProductName, productQty, productPrice;
    private DecimalFormat formatter;

    public OrderSummary(Context context) {
        super(context);
        this.context = context;
        if (!isInEditMode()) {
            init();
        }
    }

    private void init() {
        inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        parentView = inflater.inflate(R.layout.order_row, this);
        txtProductName = (TextView) parentView.findViewById(R.id.pdName);
        productQty = (TextView) parentView.findViewById(R.id.pdQty);
        productPrice = (TextView) parentView.findViewById(R.id.pdPrice);
        formatter = new DecimalFormat("#,##,###");
    }

    public void setDetails(OrderModel orderModel) {
        txtProductName.setText(orderModel.getProductName());
        productQty.setText(orderModel.getProductQty() + "");

        int total = orderModel.getProductQty() * Integer.parseInt(orderModel.getProductUnitPrice());

        productPrice.setText(context.getResources().getString(R.string.Rs) + " " + formatter.format(total));

    }

    public void setDetails(String productName, int quantity, double finalIntroPrice) {
        txtProductName.setText(productName);
        productQty.setText(quantity + "");
        productPrice.setText(context.getResources().getString(R.string.Rs) + " " + formatter.format(finalIntroPrice));
    }
}
