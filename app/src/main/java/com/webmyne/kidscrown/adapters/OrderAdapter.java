package com.webmyne.kidscrown.adapters;

import android.content.Context;
import android.support.v4.app.FragmentActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.webmyne.kidscrown.R;
import com.webmyne.kidscrown.model.OrderProduct;

import java.util.ArrayList;

/**
 * Created by sagartahelyani on 24-09-2015.
 */
public class OrderAdapter extends BaseAdapter {

    Context context;
    ArrayList<OrderProduct> data;
    LayoutInflater inflater;

    public OrderAdapter(Context context, ArrayList<OrderProduct> data) {
        this.context = context;
        this.data = data;
    }

    @Override
    public int getCount() {
        return data.size();
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
            inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.order_item, parent, false);
            holder = new ViewHolder();
            holder.txtOrderId = (TextView) convertView.findViewById(R.id.txtOrderId);
            holder.txtOrderDate = (TextView) convertView.findViewById(R.id.txtOrderDate);
            holder.txtAmount = (TextView) convertView.findViewById(R.id.txtAmount);
            holder.txtPaymentStatus = (TextView) convertView.findViewById(R.id.txtPaymentStatus);
            holder.txtShipping = (TextView) convertView.findViewById(R.id.txtShipping);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        holder.txtOrderId.setText("Order No. " + data.get(position).OrderNumber);
        holder.txtOrderDate.setText(data.get(position).OrderDate);
        holder.txtAmount.setText(context.getResources().getString(R.string.Rs) + " " + data.get(position).PayableAmount);

        if (data.get(position).IsPaymentComplete) {
            holder.txtPaymentStatus.setText("Payment Done");
        } else {
            holder.txtPaymentStatus.setText("Payment Pending");
        }

        if (data.get(position).TaxAmount.equals("")) {
            holder.txtShipping.setText("Shipping Charge " + context.getResources().getString(R.string.Rs) + " 0");
        } else {
            holder.txtShipping.setText("Shipping Charge " + context.getResources().getString(R.string.Rs) + " " + data.get(position).TaxAmount);
        }

        return convertView;
    }

    class ViewHolder {
        TextView txtOrderId, txtOrderDate, txtAmount, txtPaymentStatus, txtShipping;
    }
}
