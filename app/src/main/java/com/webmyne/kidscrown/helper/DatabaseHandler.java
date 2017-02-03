package com.webmyne.kidscrown.helper;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import com.webmyne.kidscrown.model.CartProduct;
import com.webmyne.kidscrown.model.PriceSlab;
import com.webmyne.kidscrown.model.Product;
import com.webmyne.kidscrown.model.ProductCart;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashSet;

/**
 * Created by jaydeeprana on 01-07-2015.
 */
public class DatabaseHandler extends SQLiteOpenHelper {
    public static final String KEY_ROWID = "_id";
    private static final int DATABASE_VERSION = 2;
    private static final String DATABASE_NAME = "kidscrown.db";
    private static final String TABLE_PRODUCT = "Product";
    private static final String TABLE_ADDRESS = "Address";
    private static final String TABLE_CROWN_SPECIFICATION = "CrownSpecification";
    private static final String TABLE_PRODUCT_PRICE = "ProductPrice";
    private static final String TABLE_STATES = "State";
    private static final String TABLE_CART_ITEM = "CartItem";
    private static final String TABLE_ORDERS = "Orders";
    private static final String TABLE_DISCOUNT = "Discount";

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

    public void deleteCrown(String productName) {
        myDataBase = this.getWritableDatabase();
        String selectQuery = "DELETE FROM " + TABLE_CART_ITEM + " WHERE product_name ='" + productName + "'";
        myDataBase.execSQL(selectQuery);
    }

    public void deleteCrownProduct(String productName) {
        myDataBase = this.getWritableDatabase();
        String selectQueryForCrown = "SELECT * FROM " + TABLE_CART_ITEM + " WHERE product_name ='" + productName + "'";

        Cursor cursor = null;
        cursor = myDataBase.rawQuery(selectQueryForCrown, null);
        cursor.moveToFirst();

        int crownProductID = cursor.getInt(cursor.getColumnIndex("product_id"));

        String selectQuery = "DELETE FROM " + TABLE_CART_ITEM + " WHERE product_name ='" + productName + "'";
        myDataBase.execSQL(selectQuery);

        ArrayList<CartProduct> crowns = getCrownCartProducts(crownProductID);
        int totalCrowns = 0;
        for (CartProduct crown : crowns) {
            totalCrowns += crown.getQty();
        }

        int unitPrice = new GetPriceSlab(context).getRelevantPrice(crownProductID, totalCrowns).getPrice();
        deleteCartProduct(crownProductID);

        for (CartProduct crown : crowns) {
            crown.setUnitPrice(unitPrice);
            crown.setTotalPrice(unitPrice * crown.getQty());
            addToCart(crown);
        }
    }

    public void updateCart(int qty, int unitPrice, int productID) {
        int totalPrice = unitPrice * qty;
        Log.e("updated value", qty + "--" + unitPrice + "--" + totalPrice + "--" + productID);
        myDataBase = this.getWritableDatabase();
        String selectQuery = "UPDATE " + TABLE_CART_ITEM + " SET qty=" + qty + ", total_price=" + totalPrice + ", unit_price=" + unitPrice + " WHERE product_id ='" + productID + "'";
        myDataBase.execSQL(selectQuery);
    }

    public void updateCrownCart(int qty, int totalPrice, String productName) {
        myDataBase = this.getWritableDatabase();
        String selectQuery = "UPDATE " + TABLE_CART_ITEM + " SET qty=" + qty + ", total_price=" + totalPrice + " WHERE product_name ='" + productName + "'";
        myDataBase.execSQL(selectQuery);
    }

    // Crowns Products
    public ArrayList<ProductCart> getCrownCartProduct(int productID) {

        ArrayList<ProductCart> products = new ArrayList<>();
        myDataBase = this.getWritableDatabase();
        Cursor cursor = null;
        String selectQuery = "SELECT * FROM " + TABLE_CART_ITEM + " WHERE product_id ='" + productID + "'";
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

    @Override
    public void onCreate(SQLiteDatabase db) {

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        if (oldVersion < 2) {
            String createDiscount = "CREATE TABLE `Discount` (\n" +
                    "\t`DiscountImage`\tTEXT,\n" +
                    "\t`DiscountInitial`\tTEXT,\n" +
                    "\t`DiscountPercentage`\tTEXT,\n" +
                    "\t`ProductID`\tTEXT\n" +
                    ");";
            db.execSQL(createDiscount);
        }
    }

    public void deleteCart() {
        myDataBase = this.getWritableDatabase();
        myDataBase.delete(TABLE_CART_ITEM, null, null);
    }

    public void addToCart(CartProduct cartProduct) {
        myDataBase = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put("product_id", cartProduct.getProductId());
        values.put("specific_id", cartProduct.getSpecificId());
        values.put("product_name", cartProduct.getProductName());
        values.put("qty", cartProduct.getQty());
        values.put("unit_price", cartProduct.getUnitPrice());
        values.put("is_single", cartProduct.isSingle());
        values.put("max", cartProduct.getMax());
        values.put("unit_price", cartProduct.getUnitPrice());
        values.put("total_price", cartProduct.getTotalPrice());
        myDataBase.insert(TABLE_CART_ITEM, null, values);
    }

    public int getTotalProducts() {
        ArrayList<Integer> productsIds = new ArrayList<>();
        ArrayList<CartProduct> products = new ArrayList<>();
        products = getCartProducts();
        if (products.size() > 0) {
            for (int i = 0; i < products.size(); i++) {
                productsIds.add(products.get(i).getProductId());
            }
            HashSet<Integer> hashSet = new HashSet<Integer>();
            hashSet.addAll(productsIds);
            productsIds.clear();
            productsIds.addAll(hashSet);
            return productsIds.size();

        } else {
            return 0;
        }
    }

    public ArrayList<CartProduct> getCartProducts() {
        ArrayList<CartProduct> products = new ArrayList<>();
        myDataBase = this.getWritableDatabase();
        Cursor cursor = null;
        String selectQuery = "SELECT * FROM " + TABLE_CART_ITEM;
        cursor = myDataBase.rawQuery(selectQuery, null);
        cursor.moveToFirst();
        if (cursor.getCount() > 0) {
            do {
                CartProduct cartProduct = new CartProduct();
                cartProduct.setProductId(cursor.getInt(cursor.getColumnIndexOrThrow("product_id")));
                cartProduct.setSpecificId(cursor.getInt(cursor.getColumnIndexOrThrow("specific_id")));
                cartProduct.setProductName(cursor.getString(cursor.getColumnIndexOrThrow("product_name")));
                cartProduct.setQty(cursor.getInt(cursor.getColumnIndexOrThrow("qty")));
                cartProduct.setUnitPrice(cursor.getInt(cursor.getColumnIndexOrThrow("unit_price")));
                cartProduct.setTotalPrice(cursor.getInt(cursor.getColumnIndexOrThrow("total_price")));
                cartProduct.setSingle(cursor.getInt(cursor.getColumnIndexOrThrow("is_single")));
                cartProduct.setMax(cursor.getInt(cursor.getColumnIndexOrThrow("max")));
                products.add(cartProduct);
            } while (cursor.moveToNext());
        }
        close();
        return products;
    }

    public ArrayList<CartProduct> getCrownCartProducts(int productID) {
        ArrayList<CartProduct> products = new ArrayList<>();
        myDataBase = this.getWritableDatabase();
        Cursor cursor = null;
        String selectQuery = "SELECT * FROM " + TABLE_CART_ITEM + " WHERE product_id ='" + productID + "'";
        ;
        cursor = myDataBase.rawQuery(selectQuery, null);
        cursor.moveToFirst();
        if (cursor.getCount() > 0) {
            do {
                CartProduct cartProduct = new CartProduct();
                cartProduct.setProductId(cursor.getInt(cursor.getColumnIndexOrThrow("product_id")));
                cartProduct.setSpecificId(cursor.getInt(cursor.getColumnIndexOrThrow("specific_id")));
                cartProduct.setProductName(cursor.getString(cursor.getColumnIndexOrThrow("product_name")));
                cartProduct.setQty(cursor.getInt(cursor.getColumnIndexOrThrow("qty")));
                cartProduct.setUnitPrice(cursor.getInt(cursor.getColumnIndexOrThrow("unit_price")));
                cartProduct.setTotalPrice(cursor.getInt(cursor.getColumnIndexOrThrow("total_price")));
                cartProduct.setSingle(cursor.getInt(cursor.getColumnIndexOrThrow("is_single")));
                cartProduct.setMax(cursor.getInt(cursor.getColumnIndexOrThrow("max")));
                products.add(cartProduct);
            } while (cursor.moveToNext());
        }
        close();
        return products;
    }

    public void savePriceSlab(ArrayList<Product> data) {
        myDataBase = this.getWritableDatabase();
        myDataBase.delete(TABLE_PRODUCT_PRICE, null, null);

        for (int i = 0; i < data.size(); i++) {
            Product product = data.get(i);
            ArrayList<PriceSlab> slab = new ArrayList<>();
            slab.addAll(product.getPriceSlabDCs());
            if (slab.size() > 0) {
                for (int j = 0; j < slab.size(); j++) {
                    ContentValues values = new ContentValues();
                    values.put("product_id", slab.get(j).getProductID());
                    values.put("price_id", slab.get(j).getPriceID());
                    values.put("price", slab.get(j).getPrice());
                    values.put("min", slab.get(j).getMinQty());
                    values.put("max", slab.get(j).getMaxQty());
                    myDataBase.insert(TABLE_PRODUCT_PRICE, null, values);
                }
            }
        }
    }

    public ArrayList<PriceSlab> getPriceSlab() {

        ArrayList<PriceSlab> prices = new ArrayList<>();
        ArrayList<CartProduct> products = new ArrayList<>();
        myDataBase = this.getWritableDatabase();
        Cursor cursor = null;
        String selectQuery = "SELECT * FROM " + TABLE_PRODUCT_PRICE;
        cursor = myDataBase.rawQuery(selectQuery, null);
        cursor.moveToFirst();
        if (cursor.getCount() > 0) {
            do {
                PriceSlab price = new PriceSlab();
                price.setProductID(cursor.getInt(cursor.getColumnIndexOrThrow("product_id")));
                price.setPriceID(cursor.getInt(cursor.getColumnIndexOrThrow("price_id")));
                price.setPrice(cursor.getInt(cursor.getColumnIndexOrThrow("price")));
                price.setMinQty(cursor.getInt(cursor.getColumnIndexOrThrow("min")));
                price.setMaxQty(cursor.getInt(cursor.getColumnIndexOrThrow("max")));
                prices.add(price);
            } while (cursor.moveToNext());
        }

        return prices;
    }
}
