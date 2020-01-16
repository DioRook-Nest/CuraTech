package com.example.ishmaamin.curatech;


import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.RecyclerView.ViewHolder;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class MessageAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private List<Messages> mMessageList;
    private DatabaseReference mUserDatabase;
    private Messages c;
    private String from_user;
    private String mCurrentUserId ;

    private FirebaseAuth mAuth;

    //private mCurrentUser

    public  MessageAdapter(List<Messages> mMessageList){
        this.mMessageList= mMessageList;
    }


    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View v0= LayoutInflater.from(parent.getContext())
                .inflate(R.layout.reply_single_layout,parent,false);

        switch (viewType){
            case 0:
                View v= LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.reply_single_layout,parent,false);
                return new MessageViewHolder(v);

            case 1:
                View v1= LayoutInflater.from(parent.getContext())
                        .inflate(R.layout.message_single_latout,parent,false);
                return new MessageViewHolder1(v1);

        }
        return new MessageViewHolder(v0);
    }

    @Override
    public void onBindViewHolder(final RecyclerView.ViewHolder holder, int position) {

       /* Messages c=mMessageList.get(position);
        holder.messageText.setText(c.getMessage());
        //holder.timeText.setText(c.getTime());*/



        c = mMessageList.get(position);
        from_user = c.getFrom();
        String message_type = c.getType();

        switch (holder.getItemViewType()){
            case 0:
                final MessageViewHolder viewHolder=(MessageViewHolder) holder;


                mUserDatabase = FirebaseDatabase.getInstance().getReference().child("Users").child(from_user);

                mUserDatabase.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {

                        String name = dataSnapshot.child("name").getValue().toString();
                        //String image = dataSnapshot.child("thumb_image").getValue().toString();

                        viewHolder.displayName.setText("You");

                        //Picasso.with(viewHolder.profileImage.getContext()).load(image).placeholder(R.drawable.default_avatar).into(viewHolder.profileImage);

                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });

                if(message_type.equals("text")) {

                    viewHolder.messageText.setText(c.getMessage());
                    viewHolder.messageImage.setVisibility(View.INVISIBLE);


                } else {

                    viewHolder.messageText.setVisibility(View.INVISIBLE);
                    // Picasso.with(viewHolder.profileImage.getContext()).load(c.getMessage()).placeholder(R.drawable.default_avatar).into(viewHolder.messageImage);

                }
                break;





            case 1:
                final MessageViewHolder1 viewHolder1=(MessageViewHolder1) holder;


                mUserDatabase = FirebaseDatabase.getInstance().getReference().child("Users").child(from_user);

                mUserDatabase.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {

                        String name = dataSnapshot.child("name").getValue().toString();
                        //String image = dataSnapshot.child("thumb_image").getValue().toString();

                        viewHolder1.displayName.setText(name);

                        //Picasso.with(viewHolder.profileImage.getContext()).load(image).placeholder(R.drawable.default_avatar).into(viewHolder.profileImage);

                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });

                if(message_type.equals("text")) {

                    viewHolder1.messageText.setText(c.getMessage());
                    viewHolder1.messageImage.setVisibility(View.INVISIBLE);


                } else {

                    viewHolder1.messageText.setVisibility(View.INVISIBLE);
                    // Picasso.with(viewHolder.profileImage.getContext()).load(c.getMessage()).placeholder(R.drawable.default_avatar).into(viewHolder.messageImage);

                }

                break;
        }




    }

    @Override
    public int getItemCount() {
        return mMessageList.size();
    }


    public int getItemViewType(int pos){


        mAuth= FirebaseAuth.getInstance();
        mCurrentUserId=mAuth.getCurrentUser().getUid();
        //mCurrentUserId="YHnCGCtzDDc1zEav6JD5sb9HCt63";

        c = mMessageList.get(pos);
        from_user = c.getFrom();


        if(from_user.equals(mCurrentUserId))
            return 0;
        else
            return 1;

    }

    public class MessageViewHolder1 extends ViewHolder {

        public TextView messageText;
        public TextView timeText;
        public CircleImageView profileImage;
        public TextView displayName;
        public ImageView messageImage;


        public MessageViewHolder1(View itemView) {
            super(itemView);





           /*if(from_user.equals(mCurrentUserId.equals(from_user))){

                messageText=(TextView) itemView.findViewById(R.id.reply_text_layout);
                timeText=(TextView) itemView.findViewById(R.id.reply_item_time);
                profileImage=(CircleImageView) itemView.findViewById(R.id.reply_profile_layout);
                displayName = (TextView) itemView.findViewById(R.id.reply_name_text_layout);
                messageImage = (ImageView) itemView.findViewById(R.id.reply_image_layout);

            }*/
           // else
            {
                messageText=(TextView) itemView.findViewById(R.id.message_text_layout);
                timeText=(TextView) itemView.findViewById(R.id.message_item_time);
                profileImage=(CircleImageView) itemView.findViewById(R.id.message_profile_layout);
                displayName = (TextView) itemView.findViewById(R.id.name_text_layout);
                messageImage = (ImageView) itemView.findViewById(R.id.message_image_layout);
            }

        }
    }








    public class MessageViewHolder extends ViewHolder {

        public TextView messageText;
        public TextView timeText;
        public CircleImageView profileImage;
        public TextView displayName;
        public ImageView messageImage;


        public MessageViewHolder(View itemView) {
            super(itemView);




           //if(from_user.equals(mCurrentUserId.equals(from_user))){

                messageText=(TextView) itemView.findViewById(R.id.reply_text_layout);
                timeText=(TextView) itemView.findViewById(R.id.reply_item_time);
                profileImage=(CircleImageView) itemView.findViewById(R.id.reply_profile_layout);
                displayName = (TextView) itemView.findViewById(R.id.reply_name_text_layout);
                messageImage = (ImageView) itemView.findViewById(R.id.reply_image_layout);

            /*}
            // else
            {
                messageText=(TextView) itemView.findViewById(R.id.message_text_layout);
                timeText=(TextView) itemView.findViewById(R.id.message_item_time);
                profileImage=(CircleImageView) itemView.findViewById(R.id.message_profile_layout);
                displayName = (TextView) itemView.findViewById(R.id.name_text_layout);
                messageImage = (ImageView) itemView.findViewById(R.id.message_image_layout);
            }*/

        }
    }
}
