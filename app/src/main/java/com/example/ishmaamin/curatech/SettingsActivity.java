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
import com.google.firebase.firestore.QuerySnapshot;

import java.util.HashMap;
import java.util.Map;

public class SettingsActivity extends AppCompatActivity {
    private Toolbar posttoolbar;
    private EditText settingsName;
    private EditText settingsPhone;
    private EditText settingsType;
    private EditText settingsBlood;
    private EditText settingsQualification;
    private EditText settingsAge;
    private Button settingsbtn;
    private FirebaseAuth firebaseAuth;
    private FirebaseFirestore firebaseFirestore;
    private DatabaseReference mRootRef;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);
        posttoolbar = findViewById(R.id.post_toolbar);
        setSupportActionBar(posttoolbar);
        getSupportActionBar().setTitle("Account Settings");

        firebaseAuth=FirebaseAuth.getInstance();
        firebaseFirestore=FirebaseFirestore.getInstance();

        mRootRef= FirebaseDatabase.getInstance().getReference();

        String user_id = firebaseAuth.getCurrentUser().getUid();
        settingsName=findViewById(R.id.settings_name);
        settingsPhone=findViewById(R.id.settings_phone);
        settingsType=findViewById(R.id.settings_type);
        settingsBlood=findViewById(R.id.settings_blood);
        settingsQualification=findViewById(R.id.settings_qualification);
        settingsAge=findViewById(R.id.settings_age);
        settingsbtn=findViewById(R.id.settings_btn);

        firebaseFirestore.collection("Users").document(user_id).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if(task.isSuccessful())
                {
                    DocumentSnapshot document=task.getResult();
                    String name = document.getString("name");
                    String phone = document.getString("phone no");
                    String type = document.getString("type");
                    String bgrp = document.getString("blood group");
                    String qua = document.getString("qualification");
                    String age = document.getString("age");
                    //String name = document.getString("type");
                    //Log.i("LOGGER",value);
                    settingsName.setText(name);
                    settingsPhone.setText(phone);
                    settingsType.setText(type);
                    settingsBlood.setText(bgrp);
                    settingsQualification.setText(qua);
                    settingsAge.setText(age);
                    settingsbtn.setText("Update Profile");

                }

            }
        });

        settingsbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final String user_id =firebaseAuth.getCurrentUser().getUid();

                String user_name=settingsName.getText().toString();
                String user_phone=settingsPhone.getText().toString();
                String user_type=settingsType.getText().toString();
                String user_blood=settingsBlood.getText().toString();
                String user_qualification=settingsQualification.getText().toString();
                String user_age=settingsAge.getText().toString();


                if(!TextUtils.isEmpty(user_name) && !TextUtils.isEmpty(user_phone) && !TextUtils.isEmpty(user_type) && !TextUtils.isEmpty(user_blood) && !TextUtils.isEmpty(user_qualification) && !TextUtils.isEmpty(user_age))
                {
                    final Map<String,Object> userMap = new HashMap<>();
                    userMap.put("name",user_name);
                    userMap.put("phone no",user_phone.toString());
                    userMap.put("type",user_type);
                    userMap.put("blood group",user_blood);
                    userMap.put("qualification" ,user_qualification);
                    userMap.put("age",user_age);
                    settingsbtn.setEnabled(false);

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


                    firebaseFirestore.collection("Users").document(user_id).update(userMap).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if(task.isSuccessful())
                            {
                                settingsbtn.setEnabled(true);
                                Toast.makeText(SettingsActivity.this,"Updated",Toast.LENGTH_LONG).show();
                                
                            }
                            else
                            {
                                String error=task.getException().getMessage();
                                Toast.makeText(SettingsActivity.this,"Firestore error"+error,Toast.LENGTH_LONG).show();
                            }
                        }
                    });

                    
                }
            }

           
        });

    }
}
