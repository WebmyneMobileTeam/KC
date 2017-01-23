package com.webmyne.kidscrown.fragment;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.Html;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.webmyne.kidscrown.R;
import com.webmyne.kidscrown.api.CommonRetrofitResponseListener;
import com.webmyne.kidscrown.api.FetchAboutUsData;
import com.webmyne.kidscrown.helper.Functions;
import com.webmyne.kidscrown.model.AboutUsResponseModel;
import com.webmyne.kidscrown.ui.MyDrawerActivity;

public class AboutUsFragment extends Fragment {

    private TextView tvAboutUs;
    private ProgressDialog pd;
    private ImageView ivImage;
    private LinearLayout parentView;

    public static AboutUsFragment newInstance(String param1, String param2) {
        AboutUsFragment fragment = new AboutUsFragment();

        return fragment;
    }

    public AboutUsFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_about, container, false);
        ((MyDrawerActivity) getActivity()).setTitle("About Us");

        init(view);

        return view;
    }

    private void init(View view) {

        tvAboutUs = (TextView) view.findViewById(R.id.tvAboutUs);
        ivImage = (ImageView) view.findViewById(R.id.ivImage);
        parentView = (LinearLayout) view.findViewById(R.id.parentView);


        fetchAboutUsData();
    }

    private void fetchAboutUsData() {

        pd = ProgressDialog.show(getActivity(), "Loading", "Please wait..", true);

        new FetchAboutUsData(getActivity(), new CommonRetrofitResponseListener() {
            @Override
            public void onSuccess(Object responseBody) {

                pd.dismiss();

                AboutUsResponseModel responseModel = (AboutUsResponseModel) responseBody;

                Log.e("tag", "responseModel: " + Functions.jsonString(responseModel));

                Glide.with(getActivity()).load(responseModel.getHeaderImage())
//                        .thumbnail(0.5f)
                        .diskCacheStrategy(DiskCacheStrategy.ALL)
                        .into(ivImage);

                tvAboutUs.setText(Html.fromHtml(responseModel.getDescription()));

            }

            @Override
            public void onFail() {

                pd.dismiss();

            }
        });

    }


}
