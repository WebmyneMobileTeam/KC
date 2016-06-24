package com.webmyne.kidscrown.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.webmyne.kidscrown.R;
import com.webmyne.kidscrown.model.OrderModel;
import com.webmyne.kidscrown.model.ProductCart;
import com.webmyne.kidscrown.ui.ConfirmOrderActivity;

import java.util.ArrayList;

/**
 * Created by sagartahelyani on 01-09-2015.
 */

public class OrderListAdapter extends BaseAdapter {

    Context context;
    ArrayList<OrderModel> orders;
    LayoutInflater inflater;

    public OrderListAdapter(Context context, ArrayList<OrderModel> orders) {
        this.context = context;
        this.orders = orders;
        this.inflater = LayoutInflater.from(context);
    }

    @Override
    public int getCount() {
        return orders.size();
    }

    @Override
    public Object getItem(int position) {
        return position;
    }

    @Override
    public long getItemId(int position) {
        return (long) position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;

        if (convertView == null) {
            holder = new ViewHolder();
            convertView = inflater.inflate(R.layout.order_row, parent, false);
            holder.productName = (TextView) convertView.findViewById(R.id.pdName);
            holder.productQty = (TextView) convertView.findViewById(R.id.pdQty);
            holder.productPrice = (TextView) convertView.findViewById(R.id.pdPrice);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        holder.productName.setText(orders.get(position).getProductName());
        holder.productQty.setText(orders.get(position).getProductQty() + "");

        int total = orders.get(position).getProductQty() * Integer.parseInt(orders.get(position).getProductUnitPrice());
        holder.productPrice.setText(context.getResources().getString(R.string.Rs) + " " + total);

        return convertView;
    }

    private class ViewHolder {
        private TextView productName, productQty, productPrice;
    }
}
