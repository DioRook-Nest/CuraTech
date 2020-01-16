package com.example.ishmaamin.curatech;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentChange;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;
public class BlogMainActivity extends AppCompatActivity {

    private Toolbar toolbar;
    private FirebaseAuth mAuth;
    private FloatingActionButton addPostBtn;
    private RecyclerView blog_list_view;
    private List<BlogPost> blog_list;
    private FirebaseFirestore firebaseFirestore;
    private BlogRecyclerAdapter blogRecyclerAdapter;
    private String user_id;

    final FirebaseUser currentUser= FirebaseAuth.getInstance().getCurrentUser();



    //FirebaseUser currentUser= FirebaseAuth.getInstance().getCurrentUser();

    //DrawerLayout myLayout = findViewById( R.id.a123 );
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        toolbar=(Toolbar)findViewById(R.id.main_toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("JJHMS");

        if(currentUser!=null)
            getSupportActionBar().setTitle("Blog");


        addPostBtn=findViewById((R.id.add_post_btn));
        firebaseFirestore=FirebaseFirestore.getInstance();

        firebaseFirestore.collection("Posts").orderBy("timestamp", Query.Direction.DESCENDING)
                .addSnapshotListener(new EventListener<QuerySnapshot>() {
                    @Override
                    public void onEvent(QuerySnapshot documentSnapshots, FirebaseFirestoreException e) {
                        for(DocumentChange doc:documentSnapshots.getDocumentChanges())
                        {
                            if(doc.getType()==DocumentChange.Type.ADDED)
                            {
                                BlogPost blogPost=doc.getDocument().toObject(BlogPost.class);
                                blog_list.add(blogPost);
                                blogRecyclerAdapter.notifyDataSetChanged();

                            }
                        }
                    }
                });

        blog_list_view=findViewById(R.id.blog_list_view);
        blog_list=new ArrayList<>();
        blogRecyclerAdapter=new BlogRecyclerAdapter(blog_list);
        blog_list_view.setLayoutManager(new LinearLayoutManager(this));
        blog_list_view.setAdapter(blogRecyclerAdapter);


        mAuth=FirebaseAuth.getInstance();

        addPostBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent newPostIntent =new Intent(BlogMainActivity.this,NewPostActivity.class);
                startActivity(newPostIntent);
                //finish();


            }
        });

        if (currentUser == null ) {
            //sentToLogin();

            /*Intent intent = new Intent(BlogMainActivity.this, UserUserMainActivity.class);
            startActivity(intent);
            finish();*/

            addPostBtn.setVisibility(View.INVISIBLE);

        }
        else {
            user_id = mAuth.getCurrentUser().getUid();


            //final String  value;
            firebaseFirestore.collection("Users").document(user_id).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                @Override
                public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                    String value = null;
                    if (task.isSuccessful()) {
                        DocumentSnapshot document = task.getResult();
                        value = document.getString("type");
                        Log.i("LOGGER", value);
                    }


                    if (value.equals("User")) {
                        //sentToLogin();

                        /*Intent intent = new Intent(BlogMainActivity.this, BlogMainActivity.class);
                        startActivity(intent);
                        finish();*/

                        addPostBtn.setVisibility(View.INVISIBLE);
                    }
                    else
                        addPostBtn.setVisibility(View.VISIBLE);
                }
            });
        }


    }
    @Override
    protected void onStart()
    {
        super.onStart();


        if (currentUser == null ) {
            //sentToLogin();

            /*Intent intent = new Intent(UMainActivity.this, BlogMainActivity.class);
            startActivity(intent);
            finish();*/

            addPostBtn.setVisibility(View.INVISIBLE);

        }
        else {
            user_id = mAuth.getCurrentUser().getUid();


            //final String  value;
            firebaseFirestore.collection("Users").document(user_id).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                @Override
                public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                    String value = null;
                    if (task.isSuccessful()) {
                        DocumentSnapshot document = task.getResult();
                        value = document.getString("type");
                        Log.i("LOGGER", value);
                    }


                    if (value.equals("User")) {
                        //sentToLogin();

                        /*Intent intent = new Intent(UMainActivity.this, BlogMainActivity.class);
                        startActivity(intent);
                        finish();*/

                        addPostBtn.setVisibility(View.INVISIBLE);
                    }
                }
            });
        }



    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main_menu,menu);
        return true;

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if(item.getItemId()==R.id.action_logout_btn)
        {
            logout();
            return true;
        }
        else if(item.getItemId()==R.id.nav_blog) {
            if (currentUser == null)
                sentToLogin();
            else
                ViewBlog();
        }

        else if (item.getItemId()==R.id.nav_ehr){

            if (currentUser == null)
                sentToLogin();
            else
                ViewEhr();

        }
        else if (item.getItemId()==R.id.nav_chat){

            if (currentUser == null)
                sentToLogin();
            else
                ViewChat();

        }
        else if (item.getItemId()==R.id.nav_pres){

            if (currentUser == null)
                sentToLogin();
            else
                ViewPres();

        }





       /* switch (item.getItemId()) {



            case android.R.id.home:
                mDrawerLayout.openDrawer(GravityCompat.START);
                return true;


        }*/
        return super.onOptionsItemSelected(item);



    }

    private void ViewPres() {
        Intent intent=new Intent(BlogMainActivity.this,PresMainActivity.class);
        startActivity(intent);

    }

    public void logout()
    {
        mAuth.signOut();
        sentToLogin();

    }
    public void sentToLogin()
    {
        Intent intent=new Intent(BlogMainActivity.this,LoginActivity.class);
        startActivity(intent);
        finish();
    }

    public void  ViewBlog()
    {
        String user_id =mAuth.getCurrentUser().getUid();
        firebaseFirestore.collection("Users").document(user_id).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if(task.isSuccessful())
                {
                    DocumentSnapshot document=task.getResult();
                    String value = document.getString("type");
                    Log.i("LOGGER",value);
                    if(value.equals("Doctor"))
                    {
                        Intent mainIntent =new Intent(BlogMainActivity.this,BlogMainActivity.class);
                        startActivity(mainIntent);
                        finish();
                    }

                }

            }
        });
    }

    /*@Override
    public void onBackPressed() {
        if(mDrawerLayout.isDrawerOpen(GravityCompat.START))
        {

            mDrawerLayout.closeDrawers();
        }
        else
        {
            super.onBackPressed();
        }
    }*/


    public void  ViewEhr() {
        Intent m1Intent = new Intent(BlogMainActivity.this, EhrInsertActivity.class);
        startActivity(m1Intent);
        //finish();
    }

    public void  ViewChat() {
        Intent m1Intent = new Intent(BlogMainActivity.this, ChatMainActivity.class);
        startActivity(m1Intent);
        //finish();
    }
}
