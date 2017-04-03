package com.app.maththpt.widget;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Bitmap;
import android.util.AttributeSet;
import android.webkit.JavascriptInterface;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.app.maththpt.utils.Utils;
import com.pnikosis.materialishprogress.ProgressWheel;


/**
 * Created by manhi on 31/1/2016.
 */
public class CustomeWebView extends WebView {
    private static final String TAG = CustomeWebView.class.getSimpleName();
    private int type;
    private Context context;
    public int answer;

    public ProgressWheel getProgress_wheel() {
        return progress_wheel;
    }

    public void setProgress_wheel(ProgressWheel progress_wheel) {
        this.progress_wheel = progress_wheel;
    }

    private ProgressWheel progress_wheel;

    public int getPosition() {
        return position;
    }

    public void setPosition(int position) {
        this.position = position;
    }

    private int position;

    public void setType(int type) {
        this.type = type;
    }

    public int getType() {
        return type;
    }

    public CustomeWebView(Context context) {
        super(context);
        this.context = context;
        init();
    }

    public CustomeWebView(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.context = context;
        init();
    }

    public CustomeWebView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        this.context = context;
        init();
    }

    @SuppressLint({"SetJavaScriptEnabled", "AddJavascriptInterface"})
    private void init() {
        this.setWebViewClient(new MyBrowser());
        WebSettings webSettings = this.getSettings();
        webSettings.setLoadsImagesAutomatically(true);
        webSettings.setJavaScriptEnabled(true);
        webSettings.setLayoutAlgorithm(WebSettings.LayoutAlgorithm.SINGLE_COLUMN);
        webSettings.setDefaultTextEncodingName("utf-8");
        webSettings.setLoadWithOverviewMode(true);
//        Resources res = getResources();
//        float fontSize = res.getDimension(R.dimen.text_size_14sp);
//        if (Utils.isTablet(getContext())) {
//            fontSize = res.getDimension(R.dimen.text_size_20sp);
//        }
//        webSettings.setDefaultFontSize((int) fontSize);
        if(Utils.isTablet(getContext())){
            webSettings.setTextSize(WebSettings.TextSize.LARGER);
        }else{
            webSettings.setTextSize(WebSettings.TextSize.NORMAL);
        }

        webSettings.setUserAgentString(
                "Mozilla/5.0 (Linux; <Android Version>; <Build Tag etc.>) " +
                        "AppleWebKit/<WebKit Rev> (KHTML, like Gecko) " +
                        "Chrome/<Chrome Rev> Mobile Safari/<WebKit Rev>6");
        addJavascriptInterface(new WebAppInterface(context), "Android");

    }

    private class MyBrowser extends WebViewClient {

        @Override
        public boolean shouldOverrideUrlLoading(WebView view, String url) {
            view.loadUrl(url);
            return true;
        }

        @Override
        public void onPageStarted(WebView view, String url, Bitmap favicon) {
            super.onPageStarted(view, url, favicon);
//            prbLoading.setVisibility(View.VISIBLE);
            if (progress_wheel != null) {
                progress_wheel.setVisibility(VISIBLE);
            }
        }

        @Override
        public void onPageFinished(WebView view, String url) {
            super.onPageFinished(view, url);
            if (progress_wheel != null) {
                progress_wheel.setVisibility(GONE);
            }

        }


    }

    @Override
    public String getOriginalUrl() {
        return super.getOriginalUrl();
    }

    @Override
    public void loadUrl(String url) {
        super.loadUrl(url);
    }

    private class WebAppInterface {
        Context mContext;

        /**
         * Instantiate the interface and set the context
         */
        WebAppInterface(Context c) {
            mContext = c;
        }

        @JavascriptInterface
        public void storeText(String text) {
            //TODO  save text
//            Toast.makeText(mContext, text, Toast.LENGTH_LONG).show();
            try {
                answer = Integer.parseInt(text);
            } catch (Exception e) {
                e.printStackTrace();
                answer = 0;
            }
        }
    }
}
