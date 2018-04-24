package com.example.maor.trivia.Activity;

import android.animation.Animator;
import android.animation.ObjectAnimator;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.AssetFileDescriptor;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.media.MediaPlayer;
import android.os.CountDownTimer;
import android.os.Handler;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.animation.DecelerateInterpolator;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.example.maor.trivia.Class.GameRoom;
import com.example.maor.trivia.Class.MultipleChoice;
import com.example.maor.trivia.Class.Question;
import com.example.maor.trivia.Class.Quiz;
import com.example.maor.trivia.R;
import com.example.maor.trivia.TrueFalse;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.io.IOException;
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;

public class CompeteActivity extends AppCompatActivity {


    Button choice1, choice2, choice3, choice4;
    TextView player1TextView, player2TextView;
    private static final String CORRECT_STR_SOUND= "correct.mp3";
    private static final String WRONG_STR_SOUND= "wrong.mp3";


    FirebaseDatabase database;

    DatabaseReference gameRoomRef;
    FirebaseUser currentFirebaseUser;
    private DatabaseReference waitingRoomRef;

    private int player1Score, player2Score = 0;
    private Quiz quizMaster;
    ArrayList<Question> questionsRepo;
    int numOfQuestions;
    int currentQuestionNum;
    CountDownTimer timer;
    ObjectAnimator animation;
    Handler handler ;
    AssetFileDescriptor afd;
    int numOfQuestion;
    private com.example.maor.trivia.Activity.User player1;
    private com.example.maor.trivia.Activity.User player2;

    Drawable correctColor;
    Drawable wrongColor;
    Drawable notAnsweredColor;

    LinearLayout linearLayout;

    private static final String PLAYER1_FB_REF= "player1";
    private static final String PLAYER2_FB_REF= "player2";
    private static final String SCORE_FB_REF= "score";
    private static final String OPTION_CLICKED_FB_REF= "optionClicked";
    private static final String NUM_OF_QUESTION_TO_DISPLAY_FB_REF= "numOfQuestionToDisplay";
    private static final String WHO_PLAY_FB_REF= "whoPlay";


    ArrayList<String> forSaveData;
    ArrayList<Integer> forSaveDataNumber;
    private DatabaseReference myRef;
    private FirebaseAuth mAuth;


    Random r;
    private ProgressBar mProgressBar;
    private long timeLeft;

    private DatabaseReference numOfQuestionRef;
    private List<GameRoom> gameRoomList;
    private String gameRoomid;

    private boolean isFirstChoosen;
    private boolean isqQuestionChanged;
    private String playerToStart;
    private boolean isArrayListChanged;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_compete);
        gameRoomList = new ArrayList<>();


        isArrayListChanged = false;
        Intent intent = getIntent();
        String id = intent.getStringExtra("WaitingRoom");





        currentQuestionNum = -1;

        database = FirebaseDatabase.getInstance();
        waitingRoomRef = database.getReference("Waiting room").child(id);


        currentFirebaseUser = FirebaseAuth.getInstance().getCurrentUser() ;

        quizMaster = new Quiz(this);
        questionsRepo = quizMaster.getQuestions();

        handler = new Handler();

        notAnsweredColor = getResources().getDrawable(R.drawable.shape);
        correctColor = getResources().getDrawable(R.drawable.correct_shape);
        wrongColor = getResources().getDrawable(R.drawable.wrong_shape);


        mProgressBar=(ProgressBar)findViewById(R.id.progressbar);

        linearLayout = findViewById(R.id.question_container);



        choice1 = (Button) findViewById(R.id.choice1);
        choice2 = (Button) findViewById(R.id.choice2);
        choice3 = (Button) findViewById(R.id.choice3);
        choice4 = (Button) findViewById(R.id.choice4);

        player1TextView = (TextView) findViewById(R.id.player1Score);
        player2TextView = (TextView) findViewById(R.id.player2Score);

        initilaizeTextView(player1TextView, "Score:");
        initilaizeTextView(player2TextView, "Score:");


        isFirstChoosen = false;
        getQuestionNum();



        try {
            quizMaster.readTrueFalseQuestionFromFile();
            quizMaster.readMCQuestionsFromFile();
            Log.e("ArrayList:", quizMaster.toString());
        } catch (IOException e) {
            e.printStackTrace();
        }






        forSaveData = new ArrayList<>();
        forSaveDataNumber = new ArrayList<>();







    }





    @Override
    protected void onStop() {
        super.onStop();
       // gameRoomRef.removeValue();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        gameRoomRef.removeValue();
    }

    @Override
    protected void onStart() {
        super.onStart();

        waitingRoomRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                gameRoomid = (String)dataSnapshot.child("Game Room id").getValue();

                if(gameRoomid != null) {
                    gameRoomRef = database.getReference("Game Room").child(gameRoomid);
                }





            }



            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });




    }



    private void getQuestionNum() {
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                gameRoomRef.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {




                        Long n = (Long) dataSnapshot.child(NUM_OF_QUESTION_TO_DISPLAY_FB_REF).getValue();




                        player1 =  dataSnapshot.child("player1").getValue(com.example.maor.trivia.Activity.User.class);
                        player2 =  dataSnapshot.child("player2").getValue(com.example.maor.trivia.Activity.User.class);

                        if(!isFirstChoosen &
                                currentFirebaseUser.getUid().
                                        equals(player1.getId())){
                            isFirstChoosen = true;
                            whoPlayFirst();
                        }

                        playerToStart = (String)dataSnapshot.child("whoPlay").getValue();
                        if(playerToStart != null ) {


                            setPlayerTurn(playerToStart);
                        }

                        ArrayList<String> arrayList = (ArrayList<String>) dataSnapshot.child("options").getValue();
                        if(arrayList != null && !isArrayListChanged){
                            isArrayListChanged = true;
                            setTextToOptionsButtons(arrayList);
                        }

                        if(playerToStart != null ) {
                            if (n.intValue() != currentQuestionNum) {String name = (String) dataSnapshot.child(playerToStart).child("name").getValue();
//                             currentQuestionNum = numOfQuestionToDisplay;
                                final int s = n.intValue();
                                displayQuestion(s, playerToStart, name);
                            }
                        }





                        Long data = (Long) dataSnapshot.child(PLAYER1_FB_REF).child(SCORE_FB_REF).getValue(); // TODO MAOR HERE U CAN GET THE SCORE OF PLAYER 1
                        Log.d("hereplayer1",SCORE_FB_REF+data);
                        int score = data.intValue();

                        player1TextView.setText(player1.getName() + score);

                        Long data1 = (Long) dataSnapshot.child(PLAYER2_FB_REF).child(SCORE_FB_REF).getValue(); // TODO MAOR HERE U CAN GET THE SCORE OF PLAYER 2
                        int score1 = data1.intValue();
                        Log.d("hereplayer2","score"+data1);

                        player2TextView.setText(player2.getName() + score1);

                        forSaveData.add(0,player1.getName());
                        forSaveData.add(1,player2.getName());

                        forSaveDataNumber.add(0,score);
                        forSaveDataNumber.add(1,score1);



                        Long playerClick = (Long)dataSnapshot.child(OPTION_CLICKED_FB_REF).getValue();

                        findOptionByTag(playerClick,playerToStart);






                    }



                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });

            }
        }, 100);
    }

    private void findOptionByTag(Long playerClick, String playerToStart) {
        if(playerClick != -1){

            int size =linearLayout.getChildCount();
            for(int i = 0 ; i < size ; i++){
                int b= Integer.parseInt((String) linearLayout.getChildAt(i).getTag());

                if(b == playerClick){
                    Button button = (Button) linearLayout.getChildAt(i);
                    if(quizMaster.isCorrect(currentQuestionNum,button.getText().toString())){
                        setAnimationToButtons(button, correctColor);
                        playSound(CORRECT_STR_SOUND);
                    }
                    else{
                        setAnimationToButtons(button, wrongColor);
                        playSound(WRONG_STR_SOUND);
                    }

                }

            }
            timer.cancel();
            animation.cancel();
            gameRoomRef.child(OPTION_CLICKED_FB_REF).setValue(-1);
            changeTurns(playerToStart);


            isqQuestionChanged = false;
            delayTillNextQuestion();



//            delayTillNextQuestion();
        }
    }

    private void changeTurns(String playerToStart) {

        if(playerToStart.equals(PLAYER1_FB_REF)) {

            gameRoomRef.child(WHO_PLAY_FB_REF).setValue(PLAYER2_FB_REF);
        }
        else {

            gameRoomRef.child(WHO_PLAY_FB_REF).setValue(PLAYER1_FB_REF);
        }

    }

    private void setPlayerTurn(String playerToStart) {
        if(playerToStart != null) {
            if (playerToStart.equals(PLAYER1_FB_REF)) {
                player1.setTurn(true);

            } else {
                player2.setTurn(true);
            }
        }
    }

    @Override
    protected void onResume() {

        super.onResume();


    }

    private void setListenersToNull() {
        choice1.setOnClickListener(null);
        choice2.setOnClickListener(null);
        choice3.setOnClickListener(null);
        choice4.setOnClickListener(null);
    }

    private void setButtonsListener() {
        int size = linearLayout.getChildCount();
        for (int i = 0 ; i < size ; i++) {
            Button btn =(Button) linearLayout.getChildAt(i);
            btn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    disableAllButtonsThatNotClicked();
                    timer.cancel();
                    animation.cancel();
                    if (quizMaster.isCorrect(currentQuestionNum, choice1.getText().toString())){
                        if(quizMaster.isUserTurn()) {
                            player1Score += quizMaster.calculateScore(timeLeft);
                            player1TextView.setText("Score: " + player1Score);
                        }
                        else{
                            player2Score += quizMaster.calculateScore(timeLeft);
                            player2TextView.setText("Score: "+ player2Score);
                        }
                        setAnimationToButtons(v, correctColor);
                        playSound(CORRECT_STR_SOUND);
                    }
                    else{
                        setAnimationToButtons(v, wrongColor);
                        playSound(WRONG_STR_SOUND);
                    }
                    delayTillNextQuestion();
                }
            });

        }




    }

    private void whoPlayFirst() {
        //if n = 0 -> player1 starts , if n = 1 player2 play first
        r = new Random();
        int n = r.nextInt(2);
        if(currentFirebaseUser.getUid().equals(player1.getId())) {
            waitingRoomRef.removeValue();
            if (n == 0) {
                gameRoomRef.child(WHO_PLAY_FB_REF).setValue(PLAYER1_FB_REF);
            } else {
                gameRoomRef.child(WHO_PLAY_FB_REF).setValue(PLAYER2_FB_REF);
            }
        }
    }

    private void choice1Listener() {
        choice1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                disableAllButtonsThatNotClicked();
                v.setBackground(notAnsweredColor);
                int answerClicked = Integer.parseInt((String)v.getTag());//TODO Send player click to firebase
                sendUserClickToDataBase(answerClicked);
                calculateScoreAndAddToFireBase((Button)v);



            }
        });
    }

    private void calculateScoreAndAddToFireBase(Button v) {

        if(quizMaster.isCorrect(currentQuestionNum,v.getText().toString())) {
            if (currentFirebaseUser.getUid().equals(player1.getId())) {

                player1Score += quizMaster.calculateScore(timeLeft);
                gameRoomRef.child(PLAYER1_FB_REF).child("score").setValue(player1Score);

            }
            if (currentFirebaseUser.getUid().equals(player2.getId())) {
                player2Score += quizMaster.calculateScore(timeLeft);
                gameRoomRef.child("player2").child(SCORE_FB_REF).setValue(player2Score);

            }
        }
    }

    private void choice2Listener() {
        choice2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                disableAllButtonsThatNotClicked();
//                setTimeOnFireBase();
                v.setBackground(notAnsweredColor);
                //  timer.cancel();
                //  animation.cancel();
                int answerClicked = Integer.parseInt((String)v.getTag());//TODO Send player click to firebase
                sendUserClickToDataBase(answerClicked);
                calculateScoreAndAddToFireBase((Button)v);

            }
        });
    }
    private void choice3Listener() {
        choice3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                disableAllButtonsThatNotClicked();
                v.setBackground(notAnsweredColor);
                int answerClicked = Integer.parseInt((String)v.getTag());//TODO Send player click to firebase
                sendUserClickToDataBase(answerClicked);
                calculateScoreAndAddToFireBase((Button)v);



            }
        });
    }
    private void choice4Listener() {
        choice4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                disableAllButtonsThatNotClicked();
                v.setBackground(notAnsweredColor);
                int answerClicked = Integer.parseInt((String)v.getTag()); //TODO Send player click to firebase
                sendUserClickToDataBase(answerClicked);
                calculateScoreAndAddToFireBase((Button)v);





            }
        });
    }

    private void setTimeOnFireBase() {
        gameRoomRef.child("isTimeStopped").setValue(true);
    }


    private void sendUserClickToDataBase(Object userClick){

        if(userClick instanceof Integer) {
            int optionSelected = (int)userClick;
            gameRoomRef.child(OPTION_CLICKED_FB_REF).setValue(optionSelected);
        }
    }

    private void disableAllButtonsThatNotClicked() {
        LinearLayout linearLayout = findViewById(R.id.question_container);
        int size = linearLayout.getChildCount();
        for( int i = 0 ; i < size; i++) {
            View view = linearLayout.getChildAt(i);
            view.setEnabled(false);
            view.setBackgroundResource(R.drawable.mydisablecolor);


        }
    }

    private void delayTillNextQuestion() {
        new Timer().schedule(new TimerTask() {
            @Override
            public void run() {
                if(!isqQuestionChanged) {
                    isqQuestionChanged = true;
                    if (currentFirebaseUser.getUid().equals(player1.getId())) {
                        final int n = r.nextInt(questionsRepo.size());
                        gameRoomRef.child(NUM_OF_QUESTION_TO_DISPLAY_FB_REF).setValue(n);
                    }
                }
            }
        }, 2500);
    }

    private void setAnimationToButtons(View v, Drawable color) {
        final Drawable colorToPaint = color;
        if(v instanceof Button){
            final Button btn = (Button)v;
            v.animate().scaleY(2).scaleX(2).setDuration(200).withEndAction(new Runnable() {
                @Override
                public void run() {
                    btn.animate().scaleY(1).scaleX(1).setDuration(200).start();

                    btn.setBackground(colorToPaint);
                }
            }).start();
        }
    }

    private void playSound(String str) {
        try {
            afd = getAssets().openFd(str);
            MediaPlayer player = new MediaPlayer();
            player.setDataSource(afd.getFileDescriptor(),afd.getStartOffset(),afd.getLength());
            player.prepare();
            player.start();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void displayQuestion(int i, String playerToStart, String name) {
        quizMaster.incrementQuestionsAsked();


        if (!quizMaster.isGameOver()) {
            initTimer();
            animateProgressBar();

            timer.start();
            Question question = questionsRepo.get(i); // choose a random question from the repo
            currentQuestionNum = i;
            setColorToNotAnsweredAndEnable();
            //True false question
            if (question instanceof TrueFalse && !(question.isAsked())) {
                initTFQuestion();
            }
            //Multiple choice question
            else if (question instanceof MultipleChoice && !(question.isAsked())) {
                isArrayListChanged = false;
                initMCQuestion(((MultipleChoice) question).getmChoices());
            }


            TextView questionTextView = findViewById(R.id.question);
            initilaizeTextView(questionTextView, question.getQuestion());

            TextView whoPlayTextView = findViewById(R.id.who_play);
            initilaizeTextView(whoPlayTextView, name + " Play's");

            setListenersByTurn(playerToStart);
        }
        else {
            updateForStatsEveryPlayer();
            updateForLeaderBoard();
            intentActivityResult();

        }


    }



    private void setListenersByTurn(String playerToStart) {
        if(playerToStart != null) {
            if (playerToStart.equals(PLAYER1_FB_REF)) {
                if (currentFirebaseUser.getUid().equals(player2.getId())) {
                    setListenersToNull();
                } else {
                    setListeners();
                }
            } else {
                if (currentFirebaseUser.getUid().equals(player1.getId())) {
                    setListenersToNull();
                } else {
                    setListeners();
                }
            }
        }
    }


    //TODO HERE WILL BE LISTENER FOR THE OPPENENT

    //TODO choose randomly an answer
//            computerTurn(i);


    private void computerTurn(int i) {

        int index ;
        Question currentQuestion  = questionsRepo.get(i);
        if (currentQuestion instanceof TrueFalse && !(currentQuestion.isAsked())) {
            index = r.nextInt(2);
            TrueFalse currentQue = (TrueFalse)currentQuestion;
            if(index == 1){//TRUE
                Button btn = findButtonWithSameString("True");
                if(btn !=  null ) {
                    if (((TrueFalse) currentQuestion).getAnswer().equals("True")) {
                        setAnimationToButtons(btn, correctColor);
                    } else {
                        setAnimationToButtons(btn, wrongColor);
                    }
                }
            }
            else{
                Button btn = findButtonWithSameString("False");
                if( btn != null) {
                    if (((TrueFalse) currentQuestion).getAnswer().equals("False")) {
                        setAnimationToButtons(btn, wrongColor);
                    } else {
                        setAnimationToButtons(btn, correctColor);
                    }
                }
            }

        }
        //Multiple choice question
        else if (currentQuestion instanceof MultipleChoice && !(currentQuestion.isAsked())) {
            index = r.nextInt(4);
            MultipleChoice currentQue = (MultipleChoice)currentQuestion;
            String randomChoice = currentQue.getmChoices().get(index);
            Button btn = findButtonWithSameString(randomChoice);
            if (quizMaster.isCorrect(i, randomChoice)) {
                setAnimationToButtons(btn,correctColor);
            }
            else{
                setAnimationToButtons(btn,wrongColor);
            }
        }

    }

    private Button findButtonWithSameString(String randomChoice) {

        LinearLayout linearLayout = findViewById(R.id.question_container);
        int size =  linearLayout.getChildCount();
        for (int i = 0; i < size; i++) {
            Button btn = (Button)linearLayout.getChildAt(i);
            String str = (String) btn.getText();
            if(str.equals(randomChoice)){
                return (Button)linearLayout.getChildAt(i);
            }
        }
        return null;
    }

    private void setListeners() {
        choice1Listener();
        choice2Listener();
        choice3Listener();
        choice4Listener();
    }

    private void setColorToNotAnsweredAndEnable() {
        LinearLayout linearLayout = findViewById(R.id.question_container);
        int size = linearLayout.getChildCount();
        for( int i = 0 ; i < size; i++){
            View v = linearLayout.getChildAt(i);
            if(v instanceof Button){
                Button btn = (Button)v;
                btn.setEnabled(true);
                btn.setBackground(notAnsweredColor);
            }
        }
    }

    private void initTimer() {
        timer = new CountDownTimer(20000,1000){

            @Override
            public void onTick(long millisUntilFinished) {
                timeLeft = millisUntilFinished / 1000;
            }

            @Override
            public void onFinish() {
                changeTurns(playerToStart);
                isqQuestionChanged = false;
                delayTillNextQuestion();
            }
        };
    }

    private void animateProgressBar() {
        animation = ObjectAnimator.ofInt(mProgressBar, "progress", 0, 100);
        animation.setDuration(20000);
        animation.setInterpolator(new DecelerateInterpolator());
        animation.addListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animator) { }

            @Override
            public void onAnimationEnd(Animator animator) {

            }

            @Override
            public void onAnimationCancel(Animator animator) { }

            @Override
            public void onAnimationRepeat(Animator animator) { }
        });
        animation.start();
    }

    private void initMCQuestion(ArrayList<String> options) {
        setMcOptions();

        String[] arr = {null, null, null, null};
        if(currentFirebaseUser.getUid().equals(player1.getId())) {

            setMcOptions();


            r = new Random();
            int i = 0;

            int[] arrTocheck = {0, 0, 0, 0};


            //Random place for the answers
            do {
                int index = r.nextInt(options.size());
                if (arrTocheck[index] == 0) {
                    arrTocheck[index] = 1;
                    String choice = options.get(index);
                    arr[i] = choice;
                    i++;
                }
            } while (!isAllPlaced(arrTocheck));

            ArrayList<String> arrayList = setArrayToList(arr);
            gameRoomRef.child("options").setValue(arrayList);
        }

    }

    private ArrayList<String> setArrayToList(String[] arr) {
        ArrayList<String> arrayList = new ArrayList<>();
        for(int i = 0 ; i < arr.length ; i++){
            arrayList.add(arr[i]);
        }

        return arrayList;
    }

    private boolean isAllPlaced(int[] arr) {
        for(int i = 0 ; i < arr.length ; i++){
            if(arr[i] == 0)
                return false;
        }
        return true;
    }

    private void setTextToOptionsButtons(ArrayList<String> arr) {
        choice1.setText(arr.get(0));
        choice2.setText(arr.get(1));
        choice3.setText(arr.get(2));
        choice4.setText(arr.get(3));
    }

    private void setMcOptions() {
        choice1.setVisibility(View.VISIBLE);
        choice2.setVisibility(View.VISIBLE);
        choice3.setVisibility(View.VISIBLE);
        choice4.setVisibility(View.VISIBLE);
    }



    private void initTFQuestion() {
        r = new Random();
        int randomPosition = r.nextInt(2);
        setTFOptions();

        if(randomPosition == 0){
            choice2.setText("True");
            choice3.setText("False");
        }
        else{
            choice3.setText("False");
            choice2.setText("True");
        }





    }

    private void setTFOptions() {
        choice1.setVisibility(View.INVISIBLE);
        choice2.setVisibility(View.VISIBLE);
        choice3.setVisibility(View.VISIBLE);
        choice4.setVisibility(View.INVISIBLE);
    }

    private void initilaizeTextView(TextView textView, String str) {

        textView.setText(str);
//        Typeface custom_font = Typeface.createFromAsset(getAssets(), "fonts/Sansaul_Petronika.ttf");
        //       textView.setTypeface(custom_font);
        textView.setTextColor(Color.BLACK);
    }

    private void gameOver() {
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(CompeteActivity.this);
        alertDialogBuilder
                .setMessage("Game Over ! your score is "+ player1Score + " points")
                .setCancelable(false)
                .setPositiveButton("New Game",
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int i) {
                                startActivity(new Intent(getApplicationContext(),CompeteActivity.class));
                            }
                        })
                .setNegativeButton("Exit",
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int i) {
                                finish();
                            }
                        });

        AlertDialog alertDialog = alertDialogBuilder.create();
        alertDialog.show();
    }

    private void updateForStatsEveryPlayer() {

        myRef = FirebaseDatabase.getInstance().getReference("stats");
        mAuth = FirebaseAuth.getInstance();

        String emailWithOutPoint = mAuth.getCurrentUser().getEmail().toString();
        Log.d("Email", "check" + emailWithOutPoint);
        emailWithOutPoint = emailWithOutPoint.replace(".", "");


        myRef.child(emailWithOutPoint).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {


                if (mAuth.getCurrentUser().getDisplayName().equals(forSaveData.get(0))) {
                    Object highScoreFromDataBase0 = dataSnapshot.child("High_Score").getValue();
                    if(highScoreFromDataBase0 != null) {

                        int highScoreFromDataBaseNumber0 = Integer.parseInt(highScoreFromDataBase0.toString());
                        if (highScoreFromDataBaseNumber0 < forSaveDataNumber.get(0)) // highScoreFromDataBaseNumber < theScoreInTheGame(the value for this game)
                        {
                            ///change the score to string
                            dataSnapshot.getRef().child("High_Score").setValue(forSaveDataNumber.get(0));
                        }
                    }
                }

                if (mAuth.getCurrentUser().getDisplayName().equals(forSaveData.get(1))) {
                    String highScoreFromDataBase1 = String.valueOf(dataSnapshot.child("High_Score").getValue());
                    if(highScoreFromDataBase1 != null) {
                        int highScoreFromDataBaseNumber1 = Integer.parseInt(highScoreFromDataBase1);
                        if (highScoreFromDataBaseNumber1 < forSaveDataNumber.get(1)) // highScoreFromDataBaseNumber < theScoreInTheGame(the value for this game)
                        {
                            ///change the score to string
                            dataSnapshot.getRef().child("High_Score").setValue(forSaveDataNumber.get(1));
                        }
                    }
                }


                if (forSaveDataNumber.get(0) > forSaveDataNumber.get(1)) {
                    if (mAuth.getCurrentUser().getDisplayName().equals(forSaveData.get(0))) {
                        String winOndatabase = String.valueOf(dataSnapshot.child("Win").getValue());
                        int winBuilder = Integer.parseInt(winOndatabase);
                        winBuilder++;
                        String strI = Integer.toString(winBuilder);
                        dataSnapshot.getRef().child("Win").setValue(strI);
                    }
                }

                if (forSaveDataNumber.get(0) < forSaveDataNumber.get(1)) {
                    if (mAuth.getCurrentUser().getDisplayName().equals(forSaveData.get(1))) {
                        String winOndatabase = String.valueOf(dataSnapshot.child("Win").getValue());
                        int winBuilder = Integer.parseInt(winOndatabase);
                        winBuilder++;
                        String strI = Integer.toString(winBuilder);
                        dataSnapshot.getRef().child("Win").setValue(strI);
                    }
                }

                if (forSaveDataNumber.get(0) < forSaveDataNumber.get(1)) {
                    if (mAuth.getCurrentUser().getDisplayName().equals(forSaveData.get(0))) {
                        String loseOndatabase = String.valueOf(dataSnapshot.child("Lose").getValue());
                        int loseBuilder = Integer.parseInt(loseOndatabase);
                        loseBuilder++;
                        String strII = Integer.toString(loseBuilder);
                        dataSnapshot.getRef().child("Lose").setValue(strII);
                    }
                }

                if (forSaveDataNumber.get(0) > forSaveDataNumber.get(1)) {
                    if (mAuth.getCurrentUser().getDisplayName().equals(forSaveData.get(1))) {
                        String loseOndatabase = String.valueOf(dataSnapshot.child("Lose").getValue());
                        int loseBuilder = Integer.parseInt(loseOndatabase);
                        loseBuilder++;
                        String strII = Integer.toString(loseBuilder);
                        dataSnapshot.getRef().child("Lose").setValue(strII);
                    }
                }


                if (mAuth.getCurrentUser().getDisplayName().equals(forSaveData.get(0))) {
                    String myString0 = String.valueOf(dataSnapshot.child("Total_games_played").getValue());
                    int num0 = Integer.parseInt(myString0);
                    num0++;
                    String strIII0 = Integer.toString(num0);
                    dataSnapshot.getRef().child("Total_games_played").setValue(strIII0);
                }
                if (mAuth.getCurrentUser().getDisplayName().equals(forSaveData.get(1))) {
                    String myString1 = String.valueOf(dataSnapshot.child("Total_games_played").getValue());
                    int num1 = Integer.parseInt(myString1);
                    num1++;
                    String strIII1 = Integer.toString(num1);
                    dataSnapshot.getRef().child("Total_games_played").setValue(strIII1);
                }
            }


            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });


    }

    private void updateForLeaderBoard() {

        myRef = FirebaseDatabase.getInstance().getReference("LeaderBoard");
        mAuth = FirebaseAuth.getInstance();


        if(mAuth.getCurrentUser().getDisplayName().equals(forSaveData.get(0))){
              if(forSaveDataNumber.get(0) > forSaveDataNumber.get(1)) {
            Map<String, String> addToLeaderBoard = new HashMap<String, String>();
            addToLeaderBoard.put("name", forSaveData.get(0)); // who is when put in whoWin
            String casting = Integer.toString(forSaveDataNumber.get(0));
            addToLeaderBoard.put(SCORE_FB_REF, casting); // score who win in 1
            myRef.push().setValue(addToLeaderBoard);
             }

        }
        if(mAuth.getCurrentUser().getDisplayName().equals(forSaveData.get(1))){

            if(forSaveDataNumber.get(0) < forSaveDataNumber.get(1)) {
                Map<String, String> addToLeaderBoard = new HashMap<String, String>();
                addToLeaderBoard.put("name", forSaveData.get(1)); // who is when put in whoWin
                String casting = Integer.toString(forSaveDataNumber.get(1));
                addToLeaderBoard.put(SCORE_FB_REF, casting); // score who win in 1
                myRef.push().setValue(addToLeaderBoard);
            }
        }



    }

    private void intentActivityResult() {
        if (mAuth.getCurrentUser().getDisplayName().equals(forSaveData.get(0))) {
            if (forSaveDataNumber.get(0) > forSaveDataNumber.get(1)) {
                Intent intent = new Intent(CompeteActivity.this, resultActivity.class);

                intent.putExtra("status", "You Win");

                        String Builder = Integer.toString(forSaveDataNumber.get(0));
                intent.putExtra(SCORE_FB_REF, Builder);
                startActivity(intent);
            }
            else
            {
                Intent intent = new Intent(CompeteActivity.this, resultActivity.class);
                String Builder = Integer.toString(forSaveDataNumber.get(0));

                intent.putExtra("status", "You Lose");
                intent.putExtra(SCORE_FB_REF, Builder);
                startActivity(intent);
            }
        }

        if (mAuth.getCurrentUser().getDisplayName().equals(forSaveData.get(1))) {
            if (forSaveDataNumber.get(0) < forSaveDataNumber.get(1)) {
                Intent intent = new Intent(CompeteActivity.this, resultActivity.class);
                String Builder = Integer.toString(forSaveDataNumber.get(1));

                intent.putExtra("status", "You Win");
                intent.putExtra(SCORE_FB_REF, Builder);
                startActivity(intent);
            }
            else
            {
                Intent intent = new Intent(CompeteActivity.this, resultActivity.class);
                String Builder = Integer.toString(forSaveDataNumber.get(1));

                intent.putExtra("status", "You Lose");
                intent.putExtra(SCORE_FB_REF, Builder);
                startActivity(intent);
            }
        }
    }



}

