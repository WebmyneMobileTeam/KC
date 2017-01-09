package com.webmyne.kidscrown.helper;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.webmyne.kidscrown.R;
import com.webmyne.kidscrown.model.OrderModel;

import java.text.DecimalFormat;

/**
 * Created by sagartahelyani on 05-01-2017.
 */

public class OfferView extends LinearLayout {

    private Context context;
    private LayoutInflater inflater;
    private View parentView;
    private TextView txtTextView, txtValue;
    String key;
    int value;
    private DecimalFormat formatter;

    public OfferView(Context context, String key, int value) {
        super(context);
        this.context = context;
        this.key = key;
        this.value = value;
        if (!isInEditMode()) {
            init();
        }
    }

    private void init() {
        inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        parentView = inflater.inflate(R.layout.offer_row, this);
        txtTextView = (TextView) parentView.findViewById(R.id.text);
        txtValue = (TextView) parentView.findViewById(R.id.value);
        formatter = new DecimalFormat("#,##,###");
        setDetails(key, value);

    }

    public void setDetails(String key, int value) {
        txtTextView.setText(key);
        txtValue.setText(String.format("%s %s", context.getString(R.string.Rs), formatter.format(value)));
    }
}
