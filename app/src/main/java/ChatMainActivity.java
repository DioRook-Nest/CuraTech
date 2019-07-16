package com.example.ishmaamin.curatech;


import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ServerValue;

public class ChatMainActivity extends AppCompatActivity {

    private FirebaseAuth mAuth;
    private Toolbar mToolbar;
    private ViewPager mViewPager;
    private SectionsPagerAdapter mSectionsPagerAdapter;
    private TabLayout mTabLayout;
    private DatabaseReference mUserref;
    @Override
    protected void onCreate(Bundle savedInstanceState) {


        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_chat);

        mAuth= FirebaseAuth.getInstance();

        //if (mAuth.getCurrentUser()!=null)
            mUserref= FirebaseDatabase.getInstance().getReference().child("Users").child(mAuth.getCurrentUser().getUid());


        mToolbar=(Toolbar) findViewById(R.id.main_page_toolbar);
        setSupportActionBar(mToolbar);
        getSupportActionBar().setTitle("Chat");


        mViewPager=(ViewPager) findViewById(R.id.main_tabPager);
        mSectionsPagerAdapter= new SectionsPagerAdapter(getSupportFragmentManager());
        mViewPager.setAdapter(mSectionsPagerAdapter);
        mTabLayout= (TabLayout) findViewById(R.id.main_tabs);
        mTabLayout.setupWithViewPager(mViewPager);




    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

     super.onOptionsItemSelected(item);

                if (item.getItemId()==R.id.main_logout_btn) {
                    FirebaseAuth.getInstance().signOut();
                    sendToStart();
                }

                if (item.getItemId()==R.id.main_all_btn){
                    Intent usersIntent= new Intent(ChatMainActivity.this,UsersActivity.class);
                    startActivity(usersIntent);
                }
                if (item.getItemId()==R.id.main_settings_btn){
                    Intent usersIntent= new Intent(ChatMainActivity.this,SettingsActivity.class);
                    startActivity(usersIntent);
                }


        return true;

    }



    @Override
    public void onStart() {
        super.onStart();
        FirebaseUser currentUser=mAuth.getCurrentUser();

        if (currentUser ==null){
            //sendToStart();
        }else
        {
            mUserref.child("online").setValue("true");
        }

    }

    private void sendToStart() {
        Intent startIntent = new Intent(ChatMainActivity.this, StartActivity.class);
        new StartActivity(startIntent);
        finish();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu){
        super.onCreateOptionsMenu(menu);

        getMenuInflater().inflate(R.menu.menu_main_chat,menu);

        return true;
    }

    @Override
    protected  void onStop(){

        super.onStop();
        FirebaseUser currentUser=mAuth.getCurrentUser();
        //if (currentUser!=null)
        mUserref.child("online").setValue(ServerValue.TIMESTAMP);

    }



}
