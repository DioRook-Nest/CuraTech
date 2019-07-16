package com.example.ishmaamin.curatech;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class ReplyAdapter extends RecyclerView.Adapter<ReplyAdapter.MessageViewHolder> {

    private List<Messages> mMessageList;
    private DatabaseReference mUserDatabase;
    //private mCurrentUser

    public ReplyAdapter(List<Messages> mMessageList){
        this.mMessageList= mMessageList;
    }


    @Override
    public ReplyAdapter.MessageViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View v= LayoutInflater.from(parent.getContext())
                .inflate(R.layout.reply_single_layout,parent,false);
        return new MessageViewHolder(v);
    }

    @Override
    public void onBindViewHolder(final ReplyAdapter.MessageViewHolder holder, int position) {

       /* Messages c=mMessageList.get(position);
        holder.messageText.setText(c.getMessage());
        //holder.timeText.setText(c.getTime());*/

        Messages c = mMessageList.get(position);

        String from_user = c.getFrom();
        String message_type = c.getType();


        mUserDatabase = FirebaseDatabase.getInstance().getReference().child("Users").child(from_user);

        mUserDatabase.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                String name = dataSnapshot.child("name").getValue().toString();
                String image = dataSnapshot.child("thumb_image").getValue().toString();

                holder.displayName.setText(name);

                //Picasso.with(viewHolder.profileImage.getContext()).load(image).placeholder(R.drawable.default_avatar).into(viewHolder.profileImage);

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        if(message_type.equals("text")) {

            holder.messageText.setText(c.getMessage());
            holder.messageImage.setVisibility(View.INVISIBLE);


        } else {

            holder.messageText.setVisibility(View.INVISIBLE);
           // Picasso.with(viewHolder.profileImage.getContext()).load(c.getMessage()).placeholder(R.drawable.default_avatar).into(viewHolder.messageImage);

        }

    }

    @Override
    public int getItemCount() {
        return mMessageList.size();
    }

    public class MessageViewHolder extends RecyclerView.ViewHolder {

        public TextView messageText;
        public TextView timeText;
        public CircleImageView profileImage;
        public TextView displayName;
        public ImageView messageImage;


        public MessageViewHolder(View itemView) {
            super(itemView);

            messageText=(TextView) itemView.findViewById(R.id.reply_text_layout);
            timeText=(TextView) itemView.findViewById(R.id.reply_item_time);
            profileImage=(CircleImageView) itemView.findViewById(R.id.reply_profile_layout);
            displayName = (TextView) itemView.findViewById(R.id.reply_name_text_layout);
            messageImage = (ImageView) itemView.findViewById(R.id.reply_image_layout);
        }
    }
}
