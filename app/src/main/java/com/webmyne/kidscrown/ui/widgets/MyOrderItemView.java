package com.webmyne.kidscrown.ui.widgets;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.webmyne.kidscrown.R;
import com.webmyne.kidscrown.helper.Functions;
import com.webmyne.kidscrown.model.OrderProduct;

import java.text.DecimalFormat;

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
    int totalAm = 0;

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
        Log.e("order", Functions.jsonString(orderObject));

        //  txtOrderId.setText("Order : " + orderObject.OrderNumber);

        txtOrderId.setText(orderObject.OrderNumber);
        txtOrderDate.setText(orderObject.OrderDate);

        int productDiscount = orderObject.IntroDiscount + orderObject.AssortDiscount + orderObject.RefillDiscount;
        int t = Integer.parseInt(orderObject.TotalAmount) + orderObject.TaxAmount + productDiscount;

        int shipping = orderObject.TaxAmount;

        if (orderObject.TotalDiscount == 0) {
            Log.e("tag", "if");
            if (orderObject.TaxAmount == 0) {
                txtTotalAmount.setText(context.getResources().getString(R.string.Rs) + " " + t);
            } else {
                txtTotalAmount.setText(context.getResources().getString(R.string.Rs) + " " + t + " (" + orderObject.TotalAmount + " + " + orderObject.TaxAmount + " Shipping Cost)");
            }
            layout1.setVisibility(GONE);
            layout2.setVisibility(GONE);

        } else {
            Log.e("tag", "else");
            layout1.setVisibility(VISIBLE);
            layout2.setVisibility(VISIBLE);

            int totalAmount = orderObject.PayableAmount + (int) orderObject.TotalDiscount;

            if (orderObject.TaxAmount == 0) {
                Log.e("tag", "TAX 0");
                // txtTotalAmount.setText(context.getResources().getString(R.string.Rs) + " " + totalAmount);
            } else {
                Log.e("tag", "TAX " + orderObject.TaxAmount);
                //txtTotalAmount.setText(context.getResources().getString(R.string.Rs) + " " + totalAmount + " (" + (totalAmount - orderObject.TaxAmount) + " + " + orderObject.TaxAmount + " Shipping Cost)");
            }

        }

        if (orderObject.TaxAmount == 0) {
            txtPayable.setText(context.getResources().getString(R.string.Rs) + " " + orderObject.PayableAmount);
        } else {
            String text = " (" + (orderObject.PayableAmount - shipping) + " + " + shipping + " Shipping Cost)";
            txtPayable.setText(context.getResources().getString(R.string.Rs) + " " + orderObject.PayableAmount + text);
        }

        if (orderObject.IsPaymentComplete) {
            txtPaymentStatus.setText("Payment: Done");
        } else {
            txtPaymentStatus.setText("Payment: Pending");
        }

        if (orderObject.TaxAmount == 0) {
            txtShipping.setText("Shipping Charge " + context.getResources().getString(R.string.Rs) + " 0");
        } else {
            txtShipping.setText("Shipping Charge " + context.getResources().getString(R.string.Rs) + " " + orderObject.TaxAmount);
        }

        txtSaved.setText(String.format("%s %.2f", context.getResources().getString(R.string.Rs),(orderObject.DiscountPercent + productDiscount)));
        //txtSaved.setText(context.getResources().getString(R.string.Rs) + " " + (orderObject.DiscountPercent + productDiscount));

        for (int i = 0; i < orderObject.orderProducts.size(); i++) {
            totalAm += orderObject.orderProducts.get(i).Price * orderObject.orderProducts.get(i).Quantity;
        }

        txtTotalAmount.setText(context.getResources().getString(R.string.Rs) + " " + totalAm);
    }

}
