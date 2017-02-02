package com.webmyne.kidscrown.ui;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.webmyne.kidscrown.R;
import com.webmyne.kidscrown.api.CommonRetrofitResponseListener;
import com.webmyne.kidscrown.api.PlaceOrderApi;
import com.webmyne.kidscrown.helper.CallWebService;
import com.webmyne.kidscrown.helper.ComplexPreferences;
import com.webmyne.kidscrown.helper.Constants;
import com.webmyne.kidscrown.helper.DatabaseHandler;
import com.webmyne.kidscrown.helper.Functions;
import com.webmyne.kidscrown.helper.GetSortedDiscount;
import com.webmyne.kidscrown.helper.OfferView;
import com.webmyne.kidscrown.helper.OrderSummary;
import com.webmyne.kidscrown.helper.PrefUtils;
import com.webmyne.kidscrown.model.AddressModel;
import com.webmyne.kidscrown.model.FinalOrderRequest;
import com.webmyne.kidscrown.model.OrderModel;
import com.webmyne.kidscrown.model.PlaceOrderRequest;
import com.webmyne.kidscrown.model.PlaceOrderResponse;

import org.json.JSONArray;
import org.json.JSONObject;

import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Random;

import dmax.dialog.SpotsDialog;

public class ConfirmOrderActivity extends AppCompatActivity {

    Toolbar toolbar;
    ArrayList<OrderModel> orders = new ArrayList<>();
    TextView totalPrice, txtBilling, txtShipping, subtotalPrice, txtSaved, txtSavedPrice;
    LinearLayout totalLayout, offerLayout;
    int price, finalPrice, introPrice = 0, assortedPrice = 0, crownPrice = 0;
    private LinearLayout orderSummaryLayout;
    ArrayList<AddressModel> addressModels;
    RelativeLayout continueLayout;
    String randomOrderId, userId;
    String dateTime;
    String shippingAddress;
    DatabaseHandler handler;
    int crownProductId;
    ProgressDialog pd1;
    SharedPreferences preferences;
    boolean isOffer;
    float percentage;
    GetSortedDiscount getSortedDiscount;
    private RelativeLayout chargeLayout;
    private TextView txtCharge;
    private LinearLayout.LayoutParams params;
    DecimalFormat formatter;
    int introDiscount, assortedDiscount, crownDiscount, invoiceDiscount;
    private boolean isIntro = false, isAssorted = false, isRefil = false;

    PlaceOrderResponse.DataBean resBean;
    PlaceOrderRequest reqBean;
    private TextView txtCustomTitle;
    private Button btnContinue;
    private SpotsDialog dialog;
    ComplexPreferences complexPreferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_confirm_order);

        getSortedDiscount = new GetSortedDiscount(this);

        init();

        complexPreferences = ComplexPreferences.getComplexPreferences(ConfirmOrderActivity.this, Constants.PREF_NAME, 0);
        resBean = complexPreferences.getObject("placeOrderRes", PlaceOrderResponse.DataBean.class);
        reqBean = complexPreferences.getObject("placeOrderReq", PlaceOrderRequest.class);

        txtBilling.setText(reqBean.getBillingAddressDC().toString());
        txtShipping.setText(reqBean.getShippingAddressDC().toString());

        preferences = getSharedPreferences("login", Context.MODE_PRIVATE);
        crownProductId = preferences.getInt("crownProductId", 0);
        isOffer = preferences.getBoolean("offer", false);

        if (getSortedDiscount.isOffer()) {
            percentage = preferences.getFloat("percentage", 0);
            offerLayout.setVisibility(View.VISIBLE);
            //txtSaved.setText("You saved as per " + percentage + "%");
        } else {
            offerLayout.setVisibility(View.GONE);
        }

        // Generate Random Order Id
        char[] chars = "ABCDEFGHIJKLMNOPQRSTUVWXYZ1234567890".toCharArray();
        StringBuilder sb = new StringBuilder();
        Random random = new Random();
        for (int i = 0; i < 8; i++) {
            char c = chars[random.nextInt(chars.length)];
            sb.append(c);
        }
        randomOrderId = sb.toString();

        params = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);

        displayProducts();

        //fetchCartDetails();
    }

    private void displayProducts() {

        orderSummaryLayout.removeAllViews();
        orderSummaryLayout.invalidate();

        for (int i = 0; i < resBean.getPlaceOrderCalculationDC().getProductCalculationDCs().size(); i++) {
            String productName = "";
            PlaceOrderResponse.DataBean.PlaceOrderCalculationDCBean.ProductCalculationDCsBean bean = resBean.getPlaceOrderCalculationDC().getProductCalculationDCs().get(i);
            OrderSummary summary = new OrderSummary(ConfirmOrderActivity.this);

            if (bean.getPlaceOrderRiffileDC() == null || bean.getPlaceOrderRiffileDC().isEmpty()) {
                productName = bean.getProductName();

            } else {
                StringBuilder sb = new StringBuilder();
                for (int i1 = 0; i1 < bean.getPlaceOrderRiffileDC().size(); i1++) {
                    sb.append(bean.getPlaceOrderRiffileDC().get(i1).getRiffleName());
                }
                productName = bean.getProductName() + "\n" + sb.toString();
            }

            summary.setDetails(productName, bean.getQuantity(), bean.getTotalPrice());
            orderSummaryLayout.addView(summary, params);
        }

        subtotalPrice.setText(getString(R.string.Rs) + "" + resBean.getPlaceOrderCalculationDC().getTotalAmount());
        txtSavedPrice.setText(getString(R.string.Rs) + "" + resBean.getPlaceOrderCalculationDC().getTotalDiscount());
        txtCharge.setText(getString(R.string.Rs) + "" + resBean.getPlaceOrderCalculationDC().getDeliveryCharge());
        totalPrice.setText(getString(R.string.Rs) + "" + resBean.getPlaceOrderCalculationDC().getPayableAmount());

    }

    private void fetchCartDetails() {

        introDiscount = 0;
        assortedDiscount = 0;
        crownDiscount = 0;
        invoiceDiscount = 0;

        orderSummaryLayout.removeAllViews();
        orderSummaryLayout.invalidate();

        offerLayout.removeAllViews();
        offerLayout.invalidate();

        // Fetch Address Details
        addressModels = new ArrayList<>();
        addressModels.clear();

        try {
            DatabaseHandler handler = new DatabaseHandler(ConfirmOrderActivity.this);
            handler.openDataBase();
            addressModels = handler.getAddressDetails();
            handler.close();
        } catch (Exception e) {
            e.printStackTrace();
        }

        if (addressModels.get(0).getShipping().equals("true")) {
            txtShipping.setText(addressModels.get(0).getAddress1() + ", " + addressModels.get(0).getAddress2() + ",\n" + addressModels.get(0).getCity() + " - " + addressModels.get(0).getPincode() + "\n" + addressModels.get(0).getState() + ", " + addressModels.get(0).getCountry());
            txtBilling.setText(addressModels.get(1).getAddress1() + ", " + addressModels.get(1).getAddress2() + ",\n" + addressModels.get(1).getCity() + " - " + addressModels.get(1).getPincode() + "\n" + addressModels.get(1).getState() + ", " + addressModels.get(1).getCountry());
        } else {
            txtBilling.setText(addressModels.get(0).getAddress1() + ", " + addressModels.get(0).getAddress2() + ",\n" + addressModels.get(0).getCity() + " - " + addressModels.get(0).getPincode() + "\n" + addressModels.get(0).getState() + ", " + addressModels.get(0).getCountry());
            txtShipping.setText(addressModels.get(1).getAddress1() + ", " + addressModels.get(1).getAddress2() + ",\n" + addressModels.get(1).getCity() + " - " + addressModels.get(1).getPincode() + "\n" + addressModels.get(1).getState() + ", " + addressModels.get(1).getCountry());
        }

        // Total Products from Cart
        try {
            DatabaseHandler handler = new DatabaseHandler(ConfirmOrderActivity.this);
            handler.openDataBase();
            orders = handler.getProducts();
            handler.close();
        } catch (Exception e) {
            e.printStackTrace();
        }

        for (int i = 0; i < orders.size(); i++) {
            int p = orders.get(i).getProductQty() * Integer.parseInt(orders.get(i).getProductUnitPrice());
            price += p;

            if (orders.get(i).getProductId() == Integer.parseInt(Constants.INTRO)) {
                isIntro = true;
                introPrice += orders.get(i).getProductQty() * Integer.parseInt(orders.get(i).getProductUnitPrice());
            }
            if (orders.get(i).getProductId() == Integer.parseInt(Constants.ASSORTED)) {
                isAssorted = true;
                assortedPrice += orders.get(i).getProductQty() * Integer.parseInt(orders.get(i).getProductUnitPrice());
            }
            if (orders.get(i).getProductId() == Integer.parseInt(Constants.CROWN)) {
                isRefil = true;
                crownPrice += orders.get(i).getProductQty() * Integer.parseInt(orders.get(i).getProductUnitPrice());
            }

            OrderSummary summary = new OrderSummary(ConfirmOrderActivity.this);
            summary.setDetails(orders.get(i));
            orderSummaryLayout.addView(summary, params);
        }

        if (getSortedDiscount.isOffer()) {
            OfferView view = null;

            if (isIntro && getSortedDiscount.getOffer(Constants.INTRO) != null && !getSortedDiscount.getOffer(Constants.INTRO).DiscountPercentage.equals("0.00")) {
                introDiscount = (int) (introPrice * Double.valueOf(getSortedDiscount.getOffer(Constants.INTRO).DiscountPercentage)) / 100;
                Log.e("introDiscount", introDiscount + "");
                price -= introDiscount;
                view = new OfferView(this, "Intro Kit Discount (" + getSortedDiscount.getOffer(Constants.INTRO).DiscountPercentage + "%)", introDiscount);
                offerLayout.addView(view, params);
            }

            if (isAssorted && getSortedDiscount.getOffer(Constants.ASSORTED) != null && !getSortedDiscount.getOffer(Constants.ASSORTED).DiscountPercentage.equals("0.00")) {
                assortedDiscount = (int) (assortedPrice * Double.valueOf(getSortedDiscount.getOffer(Constants.ASSORTED).DiscountPercentage)) / 100;
                Log.e("assortedDiscount", assortedDiscount + "");
                price -= assortedDiscount;
                view = new OfferView(this, "Assorted Kit Discount (" + getSortedDiscount.getOffer(Constants.ASSORTED).DiscountPercentage + "%)", assortedDiscount);
                offerLayout.addView(view, params);
            }

            if (isRefil && getSortedDiscount.getOffer(Constants.CROWN) != null && !getSortedDiscount.getOffer(Constants.CROWN).DiscountPercentage.equals("0.00")) {
                crownDiscount = (int) (crownPrice * Double.valueOf(getSortedDiscount.getOffer(Constants.CROWN).DiscountPercentage)) / 100;
                Log.e("crownDiscount", crownDiscount + "");
                price -= crownDiscount;
                view = new OfferView(this, "Refil Discount (" + getSortedDiscount.getOffer(Constants.CROWN).DiscountPercentage + "%)", crownDiscount);
                offerLayout.addView(view, params);
            }

            view = new OfferView(this, "SubTotal", price);
            offerLayout.addView(view, params);

            if (getSortedDiscount.getOffer(Constants.INVOICE) != null && !getSortedDiscount.getOffer(Constants.INVOICE).DiscountPercentage.equals("0.00")) {
                invoiceDiscount = (int) (price * Double.valueOf(getSortedDiscount.getOffer(Constants.INVOICE).DiscountPercentage)) / 100;
                Log.e("invoiceDiscount", invoiceDiscount + "");
                price -= invoiceDiscount;
                view = new OfferView(this, getSortedDiscount.getOffer(Constants.INVOICE).DiscountInitial + " (" + getSortedDiscount.getOffer(Constants.INVOICE).DiscountPercentage + "%)", invoiceDiscount);
                offerLayout.addView(view, params);
            }

            // savedPrice = ((price * percentage) / 100);
            // txtSavedPrice.setText(getString(R.string.Rs) + " " + price);
            finalPrice = price;


            totalPrice.setText(getString(R.string.Rs) + " " + formatter.format(price));
        } else {
            finalPrice = price;

            totalPrice.setText(getString(R.string.Rs) + " " + formatter.format(price));
        }

        int shippingCost = 0;

        if (finalPrice < 3000) {
            Log.e("tag", "tax");
            shippingCost = 100;
            offerLayout.setVisibility(View.VISIBLE);
            OfferView view = new OfferView(this, "Shipping Cost", shippingCost);
            offerLayout.addView(view, params);
        } else {
            Log.e("tag", "no tax");
            shippingCost = 0;
        }

        totalPrice.setText(getString(R.string.Rs) + " " + formatter.format((finalPrice + shippingCost)));
    }

    private void init() {
        handler = new DatabaseHandler(ConfirmOrderActivity.this);
        formatter = new DecimalFormat("#,##,###");

        offerLayout = (LinearLayout) findViewById(R.id.offerLayout);
        txtSaved = (TextView) findViewById(R.id.txtSaved);
        txtSavedPrice = (TextView) findViewById(R.id.txtSavedPrice);
        subtotalPrice = (TextView) findViewById(R.id.subtotalPrice);
        chargeLayout = (RelativeLayout) findViewById(R.id.chargeLayout);
        txtCharge = (TextView) findViewById(R.id.txtCharge);
        btnContinue = (Button) findViewById(R.id.btnContinue);

        txtCharge.setText(getString(R.string.Rs) + " 100");

        continueLayout = (RelativeLayout) findViewById(R.id.continueLayout);
        orderSummaryLayout = (LinearLayout) findViewById(R.id.orderSummaryLayout);

        txtBilling = (TextView) findViewById(R.id.txtBilling);
        txtShipping = (TextView) findViewById(R.id.txtShipping);
        totalLayout = (LinearLayout) findViewById(R.id.totalLayout);
        ImageView imgCart = (ImageView) findViewById(R.id.imgCartMenu);
        totalPrice = (TextView) findViewById(R.id.totalPrice);
        imgCart.setVisibility(View.GONE);

        toolbar = (Toolbar) findViewById(R.id.toolbar);
        if (toolbar != null) {
            txtCustomTitle = (TextView) toolbar.findViewById(R.id.txtCustomTitle);
            toolbar.setTitle("");
            txtCustomTitle.setText("Order Confirmation");
            setSupportActionBar(toolbar);
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }

        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
                overridePendingTransition(R.anim.push_down_in, R.anim.push_down_out);
            }
        });

        continueLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                continueLayout.setClickable(false);

                pd1 = ProgressDialog.show(ConfirmOrderActivity.this, "Loading", "Please wait..", true);

                if (!Functions.isConnected(ConfirmOrderActivity.this)) {
                    Functions.snack(v, getString(R.string.no_internet));
                    pd1.dismiss();
                    continueLayout.setClickable(true);
                    return;
                }

                // Current date-time
                Calendar c = Calendar.getInstance();
                SimpleDateFormat df = new SimpleDateFormat("dd-MMM-yyyy, EEE");
                dateTime = df.format(c.getTime());

                try {
                    handler = new DatabaseHandler(ConfirmOrderActivity.this);
                    handler.openDataBase();
                    shippingAddress = handler.getShippingAddress();
                    handler.addOrderItem(orders, randomOrderId, dateTime);

                    handler.deleteCart();

                    handler.close();
                } catch (Exception e) {
                    e.printStackTrace();
                }

                createAnotherOrder(orders);
            }
        });

        clickListener();
    }

    private void clickListener() {
        btnContinue.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

               // showProgress();

                FinalOrderRequest orderRequest = new FinalOrderRequest();
                orderRequest.setMobileOS("A");
                orderRequest.setUserID(PrefUtils.getUserId(ConfirmOrderActivity.this));
                orderRequest.setBillingAddressDC(reqBean.getBillingAddressDC());
                orderRequest.setPlaceOrderCalculationDC(resBean.getPlaceOrderCalculationDC());
                orderRequest.setPlaceOrderProductDC(reqBean.getPlaceOrderProductDC());
                orderRequest.setShippingAddressDC(reqBean.getShippingAddressDC());

                Log.e("final_req", Functions.jsonString(orderRequest));

                //reqBean.setPlaceOrderCalculationDC(resBean.getPlaceOrderCalculationDC());

                new PlaceOrderApi(ConfirmOrderActivity.this, new CommonRetrofitResponseListener() {
                    @Override
                    public void onSuccess(Object responseBody) {
                     //   hideProgress();

                        PlaceOrderResponse placeOrderResponse = (PlaceOrderResponse) responseBody;
                        Log.e("final_res", Functions.jsonString(placeOrderResponse));

                        complexPreferences.putObject("placeOrderRes", placeOrderResponse.getData());
                        complexPreferences.commit();

                        handler.deleteCart();

                        Intent i = new Intent(ConfirmOrderActivity.this, PaymentActivity.class);
                        startActivity(i);
                        overridePendingTransition(R.anim.push_up_in, R.anim.push_up_out);
                    }

                    @Override
                    public void onFail() {
                      //  hideProgress();
                    }
                }, orderRequest);
            }
        });
    }

    private void showProgress() {
        if (dialog == null) {
            dialog = new SpotsDialog(this, "Loading..", R.style.Custom);
        }
        dialog.setCancelable(false);
        dialog.show();
    }

    private void hideProgress() {
        if (dialog.isShowing()) {
            dialog.dismiss();
        }
    }

    private void createAnotherOrder(final ArrayList<OrderModel> orders) {

        new AsyncTask<Void, Void, Void>() {

            JSONObject mainObect = new JSONObject();
            JSONObject kitObject = new JSONObject();
            JSONObject crownMainObject = new JSONObject();
            JSONObject crownSubObject = new JSONObject();

            int grandTotal = 0, crownTotal = 0, crownQty = 0, crownSpecificId = 0;
            final JSONArray ordersArray = new JSONArray();
            final JSONArray crownArray = new JSONArray();

            @Override
            protected Void doInBackground(Void... params) {

                try {
                    handler = new DatabaseHandler(ConfirmOrderActivity.this);
                    handler.openDataBase();

                    for (int i = 0; i < orders.size(); i++) {
                        if (orders.get(i).getProductId() == crownProductId) {
                            crownQty += orders.get(i).getProductQty();
                            crownMainObject = new JSONObject();
                            crownMainObject.put("product_id", orders.get(i).getProductId());
                            crownMainObject.put("price_id", orders.get(i).getPriceId());
                            crownMainObject.put("ProductDiscount", crownDiscount);

                            crownSubObject = new JSONObject();
                            crownSubObject.put("product_name", orders.get(i).getProductName());
                            crownSubObject.put("qty", orders.get(i).getProductQty());

                            try {
                                crownSpecificId = handler.getSpecificId(orders.get(i).getProductName());

                            } catch (Exception e) {
                                e.printStackTrace();
                            }

                            crownSubObject.put("crown_specific_id", crownSpecificId);
                            crownSubObject.put("unit_price", orders.get(i).getProductUnitPrice());
                            crownSubObject.put("total_price", orders.get(i).getProductTotalPrice());

                            int crownTotalPrice = orders.get(i).getProductQty() * Integer.parseInt(orders.get(i).getProductUnitPrice());

                            grandTotal = grandTotal + crownTotalPrice;
                            crownTotal = crownTotal + crownTotalPrice;
                            crownArray.put(crownSubObject);

                        } else {
                            kitObject = new JSONObject();
                            kitObject.put("product_id", orders.get(i).getProductId());

                            if (orders.get(i).getProductId() == Integer.parseInt(Constants.INTRO)) {
                                kitObject.put("ProductDiscount", introDiscount);
                            } else if (orders.get(i).getProductId() == Integer.parseInt(Constants.ASSORTED)) {
                                kitObject.put("ProductDiscount", assortedDiscount);
                            }

                            kitObject.put("product_name", orders.get(i).getProductName());
                            kitObject.put("qty", orders.get(i).getProductQty());
                            kitObject.put("price_id", orders.get(i).getPriceId());
                            kitObject.put("unit_price", orders.get(i).getProductUnitPrice());
                            kitObject.put("total_price", orders.get(i).getProductTotalPrice());
                            // grandTotal = grandTotal + Integer.parseInt(orders.get(i).getProductTotalPrice());
                            ordersArray.put(kitObject);
                        }
                    }

                    handler.close();
                    if (crownQty != 0) {
                        crownMainObject.put("crownQty", crownQty);
                        crownMainObject.put("crownTotal", crownTotal);
                        crownMainObject.put("Crowns", crownArray);
                        ordersArray.put(crownMainObject);
                    }

                    // Main JSON Object
                    mainObect = new JSONObject();
                    mainObect.put("date", dateTime);
                    mainObect.put("user_id", userId);

                    if (finalPrice < 3000) {
                        grandTotal += 100;
                        mainObect.put("shipping_cost", 100);
                        mainObect.put("DiscountPercent", invoiceDiscount);
                        mainObect.put("grand_total", finalPrice);

                    } else {
                        mainObect.put("shipping_cost", 0);
                        mainObect.put("DiscountPercent", invoiceDiscount);
                        mainObect.put("grand_total", finalPrice);
                    }

                    mainObect.put("shipping_address", shippingAddress);
                    mainObect.put("OrdersArray", ordersArray);
                    mainObect.put("MobileOS", "A");

                } catch (Exception e) {
                    e.printStackTrace();
                }

                return null;
            }

            @Override
            protected void onPostExecute(Void aVoid) {
                super.onPostExecute(aVoid);

                Log.e("API", Constants.PLACE_ORDER);
                Log.e("order_request", mainObect.toString());

                new CallWebService(Constants.PLACE_ORDER, CallWebService.TYPE_POST, mainObect) {
                    @Override
                    public void response(String response) {
                        pd1.dismiss();
                        Log.e("order_response", response);
                        try {
                            Functions.fireIntent(ConfirmOrderActivity.this, PaymentActivity.class);
                            overridePendingTransition(R.anim.push_up_in, R.anim.push_up_out);
                            continueLayout.setClickable(true);
                        } catch (Exception e) {
                            pd1.dismiss();
                            Log.e("error", e.getMessage());
                        }
                    }

                    @Override
                    public void error(String error) {
                        pd1.dismiss();
                        if (TextUtils.isEmpty(error)) {
                            Log.e("error", error);
                            Functions.snack(continueLayout, error);
                        }
                        continueLayout.setClickable(true);
                    }
                }.call();
            }
        }.execute();

    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
        overridePendingTransition(R.anim.push_down_in, R.anim.push_down_out);
    }
}
