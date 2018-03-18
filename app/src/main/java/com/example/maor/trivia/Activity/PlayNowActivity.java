package com.example.maor.trivia.Activity;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.example.maor.trivia.R;

public class PlayNowActivity extends AppCompatActivity {

    Button quickGame , compete;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_play_now);






        quickGame = (Button) findViewById(R.id.quickGame);
        compete = findViewById(R.id.compete);

        quickGameListener();
        competeListener();

    }

    private void competeListener() {
        compete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // 1 - check to see if there is a waiting room open

                checkWaitingRoom();

                //2 - creating an empty waiting room and wait for a user to join



                startActivity(new Intent(getApplicationContext(), WaitingRoomActivity.class));

            }
        });

    }



    private void checkWaitingRoom() {
    }


    private void quickGameListener() {
        quickGame.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getApplicationContext(), QuickGameActivity.class));
            }
        });
    }
}
