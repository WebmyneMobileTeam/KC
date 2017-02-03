package com.webmyne.kidscrown.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
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
        formatter = new DecimalFormat("#,##,###.##");
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

            txtOrderId.setText("Invoice Number " + oreder.getInvoiceNumber());
            txtOrderDate.setText(oreder.getOrderDate());
            txtAmount.setText(context.getResources().getString(R.string.Rs) + " " + formatter.format(oreder.getTotalAmount()));

            txtPaymentStatus.setText(oreder.getStatus());

            if (oreder.getShippingCost() == 0) {
                txtShipping.setText("Shipping Charge Free");
            } else {
                txtShipping.setText("Shipping Charge " + context.getResources().getString(R.string.Rs) + " " + oreder.getShippingCost());
            }

        }
    }

}