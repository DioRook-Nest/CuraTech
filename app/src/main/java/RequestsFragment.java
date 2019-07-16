package com.example.ishmaamin.curatech;


import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;


/**
 * A simple {@link Fragment} subclass.
 */
public class RequestsFragment extends Fragment {

    private RecyclerView mRequestList;
    private DatabaseReference mRequestsDatabase;
    private DatabaseReference mUsersDatabase;

    private FirebaseAuth mAuth;

    private String mCurrent_user_id;

    private View mMainView;


    public RequestsFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        mMainView=inflater.inflate(R.layout.fragment_requests,container,false);

        mRequestList=(RecyclerView) mMainView.findViewById(R.id.req_list);


        mAuth= FirebaseAuth.getInstance();

        mCurrent_user_id=mAuth.getCurrentUser().getUid();
        //mCurrent_user_id="NYh0mKrsuahDVeZ7AmDdOhTTH9J2";

        mRequestsDatabase= FirebaseDatabase.getInstance().getReference().child("Friend_req").child(mCurrent_user_id);
        mRequestsDatabase.keepSynced(true);
        mUsersDatabase= FirebaseDatabase.getInstance().getReference().child("Users");
        mUsersDatabase.keepSynced(true);

        mRequestList.setHasFixedSize(true);
        mRequestList.setLayoutManager(new LinearLayoutManager(getContext()));
        return mMainView;
    }

    @Override
    public void onStart() {
        super.onStart();

        FirebaseRecyclerAdapter<Requests, RequestsViewHolder> RequestsRecyclerAdapter= new FirebaseRecyclerAdapter<Requests, RequestsViewHolder>(
                Requests.class,
                R.layout.users_single_layout,
                RequestsViewHolder.class,
                mRequestsDatabase


        ) {
            @Override
            protected void populateViewHolder(final RequestsViewHolder RequestsViewHolder, final Requests Requests, int position) {

                RequestsViewHolder.setDate(Requests.getDate());
                final String list_user_id=getRef(position).getKey();
               // String req_type;

               mRequestsDatabase.child(list_user_id).addListenerForSingleValueEvent(new ValueEventListener() {
                   @Override
                   public void onDataChange(DataSnapshot dataSnapshot) {
                      String req_type=dataSnapshot.getValue().toString();
                      if(req_type.equals("sent"))
                          RequestsViewHolder.setDate("You sent a Request");
                      else
                          RequestsViewHolder.setDate("You recieved a Request");
                   }

                   @Override
                   public void onCancelled(DatabaseError databaseError) {

                   }
               });

                mUsersDatabase.child(list_user_id).addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        final String username=dataSnapshot.child("name").getValue().toString();


                        /*if (dataSnapshot.hasChild("online")){
                            String useronline=(boolean) dataSnapshot.child("online").getValue();
                            RequestsViewHolder.setUserOnline(useronline);
                        }*/

                        RequestsViewHolder.setName(username);
                        RequestsViewHolder.mView.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                /*CharSequence options[]=new CharSequence[]{"Open Profile","Send message"};
                                AlertDialog.Builder builder=new AlertDialog.Builder(getContext());
                                builder.setTitle("Select Options");
                                builder.setItems(options, new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int i) {
                                        //Click event for ach item
                                        if (i==0){*/
                                            Intent profileIntent= new Intent(getContext(),ProfileActivity.class)  ;
                                            profileIntent.putExtra("user_id",list_user_id);

                                            startActivity(profileIntent);
                                       /* }
                                        if (i==1){
                                            Intent chatIntent= new Intent(getContext(),ChatActivity.class)  ;
                                            chatIntent.putExtra("user_id",list_user_id);
                                            chatIntent.putExtra("user_name",username);

                                            startActivity(chatIntent);
                                        }*/
                                    //}
                               // });
                                //builder.show();
                            }
                        });

                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });

            }
        };

        mRequestList.setAdapter(RequestsRecyclerAdapter);
    }

    public static  class RequestsViewHolder extends RecyclerView.ViewHolder{

        View mView;

        public  RequestsViewHolder(View itemView) {
            super(itemView);

            mView=itemView;
        }

        public  void setDate(String date){
            TextView userNameView=(TextView) mView.findViewById(R.id.user_single_status);
            userNameView.setText(date);
        }

        public void setName(String name){
            TextView userNameView= (TextView) mView.findViewById(R.id.user_single_name);
            userNameView.setText(name);
        }

       /* public void setUserOnline(Boolean userOnline) {
            this.userOnline = userOnline;
        }*/
    }

}
