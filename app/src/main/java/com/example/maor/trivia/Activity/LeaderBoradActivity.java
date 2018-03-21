package com.example.maor.trivia.Activity;

import android.content.Context;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.example.maor.trivia.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class LeaderBoradActivity extends AppCompatActivity  {

    private DatabaseReference myRef;
    private FirebaseAuth mAuth;
    private String userID;
    private FirebaseDatabase database;
    FirebaseUser currentFirebaseUser;


    ListView listView;


    Context context;
    List<String> tasks;
    ArrayAdapter<String> adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_leader_borad);


        context = this;
        tasks = new ArrayList<String>();


        mAuth = FirebaseAuth.getInstance();
        userID = mAuth.getUid();


        database=FirebaseDatabase.getInstance();
        myRef = database.getReference();

       // createANodeInFirebase();



        adapter = new ArrayAdapter<String>(context,android.R.layout.simple_list_item_1,tasks);
        ListView listView = (ListView) findViewById(R.id.leader_board_list);
        listView.setAdapter(adapter);

    }






    @Override
    public void onStart() {
        super.onStart();
        currentFirebaseUser = FirebaseAuth.getInstance().getCurrentUser() ;
        myRef = myRef.child("LeaderBoard");
    }

    @Override
    public void onResume() {
        super.onResume();
       readDataFormFirebase();

    }

    private void SortingScore() {

        Query queryRef = myRef.orderByChild("score").limitToFirst(3);
        queryRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

    }

    private void readDataFormFirebase() {
        Query queryRef = myRef.orderByChild("score").limitToLast(10);

        queryRef.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {

                    Log.d("getValue",""+String.valueOf(dataSnapshot.getValue())); //present all the LeaderBoard
                    Log.d("getKey",""+String.valueOf(dataSnapshot.getKey())); //present all the LeaderBoard
                    Log.d("data",""+String.valueOf(dataSnapshot.getChildren().toString())); //present all the LeaderBoard
                    Log.d("getChildrenCount",""+String.valueOf(dataSnapshot.getChildrenCount())); //present all the LeaderBoard
                    for(DataSnapshot ds : dataSnapshot.getChildren())
                    {
                        Log.d("value",""+String.valueOf(ds.getValue()));
                        String strBuilder = String.valueOf(ds.getValue());
                        strBuilder = strBuilder.replace(",","  ");
                        strBuilder = strBuilder.replace("{"," ");
                        strBuilder = strBuilder.replace("}"," ");
                        tasks.add(strBuilder);
                    }
                    adapter.notifyDataSetChanged();
                }
                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            });
        }
    private void createANodeInFirebase() {

     //   private DatabaseReference myRef;
       // private FirebaseDatabase database;

      //  database=FirebaseDatabase.getInstance();
      //  myRef = database.getReference();


      // myRef = myRef.child("LeaderBoard");

        Map<String, String> addToLeaderBoard = new HashMap<String, String>();
        addToLeaderBoard.put("name","whoWin"); // who is when put in whoWin
        addToLeaderBoard.put("score","TheScore"); // score who win in 1
        myRef.push().setValue(addToLeaderBoard);

    }
}
