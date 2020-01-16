package com.example.ishmaamin.curatech;

import android.content.Intent;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.firestore.DocumentChange;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;

public class EhrRetrieveActivity extends AppCompatActivity {

    private RecyclerView list_view;

    private List<post>list;
    private FirebaseAuth firebaseAuth;
    private FirebaseFirestore firebaseFirestore;
    private EhrAdapter ad;
    private String user_id;
    public Toolbar toolbar;
   // DatabaseReference userid;


    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ehr_retrieve);
        toolbar=(Toolbar)findViewById(R.id.ret_toolbar);
        setSupportActionBar(toolbar);

        ActionBar actionbar = (ActionBar)getSupportActionBar();
        actionbar.setDisplayHomeAsUpEnabled(true);


        //Intent bundle = getIntent();
       // String stuff = bundle.getString("stuff");

        Intent intent = getIntent();
        String stuff = intent.getStringExtra(EhrHistoryActivity.EXTRA_MESSAGE);

        firebaseFirestore=FirebaseFirestore.getInstance();
        firebaseAuth=FirebaseAuth.getInstance();

        user_id = firebaseAuth.getCurrentUser().getUid();




        list_view=findViewById(R.id.report_post);
        list =new ArrayList<>();
        ad=new EhrAdapter(list);
        list_view.setLayoutManager(new LinearLayoutManager(this));
        list_view.setAdapter(ad);
        Query firstQuery=null;

        firebaseFirestore=FirebaseFirestore.getInstance();
        String uid=firebaseAuth.getInstance().getCurrentUser().getUid();

        final String blood="blood report"+uid;
        final String urine="urine culture"+uid;
        final String other="other"+uid;

       if(stuff.equalsIgnoreCase("blood report")){
           getSupportActionBar().setTitle("Blood Report");
        firstQuery = firebaseFirestore.collection("blood report")//.whereEqualTo("Uid",uid)
                .orderBy("timestamp",Query.Direction.DESCENDING);}

       if(stuff.equalsIgnoreCase("urine culture")){
           getSupportActionBar().setTitle("Urine Culture");
         firstQuery = firebaseFirestore.collection("urine culture")//.whereEqualTo("Uid",user_id)
                 .orderBy("timestamp",Query.Direction.DESCENDING);}

       if(stuff.equalsIgnoreCase("other")){
           getSupportActionBar().setTitle("Other Reports");
       //firstQuery = firebaseFirestore.collection("other").whereEqualTo("Uid",user_id).orderBy("timestamp",Query.Direction.DESCENDING);}
           firstQuery = firebaseFirestore.collection("other") //.whereEqualTo("Uid",user_id)
                   .orderBy("timestamp",Query.Direction.DESCENDING);}

        Log.i("abcd", String.valueOf(firstQuery));

        firstQuery.addSnapshotListener(new EventListener<QuerySnapshot>() {

            public void onEvent(QuerySnapshot documentSnapshots, FirebaseFirestoreException e) {
                        for(DocumentChange doc: documentSnapshots.getDocumentChanges()){
                            if(doc.getType()== DocumentChange.Type.ADDED){
                                    post post1=doc.getDocument().toObject(post.class);
                                    list.add(post1);
                                    ad.notifyDataSetChanged();
                            }
                        }
            }
        });







    }
}
