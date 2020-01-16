package com.example.ishmaamin.curatech;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.view.GravityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.method.ScrollingMovementMethod;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentChange;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;
import android.app.Activity;
import android.os.Bundle;
import android.webkit.WebSettings;
import android.webkit.WebView;

public class UMainActivity extends AppCompatActivity {
    private Toolbar toolbar;
    private FirebaseAuth mAuth;
    private FloatingActionButton addPostBtn;
    private RecyclerView blog_list_view;
    private List<BlogPost> blog_list;
    private FirebaseFirestore firebaseFirestore;
    private BlogRecyclerAdapter blogRecyclerAdapter;
    private String user_id;

    final FirebaseUser currentUser= FirebaseAuth.getInstance().getCurrentUser();





    @Override
    protected void onCreate(Bundle savedInstanceState) {

        /*if (currentUser == null ) {
            //sentToLogin();

            Intent intent = new Intent(UMainActivity.this, BlogMainActivity.class);
            startActivity(intent);
            finish();
        }*/
        super.onCreate(savedInstanceState);
        setContentView(R.layout.webview);

        WebView webView = (WebView) findViewById(R.id.webview);

        WebSettings webSettings = webView.getSettings();
        webSettings.setJavaScriptEnabled(true);

        WebViewClientImpl webViewClient = new WebViewClientImpl(this);
        webView.setWebViewClient(webViewClient);

        webView.loadUrl("https://www.lybrate.com");



    }


}
