package com.example.ishmaamin.curatech;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.CardView;
import android.view.View;
import android.widget.GridLayout;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class MenuActivity extends AppCompatActivity {

    GridLayout mainGrid1;
    private FirebaseAuth mAuth;
    final FirebaseUser currentUser= FirebaseAuth.getInstance().getCurrentUser();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_page2);

        mainGrid1= (GridLayout) findViewById(R.id.mainGrid1);
        setEvent(mainGrid1);
    }

    private void setEvent(GridLayout mainGrid1) {

        for (int i = 0; i < mainGrid1.getChildCount(); i++) {
            CardView cardview = (CardView) mainGrid1.getChildAt(i);
            final int finalI = i;
            cardview.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(finalI==0) {
                        if (currentUser == null)
                            sentToLogin();
                        else {

                            Intent intent = new Intent(MenuActivity.this, BlogMainActivity.class);
                            startActivity(intent);
                        }
                    }

                    else if(finalI==1) {
                        if (currentUser == null)
                            sentToLogin();
                        else
                            ViewPres();
                    }

                    else if(finalI==2) {
                        if (currentUser == null)
                            sentToLogin();
                        else
                            ViewEhr();
                    }

                    else if(finalI==3) {
                        if (currentUser == null)
                            sentToLogin();
                        else
                            ViewChat();
                    }
                }

            });
        }
    }

    private void ViewPres() {
        Intent intent=new Intent(MenuActivity.this,PresMainActivity.class);
        startActivity(intent);

    }

    public void logout()
    {
        mAuth.signOut();
        sentToLogin();

    }
    public void sentToLogin()
    {
        Intent intent=new Intent(MenuActivity.this,LoginActivity.class);
        startActivity(intent);
        finish();
    }

    public void  ViewEhr() {
        Intent m1Intent = new Intent(MenuActivity.this, EhrInsertActivity.class);
        startActivity(m1Intent);
        //finish();
    }

    public void  ViewChat() {
        Intent m1Intent = new Intent(MenuActivity.this, ChatMainActivity.class);
        startActivity(m1Intent);
        //finish();
    }
}
