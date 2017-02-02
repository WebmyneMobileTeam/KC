package com.webmyne.kidscrown.fragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.webmyne.kidscrown.R;
import com.webmyne.kidscrown.api.CommonRetrofitResponseListener;
import com.webmyne.kidscrown.api.FetchContactUsData;
import com.webmyne.kidscrown.model.ContactUsResponseModel;
import com.webmyne.kidscrown.ui.MyDrawerActivity;

public class ContactUsFragment extends Fragment {

    private LinearLayout parentView;
    private LinearLayout.LayoutParams params;

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

        params = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        parentView = (LinearLayout) view.findViewById(R.id.parentView);

        fetchAboutUsData();
    }

    private void fetchAboutUsData() {

        new FetchContactUsData(getActivity(), new CommonRetrofitResponseListener() {
            @Override
            public void onSuccess(Object responseBody) {

                ContactUsResponseModel responseModel = (ContactUsResponseModel) responseBody;

                parentView.removeAllViews();

                for (int i = 0; i < responseModel.getData().size(); i++) {

                    View view = LayoutInflater.from(getActivity()).inflate(R.layout.row_contact_us, null, false);

                    TextView tvTitle = (TextView) view.findViewById(R.id.tvTitle);
                    TextView tvDesc = (TextView) view.findViewById(R.id.tvDesc);

                    tvTitle.setText(responseModel.getData().get(i).getPageName());
                    tvDesc.setText(Html.fromHtml(responseModel.getData().get(i).getDescription()));

                    parentView.addView(view, params);
                }
            }

            @Override
            public void onFail() {

            }
        });
    }
}