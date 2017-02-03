package com.webmyne.kidscrown.custom;

import android.content.Context;
import android.graphics.PorterDuff;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.webmyne.kidscrown.R;
import com.webmyne.kidscrown.model.ProductSlab;

/**
 * Created by sagartahelyani on 30-01-2017.
 */

public class CrownItemAnother extends LinearLayout {

    private Context context;
    private View parentView;
    private TextView txtCrown;
    private TextView txtDetails;
    private int image, color;
    private ProductSlab slab;
    private LinearLayout viewCB;

    public CrownItemAnother(Context context, ProductSlab slab, int image, int color) {
        super(context);
        this.context = context;
        this.slab = slab;
        this.image = image;
        this.color = color;
        init();
    }

    private void init() {
        Log.e("slab", slab.getModelName());
        parentView = inflate(context, R.layout.crown_item_another, this);

        viewCB = (LinearLayout) parentView.findViewById(R.id.viewCB);
        txtCrown = (TextView) parentView.findViewById(R.id.txtCrown);
        txtDetails = (TextView) parentView.findViewById(R.id.txtDetails);

        txtCrown.setText(slab.getModelName());
        txtDetails.setText(slab.getModelNumber());
        viewCB.setBackgroundResource(image);
        viewCB.getBackground().setColorFilter(color, PorterDuff.Mode.SRC_ATOP);
    }

    public CrownItemAnother(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.context = context;
        init();
    }
}
