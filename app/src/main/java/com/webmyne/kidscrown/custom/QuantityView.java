package com.webmyne.kidscrown.custom;

import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.webmyne.kidscrown.R;
import com.webmyne.kidscrown.helper.Functions;

import java.util.Locale;


/**
 * Created by sagartahelyani on 20-06-2016.
 */
public class QuantityView extends LinearLayout {

    private Context context;
    private View parentView;
    private TextView txtMinus, txtActualQty, txtPlus;
    private int userQty = 1;
    private int orderLimit;

    public void setOnQtyChangeListener(QuantityView.OnQtyChangeListener onQtyChangeListener) {
        OnQtyChangeListener = onQtyChangeListener;
    }

    private OnQtyChangeListener OnQtyChangeListener;

    public QuantityView(Context context) {
        super(context);
        if (!isInEditMode()) {
            this.context = context;
            init();
        }
    }

    public QuantityView(Context context, AttributeSet attrs) {
        super(context, attrs);
        if (!isInEditMode()) {
            this.context = context;
            init();
        }
    }

    public QuantityView(Context context, int qty) {
        super(context);
        if (!isInEditMode()) {
            this.context = context;
            this.userQty = qty;
            init();
        }
    }

    private void init() {
        parentView = inflate(context, R.layout.qty_view, this);
        txtMinus = (TextView) parentView.findViewById(R.id.txtMinus);
        txtActualQty = (TextView) parentView.findViewById(R.id.txtActualQty);
        txtPlus = (TextView) parentView.findViewById(R.id.txtPlus);

        txtActualQty.setText(String.format(Locale.US, "%02d", userQty));

        actionClick();
    }

    private void actionClick() {

        txtPlus.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                if (userQty == orderLimit) {
                    Functions.showToast(context, "Max limit");
                } else {
                    userQty++;
                    txtActualQty.setText(String.format(Locale.US, "%02d", userQty));
                    if (OnQtyChangeListener != null) {
                        OnQtyChangeListener.onChange(userQty);
                    }
                }
            }
        });

        txtMinus.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                if (userQty == 1) {
                    Functions.showToast(context, "Min limit");
                } else {
                    userQty--;
                    txtActualQty.setText(String.format(Locale.US, "%02d", userQty));
                    if (OnQtyChangeListener != null) {
                        OnQtyChangeListener.onChange(userQty);
                    }
                }
            }
        });

        txtActualQty.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                new QuantityDialog(context, userQty, orderLimit, new QuantityDialog.onSubmitListener() {
                    @Override
                    public void onSubmit(int qty) {
                        userQty = qty;
                        txtActualQty.setText(String.format(Locale.US, "%02d", qty));
                        if (OnQtyChangeListener != null) {
                            OnQtyChangeListener.onChange(qty);
                        }
                    }
                }).show();
            }
        });
    }

    public void setMaxLimit(int orderLimit) {
        this.orderLimit = orderLimit;
    }

    public void setQty(int userQty) {
        this.userQty = userQty;
        txtActualQty.setText(String.format(Locale.US, "%02d", userQty));
    }

    public interface OnQtyChangeListener {
        void onChange(int qty);
    }
}
