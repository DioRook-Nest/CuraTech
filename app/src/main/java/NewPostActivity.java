package com.example.ishmaamin.curatech;

import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.support.v7.widget.Toolbar;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.theartofdev.edmodo.cropper.CropImage;
import com.theartofdev.edmodo.cropper.CropImageView;

import java.util.HashMap;
import java.util.Map;
import java.util.Random;


public class NewPostActivity extends AppCompatActivity {
    private Button submitbtn;
    private Toolbar posttoolbar;
    private ImageView newPostImage;
    private EditText newPostDesc;
    private EditText newPostTitle;
    private Button newPostBtn;
    private Uri postImageUri;
    private ProgressBar newPostProgress;
    private StorageReference storageReference;
    private FirebaseFirestore firebaseFirestore;
    private FirebaseAuth mAuth;
    private String current_user_id;
    private String disc;




    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_post);
        submitbtn = findViewById(R.id.post_btn);

        storageReference= FirebaseStorage.getInstance().getReference();
        firebaseFirestore=FirebaseFirestore.getInstance();
        mAuth=FirebaseAuth.getInstance();
current_user_id=mAuth.getCurrentUser().getUid();
        posttoolbar = findViewById(R.id.post_toolbar);
        setSupportActionBar(posttoolbar);
        getSupportActionBar().setTitle("Add a post");

        submitbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent submitintent = new Intent(NewPostActivity.this, UMainActivity.class);
                startActivity(submitintent);
            }
        });
        newPostImage = findViewById(R.id.new_post_img);
        newPostDesc = findViewById(R.id.new_pos_desc);
        newPostTitle = findViewById(R.id.new_pos_title);
        newPostBtn = findViewById(R.id.post_btn);
        newPostProgress=findViewById(R.id.new_post_progress);

        newPostImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                CropImage.activity()
                        .setGuidelines(CropImageView.Guidelines.ON)
                        .setMinCropResultSize(512, 512)
                       // .setAspectRatio(1, 1)
                        .start(NewPostActivity.this);


            }
        });
        newPostBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final String desc=newPostDesc.getText().toString();

                final String title=newPostTitle.getText().toString();

                if(!TextUtils.isEmpty(desc) && postImageUri!=null)
                {
                    String randomName =getSaltString();
                    StorageReference filepath =storageReference.child("post_images").child(randomName+ ".jpg");
                    filepath.putFile(postImageUri).addOnCompleteListener(new OnCompleteListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onComplete(@NonNull Task<UploadTask.TaskSnapshot> task) {
                           if(task.isSuccessful())

                           {
                            String downloadUri= task.getResult().getDownloadUrl().toString();

                               Map<String, Object> postMap=new HashMap<>();
                               postMap.put("image_url",downloadUri);
                               postMap.put("desc",desc);
                               postMap.put("title",title);
                               postMap.put("user_id",current_user_id);
                               postMap.put("timestamp",FieldValue.serverTimestamp());
                               firebaseFirestore.collection("Posts").add(postMap).addOnCompleteListener(new OnCompleteListener<DocumentReference>() {
                                   @Override
                                   public void onComplete(@NonNull Task<DocumentReference> task) {
                                    if(task.isSuccessful())
                                    {
                                        Toast.makeText(NewPostActivity.this,"post added",Toast.LENGTH_LONG).show();
                                        newPostProgress.setVisibility(View.INVISIBLE);

                                    }
                                    else
                                    {
                                       newPostProgress.setVisibility(View.INVISIBLE);
                                    }
                                   }
                               });
                           }

                           else
                           {
                            newPostProgress.setVisibility(View.INVISIBLE);
                           }
                        }
                    });






                }
            }
        });

    }

        public void onActivityResult(int requestCode, int resultCode, Intent data) {
            if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE) {
                CropImage.ActivityResult result = CropImage.getActivityResult(data);
                if (resultCode == RESULT_OK) {
                    postImageUri=result.getUri();
                    newPostImage.setImageURI(postImageUri);

                } else if (resultCode == CropImage.CROP_IMAGE_ACTIVITY_RESULT_ERROR_CODE) {
                    Exception error = result.getError();
                }
            }
        }
    protected String getSaltString() {
        String SALTCHARS = "ABCDEFGHIJKLMNOPQRSTUVWXYZ1234567890";
        StringBuilder salt = new StringBuilder();
        Random rnd = new Random();
        while (salt.length() < 18) { // length of the random string.
            int index = (int) (rnd.nextFloat() * SALTCHARS.length());
            salt.append(SALTCHARS.charAt(index));
        }
        String saltStr = salt.toString();
        return saltStr;

    }



}
