package com.webmyne.kidscrown.ui.widgets;

import android.content.Context;
import android.content.DialogInterface;
import android.support.v7.app.AlertDialog;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.webmyne.kidscrown.R;
import com.webmyne.kidscrown.custom.QuantityView;
import com.webmyne.kidscrown.helper.DatabaseHandler;
import com.webmyne.kidscrown.helper.Functions;
import com.webmyne.kidscrown.helper.GetPriceSlab;
import com.webmyne.kidscrown.model.CartProduct;

import java.util.Locale;

/**
 * Created by sagartahelyani on 24-08-2015.
 */
public class ItemCartView extends LinearLayout {

    LayoutInflater inflater;
    View view;
    Context context;

    OnValueChangeListener onValueChangeListener;

    onRemoveProductListener onRemoveProductListener;

    // new
    private CartProduct cartProduct;
    private QuantityView quantityView;
    private TextView txtPriceIndividual, txtName;
    private TextView txtQTY, txtPriceTotal;
    private DatabaseHandler handler;
    private Button btnRemove;

    public ItemCartView(Context context, CartProduct cartProduct) {
        super(context);
        this.context = context;
        this.cartProduct = cartProduct;
        init();
    }

    public void setOnRemoveProductListener(ItemCartView.onRemoveProductListener onRemoveProductListener) {
        this.onRemoveProductListener = onRemoveProductListener;
    }

    public ItemCartView(Context context) {
        super(context);
        this.context = context;
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
        handler = new DatabaseHandler(context);
        inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        view = inflater.inflate(R.layout.cart_product, this);
        setOrientation(VERTICAL);

        btnRemove = (Button) view.findViewById(R.id.btnRemove);
        txtName = (TextView) view.findViewById(R.id.txtName);
        quantityView = (QuantityView) view.findViewById(R.id.quantityView);
        txtPriceIndividual = (TextView) view.findViewById(R.id.txtPriceIndividual);
        txtQTY = (TextView) view.findViewById(R.id.txtQTY);
        txtPriceTotal = (TextView) view.findViewById(R.id.txtPriceTotal);

        setDetails();

        clickListener();
    }

    private void clickListener() {
        btnRemove.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                promptDialog(cartProduct.getProductId());
            }
        });

        quantityView.setOnQtyChangeListener(new QuantityView.OnQtyChangeListener() {
            @Override
            public void onChange(int qty) {
                txtQTY.setText(String.format(Locale.US, "%s %d", " x ", qty));
                int unitPrice = new GetPriceSlab(context).getRelevantPrice(cartProduct.getProductId(), qty).getPrice();
                int totalPrice = unitPrice * qty;
                txtPriceTotal.setText(String.format(Locale.US, " = %s %s", context.getString(R.string.Rs), Functions.priceFormat(totalPrice)));
                handler.updateCart(qty, unitPrice, cartProduct.getProductId());
                if (onValueChangeListener != null) {
                    onValueChangeListener.onChange();
                }
            }
        });
    }

    private void promptDialog(final int productId) {
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(
                context);
        alertDialogBuilder.setTitle("Remove product");
        alertDialogBuilder
                .setMessage("Are you sure want to remove this product from cart?")
                .setCancelable(false)
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        if (onRemoveProductListener != null) {
                            onRemoveProductListener.removeProduct(productId);
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

    private void setDetails() {
        txtName.setText(cartProduct.getProductName());
        quantityView.setQty(cartProduct.getQty());
        quantityView.setMaxLimit(cartProduct.getMax());

        txtPriceIndividual.setText(String.format(Locale.US, "%s %s", context.getString(R.string.Rs), Functions.priceFormat(cartProduct.getUnitPrice())));
        txtQTY.setText(String.format(Locale.US, "%s %d", " x ", cartProduct.getQty()));
        txtPriceTotal.setText(String.format(Locale.US, " = %s %s", context.getString(R.string.Rs), Functions.priceFormat(cartProduct.getTotalPrice())));
    }

    public interface OnValueChangeListener {
        void onChange();
    }

    /* public void hideControls() {
         remove.setVisibility(GONE);
         combo.setVisibility(GONE);
         cartProductFrame.setPadding(0, 0, 0, 20);
     }
 */
    public interface onRemoveProductListener {
        void removeProduct(int productId);
    }
}
