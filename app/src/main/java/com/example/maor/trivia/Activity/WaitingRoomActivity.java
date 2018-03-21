package com.example.maor.trivia.Activity;

import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.maor.trivia.Class.Question;
import com.example.maor.trivia.Class.Quiz;
import com.example.maor.trivia.R;
import com.example.maor.trivia.WaitingRoom;
import com.example.maor.trivia.WaitingRoomList;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class WaitingRoomActivity extends AppCompatActivity {


    Button createRoomButton;
    LinearLayout linearLayout;
    boolean isUserOpenedRoom;

    LinearLayout textButtonContainer;

    FirebaseDatabase database;
    DatabaseReference myRef;
    FirebaseUser currentFirebaseUser;

    ListView listViewWaitingRoom;

    List<WaitingRoom> waitingRoomList;
    private String id;
    private Button joinButton;
    private Drawable correctColor;
    private DatabaseReference questionsRef;
    private Quiz quiz;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_waiting_room);


        quiz = new Quiz(this);

        createRoomButton =findViewById(R.id.create_room);
//        linearLayout =findViewById(R.id.waiting_room_container);
        listViewWaitingRoom =findViewById(R.id.waiting_rooms_container);

        database = FirebaseDatabase.getInstance();
        myRef = database.getReference("Waiting room");
        questionsRef = database.getReference("Questions Repo");

        currentFirebaseUser = FirebaseAuth.getInstance().getCurrentUser() ;
        myRef.child("Waiting Room").child(currentFirebaseUser.getUid());
        waitingRoomList = new ArrayList<>();

        isUserOpenedRoom = false;

        readWaitingRoomFromFireBase();


        joinButton = findViewById(R.id.join_game);





        //   writeQuestionToFireBase();
        createWaitingRoom();

    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if ((keyCode == KeyEvent.KEYCODE_BACK)) {
            Log.d(this.getClass().getName(), "back button pressed");
            myRef.removeValue();
        }
        return super.onKeyDown(keyCode, event);
    }

    private void writeQuestionToFireBase() {


        ArrayList<Question> questionsRepo = quiz.getQuestions();
        String questionId = questionsRef.push().getKey();
        readQuestionsFromFiles();
        int numOfQuestions = questionsRepo.size();

        for (int i = 0 ; i <  numOfQuestions; i ++){
            questionId = questionsRef.push().getKey();
            questionsRepo.get(i).setId(questionId);
            questionsRef.child(questionsRepo.get(i).getId()).setValue(questionsRepo.get(i));
        }



    }

    private void readQuestionsFromFiles() {
        try {
            quiz.readTrueFalseQuestionFromFile();
            quiz.readMCQuestionsFromFile();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    public void joinGame() {
        int size = listViewWaitingRoom.getChildCount();
        for(int i = 0 ; i < size; i++){
            if(listViewWaitingRoom.getChildAt(i) instanceof Button) {
                Button btn = (Button) listViewWaitingRoom.getChildAt(i);
                btn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        v.setBackground(correctColor);
                    }
                });
            }

        }
    }
    @Override
    protected void onResume() {
        super.onResume();
        //readWaitingRoomFromFireBase();
    }

    @Override
    protected void onStop() {
        super.onStop();
        if(id != null) {
            //  myRef.child(id).removeValue();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if(id != null) {
//            myRef.child(id).removeValue();
        }
    }

    private void readWaitingRoomFromFireBase() {
    }

    @Override
    protected void onStart() {
        super.onStart();



        myRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                waitingRoomList.clear();

                for(DataSnapshot waitingRoomSnapShot : dataSnapshot.getChildren()){
                    WaitingRoom waitingRoom = waitingRoomSnapShot.getValue(WaitingRoom.class);
                    waitingRoomList.add(waitingRoom);
                }

                WaitingRoomList adapter = new WaitingRoomList(WaitingRoomActivity.this, waitingRoomList);
                listViewWaitingRoom.setAdapter(adapter);


                joinGame();
            }


            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    private void  createWaitingRoom() {
        createRoomButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                if(!isUserOpenedRoom) {
                    isUserOpenedRoom = true;
                    WaitingRoom waitingRoom = createWaitingRoomInFireBase();
                    final String waitingId = waitingRoom.getWaitRoomId();

                    final DatabaseReference isWaitingReference = database.getReference("Waiting room").child(waitingRoom.getWaitRoomId());
                    DatabaseReference isReadyReference = isWaitingReference.child("ready");

                    isReadyReference.addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {
                            Boolean isReady = dataSnapshot.getValue(Boolean.class);
                            if (isReady != null) {
                                if (isReady) {
//                                isWaitingReference.removeValue();

                                    Intent intent = new Intent(getApplicationContext(), CompeteActivity.class);
                                    intent.putExtra("WaitingRoom", waitingId);
                                    startActivity(intent);
                                }
                            }
                        }

                        @Override
                        public void onCancelled(DatabaseError databaseError) {

                        }
                    });

//                    Intent intent = new Intent(getApplicationContext(), com.example.maor.trivia.SingleWaitingRoomActivity.class);
//                    intent.putExtra("Room id", waitingRoom.getWaitRoomId());
//                    intent.putExtra("Player waiting name", waitingRoom.getUserWaiting().getName());
//                    intent.putExtra("Player waiting id", waitingRoom.getUserWaiting().getId());
//                    intent.putExtra("isFull",""+waitingRoom.isReady());
//                    startActivity(intent);
                }
            }
        });
    }


    private WaitingRoom createWaitingRoomInFireBase() {


        User userWaiting = new User(currentFirebaseUser.getDisplayName(), currentFirebaseUser.getUid());


        id = myRef.push().getKey();
        WaitingRoom newWaitingRoom = new WaitingRoom(userWaiting,null,id);

        myRef.child(id).setValue(newWaitingRoom);

        return newWaitingRoom;


    }

    private LinearLayout createLayoutForWaitingRoom() {
        LinearLayout linearLayout = new LinearLayout(this);
        Button btn = new Button(this);
        TextView txtView = new TextView(this);

        btn.setText("Join");
        txtView.setText(currentFirebaseUser.getDisplayName());

        RelativeLayout.LayoutParams lp = new RelativeLayout.LayoutParams(
                RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
        lp.addRule(RelativeLayout.BELOW, R.id.create_room);
        linearLayout.addView(btn);
        linearLayout.addView(txtView);

        return linearLayout;


    }
}
