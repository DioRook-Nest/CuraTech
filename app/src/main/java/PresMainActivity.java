package com.example.ishmaamin.curatech;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

public class PresMainActivity extends AppCompatActivity {
    private Toolbar mainToolbar;
    private FirebaseAuth mAuth;
    private FirebaseFirestore firebaseFirestore;
    private String current_user_id;
    private HomeFrag Home;
    private NotFrag Noti;
    private DocFrag Doc;


    private BottomNavigationView mainBottom;
    private FloatingActionButton add;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);

        mainToolbar = (Toolbar) findViewById(R.id.mainToolbar);
        setSupportActionBar(mainToolbar);
        getSupportActionBar().setTitle("Prescriptions");

        mAuth=FirebaseAuth.getInstance();
        firebaseFirestore=FirebaseFirestore.getInstance();
        add=findViewById(R.id.add);


        if(mAuth.getCurrentUser()!=null){

            Home=new HomeFrag();
            Doc=new DocFrag();
            Noti=new NotFrag();
            mainBottom=findViewById(R.id.mainBottom);
            initializeFragment();
            mainBottom.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
                @Override
                public boolean onNavigationItemSelected(@NonNull MenuItem item) {

                    Fragment currentFragment = getSupportFragmentManager().findFragmentById(R.id.maincont);

                    switch (item.getItemId()) {

                        case R.id.bothome:

                            replaceFragment(Home, currentFragment);
                            return true;


                        case R.id.botnot:

                            replaceFragment(Noti, currentFragment);
                            return true;

                        default:
                            return false;


                    }

                }
            });


            add.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent=new Intent(PresMainActivity.this,newQ.class);
                    startActivity(intent);
                }
            });

        }



    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        getMenuInflater().inflate(R.menu.main_menu1,menu);


        return true;

    }
    @Override
    protected void onStart() {
        super.onStart();

        FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();
        if(currentUser == null){

            //toLogin();

        } else {

            current_user_id = mAuth.getCurrentUser().getUid();

            firebaseFirestore.collection("Users").document(current_user_id).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                @Override
                public void onComplete(@NonNull Task<DocumentSnapshot> task) {

                    if(task.isSuccessful()){

                        if(!task.getResult().exists()){

                           // Intent setupIntent = new Intent(PresMainActivity.this, Setup.class);
                           // startActivity(setupIntent);
                           // finish();

                        }

                    } else {

                        String errorMessage = task.getException().getMessage();
                        Toast.makeText(PresMainActivity.this, "Error : " + errorMessage, Toast.LENGTH_LONG).show();


                    }

                }
            });

        }

    }



    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()){
            case R.id.logout:
                logout();
                return true;


            case R.id.accSet:
                toSettings();


            default:
                return false;


        }
    }

    private void toSettings() {
        Intent intent = new Intent(PresMainActivity.this,SettingsActivity.class);
        startActivity(intent);
    }

    private void logout() {

        mAuth.signOut();
        //toLogin();
    }

    /*private void toLogin() {

        Intent intent = new Intent(PresMainActivity.this,loginActivity.class);
        startActivity(intent);
        finish();
    }*/

    private void initializeFragment(){

        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();

        fragmentTransaction.add(R.id.maincont, Home);
        fragmentTransaction.add(R.id.maincont, Noti);

        fragmentTransaction.hide(Noti);
        fragmentTransaction.commit();

    }

    private void replaceFragment(Fragment fragment, Fragment currentFragment){

        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        if(fragment == Home){

            fragmentTransaction.hide(Noti);

        }


        if(fragment == Noti){

            fragmentTransaction.hide(Home);

        }
        fragmentTransaction.show(fragment);

        //fragmentTransaction.replace(R.id.main_container, fragment);
        fragmentTransaction.commit();

    }

}
