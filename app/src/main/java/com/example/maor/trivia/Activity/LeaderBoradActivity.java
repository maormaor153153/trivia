package com.example.maor.trivia.Activity;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.example.maor.trivia.Class.LeaderBoardInfo;
import com.example.maor.trivia.Class.User;
import com.example.maor.trivia.Class.UserInformation;
import com.example.maor.trivia.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;

public class LeaderBoradActivity extends AppCompatActivity {

    private DatabaseReference myRef;
    private FirebaseAuth mAuth;
    private String userID;
    private ListView mListView;
    FirebaseUser currentFirebaseUser;


    private  ArrayList<String> mUsername = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(com.example.maor.trivia.R.layout.activity_leader_borad);
        mAuth = FirebaseAuth.getInstance();
        userID = mAuth.getUid();

        mListView =  findViewById(R.id.listviewLeaderBoard);
        currentFirebaseUser = FirebaseAuth.getInstance().getCurrentUser() ;
        myRef = FirebaseDatabase.getInstance().getReference().child(currentFirebaseUser.getUid());


        final ArrayAdapter<String> arrayAdapter =new ArrayAdapter<String>(this,android.R.layout.simple_list_item_1,mUsername);

        mListView.setAdapter(arrayAdapter);


        //create a Leader Board for firebase
        ///////////////////////
        /*
        HashMap forLeaderBoard = new HashMap();
        forLeaderBoard.put("EmailLeader", mAuth.getCurrentUser().getEmail());
        forLeaderBoard.put("ScoreLeader", 0);
        myRef.push().setValue(forLeaderBoard);
        */
        ////////////////////

       // myRef.child("LeaderBoard").child(currentFirebaseUser.getUid());

            myRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                showData(dataSnapshot);
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });


    }

    private void showData(DataSnapshot dataSnapshot) {

        for (DataSnapshot ds : dataSnapshot.getChildren()) {
            LeaderBoardInfo uInfo = new LeaderBoardInfo();
            Log.d("hello","world");
            uInfo.setEmailLeader(ds.child("EmailLeader").getValue(LeaderBoardInfo.class).getEmailLeader()); //set the name
            uInfo.setScoreLeader(ds.child("ScoreLeader").getValue(LeaderBoardInfo.class).getScoreLeader()); //set the email


            ArrayList<String> array = new ArrayList<>();
            array.add(uInfo.getScoreLeader());
            array.add(uInfo.getEmailLeader());
            ArrayAdapter adapter = new ArrayAdapter(this, android.R.layout.simple_list_item_1, array);
            mListView.setAdapter(adapter);
        }
    }

}
