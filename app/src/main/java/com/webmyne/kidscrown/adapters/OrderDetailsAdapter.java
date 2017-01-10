package com.webmyne.kidscrown.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.webmyne.kidscrown.R;
import com.webmyne.kidscrown.model.OrderModel;
import com.webmyne.kidscrown.model.OrderProduct;

import java.util.ArrayList;

/**
 * Created by sagartahelyani on 01-09-2015.
 */

public class OrderDetailsAdapter extends BaseAdapter {

    Context context;
    OrderProduct orderObject;
    LayoutInflater inflater;

    public OrderDetailsAdapter(Context context, OrderProduct orderObject) {
        this.context = context;
        this.orderObject = orderObject;
        this.inflater = LayoutInflater.from(context);
    }

    @Override
    public int getCount() {
        return orderObject.orderProducts.size();
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

        if (orderObject.orderProducts.get(position).ProductNumber == null) {
            holder.productName.setText(orderObject.orderProducts.get(position).ProductName);
        } else {
            holder.productName.setText(orderObject.orderProducts.get(position).ProductName + " - " + orderObject.orderProducts.get(position).ProductNumber);
        }
        holder.productQty.setText(orderObject.orderProducts.get(position).Quantity);
        holder.productPrice.setText(context.getResources().getString(R.string.Rs) + " " + ((orderObject.orderProducts.get(position).Quantity) * (orderObject.orderProducts.get(position).Price)));

        return convertView;
    }

    private class ViewHolder {
        private TextView productName, productQty, productPrice;
    }
}
