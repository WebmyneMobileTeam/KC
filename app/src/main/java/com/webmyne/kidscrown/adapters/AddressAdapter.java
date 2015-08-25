package com.webmyne.kidscrown.adapters;

import android.content.Context;
import android.database.Cursor;
import android.support.v4.widget.SimpleCursorAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.webmyne.kidscrown.R;
import com.webmyne.kidscrown.helper.DatabaseHandler;
import com.webmyne.kidscrown.helper.Functions;

/**
 * Created by sagartahelyani on 20-08-2015.
 */
public class AddressAdapter extends SimpleCursorAdapter {

    private Context _ctx;


    public AddressAdapter(Context context, int layout, Cursor c, String[] from, int[] to, int flags) {
        super(context, layout, c, from, to, flags);
        this._ctx = context;

    }

    class ViewHolder {
        TextView txtAddress1, txtAddress2, txtCity, txtState, txtPincode, txtCountry, txtPhone;
        LinearLayout linearParent;
        ImageView delete;
    }

    @Override
    public View newView(final Context context, final Cursor cursor, ViewGroup parent) {

        LayoutInflater layoutInflator1 = LayoutInflater.from(context);
        View rowView = layoutInflator1.inflate(R.layout.address_row, parent, false);
        ViewHolder holder = new ViewHolder();
        holder.txtAddress1 = (TextView) rowView.findViewById(R.id.txtAddress1);
        holder.txtAddress2 = (TextView) rowView.findViewById(R.id.txtAddress2);
        holder.txtCity = (TextView) rowView.findViewById(R.id.txtCity);
        holder.txtState = (TextView) rowView.findViewById(R.id.txtState);
        holder.txtPincode = (TextView) rowView.findViewById(R.id.txtPincode);
        holder.txtCountry = (TextView) rowView.findViewById(R.id.txtCountry);
        holder.txtPhone = (TextView) rowView.findViewById(R.id.txtPhone);
        holder.delete = (ImageView) rowView.findViewById(R.id.delete);

        holder.linearParent = (LinearLayout) rowView.findViewById(R.id.linearParent);

        holder.delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Functions.displayMessage(context, "id " + cursor.getInt(cursor.getColumnIndexOrThrow("_id")));
                DatabaseHandler handler = new DatabaseHandler(context);
                handler.deleteAddress(cursor.getInt(cursor.getColumnIndexOrThrow("_id")));

            }
        });
        displayValues(holder, cursor);
        rowView.setTag(holder);
        return rowView;
    }

    private void displayValues(ViewHolder holder, Cursor cursor) {

        int color = cursor.getInt(cursor.getColumnIndexOrThrow("color"));
        holder.linearParent.setBackgroundColor(color);

        String address1 = cursor.getString(cursor.getColumnIndexOrThrow("address_1"));
        String address2 = cursor.getString(cursor.getColumnIndexOrThrow("address_2"));
        String pincode = cursor.getString(cursor.getColumnIndexOrThrow("pincode"));
        String mobile = cursor.getString(cursor.getColumnIndexOrThrow("mobile"));
        String state = cursor.getString(cursor.getColumnIndexOrThrow("state_id"));
        String country = cursor.getString(cursor.getColumnIndexOrThrow("country_id"));
        String city = cursor.getString(cursor.getColumnIndexOrThrow("city_id"));

        holder.txtAddress1.setText(address1);
        holder.txtAddress2.setText(address2);
        holder.txtPincode.setText(pincode);
        holder.txtPhone.setText(mobile);
        holder.txtState.setText("State");
        holder.txtCountry.setText("Country");
        holder.txtCity.setText("City");

    }

    @Override
    public void bindView(View view, Context context, Cursor cursor) {
        super.bindView(view, context, cursor);

        ViewHolder holder = (ViewHolder) view.getTag();
        displayValues(holder, cursor);
    }
}
