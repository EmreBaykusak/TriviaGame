package com.company.triviagame;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class Trivia extends AppCompatActivity {

    TextView time, correct, wrong;
    TextView question, a, b, c, d;
    Button finish, next;

    String triviaQuestion;
    String triviaAnswerA;
    String triviaAnswerB;
    String triviaAnswerC;
    String triviaAnswerD;
    String triviaCorrectAnswer;

    int questionCount;
    int questionNumber = 1;

    int userCorrect = 0;
    int userWrong = 0;
    String userAnswer;

    CountDownTimer countDownTimer;
    private static final long TOTAL_TIME = 25000;
    Boolean timerContinue;
    long timeLeft = TOTAL_TIME;

    FirebaseDatabase database;

    DatabaseReference questions;
    DatabaseReference scores;

    FirebaseAuth auth = FirebaseAuth.getInstance();
    FirebaseUser user = auth.getCurrentUser();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_trivia);

        database = FirebaseDatabase.getInstance(getApplicationContext().getResources().getString(R.string.firebase_database_url));

        questions = database.getReference().child("Questions");
        scores = database.getReference().child("Scores");

        time = findViewById(R.id.textViewTimeValue);
        correct = findViewById(R.id.textViewCorrectAnswerValue);
        wrong = findViewById(R.id.textViewWrongAnswerValue);

        question = findViewById(R.id.textViewQuestion);
        a = findViewById(R.id.textViewAnswer1);
        b = findViewById(R.id.textViewAnswer2);
        c = findViewById(R.id.textViewAnswer3);
        d = findViewById(R.id.textViewAnswer4);

        finish = findViewById(R.id.buttonFinish);
        next = findViewById(R.id.buttonNext);

        game();

        a.setOnClickListener(v-> {
            setClickableForAnswers(false);
            pauseTimer();
            userAnswer = "a";
            if (userAnswer.equals(triviaCorrectAnswer)){
                a.setBackgroundColor(Color.GREEN);
                userCorrect++;
                correct.setText("" + userCorrect);
            }
            else{
                a.setBackgroundColor(Color.RED);
                userWrong++;
                wrong.setText("" + userWrong);
                findAnswer();
            }
        });

        b.setOnClickListener(v-> {
            setClickableForAnswers(false);
            pauseTimer();
            userAnswer = "b";
            if (userAnswer.equals(triviaCorrectAnswer)){
                b.setBackgroundColor(Color.GREEN);
                userCorrect++;
                correct.setText("" + userCorrect);
            }
            else{
                b.setBackgroundColor(Color.RED);
                userWrong++;
                wrong.setText("" + userWrong);
                findAnswer();
            }
        });

        c.setOnClickListener(v-> {
            setClickableForAnswers(false);
            pauseTimer();
            userAnswer = "c";
            if (userAnswer.equals(triviaCorrectAnswer)){
                c.setBackgroundColor(Color.GREEN);
                userCorrect++;
                correct.setText("" + userCorrect);
            }
            else{
                c.setBackgroundColor(Color.RED);
                userWrong++;
                wrong.setText("" + userWrong);
                findAnswer();
            }
        });

        d.setOnClickListener(v-> {
            setClickableForAnswers(false);
            pauseTimer();
            userAnswer = "d";
            if (userAnswer.equals(triviaCorrectAnswer)){
                d.setBackgroundColor(Color.GREEN);
                userCorrect++;
                correct.setText("" + userCorrect);
            }
            else{
                d.setBackgroundColor(Color.RED);
                userWrong++;
                wrong.setText("" + userWrong);
                findAnswer();
            }
        });

        finish.setOnClickListener(v-> {
            sendScores();
            Intent i = new Intent(Trivia.this, com.company.triviagame.Score.class);
            startActivity(i);
            finish();
        });

        next.setOnClickListener(v-> {
            resetTimer();
            game();
        });
    }

    private void sendScores()
    {
        String userUid = user.getUid();
        scores.child(userUid).child("correct").setValue(userCorrect)
                .addOnSuccessListener(this, new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void unused) {
                        Toast.makeText(Trivia.this, "Scores sent successfully", Toast.LENGTH_LONG).show();
                    }
                });
        scores.child(userUid).child("wrong").setValue(userWrong);

    }

    private void game()
    {
        setClickableForAnswers(true);
        startTimer();
        setBackgroundColorForAnswers(Color.WHITE);
        // Read from the database
        questions.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                // This method is called once with the initial value and again
                // whenever data at this location is updated.

                questionCount = (int) dataSnapshot.getChildrenCount();

                triviaQuestion = dataSnapshot.child(String.valueOf(questionNumber)).child("q").getValue().toString();
                triviaAnswerA = dataSnapshot.child(String.valueOf(questionNumber)).child("a").getValue().toString();
                triviaAnswerB = dataSnapshot.child(String.valueOf(questionNumber)).child("b").getValue().toString();
                triviaAnswerC = dataSnapshot.child(String.valueOf(questionNumber)).child("c").getValue().toString();
                triviaAnswerD = dataSnapshot.child(String.valueOf(questionNumber)).child("d").getValue().toString();
                triviaCorrectAnswer = dataSnapshot.child(String.valueOf(questionNumber)).child("answer").getValue().toString();

                question.setText(triviaQuestion);
                a.setText(triviaAnswerA);
                b.setText(triviaAnswerB);
                c.setText(triviaAnswerC);
                d.setText(triviaAnswerD);

                if(questionNumber < questionCount){
                    questionNumber++;
                }
                else{
                    Toast.makeText(Trivia.this, "You answered all questions", Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void onCancelled(DatabaseError error) {
                // Failed to read value
                Toast.makeText(Trivia.this, error.getMessage(), Toast.LENGTH_LONG).show();
            }
        });
    }

    private void findAnswer()
    {
        if (triviaCorrectAnswer.equals("a")){
            a.setBackgroundColor(Color.GREEN);
        }
        else if (triviaCorrectAnswer.equals("b")){
            b.setBackgroundColor(Color.GREEN);
        }
        else if (triviaCorrectAnswer.equals("c")){
            c.setBackgroundColor(Color.GREEN);
        }
        else if (triviaCorrectAnswer.equals("d")){
            d.setBackgroundColor(Color.GREEN);
        }
    }

    private void startTimer()
    {
        countDownTimer = new CountDownTimer(timeLeft,1000) {
            @Override
            public void onTick(long millisUntilFinished) {
                timeLeft = millisUntilFinished;
                updateCountDownText();
            }

            @Override
            public void onFinish() {
                timerContinue = false;
                pauseTimer();
                question.setText("Sorry, time is up");
                setClickableForAnswers(false);
                userWrong++;
                wrong.setText("" + userWrong);
            }
        }.start();

        timerContinue = true;
        setClickableForAnswers(true);
    }

    private void pauseTimer()
    {
        countDownTimer.cancel();
        timerContinue = false;
    }

    private void resetTimer()
    {
        timeLeft = TOTAL_TIME;
        updateCountDownText();
    }

    private void updateCountDownText()
    {
        int second = (int) ((timeLeft / 1000) % 60);
        time.setText("" + second);
    }

    private void setClickableForAnswers(boolean clickable)
    {
        a.setClickable(clickable);
        b.setClickable(clickable);
        c.setClickable(clickable);
        d.setClickable(clickable);
    }

    private void setBackgroundColorForAnswers(int color) {
        a.setBackgroundColor(color);
        b.setBackgroundColor(color);
        c.setBackgroundColor(color);
        d.setBackgroundColor(color);
    }
}