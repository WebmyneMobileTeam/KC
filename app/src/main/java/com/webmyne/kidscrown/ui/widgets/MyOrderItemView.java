package com.webmyne.kidscrown.ui.widgets;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.webmyne.kidscrown.R;
import com.webmyne.kidscrown.helper.Functions;
import com.webmyne.kidscrown.model.FinalOrders;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.Random;

/**
 * Created by sagartahelyani on 08-09-2015.
 */
public class MyOrderItemView extends LinearLayout {

    Context context;
    LayoutInflater inflater;
    LinearLayout parentLayout;
    View view;
    String orderId;
    TextView txtOrderId, txtOrderDate, txtAmount, txtPaymentStatus;
    ArrayList<FinalOrders> myOrders;
    String[] paymentMethods = new String[]{"Credit Card", "PayUMoney", "Debit Card", "Net Banking"};
    private Random random = new Random();


    public MyOrderItemView(Context context) {
        super(context);
        init();
    }

    public MyOrderItemView(Context context, String orderId, ArrayList<FinalOrders> myOrders) {
        super(context);
        this.context = context;
        this.orderId = orderId;
        this.myOrders = myOrders;
        init();
    }

    private void init() {
        inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        view = inflater.inflate(R.layout.order_item, this);
        setOrientation(VERTICAL);

        txtPaymentStatus = (TextView) view.findViewById(R.id.txtPaymentStatus);
        txtOrderId = (TextView) view.findViewById(R.id.txtOrderId);
        txtOrderDate = (TextView) view.findViewById(R.id.txtOrderDate);
        txtAmount = (TextView) view.findViewById(R.id.txtAmount);

        setDetails();

    }

    private void setDetails() {
        int price = 0;
        txtOrderId.setText("Order : " + orderId);
        for (FinalOrders finalOrder : myOrders) {
            if (finalOrder.orderId.equals(orderId)) {
                txtOrderDate.setText(finalOrder.dateTime);
                price += Integer.parseInt(finalOrder.totalPrice);
            }
        }
        txtAmount.setText(context.getResources().getString(R.string.Rs) + price + " ");
        txtPaymentStatus.setText("Payment: " + "Done");
    }

}
