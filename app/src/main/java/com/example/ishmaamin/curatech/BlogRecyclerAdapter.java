package com.example.ishmaamin.curatech;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.text.format.DateFormat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.Date;
import java.util.List;

/**
 * Created by Ishma Amin on 16-09-2018.
 */

public class BlogRecyclerAdapter extends RecyclerView.Adapter<BlogRecyclerAdapter.ViewHolder>{

    public List<BlogPost> blog_list;
    public Context context;
    public BlogRecyclerAdapter(List<BlogPost> blog_list)
    {
       this.blog_list=blog_list;

    }
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.blog_list_item,parent,false);
        context=parent.getContext();
        return new ViewHolder(view);

    }

    @Override
    public void onBindViewHolder( ViewHolder holder, int position) {
String desc_data =blog_list.get(position).getDesc();
holder.setDescText(desc_data);
String title=blog_list.get(position).getTitle();
holder.setTitle(title);
String image_url=blog_list.get(position).getImage_url();
holder.setBlogImage(image_url);

//String timestamp=blog_list.get(position).getTimestamp();

        long millisecond=blog_list.get(position).getTimestamp().getTime();
        String dateString = DateFormat.format("MM/dd/yyyy", new Date(millisecond)).toString();
        holder.setTime(dateString);

String uid=blog_list.get(position).getUser_id();
holder.setUid(uid);
    }

    @Override
    public int getItemCount() {
        return blog_list.size();
    }

    public class ViewHolder extends  RecyclerView.ViewHolder{
        private View mView;

        private TextView descView;
        private ImageView blogImageView;
        private TextView titleView;
        private TextView mname;
        private TextView mprof;
        private TextView mdate;
        private TextView mtype;


        public ViewHolder(View itemView) {
            super(itemView);
            mView=itemView;

        }
        public void setDescText(String descText)
        {
            descView=mView.findViewById(R.id.blog_desc);
            descView.setText(descText);
        }
        public void setBlogImage(String downloadUri)
        {
            blogImageView=mView.findViewById(R.id.blog_image);
            Glide.with(context).load(downloadUri).into(blogImageView);
        }

        public void setTitle(String title) {
            titleView=mView.findViewById(R.id.title);
            titleView.setText(title);
        }

        public void setUid(String uid){

            mname=mView.findViewById(R.id.blog_username);
            mprof=mView.findViewById(R.id.blog_qual);
            mdate=mView.findViewById(R.id.blog_date);
            mtype=mView.findViewById(R.id.blog_type);

            FirebaseFirestore firebaseFirestore = FirebaseFirestore.getInstance();
            firebaseFirestore.collection("Users").document(uid).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                @Override
                public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                    if(task.isSuccessful())
                    {
                        DocumentSnapshot document=task.getResult();
                        String name= document.getString("name");
                        String prof= document.getString("qualification");
                        //String date=document.getString("ti");
                        String type = document.getString("type");
                        //Log.i("LOGGER",value);



                        mname.setText(name);
                        mprof.setText(prof);
                        mtype.setText(type);
                        //mdate.setText(dateString);



                    }

                }
            });





        }

        public void setTime(String time) {

            mdate=mView.findViewById(R.id.blog_date);
            //String dateString = DateFormat.format("MM/dd/yyyy", new Date(time)).toString();
            mdate.setText(time);


        }
    }
}





























































