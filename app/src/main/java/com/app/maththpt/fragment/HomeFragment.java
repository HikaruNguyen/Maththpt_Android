package com.app.maththpt.fragment;


import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.app.maththpt.R;
import com.app.maththpt.widget.CustomeWebView;

/**
 * A simple {@link Fragment} subclass.
 */
public class HomeFragment extends Fragment {
    private CustomeWebView webView;
    private Button btnCheck;
    private ProgressDialog progressDialog;

    public HomeFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_question, container, false);
        initUI(view);
        bindData();
        event();
        return view;
    }

    private void event() {
        btnCheck.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String loadingUrl = "javascript:checkAnswer1()";
                webView.loadUrl(loadingUrl);
            }
        });
    }

    private void bindData() {
        webView.loadUrl("file:///android_asset/MathView/demo.html");
    }

    private void initUI(View view) {
        webView = (CustomeWebView) view.findViewById(R.id.webView);
        btnCheck = (Button) view.findViewById(R.id.btnCheck);
    }


}
