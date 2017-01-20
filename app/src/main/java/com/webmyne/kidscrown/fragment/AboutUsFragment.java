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
//        Functions.logE("about request url", Constants.ABOUT_US_URL);

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


//        new CallWebService(Constants.ABOUT_US_URL, CallWebService.TYPE_GET) {
//            @Override
//            public void response(String response) {
//                pd.dismiss();
//                Log.e("aboutus response", response + "");
//                try {
//
//                    AboutUsResponseModel requestModel = new GsonBuilder().create().fromJson(response, AboutUsResponseModel.class);
//
//                    if (requestModel.getResponse().getResponseCode() == Constants.SUCCESS) {
//
//                        tvAboutUs.setText(requestModel.getDescription());
//
//                    } else {
//
//                        pd.dismiss();
//                        Snackbar snack = Snackbar.make(parentView, requestModel.getResponse().getResponseMsg(), Snackbar.LENGTH_LONG);
//                        View view = snack.getView();
//                        TextView tv = (TextView) view.findViewById(android.support.design.R.id.snackbar_text);
//                        tv.setTextSize(Functions.convertPixelsToDp(getResources().getDimension(R.dimen.S_TEXT_SIZE), getActivity()));
//                        snack.show();
//                    }
//
//                } catch (Exception e) {
//                    Snackbar snack = Snackbar.make(parentView, "Unable Fetch Data", Snackbar.LENGTH_LONG);
//                    View view = snack.getView();
//                    TextView tv = (TextView) view.findViewById(android.support.design.R.id.snackbar_text);
//                    tv.setTextSize(Functions.convertPixelsToDp(getResources().getDimension(R.dimen.S_TEXT_SIZE), getActivity()));
//                    snack.show();
//                }
//            }
//
//            @Override
//            public void error(String error) {
//                pd.dismiss();
//                Snackbar snack = Snackbar.make(parentView, "Unable Fetch Data. " + error, Snackbar.LENGTH_LONG);
//                View view = snack.getView();
//                TextView tv = (TextView) view.findViewById(android.support.design.R.id.snackbar_text);
//                tv.setTextSize(Functions.convertPixelsToDp(getResources().getDimension(R.dimen.S_TEXT_SIZE), getActivity()));
//                snack.show();
//
//                Log.e("error", error);
//            }
//        }.call();

    }


}
