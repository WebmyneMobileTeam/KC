package com.webmyne.kidscrown.adapters;


import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.webmyne.kidscrown.R;
import com.webmyne.kidscrown.model.OrderModel;

import java.util.ArrayList;

/**
 * Created by sagartahelyani on 01-09-2015.
 */

public class OrderListAdapter extends RecyclerView.Adapter<OrderListAdapter.ViewHolder> {

    private Context context;
    private ArrayList<OrderModel> orders;

    public OrderListAdapter(Context context, ArrayList<OrderModel> orders) {
        this.context = context;
        this.orders = orders;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(context).inflate(R.layout.order_row, parent, false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        OrderModel model = orders.get(position);
        holder.setDetails(model);
    }

    @Override
    public int getItemCount() {
        return orders.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        private TextView productName, productQty, productPrice;

        public ViewHolder(View itemView) {
            super(itemView);
            productName = (TextView) itemView.findViewById(R.id.pdName);
            productQty = (TextView) itemView.findViewById(R.id.pdQty);
            productPrice = (TextView) itemView.findViewById(R.id.pdPrice);
        }

        public void setDetails(OrderModel model) {
            productName.setText(model.getProductName());
            productQty.setText(model.getProductQty() + "");

            int total = model.getProductQty() * Integer.parseInt(model.getProductUnitPrice());
            productPrice.setText(context.getResources().getString(R.string.Rs) + " " + total);
        }
    }
}
