package com.webmyne.kidscrown.ui.widgets;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.webmyne.kidscrown.R;
import com.webmyne.kidscrown.model.OrderProduct;

/**
 * Created by sagartahelyani on 08-09-2015.
 */
public class MyOrderItemView extends LinearLayout {

    Context context;
    LayoutInflater inflater;
    OrderProduct orderObject;
    View view;
    TextView txtOrderId, txtOrderDate, txtTotalAmount, txtPaymentStatus, txtShipping, txtSaved, txtPayable;

    RelativeLayout layout1, layout2;

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

        layout1 = (RelativeLayout) view.findViewById(R.id.layout1);
        layout2 = (RelativeLayout) view.findViewById(R.id.layout2);
        txtPayable = (TextView) view.findViewById(R.id.txtPayable);
        txtSaved = (TextView) view.findViewById(R.id.txtSaved);
        txtShipping = (TextView) view.findViewById(R.id.txtShipping);
        txtOrderId = (TextView) view.findViewById(R.id.txtOrderId);
        txtOrderDate = (TextView) view.findViewById(R.id.txtOrderDate);
        txtTotalAmount = (TextView) view.findViewById(R.id.txtTotalAmount);
        txtPaymentStatus = (TextView) view.findViewById(R.id.txtPaymentStatus);

        setDetails();

    }

    private void setDetails() {
        txtOrderId.setText("Order : " + orderObject.OrderNumber);

        txtOrderId.setText(orderObject.OrderNumber);
        txtOrderDate.setText(orderObject.OrderDate);

        if (orderObject.DiscountPercent == 0) {

            int t = Integer.parseInt(orderObject.TotalAmount) + Integer.parseInt(orderObject.TaxAmount);
            if (orderObject.TaxAmount.equals("0")) {
                txtTotalAmount.setText(context.getResources().getString(R.string.Rs) + " " + t);
            } else {
                txtTotalAmount.setText(context.getResources().getString(R.string.Rs) + " " + t + " (" + orderObject.TotalAmount + " + " + orderObject.TaxAmount + " Shipping Cost)");
            }
            layout1.setVisibility(GONE);
            layout2.setVisibility(GONE);

        } else {
            layout1.setVisibility(VISIBLE);
            layout2.setVisibility(VISIBLE);
            int t = Integer.parseInt(orderObject.TotalAmount) + Integer.parseInt(orderObject.TaxAmount);
            if (orderObject.TaxAmount.equals("0")) {
                txtTotalAmount.setText(context.getResources().getString(R.string.Rs) + " " + t);
            } else {
                txtTotalAmount.setText(context.getResources().getString(R.string.Rs) + " " + t + " (" + orderObject.TotalAmount + " + " + orderObject.TaxAmount + " Shipping Cost)");
            }

            // txtTotalAmount.setText(context.getResources().getString(R.string.Rs) + " " + (Integer.parseInt(orderObject.PayableAmount) + orderObject.DiscountPercent));
        }

        txtPayable.setText(context.getResources().getString(R.string.Rs) + " " + orderObject.PayableAmount);

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

        txtSaved.setText(context.getResources().getString(R.string.Rs) + " " + orderObject.DiscountPercent);

    }

}
