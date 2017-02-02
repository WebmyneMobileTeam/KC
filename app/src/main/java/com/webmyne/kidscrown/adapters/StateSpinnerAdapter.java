package com.webmyne.kidscrown.adapters;

import android.content.Context;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.webmyne.kidscrown.R;
import com.webmyne.kidscrown.model.CountryResponse;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Android on 14-07-2015.
 */
public class StateSpinnerAdapter extends ArrayAdapter<CountryResponse.DataBean> {

    LayoutInflater inflater;
    public List<CountryResponse.DataBean> stateList = new ArrayList<CountryResponse.DataBean>();
    Context ctx;
    int textViewResourceId;

    public StateSpinnerAdapter(Context context, int textViewResourceId, List<CountryResponse.DataBean> list) {
        super(context, textViewResourceId);
        this.stateList = list;
        ctx = context;
        this.textViewResourceId = textViewResourceId;
        inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    public int getCount() {
        return stateList.size();
    }

    public CountryResponse.DataBean getItem(int i) {
        return stateList.get(i);
    }

    public long getItemId(int i) {
        return (long) i;
    }


    @Override
    public View getDropDownView(int position, View convertView, ViewGroup parent) {
        return getCustomView(position, parent);
    }

    private View getCustomView(int position, ViewGroup parent) {
        View convertView = inflater.inflate(textViewResourceId, parent, false);

        TextView txtItem = (TextView) convertView.findViewById(R.id.spinnerItem);
        txtItem.setText(stateList.get(position).getStateName());

        return convertView;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        return getCustomView(position, parent);
    }
}