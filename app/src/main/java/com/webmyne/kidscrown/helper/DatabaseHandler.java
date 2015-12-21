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
import com.webmyne.kidscrown.model.AddressModel;
import com.webmyne.kidscrown.model.CrownPricing;
import com.webmyne.kidscrown.model.CrownProductItem;
import com.webmyne.kidscrown.model.FinalOrders;
import com.webmyne.kidscrown.model.OrderModel;
import com.webmyne.kidscrown.model.Product;
import com.webmyne.kidscrown.model.ProductCart;
import com.webmyne.kidscrown.model.ProductImage;
import com.webmyne.kidscrown.model.ProductPrice;
import com.webmyne.kidscrown.model.StateModel;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.lang.reflect.Array;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

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
    private static final String TABLE_CROWN_SPECIFICATION = "CrownSpecification";
    private static final String TABLE_PRODUCT_PRICE = "ProductPrice";
    private static final String TABLE_STATES = "State";
    private static final String TABLE_CART_ITEM = "CartItem";
    private static final String TABLE_ORDERS = "Orders";

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
            values.put("price", product.price);
            values.put("product_id", product.productID);
            values.put("name", product.name);
            values.put("product_number", product.product_number);
            values.put("color", colors.get(pos));
            values.put("order_limit", product.OrderLimit);
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

    public int getLimit(int productID){
        int qty = 0;
        myDataBase = this.getWritableDatabase();
        Cursor cursor;
        String selectQuery = "SELECT order_limit FROM " + TABLE_PRODUCT + " WHERE product_id =" + productID;
        cursor = myDataBase.rawQuery(selectQuery, null);

        if (cursor != null && cursor.getCount() > 0) {
            cursor.moveToFirst();
            qty = cursor.getInt(cursor.getColumnIndexOrThrow("order_limit"));
        }
        return qty;

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

    public void deleteCrownProduct(String productName) {
        myDataBase = this.getWritableDatabase();
        String selectQuery = "DELETE FROM " + TABLE_CART_ITEM + " WHERE product_name ='" + productName + "'";
        myDataBase.execSQL(selectQuery);

        //todo update pricing
        int productID = 0;


        String selectQueryForCrown = "SELECT * FROM " + TABLE_PRODUCT + " WHERE name ='" + Constants.CROWN_PRODUCT_NAME + "'";
        Cursor cursor = null;
        cursor = myDataBase.rawQuery(selectQueryForCrown, null);
        cursor.moveToFirst();


        int crownProductID = cursor.getInt(cursor.getColumnIndex("product_id"));

        ArrayList<ProductCart> crowns = getCrownCartProduct(crownProductID);

        if (crowns == null || crowns.isEmpty()) {

        } else {

            ArrayList<CrownPricing> crownPricing = new ArrayList<>();
            crownPricing = getCrownPricing(crownProductID);

            int totalCrowns = 0;
            int unitPrice = 0;
            for (ProductCart item : crowns) {
                totalCrowns += item.getProductQty();
            }

            boolean isPass = false;
            List<Integer> maxies = new ArrayList<>();

            //todo dhruvil/sagar/krishna

            if (totalCrowns != 0) {

                for (int k = 0; k < crownPricing.size(); k++) {

                    maxies.add(crownPricing.get(k).getMax());
                    if (totalCrowns >= crownPricing.get(k).getMin() && totalCrowns <= crownPricing.get(k).getMax()) {
                        unitPrice = crownPricing.get(k).getPrice();
                        isPass = true;
                        break;
                    } else {
                        isPass = false;
                        continue;
                    }
                }
            }

            if (isPass == false) {
                int tempPos = 0;
                int max = Collections.max(maxies);
                tempPos = maxies.indexOf(Integer.valueOf(max));
                unitPrice = crownPricing.get(tempPos).getPrice();
            }

            for (ProductCart productCart : crowns) {
                ContentValues cv = new ContentValues();
                cv.put("unit_price", unitPrice);
                //myDataBase.update("textlines",cvTextLines,"id_ " + " = ?",new String[]{""+textLineID});
                myDataBase.update(TABLE_CART_ITEM, cv, "product_name " + " = ?", new String[]{"" + productCart.getProductName()});
            }


        }


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

    public String getShippingAddress() {
        myDataBase = this.getWritableDatabase();
        String shippingAddress = "";
        Cursor cursor = null;
        String selectQuery = "SELECT  * FROM " + TABLE_ADDRESS;
        cursor = myDataBase.rawQuery(selectQuery, null);
        cursor.moveToFirst();

        if (cursor.getCount() > 0) {
            do {
                if (cursor.getString(cursor.getColumnIndexOrThrow("is_shipping")).equals("true")) {
                    shippingAddress = cursor.getString(cursor.getColumnIndexOrThrow("address_1")) + ", " + cursor.getString(cursor.getColumnIndexOrThrow("address_2")) + ", " + cursor.getString(cursor.getColumnIndexOrThrow("city_name")) + " - " + cursor.getString(cursor.getColumnIndexOrThrow("pincode")) + ", " + cursor.getString(cursor.getColumnIndexOrThrow("state_name")) + ", " + cursor.getString((cursor.getColumnIndexOrThrow("country_name")));
                    break;
                }

            } while (cursor.moveToNext());
        }
        return shippingAddress;

    }

    public void addOrderItem(ArrayList<OrderModel> orderDetails, String randomOrderId, String dateTime) {
        myDataBase = this.getWritableDatabase();
        String shippingAddress = "";
        Cursor cursor = null;
        String selectQuery = "SELECT  * FROM " + TABLE_ADDRESS;
        cursor = myDataBase.rawQuery(selectQuery, null);
        cursor.moveToFirst();

        if (cursor.getCount() > 0) {
            do {
                if (cursor.getString(cursor.getColumnIndexOrThrow("is_shipping")).equals("true")) {
                    shippingAddress = cursor.getString(cursor.getColumnIndexOrThrow("address_1")) + ", " + cursor.getString(cursor.getColumnIndexOrThrow("address_2")) + ", " + cursor.getString(cursor.getColumnIndexOrThrow("city_name")) + " - " + cursor.getString(cursor.getColumnIndexOrThrow("pincode")) + ", " + cursor.getString(cursor.getColumnIndexOrThrow("state_name")) + ", " + cursor.getString((cursor.getColumnIndexOrThrow("country_name")));
                    break;
                }

            } while (cursor.moveToNext());
        }

        for (int i = 0; i < orderDetails.size(); i++) {
            ContentValues values = new ContentValues();
            values.put("order_id", randomOrderId);
            values.put("product_id", orderDetails.get(i).getProductId());
            values.put("product_name", orderDetails.get(i).getProductName());
            values.put("qty", orderDetails.get(i).getProductQty());
            values.put("unit_price", orderDetails.get(i).getProductUnitPrice());
            values.put("total_price", orderDetails.get(i).getProductTotalPrice());
            values.put("address", getShippingAddress());
            values.put("date", dateTime);
            myDataBase.insert(TABLE_ORDERS, null, values);
        }

    }

    public void updateCart(int qty, int totalPrice, int productID) {
        myDataBase = this.getWritableDatabase();
        String selectQuery = "UPDATE " + TABLE_CART_ITEM + " SET qty=" + qty + ", total_price=" + totalPrice + " WHERE product_id ='" + productID + "'";
        myDataBase.execSQL(selectQuery);
    }

    public void updateCrownCart(int qty, int totalPrice, String productName) {
        myDataBase = this.getWritableDatabase();
        String selectQuery = "UPDATE " + TABLE_CART_ITEM + " SET qty=" + qty + ", total_price=" + totalPrice + " WHERE product_name ='" + productName + "'";
        myDataBase.execSQL(selectQuery);
    }

    // Kit Products
    public ArrayList<ProductCart> getCartProduct(int productID) {
        ArrayList<ProductCart> products = new ArrayList<>();
        myDataBase = this.getWritableDatabase();
        Cursor cursor = null;
        String selectQuery = "SELECT * FROM " + TABLE_CART_ITEM + ", " + TABLE_PRODUCT_PRICE + " WHERE CartItem.unit_price = ProductPrice.price AND CartItem.product_id!=" + productID;
        cursor = myDataBase.rawQuery(selectQuery, null);
        cursor.moveToFirst();
        if (cursor.getCount() > 0) {
            do {
                ProductCart cart = new ProductCart();
                cart.setProductId(cursor.getInt(cursor.getColumnIndexOrThrow("product_id")));
                cart.setProductName(cursor.getString(cursor.getColumnIndexOrThrow("product_name")));
                cart.setProductQty(cursor.getInt(cursor.getColumnIndexOrThrow("qty")));
                cart.setProductUnitPrice(cursor.getString(cursor.getColumnIndexOrThrow("unit_price")));
                cart.setProductTotalPrice(cursor.getString(cursor.getColumnIndexOrThrow("total_price")));
                cart.setMaxQty(cursor.getInt(cursor.getColumnIndexOrThrow("max")));
                products.add(cart);
            } while (cursor.moveToNext());
        }
        return products;
    }

    // Total Products
    public ArrayList<OrderModel> getProducts() {
        ArrayList<OrderModel> orders = new ArrayList<>();
        myDataBase = this.getWritableDatabase();
        Cursor cursor = null;
        String selectQuery = "SELECT * FROM " + TABLE_CART_ITEM + ", " + TABLE_PRODUCT_PRICE + " WHERE CartItem.unit_price = ProductPrice.price";
        cursor = myDataBase.rawQuery(selectQuery, null);
        cursor.moveToFirst();
        if (cursor.getCount() > 0) {
            do {
                OrderModel cart = new OrderModel();
                cart.setProductId(cursor.getInt(cursor.getColumnIndexOrThrow("product_id")));
                cart.setProductName(cursor.getString(cursor.getColumnIndexOrThrow("product_name")));
                cart.setProductQty(cursor.getInt(cursor.getColumnIndexOrThrow("qty")));
                cart.setProductUnitPrice(cursor.getString(cursor.getColumnIndexOrThrow("unit_price")));
                cart.setProductTotalPrice(cursor.getString(cursor.getColumnIndexOrThrow("total_price")));
                cart.setMaxQty(cursor.getInt(cursor.getColumnIndexOrThrow("max")));
                cart.setPriceId(cursor.getInt(cursor.getColumnIndexOrThrow("price_id")));
                orders.add(cart);
            } while (cursor.moveToNext());
        }
        return orders;
    }

    public ArrayList<FinalOrders> getOrders() {
        ArrayList<FinalOrders> myOrders = new ArrayList<>();
        myDataBase = this.getWritableDatabase();
        Cursor cursor = null;
        String selectQuery = "SELECT * FROM " + TABLE_ORDERS;
        cursor = myDataBase.rawQuery(selectQuery, null);
        cursor.moveToFirst();

        if (cursor.getCount() > 0) {
            do {
                FinalOrders order = new FinalOrders();
                order.address = cursor.getString(cursor.getColumnIndexOrThrow("address"));
                order.orderId = cursor.getString(cursor.getColumnIndexOrThrow("order_id"));
                order.productId = cursor.getInt(cursor.getColumnIndexOrThrow("product_id")) + "";
                order.productName = cursor.getString(cursor.getColumnIndexOrThrow("product_name"));
                order.qty = cursor.getInt(cursor.getColumnIndexOrThrow("qty")) + "";
                order.unitPrice = cursor.getString(cursor.getColumnIndexOrThrow("unit_price"));
                order.totalPrice = cursor.getString(cursor.getColumnIndexOrThrow("total_price"));
                order.dateTime = cursor.getString(cursor.getColumnIndexOrThrow("date"));
                myOrders.add(order);
            } while (cursor.moveToNext());
        }

        return myOrders;
    }

    public ArrayList<OrderModel> getOrdersForOrderId(String orderId) {
        ArrayList<OrderModel> myOrders = new ArrayList<>();
        myDataBase = this.getWritableDatabase();
        Cursor cursor = null;
        String selectQuery = "SELECT * FROM " + TABLE_ORDERS + " WHERE order_id = '" + orderId + "'";
        cursor = myDataBase.rawQuery(selectQuery, null);
        cursor.moveToFirst();

        if (cursor.getCount() > 0) {
            do {
                OrderModel order = new OrderModel();
                order.setProductId(cursor.getInt(cursor.getColumnIndexOrThrow("product_id")));
                order.setProductName(cursor.getString(cursor.getColumnIndexOrThrow("product_name")));
                order.setProductQty(cursor.getInt(cursor.getColumnIndexOrThrow("qty")));
                order.setProductUnitPrice(cursor.getString(cursor.getColumnIndexOrThrow("unit_price")));
                order.setProductTotalPrice(cursor.getString(cursor.getColumnIndexOrThrow("total_price")));
                myOrders.add(order);
            } while (cursor.moveToNext());
        }

        return myOrders;
    }

    // Crowns Products
    public ArrayList<ProductCart> getCrownCartProduct(int productID) {

        ArrayList<ProductCart> products = new ArrayList<>();
        myDataBase = this.getWritableDatabase();
        Cursor cursor = null;
        String selectQuery = "SELECT * FROM " + TABLE_CART_ITEM + ", " + TABLE_PRODUCT_PRICE + " WHERE CartItem.unit_price = ProductPrice.price AND CartItem.product_id=" + productID;
        cursor = myDataBase.rawQuery(selectQuery, null);
        cursor.moveToFirst();
        if (cursor.getCount() > 0) {
            do {
                ProductCart cart = new ProductCart();
                cart.setProductId(cursor.getInt(cursor.getColumnIndexOrThrow("product_id")));
                cart.setProductName(cursor.getString(cursor.getColumnIndexOrThrow("product_name")));
                cart.setProductQty(cursor.getInt(cursor.getColumnIndexOrThrow("qty")));
                cart.setProductUnitPrice(cursor.getString(cursor.getColumnIndexOrThrow("unit_price")));
                cart.setProductTotalPrice(cursor.getString(cursor.getColumnIndexOrThrow("total_price")));
                cart.setMaxQty(cursor.getInt(cursor.getColumnIndexOrThrow("max")));
                products.add(cart);
            } while (cursor.moveToNext());
        }
        return products;
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

    public ArrayList<StateModel> getStates() {
        ArrayList<StateModel> states = new ArrayList<>();
        myDataBase = this.getWritableDatabase();
        Cursor cursor = null;
        String selectQuery = "SELECT * FROM " + TABLE_STATES;
        cursor = myDataBase.rawQuery(selectQuery, null);
        cursor.moveToFirst();

        if (cursor.getCount() > 0) {
            do {
                StateModel state = new StateModel();
                state.state_id = cursor.getInt(cursor.getColumnIndexOrThrow("state_id"));
                state.state_name = cursor.getString(cursor.getColumnIndexOrThrow("state_name"));
                states.add(state);
            } while (cursor.moveToNext());
        }
        return states;
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
            values.put("country_id", address.Country);
            values.put("city_name", "");
            values.put("city_id", address.City);
            values.put("address_id", address.AddressID);
            values.put("address_2", address.Address2);
            values.put("address_1", address.Address1);
            values.put("is_shipping", address.IsShipping);
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

    public void insertCrownItem(int productID, String crownName, int qty) {

        myDataBase = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("product_name", crownName);
        values.put("qty", qty);
        values.put("unit_price", 0);
        int totalPrice = 0;
        values.put("total_price", totalPrice);
        values.put("product_id", productID);
        myDataBase.insert(TABLE_CART_ITEM, null, values);

    }

    public void updateCrownItem(String productName, int qty) {

    }

    @Override
    public void onCreate(SQLiteDatabase db) {

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }

    public void deleteCart() {
        myDataBase = this.getWritableDatabase();
        myDataBase.delete(TABLE_CART_ITEM, null, null);
    }

    public void saveAddressDetails(ArrayList<AddressModel> addressModels) {
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

        for (AddressModel model : addressModels) {

            myDataBase = this.getWritableDatabase();
            ContentValues values = new ContentValues();
            values.put("is_shipping", model.getShipping());
            values.put("pincode", model.getPincode());
            values.put("state_name", model.getState());
            values.put("country_name", model.getCountry());
            values.put("city_name", model.getCity());
            values.put("address_2", model.getAddress2());
            values.put("address_1", model.getAddress1());
            values.put("color", colors.get(pos));

            myDataBase.insert(TABLE_ADDRESS, null, values);
            if (pos >= colors.size() - 1) {
                pos = 0;
            } else {
                pos = pos + 1;
            }
        }

    }

    public int getSpecificId(String crownName) {
        myDataBase = this.getWritableDatabase();
        int specifiicId = 0;
        Cursor cursor = null;
        String selectQuery = "SELECT  * FROM " + TABLE_CROWN_SPECIFICATION + " WHERE ModelNumber='" + crownName + "'";
        cursor = myDataBase.rawQuery(selectQuery, null);
        cursor.moveToFirst();

        if (cursor.getCount() > 0) {
            do {
                specifiicId = cursor.getInt(cursor.getColumnIndexOrThrow("ProductSpecID"));
            } while (cursor.moveToNext());
        }
        return specifiicId;

    }

    public ArrayList<AddressModel> getAddressDetails() {
        ArrayList<AddressModel> addressModels = new ArrayList<>();
        myDataBase = this.getWritableDatabase();
        Cursor cursor = null;
        String selectQuery = "SELECT * FROM " + TABLE_ADDRESS;
        cursor = myDataBase.rawQuery(selectQuery, null);
        cursor.moveToFirst();

        if (cursor.getCount() > 0) {
            do {
                AddressModel model = new AddressModel();
                model.setShipping(cursor.getString(cursor.getColumnIndexOrThrow("is_shipping")));
                model.setPincode(cursor.getString(cursor.getColumnIndexOrThrow("pincode")));
                model.setState(cursor.getString(cursor.getColumnIndexOrThrow("state_name")));
                model.setCountry(cursor.getString(cursor.getColumnIndexOrThrow("country_name")));
                model.setCity(cursor.getString(cursor.getColumnIndexOrThrow("city_name")));
                model.setAddress2(cursor.getString(cursor.getColumnIndexOrThrow("address_2")));
                model.setAddress1(cursor.getString(cursor.getColumnIndexOrThrow("address_1")));
                addressModels.add(model);
            } while (cursor.moveToNext());
        }
        return addressModels;
    }
}
