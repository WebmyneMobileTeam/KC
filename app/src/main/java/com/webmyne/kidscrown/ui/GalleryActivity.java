package com.webmyne.kidscrown.ui;

import android.content.Context;
import android.database.Cursor;
import android.graphics.Color;
import android.support.design.widget.Snackbar;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.bumptech.glide.Glide;
import com.webmyne.kidscrown.R;
import com.webmyne.kidscrown.helper.DatabaseHandler;

import java.util.ArrayList;

public class GalleryActivity extends AppCompatActivity {
    Toolbar toolbar;
    ViewPager viewPager;
    int productID;
    Cursor cursorProductImage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gallery);
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        if (toolbar != null) {
            toolbar.setTitle("");
            setSupportActionBar(toolbar);
        }
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
                overridePendingTransition(R.anim.abc_fade_in, R.anim.abc_fade_out);
            }
        });

        toolbar.setBackgroundColor(Color.TRANSPARENT);
        ImageView imgCartMenu = (ImageView) toolbar.findViewById(R.id.imgCartMenu);
        imgCartMenu.setVisibility(View.INVISIBLE);
        viewPager = (ViewPager) findViewById(R.id.viewPagerImages);

        int productID = getIntent().getIntExtra("id", 0);
        DatabaseHandler handler = new DatabaseHandler(GalleryActivity.this);
        cursorProductImage = handler.getProductImageCursor("" + productID);
        handler.close();
        displayAdapter(cursorProductImage);


    }

    private void displayAdapter(Cursor cursorProductImage) {

        ArrayList<String> paths = new ArrayList<>();
        cursorProductImage.moveToFirst();
        do {
            String imagepath = cursorProductImage.getString(cursorProductImage.getColumnIndexOrThrow("path"));
            paths.add(imagepath);
        } while (cursorProductImage.moveToNext());

        CustomPagerAdapter adapter = new CustomPagerAdapter(GalleryActivity.this, paths);
        viewPager.setAdapter(adapter);
        int pos = getIntent().getIntExtra("pos", 0);
        viewPager.setCurrentItem(pos);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        overridePendingTransition(R.anim.abc_fade_in, R.anim.abc_fade_out);
    }

    class CustomPagerAdapter extends PagerAdapter {

        Context mContext;
        LayoutInflater mLayoutInflater;
        ArrayList<String> paths;

        public CustomPagerAdapter(Context context, ArrayList<String> paths) {
            mContext = context;
            mLayoutInflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            this.paths = paths;
        }

        @Override
        public int getCount() {
            return paths.size();
        }

        @Override
        public boolean isViewFromObject(View view, Object object) {
            return view == ((LinearLayout) object);
        }

        @Override
        public Object instantiateItem(ViewGroup container, int position) {
            View itemView = mLayoutInflater.inflate(R.layout.item_viewpager, container, false);

            ImageView imageView = (ImageView) itemView.findViewById(R.id.imageView);
            Glide.with(mContext).load(paths.get(position)).into(imageView);
            container.addView(itemView);

            return itemView;
        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            container.removeView((LinearLayout) object);
        }
    }
}
