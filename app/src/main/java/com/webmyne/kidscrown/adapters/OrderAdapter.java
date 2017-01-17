package com.webmyne.kidscrown.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.webmyne.kidscrown.R;
import com.webmyne.kidscrown.model.OrderProduct;

import java.text.DecimalFormat;
import java.util.ArrayList;

/**
 * Created by sagartahelyani on 24-09-2015.
 */
public class OrderAdapter extends BaseAdapter {

    Context context;
    ArrayList<OrderProduct> data;
    LayoutInflater inflater;
    private DecimalFormat formatter;

    public OrderAdapter(Context context, ArrayList<OrderProduct> data) {
        this.context = context;
        this.data = data;
        formatter = new DecimalFormat("#,##,###");
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
            holder.txtAmount = (TextView) convertView.findViewById(R.id.txtPayable);
            holder.txtPaymentStatus = (TextView) convertView.findViewById(R.id.txtPaymentStatus);
            holder.txtShipping = (TextView) convertView.findViewById(R.id.txtShipping);

            holder.layout1 = (RelativeLayout) convertView.findViewById(R.id.layout1);
            holder.layout2 = (RelativeLayout) convertView.findViewById(R.id.layout2);

            holder.layout1.setVisibility(View.GONE);
            holder.layout2.setVisibility(View.GONE);

            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        holder.txtOrderId.setText("Order No. " + data.get(position).OrderNumber);
        holder.txtOrderDate.setText(data.get(position).OrderDate);
        holder.txtAmount.setText(context.getResources().getString(R.string.Rs) + " " + formatter.format(data.get(position).PayableAmount));

        if (data.get(position).IsPaymentComplete) {
            holder.txtPaymentStatus.setText("Payment Done");
        } else {
            holder.txtPaymentStatus.setText("Payment Pending");
        }

        if (data.get(position).TaxAmount==0) {
            holder.txtShipping.setText("Shipping Charge " + context.getResources().getString(R.string.Rs) + " 0");
        } else {
            holder.txtShipping.setText("Shipping Charge " + context.getResources().getString(R.string.Rs) + " " + data.get(position).TaxAmount);
        }

        return convertView;
    }

    class ViewHolder {
        TextView txtOrderId, txtOrderDate, txtAmount, txtPaymentStatus, txtShipping;
        RelativeLayout layout1, layout2;
    }
}
