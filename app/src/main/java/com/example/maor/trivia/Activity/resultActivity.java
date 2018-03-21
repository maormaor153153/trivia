package com.example.maor.trivia.Activity;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.example.maor.trivia.R;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class resultActivity extends AppCompatActivity {

    TextView mscore;
    TextView mstats;
    Button mbtn;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_result);



        Intent intent = getIntent();
        String status = intent.getExtras().getString("status");
        String score = intent.getExtras().getString("score");

        mscore = findViewById(R.id.statsLoseORwin);
        mstats = findViewById(R.id.score);
        mbtn = findViewById(R.id.button_back);

        mscore.setText(score);
        mstats.setText(status);

        mbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getApplicationContext(), MenuActivity.class));
                overridePendingTransition(R.anim.fui_slide_in_right, R.anim.fui_slide_out_left);

            }
        });


    }
    public void onBackPressed()
    {
        Intent intent = new Intent(resultActivity.this, MenuActivity.class);
        startActivity(intent);
        finish();

    }
    protected void onStop() {
        super.onStop();

    }
}
