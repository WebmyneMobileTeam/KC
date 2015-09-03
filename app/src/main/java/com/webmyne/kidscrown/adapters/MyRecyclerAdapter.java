package com.webmyne.kidscrown.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.TextView;
import android.widget.Toast;


import com.webmyne.kidscrown.R;
import com.webmyne.kidscrown.helper.Functions;

import java.util.List;

/**
 * Created by priyasindkar on 31-08-2015.
 */
public class MyRecyclerAdapter extends RecyclerView.Adapter<MyRecyclerAdapter.CustomViewHolder> {
    private List<String> feedItemList;
    private Context mContext;

    public void setAddQtyListener(MyRecyclerAdapter.addQtyListener addQtyListener) {
        this.addQtyListener = addQtyListener;
    }

    private addQtyListener addQtyListener;

    public MyRecyclerAdapter(Context context, List<String> feedItemList) {
        this.feedItemList = feedItemList;
        this.mContext = context;
    }

    @Override
    public CustomViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.list_row, null);

        CustomViewHolder viewHolder = new CustomViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(CustomViewHolder customViewHolder, int i) {
        String feedItem = feedItemList.get(i);

        //Setting text view title
        customViewHolder.textView.setText(Html.fromHtml(feedItem));
        customViewHolder.textView.setOnClickListener(clickListener);
        customViewHolder.textView.setTag(customViewHolder);

        setAnimation(customViewHolder.textView, 0);
    }


    @Override
    public int getItemCount() {
        return (null != feedItemList ? feedItemList.size() : 0);
    }

    public class CustomViewHolder extends RecyclerView.ViewHolder {
        protected TextView textView;

        public CustomViewHolder(View view) {
            super(view);
            this.textView = (TextView) view.findViewById(R.id.title);
        }
    }

    View.OnClickListener clickListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            CustomViewHolder holder = (CustomViewHolder) view.getTag();
            int position = holder.getPosition();
            String feedItem = feedItemList.get(position);
            addQtyListener.addQty(feedItem);
        }
    };

    private void setAnimation(View viewToAnimate, int position) {

        Animation animation = AnimationUtils.loadAnimation(mContext, R.anim.abc_slide_in_bottom);
        animation.setDuration(600);
        viewToAnimate.startAnimation(animation);
        //  lastPosition = position;

    }

    public interface addQtyListener {
        public void addQty(String value);
    }

}
