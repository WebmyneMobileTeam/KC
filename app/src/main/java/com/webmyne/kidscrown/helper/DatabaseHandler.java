package com.webmyne.kidscrown.helper;

import android.content.ContentValues;
import android.content.Context;
import android.content.res.Resources;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;
import android.widget.TextView;


import com.webmyne.kidscrown.R;
import com.webmyne.kidscrown.model.Address;
import com.webmyne.kidscrown.model.CrownPricing;
import com.webmyne.kidscrown.model.Product;
import com.webmyne.kidscrown.model.ProductImage;
import com.webmyne.kidscrown.model.ProductPrice;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.lang.reflect.Array;
import java.sql.SQLException;
import java.util.ArrayList;

/**
 * Created by jaydeeprana on 01-07-2015.
 */
public class DatabaseHandler extends SQLiteOpenHelper {
    public static final String KEY_ROWID = "_id";
    private static final int DATABASE_VERSION = 1;
    private static final String DATABASE_NAME = "kidscrown.db";
    private static final String TABLE_PRODUCT = "Product";
    private static final String TABLE_PRODUCT_IMAGE = "ProductImage";
    private static final String TABLE_ADDRESS = "Address";
    private static final String TABLE_PRODUCT_PRICE = "ProductPrice";
    private static final String TABLE_CART_ITEM = "CartItem";

    private static final String DATABASE_PATH = "/data/data/com.webmyne.kidscrown/databases/";
    private Context context;
    private SQLiteDatabase myDataBase = null;

    public void createDataBase() throws IOException {

        boolean dbExist = checkDataBase();
        if (dbExist) {
//            Log.v("log_tag", "database does exist");
        } else {
//            Log.v("log_tag", "database does not exist");
            this.getReadableDatabase();
            try {
                copyDataBase();
            } catch (IOException e) {
                e.printStackTrace();
                throw new Error("Error copying database");
            }
        }
    }

    private void copyDataBase() throws IOException {

        InputStream myInput = context.getAssets().open(DATABASE_NAME);
        String outFileName = DATABASE_PATH + DATABASE_NAME;
        OutputStream myOutput = new FileOutputStream(outFileName);
        byte[] buffer = new byte[1024];
        int length;

        while ((length = myInput.read(buffer)) > 0) {
            myOutput.write(buffer, 0, length);
        }

        myOutput.flush();
        myOutput.close();
        myInput.close();
    }

    private boolean checkDataBase() {

        File folder = new File(DATABASE_PATH);
        if (!folder.exists()) {
            folder.mkdir();
        }
        File dbFile = new File(DATABASE_PATH + DATABASE_NAME);
        return dbFile.exists();
    }

    public boolean openDataBase() throws SQLException {
        String mPath = DATABASE_PATH + DATABASE_NAME;
        //Log.v("mPath", mPath);
        myDataBase = SQLiteDatabase.openDatabase(mPath, null, SQLiteDatabase.CREATE_IF_NECESSARY);
        //mDataBase = SQLiteDatabase.openDatabase(mPath, null, SQLiteDatabase.NO_LOCALIZED_COLLATORS);
        return myDataBase != null;

    }

    @Override
    public synchronized void close() {
        if (myDataBase != null)
            myDataBase.close();
        super.close();
    }


    // Constructor
    public DatabaseHandler(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
        this.context = context;
    }


    public boolean isUserExists() {
        boolean isExists = false;
        myDataBase = this.getWritableDatabase();

        String selectQuery = "SELECT * FROM User";
        Cursor cursor = myDataBase.rawQuery(selectQuery, null);
        cursor.moveToFirst();

        if (cursor == null || cursor.getCount() == 0) {
            isExists = false;
        } else {
            isExists = true;
        }
        myDataBase.close();


        return isExists;
    }

    public void saveProducts(ArrayList<Product> products) {
        myDataBase = this.getWritableDatabase();
        myDataBase.delete(TABLE_PRODUCT, null, null);

        myDataBase = this.getWritableDatabase();
        myDataBase.delete(TABLE_PRODUCT_PRICE, null, null);

        myDataBase = this.getWritableDatabase();
        myDataBase.delete(TABLE_PRODUCT_IMAGE, null, null);

        ArrayList<Integer> colors;
        colors = new ArrayList();
        int pos = 0;
        Resources res = context.getResources();
        colors.add(res.getColor(R.color.quad_green));
        colors.add(res.getColor(R.color.quad_violate));
        colors.add(res.getColor(R.color.quad_orange));
        colors.add(res.getColor(R.color.quad_blue));

        for (Product product : products) {

            myDataBase = this.getWritableDatabase();
            ContentValues values = new ContentValues();
            values.put("description", product.description);
            // values.put("max", product.max);
            // values.put("min", product.min);
            values.put("price", product.price);
            values.put("product_id", product.productID);
            values.put("name", product.name);
            values.put("product_number", product.product_number);
            values.put("color", colors.get(pos));
            myDataBase.insert(TABLE_PRODUCT, null, values);
            if (pos >= colors.size() - 1) {
                pos = 0;
            } else {
                pos = pos + 1;
            }
            saveProductImages(product.images);

            saveProductPrices(product.prices);


        }

    }

    private void saveProductPrices(ArrayList<ProductPrice> prices) {

        for (ProductPrice price : prices) {
            myDataBase = this.getWritableDatabase();
            ContentValues values = new ContentValues();
            values.put("product_id", price.product_id);
            values.put("price_id", price.price_id);
            values.put("price", price.price);
            values.put("min", price.min);
            values.put("max", price.max);
            myDataBase.insert(TABLE_PRODUCT_PRICE, null, values);
        }

    }



    public boolean ifExists(int productID) {
        boolean available = false;
        myDataBase = this.getWritableDatabase();
        Cursor cursor;
        String selectQuery = "SELECT * FROM " + TABLE_CART_ITEM + " WHERE product_id =" + productID;
        cursor = myDataBase.rawQuery(selectQuery, null);

        if (cursor != null && cursor.getCount() > 0) {
            available = true;
            cursor.moveToFirst();
        }
        return available;
    }

    public int getQty(int productID) {
        int qty = 0;
        myDataBase = this.getWritableDatabase();
        Cursor cursor;
        String selectQuery = "SELECT qty FROM " + TABLE_CART_ITEM + " WHERE product_id =" + productID;
        cursor = myDataBase.rawQuery(selectQuery, null);

        if (cursor != null && cursor.getCount() > 0) {
            cursor.moveToFirst();
            qty = cursor.getInt(cursor.getColumnIndexOrThrow("qty"));
        }
        return qty;
    }

    public void deleteCartProduct(int productID) {
        myDataBase = this.getWritableDatabase();
        String selectQuery = "DELETE FROM " + TABLE_CART_ITEM + " WHERE product_id ='" + productID + "'";
        myDataBase.execSQL(selectQuery);
    }

    public void addCartProduct(ArrayList<String> productDetails) {

        myDataBase = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("product_id", Integer.parseInt(productDetails.get(0)));
        values.put("product_name", productDetails.get(1));
        values.put("qty", Integer.parseInt(productDetails.get(2)));
        values.put("unit_price", productDetails.get(3));
        values.put("total_price", productDetails.get(4));
        myDataBase.insert(TABLE_CART_ITEM, null, values);

    }

    public void updateCart(int qty, int totalPrice, int productID) {
        myDataBase = this.getWritableDatabase();
        String selectQuery = "UPDATE " + TABLE_CART_ITEM + " SET qty=" + qty + ", total_price=" + totalPrice + " WHERE product_id ='" + productID + "'";
        myDataBase.execSQL(selectQuery);
    }

    public Cursor getCartProduct() {
        myDataBase = this.getWritableDatabase();
        Cursor cursor = null;
        String selectQuery = "SELECT * FROM " + TABLE_CART_ITEM + ", " + TABLE_PRODUCT_PRICE + " WHERE CartItem.unit_price = ProductPrice.price";
        cursor = myDataBase.rawQuery(selectQuery, null);
        cursor.moveToFirst();

        return cursor;
    }

    public ArrayList<CrownPricing> getCrownPricing(int productID) {
        ArrayList<CrownPricing> crownPricing = new ArrayList<>();
        myDataBase = this.getWritableDatabase();
        Cursor cursor = null;
        String selectQuery = "SELECT * FROM " + TABLE_PRODUCT_PRICE + " WHERE product_id ='" + productID + "'";
        cursor = myDataBase.rawQuery(selectQuery, null);
        cursor.moveToFirst();
        if (cursor.getCount() > 0) {
            do {
                CrownPricing pricing = new CrownPricing();
                pricing.setMin(cursor.getInt(cursor.getColumnIndexOrThrow("min")));
                pricing.setMax(cursor.getInt(cursor.getColumnIndexOrThrow("max")));
                pricing.setPrice(cursor.getInt(cursor.getColumnIndexOrThrow("price")));
                crownPricing.add(pricing);
            } while (cursor.moveToNext());
        }

        return crownPricing;

    }

    public void saveAddress(ArrayList<Address> addresses) {
        myDataBase = this.getWritableDatabase();
        myDataBase.delete(TABLE_ADDRESS, null, null);

        ArrayList<Integer> colors;
        colors = new ArrayList();
        int pos = 0;
        Resources res = context.getResources();
        colors.add(res.getColor(R.color.quad_green));
        colors.add(res.getColor(R.color.quad_violate));
        colors.add(res.getColor(R.color.quad_orange));
        colors.add(res.getColor(R.color.quad_blue));

        for (Address address : addresses) {

            myDataBase = this.getWritableDatabase();
            ContentValues values = new ContentValues();
            values.put("user_id", address.UserID);
            values.put("pincode", address.PinCode);
            values.put("mobile", address.MobileNo);
            values.put("state_name", "");
            values.put("state_id", address.StateID);
            values.put("country_name", "");
            values.put("country_id", address.CountryID);
            values.put("city_name", "");
            values.put("city_id", address.CityID);
            values.put("address_id", address.AddressID);
            values.put("address_2", address.Address2);
            values.put("address_1", address.Address1);
            values.put("color", colors.get(pos));
            myDataBase.insert(TABLE_ADDRESS, null, values);
            if (pos >= colors.size() - 1) {
                pos = 0;
            } else {
                pos = pos + 1;
            }
        }

    }

    public void deleteAddress(int addressID) {

    }

    private void saveProductImages(ArrayList<ProductImage> images) {


        for (ProductImage image : images) {
            myDataBase = this.getWritableDatabase();
            ContentValues values = new ContentValues();
            values.put("path", image.imagePath);
            values.put("product_id", image.productId);
            values.put("product_image_id", image.productImageid);
            myDataBase.insert(TABLE_PRODUCT_IMAGE, null, values);
        }


    }

    public Cursor getProductsCursor() {
        myDataBase = this.getWritableDatabase();
        Cursor cursor = null;
        String selectQuery = "SELECT * FROM " + TABLE_PRODUCT;
        cursor = myDataBase.rawQuery(selectQuery, null);
        cursor.moveToFirst();

        return cursor;
    }

    public Cursor getAddressCursor() {
        myDataBase = this.getWritableDatabase();
        Cursor cursor = null;
        String selectQuery = "SELECT  * FROM " + TABLE_ADDRESS;
        cursor = myDataBase.rawQuery(selectQuery, null);
        cursor.moveToFirst();

        return cursor;
    }

    public String getImagePath(String productID) {
        myDataBase = this.getWritableDatabase();
        String path = new String();
        String selectQuery = "SELECT * FROM " + TABLE_PRODUCT_IMAGE + " WHERE product_id =" + "\"" + productID.toString().trim() + "\"";
        Cursor cursor = myDataBase.rawQuery(selectQuery, null);

        if (cursor != null && cursor.getCount() > 0) {
            cursor.moveToFirst();
            path = cursor.getString(cursor.getColumnIndexOrThrow("path"));

        }

        return path;

    }

    public Cursor getProductCursor(String productID) {
        myDataBase = this.getWritableDatabase();
        Cursor cursor;
        String selectQuery = "SELECT * FROM " + TABLE_PRODUCT + " WHERE product_id =" + "\"" + productID.toString().trim() + "\"";
        cursor = myDataBase.rawQuery(selectQuery, null);

        if (cursor != null && cursor.getCount() > 0) {
            cursor.moveToFirst();
        }

        return cursor;

    }

    public Cursor getProductPriceCursor(String productID) {
        myDataBase = this.getWritableDatabase();
        Cursor cursor;
        String selectQuery = "SELECT * FROM " + TABLE_PRODUCT_PRICE + " WHERE product_id =" + "\"" + productID.toString().trim() + "\"";
        cursor = myDataBase.rawQuery(selectQuery, null);

        if (cursor != null && cursor.getCount() > 0) {
            cursor.moveToFirst();
        }

        return cursor;

    }

    public Cursor getProductImageCursor(String productID) {
        myDataBase = this.getWritableDatabase();
        Cursor cursor;
        String selectQuery = "SELECT * FROM " + TABLE_PRODUCT_IMAGE + " WHERE product_id =" + "\"" + productID.toString().trim() + "\"";
        cursor = myDataBase.rawQuery(selectQuery, null);

        if (cursor != null && cursor.getCount() > 0) {
            cursor.moveToFirst();
        }

        return cursor;

    }

    public void insertCrownItem(int productID,String crownName,int qty){

        myDataBase = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("product_name",crownName);
        values.put("qty",qty);
        values.put("unit_price",0);
        int totalPrice = 0;
        values.put("total_price",totalPrice);
        values.put("product_id",productID);
        myDataBase.insert(TABLE_CART_ITEM,null,values);

    }

    public void updateCrownItem(String productName,int qty){

    }


//    public String getCurrentDescription(String sportID){
//        myDataBase = this.getWritableDatabase();
//        String selectQuery = "SELECT * FROM player_sports WHERE sport_id ="+"\""+sportID.toString().trim()+"\"";
//        Cursor cursor = myDataBase.rawQuery(selectQuery, null);
//        cursor.moveToFirst();
//        String des = new String();
//        if(cursor!= null){
//
//            des = cursor.getString(cursor.getColumnIndex("description"));
//        }
//        return des;
//    }

//
//    public void deleteUserSport(String sportId) {
//
//        myDataBase = this.getWritableDatabase();
//        myDataBase.delete(TABLE_USER_SPORTS, "sport_id = ?", new String[]{sportId});
//        myDataBase.close();
//    }


    @Override
    public void onCreate(SQLiteDatabase db) {

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }

}