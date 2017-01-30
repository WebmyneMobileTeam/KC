package com.webmyne.kidscrown.helper;

import android.app.Activity;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.mikepenz.actionitembadge.library.ActionItemBadge;
import com.webmyne.kidscrown.R;

/**
 * Created by sagartahelyani on 24-01-2017.
 */
public class BadgeHelper {


    private Activity context;
    private MenuItem menuItem;
    private ActionItemBadge.BadgeStyles badgeStyles;

    public BadgeHelper(Activity context, MenuItem menuItem, ActionItemBadge.BadgeStyles badgeStyles) {
        this.context = context;
        this.menuItem = menuItem;
        this.badgeStyles = badgeStyles;
    }

    public void displayBadge(int count) {

        try {
            if (count > 0) {
                ActionItemBadge.update(context, menuItem, menuItem.getIcon(), badgeStyles, count);

            } else {
                ImageView img = (ImageView) menuItem.getActionView().findViewById(R.id.menu_badge_icon);
                TextView textView = (TextView) menuItem.getActionView().findViewById(R.id.menu_badge);
                img.setImageDrawable(menuItem.getIcon());
                textView.setVisibility(View.GONE);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

    }

}
