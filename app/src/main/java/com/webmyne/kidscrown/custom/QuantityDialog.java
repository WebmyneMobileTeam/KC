package com.webmyne.kidscrown.custom;

import android.app.Dialog;
import android.content.Context;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;

import com.webmyne.kidscrown.R;
import com.webmyne.kidscrown.helper.Functions;

import java.util.Locale;

/**
 * Created by sagartahelyani on 25-01-2017.
 */

public class QuantityDialog extends Dialog {

    private final Context context;
    private View view;
    private EditText edtQty;
    private Button btnSubmit;
    private Button btnCancel;
    private String str;
    private int qty;
    private int maxLimit;
    private onSubmitListener onSubmitListener;

    public QuantityDialog(final Context context, int qty, int maxLimit, onSubmitListener onSubmitListener) {
        super(context);
        this.context = context;
        this.qty = qty;
        this.maxLimit = maxLimit;
        this.onSubmitListener = onSubmitListener;
        requestWindowFeature(Window.FEATURE_NO_TITLE);

        view = LayoutInflater.from(context).inflate(R.layout.dialog_enter_qty, null);
        setContentView(view);

        this.setCancelable(true);

        init();

    }

    private void init() {
        btnSubmit = (Button) view.findViewById(R.id.btnSubmit);
        btnCancel = (Button) view.findViewById(R.id.btnCancel);
        edtQty = (EditText) view.findViewById(R.id.edtQty);

        edtQty.setText(String.format(Locale.US, "%d", qty));
        edtQty.setSelection(Functions.getStr(edtQty).length());

        WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
        lp.copyFrom(getWindow().getAttributes());
        lp.width = WindowManager.LayoutParams.MATCH_PARENT;
        lp.height = WindowManager.LayoutParams.WRAP_CONTENT;
        lp.gravity = Gravity.CENTER;
        getWindow().setAttributes(lp);

        actionListener();

        setCancelable(false);
        setCanceledOnTouchOutside(true);
    }

    private void actionListener() {

        btnSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Functions.hideKeyPad(context, view);
                if (TextUtils.isEmpty(Functions.getStr(edtQty))) {
                    Functions.showToast(context, "Enter valid quantity");

                } else if (Integer.parseInt(Functions.getStr(edtQty)) > maxLimit) {
                    Functions.showToast(context, "Quantity cannot greater than max limit.");

                } else {
                    if (onSubmitListener != null) {
                        onSubmitListener.onSubmit(Integer.parseInt(Functions.getStr(edtQty)));
                    }
                    dismiss();
                }

            }
        });

        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Functions.hideKeyPad(context, view);
                dismiss();
            }
        });
    }

    interface onSubmitListener {
        public void onSubmit(int qty);
    }

}
