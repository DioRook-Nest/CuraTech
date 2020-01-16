package com.example.ishmaamin.curatech;


import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.DateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class ProfileActivity extends AppCompatActivity {
    private TextView mDisplayID, mBgrp,mPrf;
    private Button mReqBtn,mDeclineBtn;
    private DatabaseReference mReqDataBase;
    private DatabaseReference mUsersDataBase;
    private DatabaseReference mFriendDataBase;
    private String mCurrentState;
    private FirebaseUser mCurrentUser;
    private DatabaseReference mNotifcationDb;
    private DatabaseReference mRootRef;
    private ProgressDialog mProgressDialog;
    private Toolbar mToolbar;
    private String mType;

    private EditText chatm;

    private DatabaseReference mPersonalDb;
    private String ptype;

    private TextView mProfession;
    private TextView mQual;

    private FloatingActionButton mehr;


    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);
        mCurrentUser= FirebaseAuth.getInstance().getCurrentUser();
        final String test=mCurrentUser.getUid();
        final String user_id = getIntent().getStringExtra("user_id");

        chatm=(EditText) findViewById(R.id.chat_msg);
        mehr=(FloatingActionButton) findViewById(R.id.chat_ehr);

        mUsersDataBase = FirebaseDatabase.getInstance().getReference().child("Users").child(user_id);
        mPersonalDb= FirebaseDatabase.getInstance().getReference().child("Users").child(test);
        mReqDataBase = FirebaseDatabase.getInstance().getReference().child("Friend_req");
        mFriendDataBase=FirebaseDatabase.getInstance().getReference().child("Friends");
        mNotifcationDb=FirebaseDatabase.getInstance().getReference().child("notifications");
        mRootRef= FirebaseDatabase.getInstance().getReference();
        mDisplayID = (TextView) findViewById(R.id.prof_name);
        //mProfession= (TextView) findViewById(R.id.profile_status);
        mPrf = (TextView) findViewById(R.id.profile_prf);
        mBgrp = (TextView) findViewById(R.id.prof_bgrp);
        mQual = (TextView) findViewById(R.id.profile_qual);


        mReqBtn = (Button) findViewById(R.id.req_btn);
        mDeclineBtn = (Button) findViewById(R.id.profile_decline_btn);

        mCurrentState = "not_friends";
        mDeclineBtn.setVisibility(View.INVISIBLE);
        mDeclineBtn.setEnabled(false);

        //mType=FirebaseDatabase.getInstance().getReference().child("Users").child(test).child("type").addChildEventListener().toString();

       /* if (mType.equals("User"))
            mReqBtn.setEnabled(false);
        else
            mReqBtn.setEnabled(true);*/

       // mReqBtn.setEnabled(false);

        //mReqBtn.setEnabled(false);



        mToolbar= (Toolbar) findViewById(R.id.profilebar);
        setSupportActionBar(mToolbar);
        //getSupportActionBar().setTitle("All Users");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        mProgressDialog = new ProgressDialog(this);
        mProgressDialog.setTitle("Loading User Data");
        mProgressDialog.setMessage("Please wait while we load the user data.");
        mProgressDialog.setCanceledOnTouchOutside(false);
        mProgressDialog.show();


        mPersonalDb.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                ptype=dataSnapshot.child("type").getValue().toString();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });


        mUsersDataBase.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                String display_name = dataSnapshot.child("name").getValue().toString();

                mType = dataSnapshot.child("type").getValue().toString();

                String bgrp = dataSnapshot.child("blood group").getValue().toString();

                String qual = dataSnapshot.child("qualification").getValue().toString();



                if (ptype.equals("User")) {
                    mReqBtn.setEnabled(false);
                    if (!mType.equals(ptype))
                        if (mReqBtn.getText().toString().toUpperCase().equals("SEND CHAT REQUEST"))
                            Toast.makeText(ProfileActivity.this, "Only Doctors can send Request", Toast.LENGTH_SHORT).show();

                }
                else{
                    mReqBtn.setEnabled(true);
                    //mReqBtn.setVisibility(View.INVISIBLE);
                }

                mDisplayID.setText(display_name);
                mPrf.setText(mType);
                mBgrp.setText(bgrp);
                mQual.setText(qual);

                if(test.equals(user_id)){

                    mDeclineBtn.setEnabled(false);
                    mDeclineBtn.setVisibility(View.INVISIBLE);

                    mReqBtn.setEnabled(false);
                    mReqBtn.setVisibility(View.INVISIBLE);

                }

                //-----friend list/ request feature-----------------//

                //if (mType.equals("Doctor"))
                //                 mCurrentUser.getUid()
                mReqDataBase.child(test).addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        if (dataSnapshot.hasChild(user_id)) {
                            String req_type=dataSnapshot.child(user_id).child("request_type").getValue().toString();
                            if (req_type.equals("received")){
                                mCurrentState="req_received";
                                mReqBtn.setText("Accept Chat Request");

                                //if (mType.equals("User"))
                                    mReqBtn.setEnabled(true);
                                    mReqBtn.setVisibility(View.VISIBLE);


                                mDeclineBtn.setText("Decline Chat Request");

                                mDeclineBtn.setVisibility(View.VISIBLE);
                                mDeclineBtn.setEnabled(true);

                            }else if (req_type.equals("sent"))
                            {
                                mCurrentState="req_sent";
                                mReqBtn.setText("Cancel Chat Request");

                                mDeclineBtn.setVisibility(View.VISIBLE);
                                mDeclineBtn.setEnabled(false);

                            }
                            mProgressDialog.dismiss();
                        }else{
                            //                  mCurrentUser.getUid()
                            mFriendDataBase.child(test).addListenerForSingleValueEvent(new ValueEventListener() {
                                @Override
                                public void onDataChange(DataSnapshot dataSnapshot) {
                                    if(dataSnapshot.hasChild(user_id)){
                                         mCurrentState="friends";
                                         mReqBtn.setText("Terminate Chat");

                                        //if (mType.equals("User"))
                                        mReqBtn.setEnabled(true);

                                        mDeclineBtn.setVisibility(View.INVISIBLE);
                                        mDeclineBtn.setEnabled(false);
                                    }

                                    mProgressDialog.dismiss();

                                    /*if (ptype.equals("User")) {
                                        mReqBtn.setEnabled(false);
                                        mReqBtn.setVisibility(View.INVISIBLE);
                                    }*/

                                }

                                @Override
                                public void onCancelled(DatabaseError databaseError) {

                                    mProgressDialog.dismiss();

                                }
                            });
                        }
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
        mReqBtn.setOnClickListener(new View.OnClickListener() {

            //---------------------------------Not Friend State
            @Override
            public void onClick(View v) {
                mReqBtn.setEnabled(false);

                //--------------------Not Friend State
                if (mCurrentState.equals("not_friends")) {
                    DatabaseReference newNotref=mRootRef.child("notifications").child(user_id).push();
                    String newNotId=newNotref.getKey();

                    HashMap<String,String> notificationData=new HashMap<>();
                    notificationData.put("from",test);
                    notificationData.put("type","request");

                    Map<String,Object> requestMap =new HashMap<>();
                    //              mCurrentUser.getUid()
                    requestMap.put("Friend_req/"+test+"/"+user_id+"/request_type","sent");
                    requestMap.put("Friend_req/"+user_id+"/"+test+"/request_type","received");
                    requestMap.put("notifications/"+user_id+"/"+newNotId,notificationData);


                    mRootRef.updateChildren(requestMap, new DatabaseReference.CompletionListener() {
                        @Override
                        public void onComplete(DatabaseError databaseError, DatabaseReference databaseReference) {

                           if (databaseError!=null){
                               Toast.makeText(ProfileActivity.this,"There was some error",Toast.LENGTH_SHORT).show();
                           }
                            else{

                               mCurrentState="req_sent";
                               mReqBtn.setText("Cancel Chat Request");

                               Toast.makeText(ProfileActivity.this,"Request sent successfully", Toast.LENGTH_SHORT).show();

                           }
                            if (ptype.equals("Doctor"))
                                mReqBtn.setEnabled(true);

                        }
                    });


                   /* mReqDataBase.child(mCurrentUser.getUid()).child(user_id).child("request_type").setValue("sent")
                            .addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if (task.isSuccessful()){

                                mReqDataBase.child(user_id).child(mCurrentUser.getUid()).child("request_type")
                                        .setValue("received").addOnSuccessListener(new OnSuccessListener<Void>() {
                                    @Override
                                    public void onSuccess(Void aVoid) {

                                        mCurrentState="req_sent";
                                        mReqBtn.setText("Cancel Friend Request");
                                        Toast.makeText(ProfileActivity.this,"Request sent successfully", Toast.LENGTH_SHORT).show();

                                    }
                                });
                            }
                            else{
                                Toast.makeText(ProfileActivity.this,"Failed Sending Request", Toast.LENGTH_SHORT).show();
                            }
                            mReqBtn.setEnabled(true);
                        }
                    });*/
                }

                //----------------------Cancel Request State
                if (mCurrentState.equals("req_sent")){
                    // mCurrentUser.getUid()
                    mReqDataBase.child(test).child(user_id).removeValue().addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void aVoid) {
                            //    mCurrentUser.getUid()
                            mReqDataBase.child(user_id).child(test).removeValue().addOnSuccessListener(new OnSuccessListener<Void>() {
                                @Override
                                public void onSuccess(Void aVoid) {


                                    mCurrentState="not_friends";
                                    mReqBtn.setText("Send Chat Request");



                                    if (ptype.equals("Doctor"))
                                        mReqBtn.setEnabled(true);
                                    else
                                        if (mReqBtn.getText().toString().toUpperCase().equals("SEND CHAT REQUEST"))
                                            Toast.makeText(ProfileActivity.this,
                                                    "Only Doctors can send Request", Toast.LENGTH_SHORT).show();





                                    mDeclineBtn.setVisibility(View.INVISIBLE);
                                    mDeclineBtn.setEnabled(false);

                                }
                            });
                        }
                    });
                }

                //----------- Req Received State
                if (mCurrentState.equals("req_received")){

                    final String currentDate= DateFormat.getDateTimeInstance().format(new Date());

                    Map <String,Object> friendsMap =new HashMap<>();
                    //              mCurrentUser.getUid()
                    friendsMap.put("Friends/"+test+"/"+user_id+"/date",currentDate);
                    friendsMap.put("Friends/"+user_id+"/"+test+"/date",currentDate);

                    friendsMap.put("Friend_req/"+user_id+"/"+test,null);
                    friendsMap.put("Friend_req/"+test+"/"+user_id,null);
                    mRootRef.updateChildren(friendsMap, new DatabaseReference.CompletionListener() {
                        @Override
                        public void onComplete(DatabaseError databaseError, DatabaseReference databaseReference) {

                            if (databaseError!=null){


                                String error=databaseError.getMessage();
                                Toast.makeText(ProfileActivity.this,error,Toast.LENGTH_SHORT).show();

                                mDeclineBtn.setVisibility(View.INVISIBLE);
                                mDeclineBtn.setEnabled(false);

                            }
                            else{
                                mReqBtn.setEnabled(true);
                                mCurrentState="friends";
                                mReqBtn.setText("Terminate Chat");
                                Toast.makeText(ProfileActivity.this,"You can now Chat.", Toast.LENGTH_SHORT).show();
                                //chatm.setEnabled(true);
                                mehr.setEnabled(true);
                                mDeclineBtn.setVisibility(View.INVISIBLE);
                                mDeclineBtn.setEnabled(false);

                            }




                        }
                    });

                   /* // mCurrentUser.getUid()
                    mFriendDataBase.child(test).child(user_id).setValue(currentDate).addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void aVoid) {

                            mFriendDataBase.child(user_id).child(test).setValue(currentDate).addOnSuccessListener(new OnSuccessListener<Void>() {
                                @Override
                                public void onSuccess(Void aVoid) {

                                    mReqDataBase.child(test).child(user_id).removeValue().addOnSuccessListener(new OnSuccessListener<Void>() {
                                        @Override
                                        public void onSuccess(Void aVoid) {
                                            mReqDataBase.child(user_id).child(test).removeValue().addOnSuccessListener(new OnSuccessListener<Void>() {
                                                @Override
                                                public void onSuccess(Void aVoid) {

                                                    mReqBtn.setEnabled(true);
                                                    mCurrentState="friends";
                                                    mReqBtn.setText("UnFriend this person");

                                                }
                                            });
                                        }
                                    });

                                }
                            });

                        }
                    });*/

                }
                //-------------------------------UNFRIEND---

                if (mCurrentState.equals("friends")){

                    mFriendDataBase.child(test).child(user_id).removeValue().addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void aVoid) {
                            mFriendDataBase.child(user_id).child(test).removeValue().addOnSuccessListener(new OnSuccessListener<Void>() {
                                @Override
                                public void onSuccess(Void aVoid) {



                                        mCurrentState = "not_friends";
                                        mReqBtn.setText("Send Chat Request");

                                        mDeclineBtn.setVisibility(View.INVISIBLE);
                                        mDeclineBtn.setEnabled(false);

                                        chatm.setEnabled(false);
                                        mehr.setEnabled(false);

                                        if (ptype.equals("Doctor")) {
                                            mReqBtn.setEnabled(true);

                                        }
                                        else
                                        if (mReqBtn.getText().toString().toUpperCase().equals("SEND CHAT REQUEST"))
                                            Toast.makeText(ProfileActivity.this,
                                                    "Only Doctors can send Request", Toast.LENGTH_SHORT).show();


                                }
                            });
                        }
                    });




                }
            }


        });

        mDeclineBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                mDeclineBtn.setEnabled(false);
                mReqBtn.setEnabled(false);

                mReqDataBase.child(test).child(user_id).removeValue().addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        //    mCurrentUser.getUid()
                        mReqDataBase.child(user_id).child(test).removeValue().addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void aVoid) {

                                mCurrentState="not_friends";
                                mReqBtn.setText("Send Chat Request");

                                if (ptype.equals("Doctor"))
                                    mReqBtn.setEnabled(true);
                                else
                                    if (mReqBtn.getText().toString().toUpperCase().equals("SEND CHAT REQUEST"))
                                        Toast.makeText(ProfileActivity.this,"Only Doctors can send Request", Toast.LENGTH_SHORT).show();





                                mDeclineBtn.setVisibility(View.INVISIBLE);
                                mDeclineBtn.setEnabled(false);

                            }
                        });
                    }
                });



            }
        });
    }
}
