package com.webmyne.kidscrown.fragment;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.Html;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.webmyne.kidscrown.R;
import com.webmyne.kidscrown.api.CommonRetrofitResponseListener;
import com.webmyne.kidscrown.api.FetchContactUsData;
import com.webmyne.kidscrown.helper.Functions;
import com.webmyne.kidscrown.model.ContactUsResponseModel;
import com.webmyne.kidscrown.ui.MyDrawerActivity;

public class ContactUsFragment extends Fragment {

    //    private TextView tvContactUs, tvBankDetail;
    private ProgressDialog pd;
    private LinearLayout parentView;

    public static ContactUsFragment newInstance(String param1, String param2) {
        ContactUsFragment fragment = new ContactUsFragment();

        return fragment;
    }

    public ContactUsFragment() {
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
        View view = inflater.inflate(R.layout.fragment_contact_us, container, false);
        ((MyDrawerActivity) getActivity()).setTitle(getActivity().getString(R.string.contact_us));

        init(view);

        return view;
    }

    private void init(View view) {

//        tvContactUs = (TextView) view.findViewById(R.id.tvContactUs);
//        tvBankDetail = (TextView) view.findViewById(R.id.tvBankDetail);
        parentView = (LinearLayout) view.findViewById(R.id.parentView);


        fetchAboutUsData();
    }

    private void fetchAboutUsData() {

        pd = ProgressDialog.show(getActivity(), getActivity().getString(R.string.loading), getActivity().getString(R.string.please_wait), true);

        new FetchContactUsData(getActivity(), new CommonRetrofitResponseListener() {
            @Override
            public void onSuccess(Object responseBody) {

                pd.dismiss();

                ContactUsResponseModel responseModel = (ContactUsResponseModel) responseBody;

                Log.e("tag", "responseModel: " + Functions.jsonString(responseModel));

                parentView.removeAllViews();

                for (int i = 0; i < responseModel.getData().size(); i++) {

                    View view = LayoutInflater.from(getActivity()).inflate(R.layout.row_contact_us, null, false);

                    TextView tvTitle = (TextView) view.findViewById(R.id.tvTitle);
                    TextView tvDesc = (TextView) view.findViewById(R.id.tvDesc);

                    tvTitle.setText(responseModel.getData().get(i).getPageName());
                    tvDesc.setText(Html.fromHtml(responseModel.getData().get(i).getDescription()));

                    parentView.addView(view);
                }


            }

            @Override
            public void onFail() {

                pd.dismiss();

            }
        });

    }


}
