package com.webmyne.kidscrown.helper;

import android.animation.ObjectAnimator;
import android.content.Context;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.readystatesoftware.viewbadger.BadgeView;
import com.webmyne.kidscrown.R;
import com.webmyne.kidscrown.model.ProductCart;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;

import fr.castorflex.android.smoothprogressbar.SmoothProgressBar;

/**
 * Created by dhruvil on 18-08-2015.
 */
public class ToolHelper {

    Context _ctx;
    View view;
    ImageView imgCartMenu;
    BadgeView badge;
    int crownProductId;
    SharedPreferences preferences;

    public ToolHelper(Context _ctx, View view) {
        this._ctx = _ctx;
        this.view = view;
        imgCartMenu = (ImageView) view.findViewById(R.id.imgCartMenu);
        badge = new BadgeView(_ctx, imgCartMenu);
        preferences = _ctx.getSharedPreferences("login", Context.MODE_PRIVATE);
        crownProductId = preferences.getInt("crownProductId", 0);

    }

    public void displayBadge() {
        int value = 0;
        ArrayList<Integer> counts = new ArrayList<>();
        ArrayList<ProductCart> products = new ArrayList<>();
        ArrayList<ProductCart> crowns = new ArrayList<>();
        try {
            DatabaseHandler handler = new DatabaseHandler(_ctx);
            handler.openDataBase();
            products = handler.getCartProduct(crownProductId);
            crowns = handler.getCrownCartProduct(crownProductId);
            handler.close();
            if (products.size() != 0) {
                for (int i = 0; i < products.size(); i++) {
                    counts.add(products.get(i).getProductId());
                }
            }
            if (crowns.size() != 0) {
                for (int i = 0; i < crowns.size(); i++) {
                    counts.add(crowns.get(i).getProductId());
                }
            }
            HashSet hs = new HashSet();
            hs.addAll(counts);
            counts.clear();
            counts.addAll(hs);
            value = counts.size();
        } catch (SQLException e) {
            e.printStackTrace();
            Log.e("exception", e.toString());
        }

        if (value > 0) {

            imgCartMenu.setPadding(0, 18, 20, 0);
            badge.setText(String.valueOf(value));
            badge.setTextSize(_ctx.getResources().getDimension(R.dimen.BADGE_TEXT_SIZE));
            badge.setBadgeBackgroundColor(_ctx.getResources().getColor(R.color.quad_orange));
            badge.show();
            ObjectAnimator animator = ObjectAnimator.ofFloat(badge, "rotationY", 0f, 360f);
            animator.setDuration(2500);
            animator.start();
        } else {
            imgCartMenu.setPadding(0, 0, 0, 0);
            badge.hide();
        }

    }

}
