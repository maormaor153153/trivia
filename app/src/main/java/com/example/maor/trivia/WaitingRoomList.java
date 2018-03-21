package com.example.maor.trivia;

import android.app.Activity;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.TextView;

import com.example.maor.trivia.Activity.CompeteActivity;
import com.example.maor.trivia.Class.GameRoom;
import com.example.maor.trivia.Class.Question;
import com.example.maor.trivia.Class.Quiz;
import com.example.maor.trivia.Activity.User;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * Created by orenshadmi on 18/03/2018.
 */

public class WaitingRoomList extends ArrayAdapter<WaitingRoom> {

    private final Object correctColor;
    private final FirebaseUser currentFirebaseUser;
    private final FirebaseDatabase database;
    private final DatabaseReference gameRoomRef;
    private final DatabaseReference waitingRoomRef;
    private Activity context;
    private List<WaitingRoom> waitingRoomList;
    private String id;
    Random r;

    private Quiz quizMaster;
    ArrayList<Question> questionsRepo;
    WaitingRoom waitingRoom;

    public WaitingRoomList(Activity context,List<WaitingRoom> waitingRoomList){
        super(context, R.layout.listlayout,waitingRoomList);
        this.context = context;
        this.waitingRoomList = waitingRoomList;

        database = FirebaseDatabase.getInstance();
        gameRoomRef = database.getReference("Game Room");
        waitingRoomRef = database.getReference("Waiting room");


        currentFirebaseUser = FirebaseAuth.getInstance().getCurrentUser() ;

        correctColor = context.getResources().getDrawable(R.drawable.correct_shape);
    }

    @NonNull
    @Override
    public View getView(final int position, @Nullable View convertView, @NonNull final ViewGroup parent) {
        LayoutInflater inflater = context.getLayoutInflater();

        View listViewItem = inflater.inflate(R.layout.listlayout,null, true);

        Button button = listViewItem.findViewById(R.id.Join);


        TextView textView = listViewItem.findViewById(R.id.name);

        waitingRoom =waitingRoomList.get(position);

        textView.setText(waitingRoom.getUserWaiting().getName());


        if(button !=null) {
            button.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(waitingRoom.getUserWaiting().getId() != currentFirebaseUser.getUid()){
                        String gameRoomId = createGameRoom(waitingRoom, parent);
                        if(waitingRoom.getWaitRoomId() != null) {
                            waitingRoom.setReady(true);
//                           waitingRoomRef.child(waitingRoom.getWaitRoomId()).removeValue();
                            Intent intent=new Intent(parent.getContext(), CompeteActivity.class);

                            intent.putExtra("WaitingRoom", waitingRoom.getWaitRoomId());
//                            zoom.putExtra("GameRoom ID", gameRoomId);
//                            zoom.putExtra("Player2 id", currentFirebaseUser.getUid());
//                            zoom.putExtra("Room id", waitingRoom.getWaitRoomId());

                            parent.getContext().startActivity(intent);
                        }

                    }
                }
            });
        }

        return listViewItem;
    }

    private String createGameRoom(WaitingRoom waitingRoom, ViewGroup parent) {

        User player1 = waitingRoom.getUserWaiting();
        User player2 = new User (currentFirebaseUser.getDisplayName(),currentFirebaseUser.getUid());
        waitingRoom.setUserJoining(player2);

        id = gameRoomRef.push().getKey();
        waitingRoom.setGameRoomId(id);

        waitingRoomRef.child(waitingRoom.getWaitRoomId()).child("Game Room id").setValue(id);
        waitingRoomRef.child(waitingRoom.getWaitRoomId()).child("userJoining").setValue(player2);
        waitingRoomRef.child(waitingRoom.getWaitRoomId()).child("ready").setValue(true);


        int numOfQuestion = getRandomQuestionNum(parent);


        GameRoom gameRoom = new GameRoom(player1,player2,id , numOfQuestion);

        gameRoomRef.child(id).setValue(gameRoom);
        return id;
    }




    private int getRandomQuestionNum(ViewGroup parent) {

        r = new Random();
        quizMaster = new Quiz(parent.getContext());
        questionsRepo = quizMaster.getQuestions();

        try {
            quizMaster.readTrueFalseQuestionFromFile();
            quizMaster.readMCQuestionsFromFile();
            Log.e("ArrayList:", quizMaster.toString());
        } catch (IOException e) {
            e.printStackTrace();
        }

        return r.nextInt(questionsRepo.size());
    }


}


