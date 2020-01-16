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
public class FriendsFragment extends Fragment {

    private RecyclerView mFriendList;
    private DatabaseReference mFriendsDatabase;
    private DatabaseReference mUsersDatabase;

    private FirebaseAuth mAuth;

    private String mCurrent_user_id;

    private View mMainView;


    public FriendsFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        mMainView=inflater.inflate(R.layout.fragment_friends,container,false);

        mFriendList=(RecyclerView) mMainView.findViewById(R.id.friends_list);


        mAuth= FirebaseAuth.getInstance();

        mCurrent_user_id=mAuth.getCurrentUser().getUid();

       // mCurrent_user_id=mAuth.getCurrentUser().getUid();
        //mCurrent_user_id="NYh0mKrsuahDVeZ7AmDdOhTTH9J2";

        mFriendsDatabase= FirebaseDatabase.getInstance().getReference().child("Friends").child(mCurrent_user_id);
        mFriendsDatabase.keepSynced(true);
        mUsersDatabase= FirebaseDatabase.getInstance().getReference().child("Users");
        mUsersDatabase.keepSynced(true);

        mFriendList.setHasFixedSize(true);
        mFriendList.setLayoutManager(new LinearLayoutManager(getContext()));
        return mMainView;
    }

    @Override
    public void onStart() {
        super.onStart();

        FirebaseRecyclerAdapter<Friends, FriendsViewHolder> friendsRecyclerAdapter= new FirebaseRecyclerAdapter<Friends, FriendsViewHolder>(
                Friends.class,
                R.layout.users_single_layout,
                FriendsViewHolder.class,
                mFriendsDatabase


        ) {
            @Override
            protected void populateViewHolder(final FriendsViewHolder friendsViewHolder, final Friends friends, int position) {

                friendsViewHolder.setDate(friends.getDate());
                final String list_user_id=getRef(position).getKey();

                mUsersDatabase.child(list_user_id).addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        final String username=dataSnapshot.child("name").getValue().toString();


                        /*if (dataSnapshot.hasChild("online")){
                            String useronline=(boolean) dataSnapshot.child("online").getValue();
                            friendsViewHolder.setUserOnline(useronline);
                        }*/

                        friendsViewHolder.setName(username);
                        friendsViewHolder.mView.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                CharSequence options[]=new CharSequence[]{"Open Profile","Send message"};
                                AlertDialog.Builder builder=new AlertDialog.Builder(getContext());
                                builder.setTitle("Select Options");
                                builder.setItems(options, new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int i) {
                                        //Click event for ach item
                                        if (i==0){
                                            Intent profileIntent= new Intent(getContext(),ProfileActivity.class)  ;
                                            profileIntent.putExtra("user_id",list_user_id);

                                            startActivity(profileIntent);
                                        }
                                        if (i==1){
                                            Intent chatIntent= new Intent(getContext(),ChatActivity.class)  ;
                                            chatIntent.putExtra("user_id",list_user_id);
                                            chatIntent.putExtra("user_name",username);

                                            startActivity(chatIntent);
                                        }
                                    }
                                });
                                builder.show();
                            }
                        });

                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });

            }
        };

        mFriendList.setAdapter(friendsRecyclerAdapter);
    }

    public static  class FriendsViewHolder extends RecyclerView.ViewHolder{

        View mView;

        public  FriendsViewHolder(View itemView) {
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
