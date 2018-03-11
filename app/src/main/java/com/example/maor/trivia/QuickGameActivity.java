package com.example.maor.trivia;

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
import android.view.Gravity;
import android.view.View;
import android.view.animation.DecelerateInterpolator;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Random;

public class QuickGameActivity extends AppCompatActivity {


    Button choice1, choice2, choice3, choice4;
    TextView score;
    private static final String CORRECT_STR_SOUND= "correct.mp3";
    private static final String WRONG_STR_SOUND= "wrong.mp3";


    private int mScore = 0;
    private Quiz quizMaster;
    ArrayList<Question> questionsRepo;
    int numOfQuestions;
    int currentQuestionNum;
    CountDownTimer timer;
    ObjectAnimator animation;
    Handler handler ;
    AssetFileDescriptor afd;
    Drawable correctColor;
    Drawable wrongColor;
    Drawable notAnsweredColor;


    Random r;
    private ProgressBar mProgressBar;
    private long timeLeft;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_quick_game);


        quizMaster = new Quiz(this);
        questionsRepo = quizMaster.getQuestions();

        handler = new Handler();

        notAnsweredColor = getResources().getDrawable(R.drawable.shape);
        correctColor = getResources().getDrawable(R.drawable.correct_shape);
        wrongColor = getResources().getDrawable(R.drawable.wrong_shape);


        mProgressBar=(ProgressBar)findViewById(R.id.progressbar);



        choice1 = (Button) findViewById(R.id.choice1);
        choice2 = (Button) findViewById(R.id.choice2);
        choice3 = (Button) findViewById(R.id.choice3);
        choice4 = (Button) findViewById(R.id.choice4);

        try {
            quizMaster.readTrueFalseQuestionFromFile();
            quizMaster.readMCQuestionsFromFile();
            Log.e("ArrayList:", quizMaster.toString());
        } catch (IOException e) {
            e.printStackTrace();
        }

        this.numOfQuestions = questionsRepo.size();

        score = (TextView) findViewById(R.id.score);
        initilaizeTextView(score, "Score:");

        Log.d("error","questionsRepo.size"+questionsRepo.size());
        r = new Random();
        int numOfQuestion = r.nextInt(questionsRepo.size())+1; //TODO:check the +1
        displayQuestion(numOfQuestion);


    }

    @Override
    protected void onResume() {
        super.onResume();


        choice1Listener();

        choice2Listener();

        choice3Listener();

        choice4Listener();




    }

    private void choice1Listener() {
        choice1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                disableAllButtonsThatNotClicked(v);
                timer.cancel();
                animation.cancel();
                if (quizMaster.isCorrect(currentQuestionNum, choice1.getText().toString())){
                    mScore += quizMaster.calculateScore(timeLeft);
                    score.setText("Score: "+ mScore);
                    setAnimation(v, correctColor);
                    playSound(CORRECT_STR_SOUND);
                }
                else{
                    setAnimation(v, wrongColor);
                    playSound(WRONG_STR_SOUND);
                }
                delayTillNextQuestion();

            }
        });
    }
    private void choice2Listener() {
        choice2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                disableAllButtonsThatNotClicked(v);
                timer.cancel();
                animation.cancel();
                if (quizMaster.isCorrect(currentQuestionNum, choice2.getText().toString())){
                    mScore += quizMaster.calculateScore(timeLeft);
                    score.setText("Score: "+ mScore);
                    setAnimation(v, correctColor);
                    playSound(CORRECT_STR_SOUND);
                }
              else{
                    setAnimation(v, wrongColor);
                    playSound(WRONG_STR_SOUND);
                }
                delayTillNextQuestion();
            }
        });
    }
    private void choice3Listener() {
        choice3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                disableAllButtonsThatNotClicked(v);
                timer.cancel();
                animation.cancel();
                if (quizMaster.isCorrect(currentQuestionNum, choice3.getText().toString())){
                    mScore += quizMaster.calculateScore(timeLeft);
                    score.setText("Score: "+ mScore);
                    setAnimation(v, correctColor);
                    playSound(CORRECT_STR_SOUND);
                }
                else{
                    setAnimation(v, wrongColor);
                    playSound(WRONG_STR_SOUND);
                }

                delayTillNextQuestion();
            }
        });
    }
    private void choice4Listener() {
        choice4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                disableAllButtonsThatNotClicked(v);
                timer.cancel();
                animation.cancel();
                if (quizMaster.isCorrect(currentQuestionNum, choice4.getText().toString())){
                    mScore += quizMaster.calculateScore(timeLeft);
                    score.setText("Score: "+ mScore);
                    setAnimation(v,correctColor);
                    playSound(CORRECT_STR_SOUND);
                }
                else{
                    playSound(WRONG_STR_SOUND);
                    setAnimation(v,wrongColor);
                }

                delayTillNextQuestion();


            }
        });
    }

    private void disableAllButtonsThatNotClicked(View v) {
        LinearLayout linearLayout = findViewById(R.id.question_container);
        int size = linearLayout.getChildCount();
        for( int i = 0 ; i < size; i++) {
            View view = linearLayout.getChildAt(i);
            if (view instanceof Button) {
                Button btn = (Button) v;
                if (!view.equals(v)) {
                    view.setEnabled(false);
                }
            }
        }
    }

    private void delayTillNextQuestion() {
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                displayQuestion(r.nextInt(numOfQuestions));
            }
        }, 2000);
    }


    private void setAnimation(View v, Drawable color) {
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


    private void displayQuestion(int i) {


        quizMaster.incrementQuestionsAsked();
        if(quizMaster.isGameOver()){
            gameOver();
        }
        else {
            setColorToNotAnsweredAndEnable();
            initTimer();

            animateProgressBar();

            timer.start();


            Question question = questionsRepo.get(i); // choose a random question from the repo
            currentQuestionNum = i;

            TextView questionTextView = findViewById(R.id.question);
            initilaizeTextView(questionTextView, question.getQuestion());


            //True false question
            if (question instanceof TrueFalse && !(question.isAsked())) {
                initTFQuestion();
            }
            //Multiple choice question
            else if (question instanceof MultipleChoice && !(question.isAsked())) {
                initMCQuestion(((MultipleChoice) question).getmChoices());
            }
        }


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

        timer = new CountDownTimer(10000,1000){

            @Override
            public void onTick(long millisUntilFinished) {
                timeLeft = millisUntilFinished / 1000;
            }

            @Override
            public void onFinish() {
                displayQuestion(r.nextInt(numOfQuestions));

            }
        };
    }


    private void animateProgressBar() {
        animation = ObjectAnimator.ofInt(mProgressBar, "progress", 0, 100);
        animation.setDuration(10000);
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


        r = new Random();
        int i = 0;
        String[] arr = {null, null, null, null};
        int []arrTocheck = {0,0,0,0};


        //Random place for the answers
        do {
            int index = r.nextInt(options.size());
            if(arrTocheck[index] == 0){
                arrTocheck[index] = 1;
                String choice = options.get(index);
                arr[i] = choice;
                i++;
            }
        } while (!isAllPlaced(arrTocheck));


        setTextToOptionsButtons(arr);
    }

    private void setTextToOptionsButtons(String[] arr) {
        choice1.setText(arr[0]);
        choice2.setText(arr[1]);
        choice3.setText(arr[2]);
        choice4.setText(arr[3]);
    }

    private void setMcOptions() {
        choice1.setVisibility(View.VISIBLE);
        choice2.setVisibility(View.VISIBLE);
        choice3.setVisibility(View.VISIBLE);
        choice4.setVisibility(View.VISIBLE);
    }


    private boolean isAllPlaced(int[] arr) {
        for(int i = 0 ; i < arr.length ; i++){
            if(arr[i] == 0)
                return false;
        }
        return true;
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
        textView.setGravity(Gravity.CENTER);
    }

    private void gameOver()
    {
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(QuickGameActivity.this);
        alertDialogBuilder
                .setMessage("Game Over ! your score is "+ mScore+ " points")
                .setCancelable(false)
                .setPositiveButton("New Game",
                        new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int i) {
                        startActivity(new Intent(getApplicationContext(),QuickGameActivity.class));
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
}
