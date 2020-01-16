package com.example.ishmaamin.curatech;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.HashMap;
import java.util.Map;

public class newQ extends AppCompatActivity {
    private Toolbar Qtool;

    private EditText postText;
    private Button postBtn;
    private ProgressBar postProg;
    private StorageReference storageReference;
    private FirebaseFirestore firebaseFirestore;
    private FirebaseAuth mAuth;
    private String userId;
    private EditText postTitle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_q);

        Toolbar Qtool =findViewById(R.id.Qtool);
        setSupportActionBar(Qtool);
        getSupportActionBar().setTitle("Add Question");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        postText=findViewById(R.id.postText);
        postBtn=findViewById(R.id.postBtn);
        postTitle=findViewById(R.id.postTitle);
        postProg=findViewById(R.id.postProg);

        storageReference= FirebaseStorage.getInstance().getReference();
        firebaseFirestore=FirebaseFirestore.getInstance();
        mAuth=FirebaseAuth.getInstance();
        userId=mAuth.getCurrentUser().getUid();
        postBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String desc= postText.getText().toString();
                String title= postTitle.getText().toString();
                if(!TextUtils.isEmpty(desc)){
                    postProg.setVisibility(View.VISIBLE);

                    long time= System.currentTimeMillis();


                    Map<String, Object> posts= new HashMap<>();
                    posts.put("title",title);
                    posts.put("desc",desc);
                    posts.put("user",userId);
                    posts.put("time", time);


                    firebaseFirestore.collection("Comments").add(posts).addOnCompleteListener(new OnCompleteListener<DocumentReference>() {
                        @Override
                        public void onComplete(@NonNull Task<DocumentReference> task) {

                            if(task.isSuccessful()){

                                Toast.makeText(newQ.this, "Post was added", Toast.LENGTH_LONG).show();
                                Intent mainIntent = new Intent(newQ.this, PresMainActivity.class);
                                startActivity(mainIntent);
                                finish();

                            } else {


                            }

                            postProg.setVisibility(View.INVISIBLE);

                        }
                    });


                }else{
                    postProg.setVisibility(View.INVISIBLE);
                }

            }
        });



    }
}
