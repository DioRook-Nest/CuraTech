package com.example.ishmaamin.curatech;
import android.app.Activity;
import android.text.method.ScrollingMovementMethod;
import android.widget.TextView;
import android.widget.Toast;

/**
 * Created by Ishma Amin on 16-09-2018.
 */

public class blogItemList extends Activity{
    blogItemList()
    {
        setContentView(R.layout.blog_list_item);
        TextView textView=(TextView) findViewById(R.id.blog_desc);
        textView.setMovementMethod(new ScrollingMovementMethod());

    }
}
