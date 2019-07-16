package com.example.ishmaamin.curatech;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

public class SetupActivity extends AppCompatActivity {
    private Toolbar posttoolbar;
    private EditText setupName;
    private EditText setupPhone;
    private EditText setupType;
    private EditText setupBlood;
    private EditText setupQualification;
    private EditText setupAge;
    private Button setupbtn;
    private FirebaseAuth firebaseAuth;
    private FirebaseFirestore firebaseFirestore;
    private DatabaseReference mRootRef;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setup);
        posttoolbar = findViewById(R.id.post_toolbar);
        setSupportActionBar(posttoolbar);
        getSupportActionBar().setTitle("Account Settings");

        firebaseAuth=FirebaseAuth.getInstance();
        firebaseFirestore=FirebaseFirestore.getInstance();

        mRootRef= FirebaseDatabase.getInstance().getReference();


        setupName=findViewById(R.id.setup_name);
        setupPhone=findViewById(R.id.setup_phone);
        setupType=findViewById(R.id.setup_type);
        setupBlood=findViewById(R.id.setup_blood);
        setupQualification=findViewById(R.id.setup_qualification);
        setupAge=findViewById(R.id.setup_age);
        setupbtn=findViewById(R.id.setup_btn);

        setupbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final String user_id =firebaseAuth.getCurrentUser().getUid();

                String user_name=setupName.getText().toString();
                String user_phone=setupPhone.getText().toString();
                String user_type=setupType.getText().toString();
                String user_blood=setupBlood.getText().toString();
                String user_qualification=setupQualification.getText().toString();
                String user_age=setupAge.getText().toString();


                if(!TextUtils.isEmpty(user_name) && !TextUtils.isEmpty(user_phone) && !TextUtils.isEmpty(user_type) && !TextUtils.isEmpty(user_blood) && !TextUtils.isEmpty(user_qualification) && !TextUtils.isEmpty(user_age))
                {
                    final Map<String,Object> userMap = new HashMap<>();
                    userMap.put("name",user_name);
                    userMap.put("phone no",user_phone.toString());
                    userMap.put("type",user_type);
                    userMap.put("blood group",user_blood);
                    userMap.put("qualification" ,user_qualification);
                    userMap.put("age",user_age);
                    setupbtn.setEnabled(false);

                    mRootRef.child("Users").child(user_id).addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {

                            mRootRef.child("Users").child(user_id).updateChildren(userMap, new DatabaseReference.CompletionListener() {
                                @Override
                                public void onComplete(DatabaseError databaseError, DatabaseReference databaseReference) {
                                    if (databaseError!=null){
                                        Log.d("CHAT_LOG",databaseError.getMessage().toString());
                                    }
                                }
                            });

                        }

                        @Override
                        public void onCancelled(DatabaseError databaseError) {

                        }
                    });

                    firebaseFirestore.collection("Users").document(user_id).set(userMap).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if(task.isSuccessful())
                            {
                                Toast.makeText(SetupActivity.this,"Updated",Toast.LENGTH_LONG).show();
                                setupbtn.setEnabled(true);
                                View();
                            }
                            else
                            {
                                String error=task.getException().getMessage();
                                Toast.makeText(SetupActivity.this,"Firestore error"+error,Toast.LENGTH_LONG).show();
                            }
                        }
                    });


                }
            }

            public void  View()
            {
                String user_id =firebaseAuth.getCurrentUser().getUid();
                firebaseFirestore.collection("Users").document(user_id).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                        if(task.isSuccessful())
                        {
                            DocumentSnapshot document=task.getResult();
                            String value = document.getString("type");
                            Log.i("LOGGER",value);
                            if(value.equals("Doctor"))
                            {
                                Intent mainIntent =new Intent(SetupActivity.this,UMainActivity.class);
                                startActivity(mainIntent);
                                finish();
                            }
                            else if(value.equals("User"))
                            {
                                Intent postIntent =new Intent(SetupActivity.this,BlogMainActivity.class);

                                startActivity(postIntent);
                                finish();

                            }
                        }

                    }
                });
            }
        });

    }
}
