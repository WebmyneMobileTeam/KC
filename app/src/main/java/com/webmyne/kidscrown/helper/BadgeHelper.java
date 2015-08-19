package com.webmyne.kidscrown.helper;

import android.animation.ObjectAnimator;
import android.content.Context;
import android.view.View;
import android.widget.ImageView;

import com.readystatesoftware.viewbadger.BadgeView;
import com.webmyne.kidscrown.R;

/**
 * Created by dhruvil on 18-08-2015.
 */
public class BadgeHelper {

    Context _ctx;
    View view;

    public BadgeHelper(Context _ctx, View view) {
        this._ctx = _ctx;
        this.view = view;
    }

    public void displayBadge(int value){

        ImageView imgCartMenu = (ImageView)view.findViewById(R.id.imgCartMenu);

        if(value>0){

            imgCartMenu.setPadding(0, 0, 32, 0);
            BadgeView badge = new BadgeView(_ctx, imgCartMenu);
            badge.setText(String.valueOf(value));
            badge.setBadgeBackgroundColor(_ctx.getResources().getColor(R.color.quad_orange));
            badge.show();

            ObjectAnimator animator = ObjectAnimator.ofFloat(badge,"rotationY",0f,360f);
            animator.setDuration(2500);
            animator.start();


        }else{

            imgCartMenu.setPadding(0, 0, 0, 0);
        }

    }

}
