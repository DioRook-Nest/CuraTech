package com.example.ishmaamin.curatech;
import android.content.Intent;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.ServerTimestamp;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import com.google.firebase.storage.UploadTask;
import com.theartofdev.edmodo.cropper.CropImage;
import com.theartofdev.edmodo.cropper.CropImageView;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class EhrInsertActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener {

FirebaseDatabase firebaseDatabase;
DatabaseReference databaseReference;
private ImageView setupimage;
private Uri imageuri = null;
private Toolbar toolbar;

private EditText type;
private Button save;
private Button view;

private String[] items;
private String text;
private String user_id ;
private StorageReference storageReference;
private FirebaseAuth firebaseAuth;
private FirebaseFirestore firebaseFirestore;
private Spinner spinner;


@Override
public void onCreate(Bundle savedInstanceState) {
super.onCreate(savedInstanceState);

setContentView(R.layout.activity_ehr_insert);

    toolbar=(Toolbar)findViewById(R.id.ins_toolbar);
    setSupportActionBar(toolbar);

    ActionBar actionbar = (ActionBar)getSupportActionBar();
    actionbar.setDisplayHomeAsUpEnabled(true);
    getSupportActionBar().setTitle("Insert into EHR");


setupimage = findViewById(R.id.setup_image);
save = findViewById(R.id.save);
view=findViewById(R.id.view);
spinner= findViewById(R.id.spinner);

firebaseAuth = FirebaseAuth.getInstance();
storageReference = FirebaseStorage.getInstance().getReference();
firebaseFirestore=FirebaseFirestore.getInstance();
    user_id = firebaseAuth.getCurrentUser().getUid();

    items = new String[]{"Blood Report", "Urine Culture", "Other"};
ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, items);
spinner.setAdapter(adapter);
    spinner.setOnItemSelectedListener(this);

save.setOnClickListener(new View.OnClickListener() {
@Override
public void onClick(View v) {
if (!TextUtils.isEmpty(text) && imageuri != null) {


final String random= UUID.randomUUID().toString();


final String blood="blood report"+user_id;
final String urine="urine culture"+user_id;
final String other="other"+user_id;

if (text.equalsIgnoreCase("blood report")) {
StorageReference image_path = storageReference.child("blood report").child(random + ".jpg");//+.child(user_id+".jpg")
image_path.putFile(imageuri).addOnCompleteListener(new OnCompleteListener<UploadTask.TaskSnapshot>() {
public void onComplete(@NonNull Task<UploadTask.TaskSnapshot> task) {
    if (task.isSuccessful()) {
    String downloaduri = task.getResult().getDownloadUrl().toString();
    Map<String,Object>postMap=new HashMap<>();
    postMap.put("image_url",downloaduri);
    postMap.put("timestamp",FieldValue.serverTimestamp());
    postMap.put("Uid",user_id);

firebaseFirestore.collection("blood report").add(postMap).addOnCompleteListener(new OnCompleteListener<DocumentReference>() {
public void onComplete(@NonNull Task<DocumentReference> task) {
if(task.isSuccessful())
{
Toast.makeText(EhrInsertActivity.this, "Done!!", Toast.LENGTH_LONG).show();

}
}
});
    Intent transfer=new Intent(EhrInsertActivity.this,EhrHistoryActivity.class);
    startActivity(transfer);
}
else {
String error = task.getException().getMessage();
Toast.makeText(EhrInsertActivity.this, "error" + error, Toast.LENGTH_LONG).show();
}
}
});


}

if (text.equalsIgnoreCase("urine culture")) {
StorageReference image_path = storageReference.child("urine culture").child(random + ".jpg");//+.child(user_id+".jpg")
image_path.putFile(imageuri).addOnCompleteListener(new OnCompleteListener<UploadTask.TaskSnapshot>() {
@Override
public void onComplete(@NonNull Task<UploadTask.TaskSnapshot> task) {
if (task.isSuccessful()) {
Uri download_uri = task.getResult().getDownloadUrl();
Map<String,Object>postMap=new HashMap<>();

postMap.put("image_url",download_uri.toString());
postMap.put("timestamp",FieldValue.serverTimestamp());
postMap.put("Uid",user_id);

  firebaseFirestore.collection("urine culture").add(postMap).addOnCompleteListener(new OnCompleteListener<DocumentReference>() {
        @Override
        public void onComplete(@NonNull Task<DocumentReference> task) {
            if (task.isSuccessful()) {
                Toast.makeText(EhrInsertActivity.this, "Done!!", Toast.LENGTH_LONG).show();

            }
        }
    });
    Intent transfer=new Intent(EhrInsertActivity.this,EhrHistoryActivity.class);
    startActivity(transfer);
}

else {
String error = task.getException().getMessage();
Toast.makeText(EhrInsertActivity.this, "error" + error, Toast.LENGTH_LONG).show();
}
}
});
}

if (text.equalsIgnoreCase("other")) {
StorageReference image_path = storageReference.child("other").child(random + ".jpg");
image_path.putFile(imageuri).addOnCompleteListener(new OnCompleteListener<UploadTask.TaskSnapshot>() {
@Override
public void onComplete(@NonNull Task<UploadTask.TaskSnapshot> task) {
if (task.isSuccessful()) {
Uri download_uri = task.getResult().getDownloadUrl();

Map<String,Object>postMap=new HashMap<>();
postMap.put("image_url",download_uri.toString());
postMap.put("timestamp",FieldValue.serverTimestamp());
postMap.put("Uid",user_id);

firebaseFirestore.collection("other").add(postMap).addOnCompleteListener(new OnCompleteListener<DocumentReference>() {
@Override
public void onComplete(@NonNull Task<DocumentReference> task) {
if(task.isSuccessful())
{
Toast.makeText(EhrInsertActivity.this, "Done!!", Toast.LENGTH_LONG).show();

}
}
});
    Intent transfer=new Intent(EhrInsertActivity.this,EhrHistoryActivity.class);
    startActivity(transfer);
} else {
String error = task.getException().getMessage();
Toast.makeText(EhrInsertActivity.this, "error" + error, Toast.LENGTH_LONG).show();
}
}
});
}


}

}
});

view.setOnClickListener(new View.OnClickListener() {
@Override
public void onClick(View v) {
Intent newintent=new Intent(EhrInsertActivity.this,EhrHistoryActivity.class);
startActivity(newintent);
}
});

setupimage.setOnClickListener(new View.OnClickListener() {
@Override
public void onClick(View v) {

CropImage.activity()
.setGuidelines(CropImageView.Guidelines.ON)
.start(EhrInsertActivity.this);
}


});

}
public void onActivityResult(int requestCode, int resultCode, Intent data){
super.onActivityResult(requestCode, resultCode, data);

if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE) {
CropImage.ActivityResult result = CropImage.getActivityResult(data);
if (resultCode == RESULT_OK) {
imageuri = result.getUri();
setupimage.setImageURI(imageuri);
} else if (resultCode == CropImage.CROP_IMAGE_ACTIVITY_RESULT_ERROR_CODE) {
Exception error = result.getError();
}
}

}
    public void onItemSelected(AdapterView<?> arg0, View arg1, int position,long id) {
        text=items[position];
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }

}




