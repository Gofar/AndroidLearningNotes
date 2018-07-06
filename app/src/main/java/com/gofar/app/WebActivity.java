package com.gofar.app;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import org.markdown4j.Markdown4jProcessor;

import java.io.IOException;
import java.io.InputStream;

/**
 * @author lcf
 * @date 2018/7/6 15:01
 * @since 1.0
 */
public class WebActivity extends AppCompatActivity {
    private WebView mWebView;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_web);
        mWebView = findViewById(R.id.web_view);

        init();
        initData();
    }

    private void init() {
        WebSettings webSettings = mWebView.getSettings();
        webSettings.setAllowFileAccess(true);
        webSettings.setSupportZoom(true);
        webSettings.setUseWideViewPort(true);
        webSettings.setLoadWithOverviewMode(true);
        webSettings.setSupportMultipleWindows(true);
        webSettings.setDatabaseEnabled(true);
        webSettings.setDomStorageEnabled(true);
        webSettings.setAppCacheEnabled(true);
        webSettings.setJavaScriptEnabled(true);

        mWebView.setWebChromeClient(new WebChromeClient());
        mWebView.setWebViewClient(new WebViewClient());
    }

    private void initData() {
        try {
            InputStream is = getAssets().open("README.md");
            String html = new Markdown4jProcessor().process(is);
            mWebView.loadDataWithBaseURL(null, html, "text/html", "utf-8", null);
            is.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void initData2(){
        mWebView.loadUrl("https://github.com/Gofar/AndroidLearningNotes/blob/master/README.md");
    }
}
