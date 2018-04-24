package com.example.maor.trivia.Activity;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import com.bumptech.glide.Glide;
import com.example.maor.trivia.R;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class MenuActivity extends AppCompatActivity {
    private FirebaseAuth mAuth;
    private GoogleSignInClient mGoogleSignInClient;
    private FirebaseUser user ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu);

        mAuth = FirebaseAuth.getInstance();
        final FirebaseUser user = mAuth.getCurrentUser();

        Button playNow = (Button) findViewById(R.id.playNow);
        playNow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getApplicationContext(), PlayNowActivity.class));
                overridePendingTransition(R.anim.fui_slide_in_right, R.anim.fui_slide_out_left);

            }
        });

        Button setting = (Button) findViewById(R.id.settings);
        setting.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getApplicationContext(), SettingsActivity.class));
                overridePendingTransition(R.anim.fui_slide_in_right, R.anim.fui_slide_out_left);

            }
        });


        ImageView editProfile = (ImageView) findViewById(R.id.image_profile);
        editProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getApplicationContext(), ProfileActivity.class));
                overridePendingTransition(R.anim.fui_slide_in_right, R.anim.fui_slide_out_left);

            }
        });


        TextView profile = findViewById(R.id.edit_profile_text);
        profile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getApplicationContext(), ProfileActivity.class));
                overridePendingTransition(R.anim.fui_slide_in_right, R.anim.fui_slide_out_left);

            }
        });

        Button stats = (Button) findViewById(R.id.yourStats);
        stats.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getApplicationContext(), statsActivity.class));
                overridePendingTransition(R.anim.fui_slide_in_right, R.anim.fui_slide_out_left);

            }
        });

        Button leaderboard = (Button) findViewById(R.id.leaderboard);
        leaderboard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getApplicationContext(), LeaderBoradActivity.class));
                overridePendingTransition(R.anim.fui_slide_in_right, R.anim.fui_slide_out_left);

            }
        });


    }
        @Override
        public void onStart() {
            ImageView imageView3 = findViewById(R.id.imageView);
            super.onStart();

        }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if ((keyCode == KeyEvent.KEYCODE_BACK)) {
            Log.d(this.getClass().getName(), "back button pressed");
            this.moveTaskToBack(true);
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }


}


