package com.webmyne.kidscrown.adapters;

import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.database.Cursor;
import android.support.v4.widget.SimpleCursorAdapter;
import android.text.Html;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CursorAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.webmyne.kidscrown.R;
import com.webmyne.kidscrown.helper.DatabaseHandler;

import java.util.ArrayList;
import java.util.Random;

public class ProductAdapter extends CursorAdapter {

    private Context _ctx;

    public ProductAdapter(Context context, Cursor c, int flags) {
        super(context, c, flags);
        this._ctx = context;
    }


    class ViewHolder {
        TextView txtProductTitle;
        TextView txtDescription;
        ImageView imgProduct;
        LinearLayout linearParent;
    }


    @Override
    public View newView(Context context, Cursor cursor, ViewGroup parent) {

        return LayoutInflater.from(context).inflate(R.layout.item_product, parent, false);

    }

    @Override
    public void bindView(View rowView, Context context, Cursor cursor) {

        TextView txtProductTitle = (TextView) rowView.findViewById(R.id.txtProductTitle);
        TextView txtDescription = (TextView) rowView.findViewById(R.id.txtProductDescription);
        ImageView imgProduct = (ImageView) rowView.findViewById(R.id.imgProduct);
        LinearLayout linearParent = (LinearLayout) rowView.findViewById(R.id.linearParent);

        String name = cursor.getString(cursor.getColumnIndexOrThrow("name"));
        String description = cursor.getString(cursor.getColumnIndexOrThrow("description"));
        String productID = cursor.getString(cursor.getColumnIndexOrThrow("product_id"));

        DatabaseHandler handler = new DatabaseHandler(_ctx);
        String path = handler.getImagePath(productID);
        handler.close();

        txtProductTitle.setText(name);
        txtDescription.setText(Html.fromHtml(description));
        if (path != null && !path.isEmpty()) {
            Glide.with(_ctx).load(path).into(imgProduct);
        }

        int color = cursor.getInt(cursor.getColumnIndexOrThrow("color"));
        linearParent.setBackgroundColor(color);
    }

    private void displayValues(ViewHolder holder, Cursor cursor) {

        String name = cursor.getString(cursor.getColumnIndexOrThrow("name"));
        String description = cursor.getString(cursor.getColumnIndexOrThrow("description"));
        String productID = cursor.getString(cursor.getColumnIndexOrThrow("product_id"));

        DatabaseHandler handler = new DatabaseHandler(_ctx);
        String path = handler.getImagePath(productID);
        handler.close();

        holder.txtProductTitle.setText(name);
        holder.txtDescription.setText(Html.fromHtml(description));
        if (path != null && !path.isEmpty()) {
            Glide.with(_ctx).load(path).into(holder.imgProduct);
        }

        int color = cursor.getInt(cursor.getColumnIndexOrThrow("color"));
        holder.linearParent.setBackgroundColor(color);


    }


}