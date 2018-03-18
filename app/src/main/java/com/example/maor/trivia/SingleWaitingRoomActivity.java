package com.example.maor.trivia;

import android.support.v7.app.AppCompatActivity;

//package com.example.maor.trivia;
//
//import android.support.v7.app.AppCompatActivity;
//import android.os.Bundle;
//import android.widget.TextView;
//
//import com.google.firebase.auth.FirebaseAuth;
//import com.google.firebase.auth.FirebaseUser;
//import com.google.firebase.database.DataSnapshot;
//import com.google.firebase.database.DatabaseError;
//import com.google.firebase.database.DatabaseReference;
//import com.google.firebase.database.FirebaseDatabase;
//import com.google.firebase.database.ValueEventListener;
//
public class SingleWaitingRoomActivity extends AppCompatActivity {

//
//
//
//    FirebaseDatabase database;
//    DatabaseReference myRef;
//    FirebaseUser currentFirebaseUser;
//    TextView roomidTextView,numofPLayers, player1TextView, player2TextView, isRoomFullTextView;
//    private User playerWaiting;
//
//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_single_waiting_room);
//
//
//        String roomId = getIntent().getStringExtra("Room id");
//        String player1Name = getIntent().getStringExtra("Player waiting name");
//        String player1Id = getIntent().getStringExtra("Player waiting id");
//        String isFull= getIntent().getStringExtra("isFull");
//
//        playerWaiting = new User(player1Name,player1Id);
//
//        database = FirebaseDatabase.getInstance();
//        currentFirebaseUser = FirebaseAuth.getInstance().getCurrentUser() ;
//        myRef = database.getReference("Waiting room");
//
//
//
//        roomidTextView= findViewById(R.id.room_id);
//        player1TextView = findViewById(R.id.player1);
//        player2TextView = findViewById(R.id.player2);
//        isRoomFullTextView = findViewById(R.id.is_room_is_full);
//
//        roomidTextView.append(roomId);
//        player1TextView.append(player1Name);
//        isRoomFullTextView.append(isFull);
//    }
//
//    protected void onStart() {
//        super.onStart();
//
//        myRef.addValueEventListener(new ValueEventListener() {
//            @Override
//            public void onDataChange(DataSnapshot dataSnapshot) {
//
//
//
//
//
//            }
//
//
//            @Override
//            public void onCancelled(DatabaseError databaseError) {
//
//            }
//        });
//    }
}
