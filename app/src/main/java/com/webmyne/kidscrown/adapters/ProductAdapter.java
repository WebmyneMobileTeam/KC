package com.webmyne.kidscrown.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.webmyne.kidscrown.R;
import com.webmyne.kidscrown.helper.Functions;
import com.webmyne.kidscrown.model.Product;

import java.util.ArrayList;
import java.util.List;

public class ProductAdapter extends RecyclerView.Adapter<ProductAdapter.ProductHolder> {

    private Context context;
    private List<Product> products;

    public ProductAdapter(Context context, List<Product> products) {
        this.context = context;
        this.products = products;
    }

    @Override
    public ProductHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_product, parent, false);
        return new ProductHolder(view);
    }

    @Override
    public void onBindViewHolder(ProductHolder holder, int position) {
        Product product = products.get(position);
        holder.setProduct(product, position);
    }

    @Override
    public int getItemCount() {
        return products.size();
    }

    public void setProducts(ArrayList<Product> data) {
        this.products = new ArrayList<>();
        this.products = data;
        notifyDataSetChanged();
    }

    class ProductHolder extends RecyclerView.ViewHolder {

        private TextView txtProductTitle;
        private TextView txtProductDescription;
        private ImageView imgProduct;
        private LinearLayout linearParent;

        private ProductHolder(View itemView) {
            super(itemView);
            txtProductTitle = (TextView) itemView.findViewById(R.id.txtProductTitle);
            txtProductDescription = (TextView) itemView.findViewById(R.id.txtProductDescription);
            imgProduct = (ImageView) itemView.findViewById(R.id.imgProduct);
            linearParent = (LinearLayout) itemView.findViewById(R.id.linearParent);

        }

        private void setProduct(Product product, int position) {
            txtProductTitle.setText(product.getProductName());
            txtProductDescription.setText(product.getDescription());
            if (!TextUtils.isEmpty(product.getRootImage())) {
                Glide.with(context).load(product.getRootImage()).into(imgProduct);
            }
            linearParent.setBackgroundColor(Functions.getBgColor(context, position));
        }
    }

}