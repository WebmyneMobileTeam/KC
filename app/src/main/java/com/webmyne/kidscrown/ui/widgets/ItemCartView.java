package com.webmyne.kidscrown.ui.widgets;

import android.content.Context;
import android.content.Intent;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.webmyne.kidscrown.R;
import com.webmyne.kidscrown.helper.DatabaseHandler;
import com.webmyne.kidscrown.helper.Functions;
import com.webmyne.kidscrown.model.ProductCart;
import com.webmyne.kidscrown.ui.CartActivity;

import java.sql.SQLException;
import java.util.ArrayList;

/**
 * Created by sagartahelyani on 24-08-2015.
 */
public class ItemCartView extends LinearLayout {

    LayoutInflater inflater;
    View view;
    Context context;
    ProductCart cart;
    TextView txtName, unitPrice, unitQty, totalPrice;
    LinearLayout remove;
    ComboSeekBar combo;
    FrameLayout cartProductFrame;

    ArrayList<String> values;
    OnValueChangeListener onValueChangeListener;

    public ItemCartView(Context context) {
        super(context);
        this.context = context;
        init();
    }

    public ItemCartView(Context context, ProductCart cart) {
        super(context);
        this.context = context;
        this.cart = cart;
        init();
    }

    public void setOnValueChangeListener(OnValueChangeListener onValueChangeListener) {
        this.onValueChangeListener = onValueChangeListener;
    }

    public ItemCartView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        this.context = context;
        init();
    }

    private void init() {
        inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        view = inflater.inflate(R.layout.cart_product, this);
        setOrientation(VERTICAL);

        txtName = (TextView) view.findViewById(R.id.txtName);
        remove = (LinearLayout) view.findViewById(R.id.remove);
        unitPrice = (TextView) view.findViewById(R.id.unitPrice);
        unitQty = (TextView) view.findViewById(R.id.unitQty);
        totalPrice = (TextView) view.findViewById(R.id.totalPrice);
        combo = (ComboSeekBar) view.findViewById(R.id.combo);


        cartProductFrame = (FrameLayout) view.findViewById(R.id.cartProductFrame);

        setDetails(cart);

        remove.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    DatabaseHandler handler = new DatabaseHandler(context);
                    handler.openDataBase();
                    handler.deleteCartProduct(cart.getProductId());
                    Intent cartIntent = new Intent(context, CartActivity.class);
                    cartIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    context.startActivity(cartIntent);
                    handler.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    private void setDetails(ProductCart cart) {
        txtName.setText(cart.getProductName());
        unitPrice.setText("Rs. " + cart.getProductUnitPrice());
        unitQty.setText("x " + cart.getProductQty() + " QTY");
        totalPrice.setText("= Rs. " + cart.getProductTotalPrice());

        int max = cart.getMaxQty();
        values = new ArrayList<>();
        for (int i = 1; i <= max; i++) {
            values.add("" + i);
        }
        combo.setAdapter(values);
        combo.setColor(R.color.quad_green);
        combo.setBackgroundResource(0);
        combo.setSelection(cart.getProductQty()-1);
        displayQTYandTotal(cart.getProductQty());
        combo.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                displayQTYandTotal(position);
                onValueChangeListener.onChange();
            }
        });
    }

    private void displayQTYandTotal(int position) {
        unitQty.setText(String.format("x %s QTY", values.get(position)));
        int qty = Integer.parseInt(values.get(position-1));
        int total = Integer.parseInt(cart.getProductUnitPrice()) * qty;
        totalPrice.setText(String.format("= Rs. %d", total));

        // update database values
        try {
            DatabaseHandler handler = new DatabaseHandler(context);
            handler.openDataBase();
            handler.updateCart(qty, total, cart.getProductId());
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public interface OnValueChangeListener{
        public void onChange();
    }

    public void hideControls() {
        remove.setVisibility(GONE);
        combo.setVisibility(GONE);
        cartProductFrame.setPadding(0,0,0,20);
    }
}
