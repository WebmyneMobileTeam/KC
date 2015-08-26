package com.webmyne.kidscrown.adapters;

import android.app.Activity;
import android.content.Context;
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

import java.util.ArrayList;

/**
 * Created by dhruvil on 17-08-2015.
 */
public class RefillOrderAdapter extends BaseAdapter {

    private Context _ctx;
    private ArrayList<String> crowns;
    private OnDeleteListner onDeleteListner;
    private onCartSelectListener onCartSelectListener;


    public void setOnCartSelectListener(RefillOrderAdapter.onCartSelectListener onCartSelectListener) {
        this.onCartSelectListener = onCartSelectListener;
    }

    public RefillOrderAdapter(Context _ctx, ArrayList<String> crowns) {
        this._ctx = _ctx;
        this.crowns = crowns;
    }

    @Override
    public int getCount() {
        return crowns.size();
    }

    class ViewHolder {
        TextView txtName;
        EditText edQty;
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
            convertView = mInflater.inflate(R.layout.item_cart, parent, false);
            holder = new ViewHolder();
            holder.txtName = (TextView) convertView.findViewById(R.id.txtItemName);
            holder.edQty = (EditText)convertView.findViewById(R.id.edItemQty);
            holder.imgDelete = (ImageView) convertView.findViewById(R.id.imgDeleteCart);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        holder.txtName.setText(crowns.get(position));
        holder.edQty.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                notifyDataSetChanged();
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

    public interface onCartSelectListener{
        public void addCart();
    }
}
