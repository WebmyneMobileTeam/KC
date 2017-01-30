package com.webmyne.kidscrown.ui;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.webmyne.kidscrown.R;
import com.webmyne.kidscrown.api.CommonRetrofitResponseListener;
import com.webmyne.kidscrown.api.PlaceOrderApi;
import com.webmyne.kidscrown.custom.EmptyLayout;
import com.webmyne.kidscrown.custom.familiarrecyclerview.FamiliarRecyclerView;
import com.webmyne.kidscrown.helper.DatabaseHandler;
import com.webmyne.kidscrown.helper.Functions;
import com.webmyne.kidscrown.helper.PrefUtils;
import com.webmyne.kidscrown.model.CartProduct;
import com.webmyne.kidscrown.model.PlaceOrderRequest;
import com.webmyne.kidscrown.model.PlaceOrderResponse;
import com.webmyne.kidscrown.ui.widgets.ItemCartView;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cart_revised);

        init();
    }

    private void init() {
        handler = new DatabaseHandler(this);
        params = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);

        linearKit = (LinearLayout) findViewById(R.id.linearKit);
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

        fetchCart();

        clickListener();
    }

    private void clickListener() {
        btnCheckout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                doCallApi();
            }
        });
    }

    private void doCallApi() {

        PlaceOrderRequest request = new PlaceOrderRequest();
        request.setBillingAddressDC(new PlaceOrderRequest.BillingAddressDCBean());
        request.setUserID(PrefUtils.getUserId(this));

        PlaceOrderResponse.DataBean.PlaceOrderCalculationDCBean placeOrderCalculationDCBean = new PlaceOrderResponse.DataBean.PlaceOrderCalculationDCBean();
        request.setPlaceOrderCalculationDC(new PlaceOrderRequest.PlaceOrderCalculationDCBean());
        request.setShippingAddressDC(new PlaceOrderRequest.ShippingAddressDCBean());

        List<PlaceOrderRequest.PlaceOrderProductDCBean> placeOrderProductDC = new ArrayList<>();

        for (int i = 0; i < cartProducts.size(); i++) {
            PlaceOrderRequest.PlaceOrderProductDCBean bean = new PlaceOrderRequest.PlaceOrderProductDCBean();
            bean.setProductID(cartProducts.get(i).getProductId());
            bean.setQuntity(cartProducts.get(i).getQty());
            placeOrderProductDC.add(bean);
        }
        request.setPlaceOrderProductDC(placeOrderProductDC);

        Log.e("req", Functions.jsonString(request));
        new PlaceOrderApi(this, new CommonRetrofitResponseListener() {
            @Override
            public void onSuccess(Object responseBody) {
                PlaceOrderResponse placeOrderResponse = (PlaceOrderResponse) responseBody;
                Log.e("res", Functions.jsonString(placeOrderResponse));
            }

            @Override
            public void onFail() {

            }
        }, request);
    }

    private void fetchCart() {
        totalPrice = 0;

        linearKit.removeAllViews();
        linearKit.invalidate();

        cartProducts = new ArrayList<>();
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
                ItemCartView cartView = new ItemCartView(this, cartProducts.get(i));
                cartView.setOnRemoveProductListener(onRemoveProductListener);
                cartView.setOnValueChangeListener(onValueChangeListener);
                linearKit.addView(cartView, params);
            }

            txtTotalPrice.setText(String.format(Locale.US, "%s %s", getString(R.string.Rs), Functions.priceFormat(totalPrice)));
        }
    }

    ItemCartView.onRemoveProductListener onRemoveProductListener = new ItemCartView.onRemoveProductListener() {
        @Override
        public void removeProduct(int productId) {
            try {
                DatabaseHandler handler = new DatabaseHandler(CartActivityRevised.this);
                handler.openDataBase();
                handler.deleteCartProduct(productId);
                handler.close();
                fetchCart();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    };

    ItemCartView.OnValueChangeListener onValueChangeListener = new ItemCartView.OnValueChangeListener() {
        @Override
        public void onChange() {
            fetchCart();
        }
    };
}
