package com.example.ishmaamin.curatech;


import android.support.annotation.NonNull;

import com.google.firebase.firestore.Exclude;

public class PresBlogPostId {


    @Exclude
    public String BlogPostId;

    public <T extends PresBlogPostId> T withId(@NonNull final String id) {
        this.BlogPostId = id;
        return (T) this;
    }

}

