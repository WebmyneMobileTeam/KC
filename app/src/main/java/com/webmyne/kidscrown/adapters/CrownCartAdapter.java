package com.webmyne.kidscrown.adapters;

import android.content.Context;
import android.content.DialogInterface;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.webmyne.kidscrown.R;
import com.webmyne.kidscrown.helper.Functions;
import com.webmyne.kidscrown.model.CartProduct;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class CrownCartAdapter extends RecyclerView.Adapter<CrownCartAdapter.ProductHolder> {

    private Context context;
    private List<CartProduct> products;
    onRemoveProductListener onRemoveProductListener;

    public interface onRemoveProductListener {
        void removeProduct(String productName);
    }

    public CrownCartAdapter(Context context, List<CartProduct> products, onRemoveProductListener onRemoveProductListener) {
        this.context = context;
        this.products = products;
        this.onRemoveProductListener = onRemoveProductListener;
    }

    @Override
    public ProductHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_crown_product, parent, false);
        return new ProductHolder(view);
    }

    @Override
    public void onBindViewHolder(ProductHolder holder, int position) {
        CartProduct product = products.get(position);
        holder.setProduct(product);
    }

    @Override
    public int getItemCount() {
        return products.size();
    }

    public void setProducts(ArrayList<CartProduct> data) {
        this.products = new ArrayList<>();
        this.products = data;
        notifyDataSetChanged();
    }

    class ProductHolder extends RecyclerView.ViewHolder {

        private TextView txtName;
        private TextView txtQTY;
        private TextView txtPriceIndividual;
        private TextView txtPriceTotal;
        private Button btnRemove;

        private ProductHolder(View itemView) {
            super(itemView);
            txtName = (TextView) itemView.findViewById(R.id.txtName);
            txtQTY = (TextView) itemView.findViewById(R.id.txtQTY);
            txtPriceIndividual = (TextView) itemView.findViewById(R.id.txtPriceIndividual);
            txtPriceTotal = (TextView) itemView.findViewById(R.id.txtPriceTotal);
            btnRemove = (Button) itemView.findViewById(R.id.btnRemove);
        }

        private void setProduct(final CartProduct product) {
            txtName.setText(product.getProductName());
            txtQTY.setText(String.format(Locale.US, "%s %d", "Qty: ", product.getQty()));
            txtPriceIndividual.setText(String.format(Locale.US, "%s  %s%s", "Per crown:", context.getString(R.string.Rs), Functions.priceFormat(product.getUnitPrice())));
            int totalPrice = product.getUnitPrice() * product.getQty();
            txtPriceTotal.setText(String.format(Locale.US, "%s  %s %s", "Sub Total:", context.getString(R.string.Rs), Functions.priceFormat(totalPrice)));

            btnRemove.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    promptDialog(product.getProductName());
                }
            });
        }
    }

    private void promptDialog(final String productId) {
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(
                context);
        alertDialogBuilder.setTitle("Remove product");
        alertDialogBuilder
                .setMessage("Are you sure want to remove this product from cart?")
                .setCancelable(false)
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        if (onRemoveProductListener != null) {
                            onRemoveProductListener.removeProduct(productId);
                        }
                    }
                })
                .setNegativeButton("No", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.cancel();
                    }
                });
        AlertDialog alertDialog = alertDialogBuilder.create();
        alertDialog.show();
    }

}