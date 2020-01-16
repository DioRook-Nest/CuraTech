package com.example.ishmaamin.curatech;;

import android.content.Intent;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;

public class EhrHistoryActivity extends AppCompatActivity {
    private Button blood;
    private Button urine;
    private Button other;
    public Toolbar toolbar;
    public static final String EXTRA_MESSAGE = "com.example.myfirstapp.MESSAGE";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ehr_history);


        toolbar=(Toolbar)findViewById(R.id.hist_toolbar);
        setSupportActionBar(toolbar);

        ActionBar actionbar = (ActionBar)getSupportActionBar();
        actionbar.setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("View EHR History");


        blood=findViewById(R.id.blood_report);
        urine=findViewById(R.id.urine_test);
        other=findViewById(R.id.other);

        blood.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(EhrHistoryActivity.this, EhrRetrieveActivity.class);
                Button editText = (Button) findViewById(R.id.blood_report);
                intent.putExtra(EXTRA_MESSAGE, "blood report");
                startActivity(intent);
            }
        });

    urine.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            Intent intent = new Intent(EhrHistoryActivity.this, EhrRetrieveActivity.class);
            Button editText = (Button) findViewById(R.id.urine_test);
            String message = editText.getText().toString();
            intent.putExtra(EXTRA_MESSAGE,"urine culture");
            startActivity(intent);

            }
    });

    other.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            Intent intent = new Intent(EhrHistoryActivity.this, EhrRetrieveActivity.class);
            Button editText = (Button) findViewById(R.id.other);
            intent.putExtra(EXTRA_MESSAGE, "other");
            startActivity(intent);
        }
    });

    }


}
