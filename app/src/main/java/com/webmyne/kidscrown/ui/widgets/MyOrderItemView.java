package com.webmyne.kidscrown.ui.widgets;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.webmyne.kidscrown.R;
import com.webmyne.kidscrown.helper.Functions;
import com.webmyne.kidscrown.model.FinalOrders;
import com.webmyne.kidscrown.model.OrderProduct;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.Random;

/**
 * Created by sagartahelyani on 08-09-2015.
 */
public class MyOrderItemView extends LinearLayout {

    Context context;
    LayoutInflater inflater;
    OrderProduct orderObject;
    View view;
    TextView txtOrderId, txtOrderDate, txtAmount, txtPaymentStatus, txtShipping;

    public MyOrderItemView(Context context, OrderProduct orderObject) {
        super(context);
        this.context = context;
        this.orderObject = orderObject;
        init();
    }

    private void init() {
        inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        view = inflater.inflate(R.layout.order_item, this);
        setOrientation(VERTICAL);

        txtShipping = (TextView) view.findViewById(R.id.txtShipping);
        txtOrderId = (TextView) view.findViewById(R.id.txtOrderId);
        txtOrderDate = (TextView) view.findViewById(R.id.txtOrderDate);
        txtAmount = (TextView) view.findViewById(R.id.txtAmount);
        txtPaymentStatus = (TextView) view.findViewById(R.id.txtPaymentStatus);

        setDetails();

    }

    private void setDetails() {
        txtOrderId.setText("Order : " + orderObject.OrderNumber);

        txtOrderId.setText(orderObject.OrderNumber);
        txtOrderDate.setText(orderObject.OrderDate);
        txtAmount.setText(context.getResources().getString(R.string.Rs) + " " + orderObject.PayableAmount);

        if (orderObject.IsPaymentComplete) {
            txtPaymentStatus.setText("Payment: Done");
        } else {
            txtPaymentStatus.setText("Payment: Pending");
        }

        if (orderObject.TaxAmount.equals("")) {
            txtShipping.setText("Shipping Charge " + context.getResources().getString(R.string.Rs) + " 0");
        } else {
            txtShipping.setText("Shipping Charge " + context.getResources().getString(R.string.Rs) + " " + orderObject.TaxAmount);
        }

    }

}
