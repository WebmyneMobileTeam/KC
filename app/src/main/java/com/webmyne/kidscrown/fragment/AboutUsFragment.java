package com.webmyne.kidscrown.fragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.webmyne.kidscrown.R;
import com.webmyne.kidscrown.api.CommonRetrofitResponseListener;
import com.webmyne.kidscrown.api.FetchAboutUsData;
import com.webmyne.kidscrown.model.AboutUsResponseModel;
import com.webmyne.kidscrown.ui.MyDrawerActivity;

public class AboutUsFragment extends Fragment {

    private TextView tvAboutUs;
    private ImageView ivImage;

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
        ((MyDrawerActivity) getActivity()).setTitle(getActivity().getString(R.string.about_us));

        init(view);

        return view;
    }

    private void init(View view) {

        tvAboutUs = (TextView) view.findViewById(R.id.tvAboutUs);
        ivImage = (ImageView) view.findViewById(R.id.ivImage);

        fetchAboutUsData();
    }

    private void fetchAboutUsData() {

        new FetchAboutUsData(getActivity(), new CommonRetrofitResponseListener() {
            @Override
            public void onSuccess(Object responseBody) {

                AboutUsResponseModel responseModel = (AboutUsResponseModel) responseBody;

                Glide.with(getActivity()).load(responseModel.getData().getHeaderImage())
                        .diskCacheStrategy(DiskCacheStrategy.ALL)
                        .into(ivImage);

                tvAboutUs.setText(Html.fromHtml(responseModel.getData().getDescription()));

            }

            @Override
            public void onFail() {

            }
        });
    }

}
