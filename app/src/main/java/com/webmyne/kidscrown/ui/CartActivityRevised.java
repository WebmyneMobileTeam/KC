package com.webmyne.kidscrown.ui;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.google.android.gms.location.places.Place;
import com.webmyne.kidscrown.R;
import com.webmyne.kidscrown.adapters.CrownCartAdapter;
import com.webmyne.kidscrown.api.CommonRetrofitResponseListener;
import com.webmyne.kidscrown.api.PlaceOrderApi;
import com.webmyne.kidscrown.custom.EmptyLayout;
import com.webmyne.kidscrown.custom.familiarrecyclerview.FamiliarRecyclerView;
import com.webmyne.kidscrown.helper.ComplexPreferences;
import com.webmyne.kidscrown.helper.Constants;
import com.webmyne.kidscrown.helper.DatabaseHandler;
import com.webmyne.kidscrown.helper.Functions;
import com.webmyne.kidscrown.helper.PrefUtils;
import com.webmyne.kidscrown.model.CartProduct;
import com.webmyne.kidscrown.model.PlaceOrderRequest;
import com.webmyne.kidscrown.model.PlaceOrderResponse;
import com.webmyne.kidscrown.ui.widgets.ItemCartView;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import dmax.dialog.SpotsDialog;

public class CartActivityRevised extends AppCompatActivity {

    private FamiliarRecyclerView cartRV;
    private ArrayList<CartProduct> cartProducts;
    private ArrayList<CartProduct> crownProducts;
    private DatabaseHandler handler;
    private LinearLayout linearKit;
    private Toolbar toolbar;
    private TextView txtCustomTitle;
    private LinearLayout.LayoutParams params;
    private TextView txtTotalPrice;
    private int totalPrice = 0;
    private Button btnCheckout;
    private RelativeLayout contentLayout;
    private EmptyLayout emptyLayout;
    private CrownCartAdapter adapter;
    private SpotsDialog dialog;
    private ArrayList<Integer> crownIds;
    ComplexPreferences complexPreferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cart_revised);

        init();
    }

    private void init() {
        handler = new DatabaseHandler(this);
        params = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);

        linearKit = new LinearLayout(this);
        linearKit.setLayoutParams(params);
        linearKit.setOrientation(LinearLayout.VERTICAL);

        txtTotalPrice = (TextView) findViewById(R.id.txtTotalPrice);
        btnCheckout = (Button) findViewById(R.id.btnCheckout);
        contentLayout = (RelativeLayout) findViewById(R.id.contentLayout);
        emptyLayout = (EmptyLayout) findViewById(R.id.emptyLayout);
        emptyLayout.setContent("Cart is Empty", R.drawable.ic_empty_cart);

        toolbar = (Toolbar) findViewById(R.id.toolbar);
        if (toolbar != null) {
            txtCustomTitle = (TextView) toolbar.findViewById(R.id.txtCustomTitle);
            txtCustomTitle.setText("My Cart");
            toolbar.setTitle("");
            setSupportActionBar(toolbar);
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }

        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
                setResult(100, getIntent());
                overridePendingTransition(R.anim.push_down_in, R.anim.push_down_out);
            }
        });

        initRecyclerView();

        fetchCart();

        clickListener();
    }

    private void initRecyclerView() {
        crownProducts = new ArrayList<>();
        adapter = new CrownCartAdapter(this, crownProducts, new CrownCartAdapter.onRemoveProductListener() {
            @Override
            public void removeProduct(String productName) {
                Log.e("productName", productName);
                handler.deleteCrownProduct(productName);
               // handler.deleteCrown(productName);
                fetchCart();
            }
        });

        cartRV = (FamiliarRecyclerView) findViewById(R.id.cartRV);
        GridLayoutManager layoutManager = new GridLayoutManager(this, 2);
        cartRV.setLayoutManager(layoutManager);
        cartRV.setAdapter(adapter);
        cartRV.addHeaderView(linearKit);
    }

    private void clickListener() {
        btnCheckout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                complexPreferences = ComplexPreferences.getComplexPreferences(CartActivityRevised.this, Constants.PREF_NAME, 0);
                complexPreferences.putObject("placeOrderReq", new PlaceOrderRequest());
                complexPreferences.commit();
                doCallApi();
            }
        });
    }

    private void doCallApi() {

        crownIds = new ArrayList<>();

        //showProgress();

        final PlaceOrderRequest request = new PlaceOrderRequest();
        request.setBillingAddressDC(new PlaceOrderRequest.BillingAddressDCBean());
        request.setUserID(PrefUtils.getUserId(this));

        request.setPlaceOrderCalculationDC(new PlaceOrderRequest.PlaceOrderCalculationDCBean());
        request.setShippingAddressDC(new PlaceOrderRequest.ShippingAddressDCBean());

        List<PlaceOrderRequest.PlaceOrderProductDCBean> placeOrderProductDC = new ArrayList<>();

        List<PlaceOrderRequest.PlaceOrderProductDCBean.PlaceOrderRiffileDCBeanX> crownDC = new ArrayList<>();
        PlaceOrderRequest.PlaceOrderProductDCBean bean = new PlaceOrderRequest.PlaceOrderProductDCBean();

        for (int i = 0; i < cartProducts.size(); i++) {
            if (cartProducts.get(i).isSingle() == Constants.SINGLE) {
                bean = new PlaceOrderRequest.PlaceOrderProductDCBean();
                bean.setProductID(cartProducts.get(i).getProductId());
                bean.setQuantity(cartProducts.get(i).getQty());
                placeOrderProductDC.add(bean);

            } else {
                int crownCount = 0;
                crownDC = new ArrayList<>();
                bean = new PlaceOrderRequest.PlaceOrderProductDCBean();
                CartProduct cartProduct = cartProducts.get(i);
                bean.setProductID(cartProduct.getProductId());
                for (int j = 0; j < crownProducts.size(); j++) {
                    if (crownProducts.get(j).getProductId() == cartProduct.getProductId()) {
                        crownCount += crownProducts.get(j).getQty();
                        PlaceOrderRequest.PlaceOrderProductDCBean.PlaceOrderRiffileDCBeanX beanX = new PlaceOrderRequest.PlaceOrderProductDCBean.PlaceOrderRiffileDCBeanX();
                        beanX.setProductSpecID(crownProducts.get(j).getSpecificId());
                        beanX.setQuantity(crownProducts.get(j).getQty());
                        beanX.setRiffleName(crownProducts.get(j).getProductName());
                        crownDC.add(beanX);
                    }
                }
                bean.setQuantity(crownCount);
                bean.setPlaceOrderRiffileDC(crownDC);
                if (!crownIds.contains(bean.getProductID())) {
                    placeOrderProductDC.add(bean);
                    crownIds.add(bean.getProductID());
                }
            }
        }
        request.setPlaceOrderProductDC(placeOrderProductDC);

        Log.e("req1", Functions.jsonString(request));
        new PlaceOrderApi(this, new CommonRetrofitResponseListener() {
            @Override
            public void onSuccess(Object responseBody) {
               // hideProgress();

                PlaceOrderResponse placeOrderResponse = (PlaceOrderResponse) responseBody;
                Log.e("res1", Functions.jsonString(placeOrderResponse));

                ComplexPreferences preferences = ComplexPreferences.getComplexPreferences(CartActivityRevised.this, Constants.PREF_NAME, 0);
                preferences.putObject("placeOrderRes", placeOrderResponse.getData());
                preferences.putObject("placeOrderReq", request);
                preferences.commit();

                Intent i = new Intent(CartActivityRevised.this, ShippingDetailsActivity.class);
                startActivity(i);
                overridePendingTransition(R.anim.push_up_in, R.anim.push_up_out);
            }

            @Override
            public void onFail() {
              //  hideProgress();
            }
        }, request);
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

    private void fetchCart() {
        totalPrice = 0;

        linearKit.removeAllViews();
        linearKit.invalidate();

        cartProducts = new ArrayList<>();
        crownProducts = new ArrayList<>();
        cartProducts.addAll(handler.getCartProducts());

        if (cartProducts.size() == 0) {
            contentLayout.setVisibility(View.GONE);
            btnCheckout.setVisibility(View.GONE);
            emptyLayout.setVisibility(View.VISIBLE);

        } else {
            contentLayout.setVisibility(View.VISIBLE);
            btnCheckout.setVisibility(View.VISIBLE);
            emptyLayout.setVisibility(View.GONE);

            for (int i = 0; i < cartProducts.size(); i++) {
                totalPrice += cartProducts.get(i).getTotalPrice();

                if (cartProducts.get(i).isSingle() == Constants.SINGLE) {
                    ItemCartView cartView = new ItemCartView(this, cartProducts.get(i));
                    cartView.setOnRemoveProductListener(onRemoveProductListener);
                    cartView.setOnValueChangeListener(onValueChangeListener);
                    linearKit.addView(cartView, params);

                } else {
                    crownProducts.add(cartProducts.get(i));
                }
            }

            adapter.setProducts(crownProducts);
            txtTotalPrice.setText(String.format(Locale.US, "%s %s", getString(R.string.Rs), Functions.priceFormat(totalPrice)));
        }
    }

    ItemCartView.onRemoveProductListener onRemoveProductListener = new ItemCartView.onRemoveProductListener() {
        @Override
        public void removeProduct(int productId) {
            handler.deleteCartProduct(productId);
            fetchCart();
        }
    };

    ItemCartView.OnValueChangeListener onValueChangeListener = new ItemCartView.OnValueChangeListener() {
        @Override
        public void onChange() {
            fetchCart();
        }
    };
}
