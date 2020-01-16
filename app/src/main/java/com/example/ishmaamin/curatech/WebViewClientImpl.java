package com.example.ishmaamin.curatech;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.webkit.WebView;
import android.webkit.WebViewClient;


public class WebViewClientImpl extends WebViewClient {

    private Activity activity = null;
    //private Context context;

    public WebViewClientImpl(Activity activity) {
        this.activity = activity;
    }

    @Override
    public boolean shouldOverrideUrlLoading(WebView webView, String url) {


        if(url.equals("http://gmcjjh.org/aboutus.php?id=25")){
            Intent i=new Intent(activity,BlogMainActivity.class);
            activity.startActivity(i);
        }

        if(url.equals("www.curatech.com")){
            Intent i=new Intent(activity,BlogMainActivity.class);
            activity.startActivity(i);
        }

            if(url.indexOf("http://gmcjjh.org") > -1 ) return false;
        Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
        activity.startActivity(intent);
        return true;
    }

}