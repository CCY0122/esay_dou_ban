package com.example.materialdesigntest.view;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.Window;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ProgressBar;

import com.example.materialdesigntest.R;

/**
 * Created by Administrator on 2016/12/17.
 */

public class WebActivity extends AppCompatActivity {

    private WebView webView;
    private ProgressBar pb;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.web_activity);
        getSupportActionBar().hide();

        Intent intent = getIntent();
        String url = intent.getStringExtra("url");

        pb = (ProgressBar) findViewById(R.id.web_pb);
        webView = (WebView) findViewById(R.id.web_view);
        webView.getSettings().setJavaScriptEnabled(true);
        webView.setWebViewClient(new WebViewClient(){
            @Override
            public void onPageStarted(WebView view, String url, Bitmap favicon) {
                super.onPageStarted(view, url, favicon);
                pb.setVisibility(View.VISIBLE);
            }

            @Override
            public void onPageFinished(WebView view, String url) {
                super.onPageFinished(view, url);
                pb.setVisibility(View.INVISIBLE);
            }
        });
        if(url != null){
            webView.loadUrl(url);
        }

    }
}
