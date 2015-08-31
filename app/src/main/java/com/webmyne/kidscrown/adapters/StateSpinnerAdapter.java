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
import com.webmyne.kidscrown.model.StateModel;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Android on 14-07-2015.
 */
public class StateSpinnerAdapter extends BaseAdapter implements SpinnerAdapter {
    LayoutInflater layoutInflator;
    public List<StateModel> stateList = new ArrayList<StateModel>();
    Context ctx;
    int mainSpinnerView, dropSpinnerView;

    public StateSpinnerAdapter(Context context, List<StateModel> list, int mainView, int dropView) {
        this.stateList = list;
        ctx = context;
        mainSpinnerView = mainView;
        dropSpinnerView = dropView;

    }

    public int getCount() {
        return stateList.size();
    }

    public Object getItem(int i) {
        return stateList.get(i);
    }

    public long getItemId(int i) {
        return (long) i;
    }


    @Override
    public View getDropDownView(int position, View convertView, ViewGroup parent) {

        layoutInflator = (LayoutInflater) ctx.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view = convertView;

        view = layoutInflator.inflate(dropSpinnerView, parent, false);

        TextView txt = (TextView) view.findViewById(R.id.spID);

        txt.setPadding(6, 12, 12, 12);
        txt.setTextSize(ctx.getResources().getDimension(R.dimen.SPINNER_TEXT));
        txt.setGravity(Gravity.CENTER_VERTICAL);
        txt.setText(stateList.get(position).state_name);

        return view;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup viewgroup) {

        layoutInflator = (LayoutInflater) ctx.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view = convertView;

        view = layoutInflator.inflate(mainSpinnerView, viewgroup, false);

        TextView txt = (TextView) view.findViewById(R.id.spID);

        txt.setGravity(Gravity.CENTER_VERTICAL);
        txt.setPadding(2, 12, 12, 12);
        txt.setTextSize(ctx.getResources().getDimension(R.dimen.SPINNER_TEXT));
        txt.setText(stateList.get(position).state_name);
        return view;
    }
}