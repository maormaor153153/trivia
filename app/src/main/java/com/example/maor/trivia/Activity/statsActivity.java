package com.example.maor.trivia.Activity;

import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.maor.trivia.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;

/**
 * Created by User on 2/8/2017.
 */

public class statsActivity extends AppCompatActivity {
    private static final String TAG = "ViewDatabase";

    //add Firebase Database stuff
    private FirebaseDatabase mFirebaseDatabase;
    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener mAuthListener;
    private DatabaseReference myRef;
    private String userID;
    private ListView mListView;
    private int  flagForStatsOpen;



    private  TextView mtotal;
    private  TextView mwin;
    private  TextView mlose;
    private  TextView mhighscore;
    private  TextView mEmail;



    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_stats);
        //declare the database reference object. This is what we use to access the database.
        //NOTE: Unless you are signed in, this will not be useable.
        mAuth = FirebaseAuth.getInstance();
        mFirebaseDatabase = FirebaseDatabase.getInstance();
        myRef = mFirebaseDatabase.getReference();
        FirebaseUser user = mAuth.getCurrentUser();
        userID = user.getUid();

    }
    @Override
    public void onStart() {
        super.onStart();
        checkAuth();
        mAuth.addAuthStateListener(mAuthListener);
    }
    @Override
    public void onResume() {
        super.onResume();

        checkTocreatestastNode();
        final Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                openStatsInFirebase();
            }
        }, 3000);


        Log.d("flag",""+ flagForStatsOpen);
       updateDateFromDateBase();



       //uploadStatsToFireBase();

    }
    @Override
    public void onStop() {
        super.onStop();
        if (mAuthListener != null) {
            mAuth.removeAuthStateListener(mAuthListener);
        }
    }
    /**
     * customizable toass
     * @param message
     */
    private void toastMessage(String message) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
    }

    public void openStatsInFirebase()
    {
        Log.d("flag","openStatsInFirebase" +flagForStatsOpen);
        if(flagForStatsOpen == 1)
        {
            HashMap forstats = new HashMap();
            myRef = FirebaseDatabase.getInstance().getReference("stats");

            forstats.put("Total_games_played", 0);
            forstats.put("Win", 0);
            forstats.put("Lose", 0);
            forstats.put("High_Score", 0);
            forstats.put("Email",mAuth.getCurrentUser().getEmail().toString());

            String emailWithOutPoint2 = mAuth.getCurrentUser().getEmail().toString();
            emailWithOutPoint2 = emailWithOutPoint2.replace(".","");
            myRef.child(emailWithOutPoint2).setValue(forstats);
        }
    }
    private void checkAuth() {
        mAuthListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser user = firebaseAuth.getCurrentUser();
                if (user != null) {
                    // User is signed in
                    Log.d(TAG, "onAuthStateChanged:signed_in:" + user.getUid());
                    toastMessage("Successfully signed in with: " + user.getEmail());
                } else {
                    // User is signed out
                    Log.d(TAG, "onAuthStateChanged:signed_out");
                    toastMessage("Successfully signed out.");
                }
                // ...
            }
        };
    }
    private void updateDateFromDateBase() {

        mtotal = findViewById(R.id.totalGameNumber);
        mwin = findViewById(R.id.winNubmer);
        mlose = findViewById(R.id.loseNumber);
        mhighscore = findViewById(R.id.highScoreNumber);
        mEmail= findViewById(R.id.EmailNumber);

        myRef = FirebaseDatabase.getInstance().getReference("stats");

        String emailWithOutPoint = mAuth.getCurrentUser().getEmail().toString();
        emailWithOutPoint = emailWithOutPoint.replace(".","");


        myRef.child(emailWithOutPoint).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                mtotal.setText(String.valueOf(dataSnapshot.child("Total_games_played").getValue()));
                mwin.setText(String.valueOf(dataSnapshot.child("Win").getValue()));
                mlose.setText(String.valueOf(dataSnapshot.child("Lose").getValue()));
                mhighscore.setText(String.valueOf(dataSnapshot.child("High_Score").getValue()));
                mEmail.setText(String.valueOf(dataSnapshot.child("Email").getValue()));

            }
            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }
    private void checkTocreatestastNode() {
        flagForStatsOpen = 1;

        myRef = FirebaseDatabase.getInstance().getReference("stats");
        myRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                String emailWithOutPoint = mAuth.getCurrentUser().getEmail().toString();
                emailWithOutPoint = emailWithOutPoint.replace(".", "");

                if (dataSnapshot.hasChild(emailWithOutPoint)) {
                        flagForStatsOpen = 0;
                }
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }



}

