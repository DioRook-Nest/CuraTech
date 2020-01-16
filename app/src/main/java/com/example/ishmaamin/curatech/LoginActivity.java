package com.example.ishmaamin.curatech;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

public class LoginActivity extends AppCompatActivity {
    private EditText loginEmailText;
    private EditText loginPassText;
    private Button loginBtn;
    private Button loginRegBtn;
    private FirebaseAuth mAuth;
    private ProgressBar loginProgress;
    private DrawerLayout mDrawerLayout;

    private FirebaseFirestore firebaseFirestore;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        mAuth=FirebaseAuth.getInstance();

        firebaseFirestore=FirebaseFirestore.getInstance();
        loginEmailText=(EditText) findViewById(R.id.login_email);
        loginPassText=(EditText) findViewById(R.id.login_password);
        loginBtn=(Button) findViewById(R.id.login_btn);
        loginRegBtn=(Button) findViewById(R.id.reg_btn);
        loginProgress=(ProgressBar) findViewById(R.id.login_progress);

       loginBtn.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(final View view) {


String loginEmail=loginEmailText.getText().toString();
String loginPassword=loginPassText.getText().toString();
if(!TextUtils.isEmpty(loginEmail)&&!TextUtils.isEmpty(loginPassword))
{
    loginProgress.setVisibility(View.VISIBLE);
    mAuth.signInWithEmailAndPassword(loginEmail,loginPassword).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
     @Override
     public void onComplete(@NonNull Task<AuthResult> task) {
         if(task.isSuccessful())
         {

View();
         }
         else
         {
             String errorMessage= task.getException().getMessage();
             Toast.makeText(LoginActivity.this, "Error:"+ errorMessage,Toast.LENGTH_LONG).show();
             loginProgress.setVisibility(View.INVISIBLE);
         }

     }
                                                                                     }
    );


   }
           }
       });
loginRegBtn.setOnClickListener(new View.OnClickListener() {
    @Override
    public void onClick(View view) {
       Intent regintent=new Intent (LoginActivity.this,RegisterActivity.class);
       startActivity(regintent);
       finish();

    }
});
    }


    protected void onStart()
    {
        super.onStart();
        FirebaseUser currentUser = mAuth.getCurrentUser();
        if(currentUser!=null)
        {
            sendToMain();
        }

    }

    private void sendToMain()
    {
        Intent mainIntent= new Intent(LoginActivity.this,UMainActivity.class);
        startActivity(mainIntent);
        finish();

    }
    public void  View()
    {
        String user_id =mAuth.getCurrentUser().getUid();
        firebaseFirestore.collection("Users").document(user_id).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if(task.isSuccessful())
                {
                    DocumentSnapshot document=task.getResult();
                    String value = document.getString("type");
                    Log.i("LOGGER",value);
                    //if(value.equals("Doctor"))
                    {
                        Intent mainIntent =new Intent(LoginActivity.this,UMainActivity.class);
                        startActivity(mainIntent);
                        finish();
                    }
                    /*else if(value.equals("User"))
                    {
                        Intent postIntent =new Intent(LoginActivity.this,BlogMainActivity.class);

                        startActivity(postIntent);
                        finish();

                    }*/
                }

            }
        });
    }

}
