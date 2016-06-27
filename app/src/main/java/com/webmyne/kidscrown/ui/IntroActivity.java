package com.webmyne.kidscrown.ui;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.webmyne.kidscrown.R;
import com.webmyne.kidscrown.helper.Functions;
import com.webmyne.kidscrown.intropager.CustomViewPager;
import com.webmyne.kidscrown.intropager.PageIndicator;

public class IntroActivity extends AppCompatActivity {

    private CustomViewPager viewPager;
    private PageIndicator pageIndicator;
    private int[] images = {R.drawable.login, R.drawable.signup, R.drawable.dashboard,
            R.drawable.product, R.drawable.refill, R.drawable.cart, R.drawable.address, R.drawable.payment};
    private String[] text;
    private TextView txtSkip;
    private SharedPreferences preferences;
    private SharedPreferences.Editor editor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_intro);

        preferences = getSharedPreferences("login", MODE_PRIVATE);

        init();

    }

    private void init() {
        txtSkip = (TextView) findViewById(R.id.txtSkip);
        viewPager = (CustomViewPager) findViewById(R.id.viewPager);
        pageIndicator = (PageIndicator) findViewById(R.id.pageIndicator);

        if (preferences.contains("isUserLogin")) {
            txtSkip.setText("Back");
        } else {
            txtSkip.setText("Skip");
        }

        txtSkip.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (preferences.contains("isUserLogin")) {
                    txtSkip.setText("Back");
                    finish();

                } else {
                    editor = preferences.edit();
                    editor.putBoolean("isSplash", true);
                    editor.apply();

                    Functions.fireIntent(IntroActivity.this, LoginActivity.class);
                    finish();
                }
            }
        });

        initPager();
    }

    private void initPager() {
        text = getResources().getStringArray(R.array.intro_text);
        TabsPagerAdapter adapter = new TabsPagerAdapter(this, images, text);
        viewPager.setAdapter(adapter);
        pageIndicator.setViewPager(viewPager);

        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                if (position == images.length-1) {
                    txtSkip.setText("Done");
                } else {
                    txtSkip.setText("Skip");
                }
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
    }

    public class TabsPagerAdapter extends PagerAdapter {

        Context context;
        LayoutInflater mLayoutInflater;
        int[] imageArray;
        String[] textArray;

        public TabsPagerAdapter(Context context, int[] imageArray, String[] textArray) {
            this.context = context;
            this.imageArray = imageArray;
            this.textArray = textArray;
            mLayoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        }

        @Override
        public int getCount() {
            return imageArray.length;
        }

        @Override
        public boolean isViewFromObject(View view, Object object) {
            return view == object;
        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            container.removeView((View) object);
        }

        @Override
        public Object instantiateItem(ViewGroup container, int position) {
            View itemView = mLayoutInflater.inflate(R.layout.fragment_slider, container, false);

            ImageView slide_image = (ImageView) itemView.findViewById(R.id.slide_image);
            slide_image.setImageResource(imageArray[position]);

            TextView slide_text = (TextView) itemView.findViewById(R.id.slide_text);
            slide_text.setText(textArray[position]);

            container.addView(itemView);
            return itemView;
        }
    }
}
