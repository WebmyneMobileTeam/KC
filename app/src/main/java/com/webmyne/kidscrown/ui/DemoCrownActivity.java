package com.webmyne.kidscrown.ui;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import com.webmyne.kidscrown.R;

public class DemoCrownActivity extends AppCompatActivity {

    TableLayout tableLayout;
    TableRow tr;
    TableRow.LayoutParams params;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.crown_grid);
        tableLayout = (TableLayout) findViewById(R.id.tableLayout);
        /*tr = new TableRow(this);
        params = new TableRow.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        params.setMargins(6, 6, 6, 6);

        for (int i = 1; i <= 6; i++) {
            View rowItem = new View(DemoCrownActivity.this);
            rowItem = getLayoutInflater().inflate(R.layout.crown_item, tr, false);
            final TextView txtCrown = (TextView) rowItem.findViewById(R.id.txtCrown);
            final EditText edtQty = (EditText) rowItem.findViewById(R.id.edtQty);
            final TextView txtDetails = (TextView) rowItem.findViewById(R.id.txtDetails);
            txtCrown.setText("D" + i);
            txtDetails.setText("D-UL-" + i);
            tr.setLayoutParams(params);
            tr.addView(rowItem);
        }
        tableLayout.addView(tr);

        tr = new TableRow(this);
        params = new TableRow.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        params.setMargins(6,6,6,6);
        for (int j = 1; j <= 6; j++) {
            View rowItem = new View(DemoCrownActivity.this);
            rowItem = getLayoutInflater().inflate(R.layout.crown_item, tr, false);
            final TextView txtCrown = (TextView) rowItem.findViewById(R.id.txtCrown);
            final EditText edtQty = (EditText) rowItem.findViewById(R.id.edtQty);
            final TextView txtDetails = (TextView) rowItem.findViewById(R.id.txtDetails);
            txtCrown.setText("E" + j);
            txtDetails.setText("E-UL-" + j);
            tr.setLayoutParams(params);
            tr.addView(rowItem);
        }
        tableLayout.addView(tr);*/
    }

}
