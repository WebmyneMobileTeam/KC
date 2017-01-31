package com.webmyne.kidscrown.adapters;

import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.webmyne.kidscrown.R;
import com.webmyne.kidscrown.model.CrownProductItem;

import java.util.ArrayList;

/**
 * Created by dhruvil on 17-08-2015.
 */
public class RefillOrderAdapterAnother extends BaseAdapter {

    private Context _ctx;
    private ArrayList<CrownProductItem> crowns;
    private OnDeleteListner onDeleteListner;
    private onCartSelectListener onCartSelectListener;
    private onTextChange onTextChange;


    public void setOnCartSelectListener(RefillOrderAdapterAnother.onCartSelectListener onCartSelectListener) {
        this.onCartSelectListener = onCartSelectListener;
    }

    public RefillOrderAdapterAnother(Context _ctx, ArrayList<CrownProductItem> crowns) {
        this._ctx = _ctx;
        this.crowns = crowns;
    }

    public void setOnTextChange(RefillOrderAdapterAnother.onTextChange onTextChange) {
        this.onTextChange = onTextChange;
    }

    @Override
    public int getCount() {
        return crowns.size();
    }

    public void setCrowns(ArrayList<CrownProductItem> crowns) {
        this.crowns = new ArrayList<>();
        this.crowns = crowns;
        notifyDataSetChanged();
    }

    class ViewHolder {
        TextView txtName;
        TextView edQty;
        ImageView imgDelete;
    }

    public void setOnDeleteListner(OnDeleteListner listner) {
        this.onDeleteListner = listner;
    }


    @Override
    public Object getItem(int position) {
        return position;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        final ViewHolder holder;
        LayoutInflater mInflater = (LayoutInflater) _ctx.getSystemService(Activity.LAYOUT_INFLATER_SERVICE);

        if (convertView == null) {
            convertView = mInflater.inflate(R.layout.item_cart_another, parent, false);
            holder = new ViewHolder();
            holder.txtName = (TextView) convertView.findViewById(R.id.txtItemName);
            holder.edQty = (TextView) convertView.findViewById(R.id.edItemQty);
            holder.edQty.setEnabled(false);
            holder.edQty.setBackgroundColor(Color.TRANSPARENT);
            holder.imgDelete = (ImageView) convertView.findViewById(R.id.imgDeleteCart);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        holder.txtName.setText(crowns.get(position).itemName);
        holder.edQty.setText("" + crowns.get(position).itemQty);

        holder.edQty.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {

                if (s.toString().length() > 0) {
                    onTextChange.onTextChange(holder.txtName.getText().toString(), Integer.parseInt(s.toString()));
                } else if (s.toString().length() == 0) {
                    onTextChange.onTextChange(holder.txtName.getText().toString(), 0);
                }

            }
        });
        holder.imgDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onDeleteListner.onDelete(position);
            }
        });

        return convertView;
    }

    public interface OnDeleteListner {
        public void onDelete(int position);
    }

    public interface onCartSelectListener {
        public void addCart();
    }

    public interface onTextChange {

        public void onTextChange(String crownName, int qty);

    }

}
