package com.webmyne.kidscrown.adapters;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import com.webmyne.kidscrown.fragment.BillingAddressTab;
import com.webmyne.kidscrown.fragment.ShippingAddressTab;

/**
 * Created by sagartahelyani on 20-08-2015.
 */
public class PagerAdapterClass extends FragmentStatePagerAdapter {
    int mNumOfTabs;

    public PagerAdapterClass(FragmentManager fm, int NumOfTabs) {
        super(fm);
        this.mNumOfTabs = NumOfTabs;
    }

    @Override
    public Fragment getItem(int position) {

        switch (position) {
            case 0:
                BillingAddressTab tab1 = new BillingAddressTab();
                return tab1;
            case 1:
                ShippingAddressTab tab2 = new ShippingAddressTab();
                return tab2;
            default:
                return null;
        }
    }

    @Override
    public int getCount() {
        return mNumOfTabs;
    }
}
