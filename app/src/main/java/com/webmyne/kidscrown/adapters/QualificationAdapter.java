package com.webmyne.kidscrown.adapters;

import android.content.Context;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.SpinnerAdapter;
import android.widget.TextView;

import com.webmyne.kidscrown.R;
import com.webmyne.kidscrown.helper.PrefUtils;
import com.webmyne.kidscrown.model.Qualification;
import com.webmyne.kidscrown.model.Salutation;
import com.webmyne.kidscrown.ui.RegisterActivity;

import java.util.ArrayList;

/**
 * Created by sagartahelyani on 18-08-2015.
 */
public class QualificationAdapter extends BaseAdapter implements SpinnerAdapter {

    Context context;
    LayoutInflater layoutInflator;
    ArrayList<Qualification> qualifications = new ArrayList<>();

    public QualificationAdapter(Context context, ArrayList<Qualification> qualifications) {
        this.context = context;
        this.qualifications = qualifications;
    }

    @Override
    public int getCount() {
        return qualifications.size();
    }

    @Override
    public Object getItem(int position) {
        return qualifications.get(position);
    }

    @Override
    public long getItemId(int position) {
        return (long) position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        layoutInflator = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view = convertView;

        view = layoutInflator.inflate(R.layout.spinner_layout_transperent, parent, false);

        TextView txt = (TextView) view.findViewById(R.id.spID);
        txt.setGravity(Gravity.CENTER_VERTICAL);
        txt.setPadding(12, 12, 12, 12);
        txt.setTextSize(12);
        txt.setText(qualifications.get(position).CodeValue);
        return view;
    }

    @Override
    public View getDropDownView(int position, View convertView, ViewGroup parent) {
        layoutInflator = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view = convertView;

        view = layoutInflator.inflate(R.layout.spinner_dropview_layout, parent, false);

        TextView txt = (TextView) view.findViewById(R.id.spID);
        txt.setPadding(12, 12, 12, 12);
        txt.setTextSize(12);
        txt.setGravity(Gravity.CENTER_VERTICAL);
        txt.setText(qualifications.get(position).CodeValue);
        return view;
    }
}
