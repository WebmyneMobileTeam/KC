package com.webmyne.kidscrown.adapters;

import android.content.Context;
import android.content.SharedPreferences;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.webmyne.kidscrown.R;
import com.webmyne.kidscrown.model.ProductCart;

import java.util.ArrayList;

/**
 * Created by sagartahelyani on 26-08-2015.
 */
public class CustomGridAdapter extends BaseAdapter {
    private Context context;
    private ArrayList<ProductCart> crowns;
    LayoutInflater inflater;
    SharedPreferences preferences;

    public CustomGridAdapter(Context context, ArrayList<ProductCart> crownNames) {
        this.context = context;
        this.crowns = crownNames;
        inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public int getCount() {
        return crowns.size();
    }

    @Override
    public Object getItem(int position) {
        return position;
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;
        if (convertView == null) {
            holder = new ViewHolder();
            convertView = inflater.inflate(R.layout.crown_grid_item, null);
            holder.txtCrownName = (TextView) convertView.findViewById(R.id.txtCrownName);
            holder.txtCrownPricing = (TextView) convertView.findViewById(R.id.txtCrownPrice);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        int crownProductId = 0;
        preferences = context.getSharedPreferences("login", Context.MODE_PRIVATE);
        crownProductId = preferences.getInt("crownProductId", 0);

        if (crowns.get(position).getProductId() == crownProductId) {
            holder.txtCrownName.setText(crowns.get(position).getProductName());
            holder.txtCrownPricing.setText("Qty " + crowns.get(position).getProductQty() + " x Rs. " + crowns.get(position).getProductUnitPrice() + " = Rs. " + crowns.get(position).getProductTotalPrice());
        }
        return convertView;
    }

    public class ViewHolder {
        TextView txtCrownName;
        TextView txtCrownPricing;
    }
}
