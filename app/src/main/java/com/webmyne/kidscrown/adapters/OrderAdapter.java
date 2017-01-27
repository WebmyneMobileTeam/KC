package com.webmyne.kidscrown.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.webmyne.kidscrown.R;
import com.webmyne.kidscrown.model.OrderHistoryModel;

import java.text.DecimalFormat;
import java.util.ArrayList;

/**
 * Created by sagartahelyani on 24-09-2015.
 */
public class OrderAdapter extends RecyclerView.Adapter<OrderAdapter.ProductHolder> {

    Context context;
    ArrayList<OrderHistoryModel> data;
    private DecimalFormat formatter;

    public OrderAdapter(Context context, ArrayList<OrderHistoryModel> data) {
        this.context = context;
        this.data = data;
        formatter = new DecimalFormat("#,##,###");
    }

    @Override
    public ProductHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.order_item, parent, false);
        return new ProductHolder(view);
    }

    @Override
    public void onBindViewHolder(ProductHolder holder, int position) {
        OrderHistoryModel order = data.get(position);
        holder.setOrders(order, position);
    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    public void setOrders(ArrayList<OrderHistoryModel> data) {
        this.data = new ArrayList<>();
        this.data = data;
        notifyDataSetChanged();
    }

    class ProductHolder extends RecyclerView.ViewHolder {

        TextView txtOrderId, txtOrderDate, txtAmount, txtPaymentStatus, txtShipping;
        RelativeLayout layout1, layout2;

        private ProductHolder(View itemView) {
            super(itemView);
            txtOrderId = (TextView) itemView.findViewById(R.id.txtOrderId);
            txtOrderDate = (TextView) itemView.findViewById(R.id.txtOrderDate);
            txtAmount = (TextView) itemView.findViewById(R.id.txtPayable);
            txtPaymentStatus = (TextView) itemView.findViewById(R.id.txtPaymentStatus);
            txtShipping = (TextView) itemView.findViewById(R.id.txtShipping);

            layout1 = (RelativeLayout) itemView.findViewById(R.id.layout1);
            layout2 = (RelativeLayout) itemView.findViewById(R.id.layout2);

            layout1.setVisibility(View.GONE);
            layout2.setVisibility(View.GONE);
        }

        private void setOrders(OrderHistoryModel oreder, int position) {

            txtOrderId.setText("Order No. " + oreder.getOrderNumber());
            txtOrderDate.setText(oreder.getOrderDate());
            txtAmount.setText(context.getResources().getString(R.string.Rs) + " " + formatter.format(oreder.getPayableAmount()));

            if (oreder.isPaymentComplete()) {
                txtPaymentStatus.setText("Payment Done");
            } else {
                txtPaymentStatus.setText("Payment Pending");
            }

            if (oreder.getTaxAmount() == 0) {
                txtShipping.setText("Shipping Charge " + context.getResources().getString(R.string.Rs) + " 0");
            } else {
                txtShipping.setText("Shipping Charge " + context.getResources().getString(R.string.Rs) + " " + oreder.getTaxAmount());
            }

        }
    }

}

//    Context context;
//    ArrayList<OrderProduct> data;
//    LayoutInflater inflater;
//    private DecimalFormat formatter;
//
//    public OrderAdapter(Context context, ArrayList<OrderProduct> data) {
//        this.context = context;
//        this.data = data;
//        formatter = new DecimalFormat("#,##,###");
//    }
//
//    @Override
//    public int getCount() {
//        return data.size();
//    }
//
//    @Override
//    public Object getItem(int position) {
//        return position;
//    }
//
//    @Override
//    public long getItemId(int position) {
//        return 0;
//    }
//
//    @Override
//    public View getView(int position, View convertView, ViewGroup parent) {
//        ViewHolder holder;
//        if (convertView == null) {
//            inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
//            convertView = inflater.inflate(R.layout.order_item, parent, false);
//            holder = new ViewHolder();
//            holder.txtOrderId = (TextView) convertView.findViewById(R.id.txtOrderId);
//            holder.txtOrderDate = (TextView) convertView.findViewById(R.id.txtOrderDate);
//            holder.txtAmount = (TextView) convertView.findViewById(R.id.txtPayable);
//            holder.txtPaymentStatus = (TextView) convertView.findViewById(R.id.txtPaymentStatus);
//            holder.txtShipping = (TextView) convertView.findViewById(R.id.txtShipping);
//
//            holder.layout1 = (RelativeLayout) convertView.findViewById(R.id.layout1);
//            holder.layout2 = (RelativeLayout) convertView.findViewById(R.id.layout2);
//
//            holder.layout1.setVisibility(View.GONE);
//            holder.layout2.setVisibility(View.GONE);
//
//            convertView.setTag(holder);
//        } else {
//            holder = (ViewHolder) convertView.getTag();
//        }
//
//        holder.txtOrderId.setText("Order No. " + data.get(position).OrderNumber);
//        holder.txtOrderDate.setText(data.get(position).OrderDate);
//        holder.txtAmount.setText(context.getResources().getString(R.string.Rs) + " " + formatter.format(data.get(position).PayableAmount));
//
//        if (data.get(position).IsPaymentComplete) {
//            holder.txtPaymentStatus.setText("Payment Done");
//        } else {
//            holder.txtPaymentStatus.setText("Payment Pending");
//        }
//
//        if (data.get(position).TaxAmount==0) {
//            holder.txtShipping.setText("Shipping Charge " + context.getResources().getString(R.string.Rs) + " 0");
//        } else {
//            holder.txtShipping.setText("Shipping Charge " + context.getResources().getString(R.string.Rs) + " " + data.get(position).TaxAmount);
//        }
//
//        return convertView;
//    }
//
//    class ViewHolder {
//        TextView txtOrderId, txtOrderDate, txtAmount, txtPaymentStatus, txtShipping;
//        RelativeLayout layout1, layout2;
//    }
//}
