package com.example.ishmaamin.curatech;


import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

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


/**
 * A simple {@link Fragment} subclass.
 */
public class HomeFrag extends Fragment {

    private RecyclerView Blogs;
    private List<PresPresBlogPost> blog_list;
    private PresBlogRecyclerAdapter presBlogRecyclerAdapter;


    private FirebaseFirestore firebaseFirestore;
    private FirebaseFirestore FS;
    private FirebaseFirestore SS;
    private FirebaseAuth mAuth;

    private Query Fq,Sq;


    public HomeFrag() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view=inflater.inflate(R.layout.fragment_home, container, false);
        Blogs=view.findViewById(R.id.Blogs);
        blog_list=new ArrayList<>();
        presBlogRecyclerAdapter = new PresBlogRecyclerAdapter(blog_list);
        Blogs.setLayoutManager(new LinearLayoutManager(container.getContext()));
        Blogs.setAdapter(presBlogRecyclerAdapter);
        mAuth= FirebaseAuth.getInstance();
        FirebaseUser firebaseUser=mAuth.getCurrentUser();
        final String userId=firebaseUser.getUid().toString();
        firebaseFirestore=FirebaseFirestore.getInstance();
        FS=FirebaseFirestore.getInstance();
        SS=FirebaseFirestore.getInstance();

        FS.collection("Users").document(userId).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {

                if(task.isSuccessful()) {
                    if(task.getResult().exists()) {
                        String type = task.getResult().getString("type");
                        if (type.equals("Doctor")) {

                            Fq = firebaseFirestore.collection("Comments").orderBy("time", Query.Direction.DESCENDING);
                            Fq.addSnapshotListener(getActivity(), new EventListener<QuerySnapshot>() {
                                @Override
                                public void onEvent(QuerySnapshot documentSnapshots, FirebaseFirestoreException e) {

                                    for (DocumentChange doc : documentSnapshots.getDocumentChanges()) {


                                        if (doc.getType() == DocumentChange.Type.ADDED) {
                                            String blogPostId=doc.getDocument().getId();

                                            PresPresBlogPost presBlogPost = doc.getDocument().toObject(PresPresBlogPost.class).withId(blogPostId);

                                            blog_list.add(presBlogPost);
                                            presBlogRecyclerAdapter.notifyDataSetChanged();

                                        }

                                    }
                                }

                            });
                            Toast.makeText(getActivity(), type, Toast.LENGTH_LONG).show();
                        } else {
                            Fq = firebaseFirestore.collection("Comments").orderBy("time", Query.Direction.DESCENDING);
                            firebaseFirestore.collection("Comments").addSnapshotListener(getActivity(), new EventListener<QuerySnapshot>() {
                                @Override
                                public void onEvent(QuerySnapshot documentSnapshots, FirebaseFirestoreException e) {

                                    for (DocumentChange doc : documentSnapshots.getDocumentChanges()) {


                                        if (doc.getType() == DocumentChange.Type.ADDED) {
                                            String blogPostId=doc.getDocument().getId();

                                            PresPresBlogPost presBlogPost = doc.getDocument().toObject(PresPresBlogPost.class).withId(blogPostId);

                                            String User = presBlogPost.user;
                                            if (User.equals(userId)) {

                                                blog_list.add(presBlogPost);
                                                presBlogRecyclerAdapter.notifyDataSetChanged();

                                            }
                                        }

                                    }

                                }
                            });
                        }
                    }
                }else {
                    Toast.makeText(getActivity(),"Error:" + task.getException().getMessage(),Toast.LENGTH_LONG).show();
                }
            }
        });




        // Inflate the layout for this fragment
        return view;
    }

}
