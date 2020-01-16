package com.example.ishmaamin.curatech;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.List;

public class PresBlogRecyclerAdapter extends RecyclerView.Adapter<PresBlogRecyclerAdapter.ViewHolder> {

    public List<PresPresBlogPost> blog_list;
    private FirebaseFirestore firebaseFirestore;
    private Context context;



    public PresBlogRecyclerAdapter(List<PresPresBlogPost> blog_list){

        this.blog_list=blog_list;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {

        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.presblogitem,viewGroup,false);
        context=viewGroup.getContext();

        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final ViewHolder viewHolder, int i) {
        String desc_data= blog_list.get(i).getDesc();
        String title_data=blog_list.get(i).getTitle();

        final String blogPostId = blog_list.get(i).BlogPostId;



        viewHolder.setDescText(desc_data);
        viewHolder.setTitle(title_data);

        firebaseFirestore=FirebaseFirestore.getInstance();

        //User Data will be retrieved here...

        final String userId=blog_list.get(i).getUser();

        firebaseFirestore.collection("Users").document(userId).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {


                if (task.isSuccessful()){

                    String username=task.getResult().getString("name");
                    viewHolder.setUsername(username);
                }else{

                }
            }
        });




        //Get Likes Count



        //Get Likes


        //Likes Feature\

        viewHolder.CommentBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent commentIntent = new Intent(context, CommentsActivity.class);
                commentIntent.putExtra("blog_post_id", blogPostId);
                context.startActivity(commentIntent);
            }
        });





}

    @Override
    public int getItemCount() {
        return blog_list.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{

        private View mView;
        private TextView descView;
        private  TextView TitleView;
        private TextView Username;
        private ImageView CommentBtn;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            mView=itemView;
            CommentBtn=mView.findViewById(R.id.blogCommentBtn);

        }

        public void setDescText(String descText){

            descView = mView.findViewById(R.id.blogText);
            descView.setText(descText);

        }
        public void setTitle(String TitleText){

            TitleView = mView.findViewById(R.id.blogTitle);
            TitleView.setText(TitleText);

        }

        public void setUsername(String username){
            Username=mView.findViewById(R.id.blogName);
            Username.setText(username);
        }
    }

}
