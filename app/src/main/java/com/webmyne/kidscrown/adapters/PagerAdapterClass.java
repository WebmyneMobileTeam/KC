package com.webmyne.kidscrown.adapters;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import com.webmyne.kidscrown.fragment.ExistingAddressTab;
import com.webmyne.kidscrown.fragment.NewAddressTab;

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
                ExistingAddressTab tab1 = new ExistingAddressTab();
                return tab1;
            case 1:
                NewAddressTab tab2 = new NewAddressTab();
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
