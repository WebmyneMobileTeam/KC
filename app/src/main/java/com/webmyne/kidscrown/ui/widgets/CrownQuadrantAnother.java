package com.webmyne.kidscrown.ui.widgets;

import android.content.Context;
import android.graphics.PorterDuff;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import com.webmyne.kidscrown.R;
import com.webmyne.kidscrown.model.QuadSheet;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by dhruvil on 14-08-2015.
 */
public class CrownQuadrantAnother extends LinearLayout implements View.OnClickListener {

    private TableLayout tableLayout;
    private TableRow row1;
    private TableRow row2;
    private QuadSheet sheet;
    public HashMap<String, String> orderMap;
    private ArrayList<String> selectedArray;
    private OnCrownClickListner onCrownClickListner;
    private LinearLayout linearParentQuad;
    private boolean isAllAdded = false;


    public CrownQuadrantAnother(Context context) {
        super(context);
        init(context);
    }

    public CrownQuadrantAnother(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }
    private void init(Context context) {
        View.inflate(context, R.layout.crown_grid_another, this);
        tableLayout = (TableLayout) findViewById(R.id.tableLayout);
        row1 = (TableRow) tableLayout.getChildAt(0);
        row2 = (TableRow) tableLayout.getChildAt(1);
        linearParentQuad = (LinearLayout) findViewById(R.id.linearParentQuad);
        //linearParentQuad.setOnClickListener(this);
        sheet = new QuadSheet();
        orderMap = new HashMap<>();
        selectedArray = new ArrayList<>();

    }

    public ArrayList<String> getSelectedArray() {
        return selectedArray;
    }

    public void setupRows(int res, int color, final String[] arr1, final String[] arr2) {

        for (int i = 0; i < row1.getChildCount(); i++) {

            final View view = row1.getChildAt(i);
            TextView txtDetails = (TextView) view.findViewById(R.id.txtDetails);
            TextView txtCrown = (TextView) view.findViewById(R.id.txtCrown);
            LinearLayout viewCB = (LinearLayout) view.findViewById(R.id.viewCB);
            // EditText edtQty = (EditText) view.findViewById(R.id.edtQty);
            viewCB.setBackgroundResource(res);
            viewCB.getBackground().setColorFilter(color, PorterDuff.Mode.SRC_ATOP);
            final String name = arr1[i];
            txtDetails.setText(name);
            String string = String.format("%c%c", arr1[i].charAt(0), arr1[i].charAt(arr1[i].length() - 1));
            txtCrown.setText(string);
            txtCrown.setPadding(8, 8, 8, 8);

            final LinearLayout itemLinear = (LinearLayout) view.findViewById(R.id.itemLinear);

            view.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {

                    if (selectedArray.contains(name)) {
                        onCrownClickListner.display(name);
                        highlightSelected(name);

                    } else {
                        itemLinear.setBackgroundResource(R.drawable.crown_bg_selected);
                        selectedArray.add(name);
                        onCrownClickListner.add(name);
                        onCrownClickListner.display(name);
                        highlightSelected(name);
                    }

                }
            });

        }

        for (int i = 0; i < row2.getChildCount(); i++) {

            final View view = row2.getChildAt(i);
            TextView txtDetails = (TextView) view.findViewById(R.id.txtDetails);
            TextView txtCrown = (TextView) view.findViewById(R.id.txtCrown);
            LinearLayout viewCB = (LinearLayout) view.findViewById(R.id.viewCB);
            //EditText edtQty = (EditText) view.findViewById(R.id.edtQty);
            viewCB.setBackgroundResource(res);
            viewCB.getBackground().setColorFilter(color, PorterDuff.Mode.SRC_ATOP);
            final String name = arr2[i];
            txtDetails.setText(name);
            String string = String.format("%c%c", arr2[i].charAt(0), arr2[i].charAt(arr2[i].length() - 1));
            txtCrown.setText(string);
            txtCrown.setPadding(8, 8, 8, 8);
            final LinearLayout itemLinear = (LinearLayout) view.findViewById(R.id.itemLinear);
            view.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {

                    if (selectedArray.contains(name)) {
                        onCrownClickListner.display(name);
                        highlightSelected(name);
                    } else {
                        itemLinear.setBackgroundResource(R.drawable.crown_bg_selected);
                        selectedArray.add(name);
                        onCrownClickListner.add(name);
                        onCrownClickListner.display(name);
                        highlightSelected(name);

                    }
                }
            });

        }

    }

    public void highlightSelected(String value) {

        for (int i = 0; i < row1.getChildCount(); i++) {

            final View view = row1.getChildAt(i);
            TextView txtDetails = (TextView) view.findViewById(R.id.txtDetails);
            final LinearLayout itemLinear = (LinearLayout) view.findViewById(R.id.itemLinear);
            if (txtDetails.getText().toString().equalsIgnoreCase(value)) {
                itemLinear.setBackgroundResource(R.drawable.crown_bg_selected_colored);
            } else {

            }
            if (selectedArray.contains(value)) {

            } else {

                itemLinear.setBackgroundResource(R.drawable.crown_bg);
            }

        }

        for (int i = 0; i < row2.getChildCount(); i++) {
            final View view = row2.getChildAt(i);
            TextView txtDetails = (TextView) view.findViewById(R.id.txtDetails);
            final LinearLayout itemLinear = (LinearLayout) view.findViewById(R.id.itemLinear);
            if (txtDetails.getText().toString().equalsIgnoreCase(value)) {

                itemLinear.setBackgroundResource(R.drawable.crown_bg_selected_colored);
            } else {

            }
            if (selectedArray.contains(value)) {

            } else {
                itemLinear.setBackgroundResource(R.drawable.crown_bg);
            }
        }

    }

    public void clearSelected(){
        selectedArray.clear();
    }

    public void setQuanity(final String crown, String value) {

        selectedArray.add(crown);

        for (int i = 0; i < row1.getChildCount(); i++) {

            final View view = row1.getChildAt(i);
            TextView txtDetails = (TextView) view.findViewById(R.id.txtDetails);
            TextView txtCrown = (TextView) view.findViewById(R.id.txtCrown);
            final LinearLayout itemLinear = (LinearLayout) view.findViewById(R.id.itemLinear);
            final String name = txtDetails.getText().toString();
            if (name.equals(crown)) {
                itemLinear.setBackgroundResource(R.drawable.crown_bg_selected);
                txtCrown.setText(value);
            }
        }

        for (int i = 0; i < row2.getChildCount(); i++) {
            final View view = row2.getChildAt(i);
            TextView txtDetails = (TextView) view.findViewById(R.id.txtDetails);
            TextView txtCrown = (TextView) view.findViewById(R.id.txtCrown);
            final LinearLayout itemLinear = (LinearLayout) view.findViewById(R.id.itemLinear);
            final String name = txtDetails.getText().toString();
            if (name.equals(crown)) {
                itemLinear.setBackgroundResource(R.drawable.crown_bg_selected);
                txtCrown.setText(value);
            }
        }

    }

    public void addCrown(String value) {

                 for (int i = 0; i < row1.getChildCount(); i++) {
                final View view = row1.getChildAt(i);
                TextView txtDetails = (TextView) view.findViewById(R.id.txtDetails);
                final String name = txtDetails.getText().toString();
                selectedArray.add(name);

            }

            for (int i = 0; i < row2.getChildCount(); i++) {
                final View view = row2.getChildAt(i);
                TextView txtDetails = (TextView) view.findViewById(R.id.txtDetails);
                final String name = txtDetails.getText().toString();
                selectedArray.add(name);

        }

    }

    public void removeSelected(String value) {

        if (selectedArray.contains(value)) {
            selectedArray.remove(value);

            for (int i = 0; i < row1.getChildCount(); i++) {

                final View view = row1.getChildAt(i);
                TextView txtDetails = (TextView) view.findViewById(R.id.txtDetails);
                TextView txtCrown = (TextView) view.findViewById(R.id.txtCrown);
                final LinearLayout itemLinear = (LinearLayout) view.findViewById(R.id.itemLinear);

                if (txtDetails.getText().toString().equalsIgnoreCase(value)) {
                    itemLinear.setBackgroundResource(R.drawable.crown_bg);
                    String string = String.format("%c%c", txtDetails.getText().toString().charAt(0), txtDetails.getText().toString().charAt(txtDetails.getText().toString().length() - 1));
                    txtCrown.setText(string);
                    txtCrown.setPadding(8, 8, 8, 8);
                }

            }

            for (int i = 0; i < row2.getChildCount(); i++) {
                final View view = row2.getChildAt(i);
                TextView txtDetails = (TextView) view.findViewById(R.id.txtDetails);
                TextView txtCrown = (TextView) view.findViewById(R.id.txtCrown);
                final LinearLayout itemLinear = (LinearLayout) view.findViewById(R.id.itemLinear);

                if (txtDetails.getText().toString().equalsIgnoreCase(value)) {
                    itemLinear.setBackgroundResource(R.drawable.crown_bg);
                    String string = String.format("%c%c", txtDetails.getText().toString().charAt(0), txtDetails.getText().toString().charAt(txtDetails.getText().toString().length() - 1));
                    txtCrown.setText(string);
                    txtCrown.setPadding(8, 8, 8, 8);
                }

            }
        }

    }


/*
    public HashMap<String, String> getValues() {

        for (int i = 0; i < row1.getChildCount(); i++) {
            View view = row1.getChildAt(i);
            TextView txtDetails = (TextView) view.findViewById(R.id.txtDetails);
            EditText edtQty = (EditText) view.findViewById(R.id.edtQty);

            if (!edtQty.getText().toString().isEmpty()) {
                orderMap.put(txtDetails.getText().toString(), edtQty.getText().toString());
            }

        }

        for (int i = 0; i < row2.getChildCount(); i++) {
            View view = row2.getChildAt(i);
            TextView txtDetails = (TextView) view.findViewById(R.id.txtDetails);
            EditText edtQty = (EditText) view.findViewById(R.id.edtQty);

            if (!edtQty.getText().toString().isEmpty()) {
                orderMap.put(txtDetails.getText().toString(), edtQty.getText().toString());
            }

        }

        return orderMap;

    }
*/

    public void setOnCrownClickListner(OnCrownClickListner listner) {
        this.onCrownClickListner = listner;
    }

    public void setUpperLeft() {
        setupRows(R.drawable.ul, getResources().getColor(R.color.quad_blue), sheet.getUl1(), sheet.getUl2());
    }

    public void setUpperRight() {
        setupRows(R.drawable.ur, getResources().getColor(R.color.quad_orange), sheet.getUr1(), sheet.getUr2());
    }

    public void setLowerLeft() {
        setupRows(R.drawable.ll, getResources().getColor(R.color.quad_green), sheet.getLl1(), sheet.getLl2());
    }

    public void setLowerRight() {
        setupRows(R.drawable.lr, getResources().getColor(R.color.quad_violate), sheet.getLr1(), sheet.getLr2());
    }

    @Override
    public void onClick(View v) {

        if (isAllAdded == false) {

            for (int i = 0; i < row1.getChildCount(); i++) {

                final View view = row1.getChildAt(i);
                TextView txtDetails = (TextView) view.findViewById(R.id.txtDetails);
                final LinearLayout itemLinear = (LinearLayout) view.findViewById(R.id.itemLinear);
                String name = txtDetails.getText().toString();
                itemLinear.setBackgroundResource(R.drawable.crown_bg_selected);
                selectedArray.add(name);
                onCrownClickListner.add(name);

            }

            for (int i = 0; i < row2.getChildCount(); i++) {
                final View view = row2.getChildAt(i);
                TextView txtDetails = (TextView) view.findViewById(R.id.txtDetails);
                final LinearLayout itemLinear = (LinearLayout) view.findViewById(R.id.itemLinear);
                String name = txtDetails.getText().toString();
                itemLinear.setBackgroundResource(R.drawable.crown_bg_selected);
                selectedArray.add(name);
                onCrownClickListner.add(name);
            }

            isAllAdded = true;

        }

    }

    public interface OnCrownClickListner {

        public void add(String value);

        public void delete(String value);

        public void display(String value);


    }


}
