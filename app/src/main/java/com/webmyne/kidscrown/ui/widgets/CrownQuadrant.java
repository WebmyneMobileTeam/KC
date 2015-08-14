package com.webmyne.kidscrown.ui.widgets;

import android.content.Context;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.util.AttributeSet;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import com.webmyne.kidscrown.R;
import com.webmyne.kidscrown.model.QuadSheet;

import java.util.HashMap;

/**
 * Created by dhruvil on 14-08-2015.
 */
public class CrownQuadrant extends LinearLayout {

    private TableLayout tableLayout;
    private TableRow row1;
    private TableRow row2;
    private QuadSheet sheet;
    public HashMap<String,String> orderMap;

    public CrownQuadrant(Context context) {
        super(context);
        init(context);
    }

    public CrownQuadrant(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);

    }

    private void init(Context context) {
        View.inflate(context, R.layout.crown_grid, this);
        tableLayout = (TableLayout) findViewById(R.id.tableLayout);
        row1 = (TableRow) tableLayout.getChildAt(0);
        row2 = (TableRow) tableLayout.getChildAt(1);
        sheet = new QuadSheet();
        orderMap = new HashMap<>();
    }


    public void setupRows(int res, int color, String[] arr1, String[] arr2) {

        for (int i = 0; i < row1.getChildCount(); i++) {
            View view = row1.getChildAt(i);
            TextView txtDetails = (TextView) view.findViewById(R.id.txtDetails);
            TextView txtCrown = (TextView) view.findViewById(R.id.txtCrown);
            EditText edtQty = (EditText) view.findViewById(R.id.edtQty);
            edtQty.setBackgroundResource(res);
            edtQty.getBackground().setColorFilter(color, PorterDuff.Mode.SRC_ATOP);
            txtDetails.setText(arr1[i]);
            String string = String.format("%c%c", arr1[i].charAt(0), arr1[i].charAt(arr1[i].length() - 1));
            txtCrown.setText(string);
        }

        for (int i = 0; i < row2.getChildCount(); i++) {
            View view = row2.getChildAt(i);
            TextView txtDetails = (TextView) view.findViewById(R.id.txtDetails);
            TextView txtCrown = (TextView) view.findViewById(R.id.txtCrown);
            EditText edtQty = (EditText) view.findViewById(R.id.edtQty);
            edtQty.setBackgroundResource(res);
            edtQty.getBackground().setColorFilter(color, PorterDuff.Mode.SRC_ATOP);
            txtDetails.setText(arr2[i]);
            String string = String.format("%c%c", arr2[i].charAt(0), arr2[i].charAt(arr2[i].length() - 1));
            txtCrown.setText(string);
        }

    }

    public HashMap<String,String> getValues(){

        for (int i = 0; i < row1.getChildCount(); i++) {
            View view = row1.getChildAt(i);
            TextView txtDetails = (TextView) view.findViewById(R.id.txtDetails);
            TextView txtCrown = (TextView) view.findViewById(R.id.txtCrown);
            EditText edtQty = (EditText) view.findViewById(R.id.edtQty);

            if(!edtQty.getText().toString().isEmpty()){
                orderMap.put(txtDetails.getText().toString(),edtQty.getText().toString());
            }

        }

        for (int i = 0; i < row2.getChildCount(); i++) {
            View view = row2.getChildAt(i);
            TextView txtDetails = (TextView) view.findViewById(R.id.txtDetails);
            TextView txtCrown = (TextView) view.findViewById(R.id.txtCrown);
            EditText edtQty = (EditText) view.findViewById(R.id.edtQty);

            if(!edtQty.getText().toString().isEmpty()){
                orderMap.put(txtDetails.getText().toString(),edtQty.getText().toString());
            }

        }

        return orderMap;

    }


    public void setUpperLeft() {
        setupRows(R.drawable.left_bottom_border, Color.BLUE, sheet.getUl1(), sheet.getUl2());
    }

    public void setUpperRight() {
        setupRows(R.drawable.right_bottom_border, Color.YELLOW, sheet.getUr1(), sheet.getUr2());
    }

    public void setLowerLeft() {
        setupRows(R.drawable.left_top_border, Color.GREEN, sheet.getLl1(), sheet.getLl2());
    }

    public void setLowerRight() {
        setupRows(R.drawable.left_bottom_border, Color.MAGENTA, sheet.getLr1(), sheet.getLr2());
    }


}
