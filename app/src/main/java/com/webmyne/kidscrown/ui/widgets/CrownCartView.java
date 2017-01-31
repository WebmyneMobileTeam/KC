package com.webmyne.kidscrown.ui.widgets;

import android.content.Context;
import android.content.DialogInterface;
import android.support.v7.app.AlertDialog;
import android.text.Editable;
import android.text.InputFilter;
import android.text.Spanned;
import android.text.TextWatcher;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.webmyne.kidscrown.R;
import com.webmyne.kidscrown.helper.DatabaseHandler;
import com.webmyne.kidscrown.model.ProductCart;

import java.sql.SQLException;
import java.util.ArrayList;

/**
 * Created by sagartahelyani on 24-08-2015.
 */
public class CrownCartView extends LinearLayout {

    LayoutInflater inflater;
    View view;
    Context context;
    ProductCart cart;
    TextView txtName, unitPrice, totalPrice;
    EditText unitQty;
    LinearLayout remove;
    //    ComboSeekBar combo;
    LinearLayout cartProductFrame;

    ArrayList<String> values;

    onRemoveCrownListener onRemoveCrownListener;

    public CrownCartView(Context context) {
        super(context);
        this.context = context;
        init();
    }

    public CrownCartView(Context context, ProductCart cart) {
        super(context);
        this.context = context;
        this.cart = cart;
        init();
    }

    public void setOnRemoveCrownListener(CrownCartView.onRemoveCrownListener onRemoveCrownListener) {
        this.onRemoveCrownListener = onRemoveCrownListener;
    }

    public CrownCartView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        this.context = context;
        init();
    }

    private void init() {
        inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        view = inflater.inflate(R.layout.crown_cart_product, this);
        setOrientation(VERTICAL);

        txtName = (TextView) view.findViewById(R.id.txtName);
        remove = (LinearLayout) view.findViewById(R.id.remove);
        unitPrice = (TextView) view.findViewById(R.id.unitPrice);
        unitQty = (EditText) view.findViewById(R.id.unitQty);
        totalPrice = (TextView) view.findViewById(R.id.totalPrice);

        cartProductFrame = (LinearLayout) view.findViewById(R.id.cartProductFrame);

        setDetails(cart);

        remove.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                promptDialog(cart.getProductName());

            }
        });
    }

    private void promptDialog(final String productName) {
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(
                context);
        alertDialogBuilder.setTitle("Remove product");
        alertDialogBuilder
                .setMessage("Want to remove this product from cart?")
                .setCancelable(false)
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        if (onRemoveCrownListener != null) {
                            onRemoveCrownListener.removeCrown(productName);
                        }
                    }
                })
                .setNegativeButton("No", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.cancel();
                    }
                });
        AlertDialog alertDialog = alertDialogBuilder.create();
        alertDialog.show();

    }

    private void setDetails(ProductCart cart) {
        txtName.setText(cart.getProductName());
        unitPrice.setText("Per Crown Rs. " + cart.getProductUnitPrice());
        unitQty.setText(cart.getProductQty() + "");
        int subTotal = Integer.parseInt(cart.getProductUnitPrice()) * cart.getProductQty();
        totalPrice.setText("Sub Total Rs. " + subTotal);

        final int unitPrice = Integer.parseInt(cart.getProductUnitPrice());
        final String productName = cart.getProductName();
        unitQty.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (s.toString().length() > 0) {
                    unitQty.setFilters(new InputFilter[]{new InputFilterMinMax("1", "100")});
                    try {
                        DatabaseHandler handler = new DatabaseHandler(context);
                        handler.openDataBase();
                        int qty = Integer.parseInt(s.toString());
                        int total = unitPrice * qty;
                        totalPrice.setText("Sub Total Rs. " + total);
                        handler.updateCrownCart(qty, total, productName);
                    } catch (SQLException e) {
                        e.printStackTrace();
                    }
                } else if (s.toString().length() == 0 || s.toString().equals("0") || s.toString().equals("00") || s.toString().equals("000")) {
                    unitQty.setError("Invalid Quantity");
                    totalPrice.setText("Sub Total Rs. 0");
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

    }

    public interface onRemoveCrownListener {
        public void removeCrown(String productName);
    }

    public class InputFilterMinMax implements InputFilter {

        private int min, max;

        public InputFilterMinMax(int min, int max) {
            this.min = min;
            this.max = max;
        }

        public InputFilterMinMax(String min, String max) {
            this.min = Integer.parseInt(min);
            this.max = Integer.parseInt(max);
        }

        @Override
        public CharSequence filter(CharSequence source, int start, int end, Spanned dest, int dstart, int dend) {
            try {
                int input = Integer.parseInt(dest.toString() + source.toString());
                if (isInRange(min, max, input)) {
                    return null;
                }
            } catch (NumberFormatException nfe) {
            }
            return "";
        }

        private boolean isInRange(int a, int b, int c) {
            return b > a ? c >= a && c <= b : c >= b && c <= a;
        }
    }
}
