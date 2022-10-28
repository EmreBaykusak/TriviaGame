package com.company.triviagame;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class Score extends AppCompatActivity {

    TextView correct, wrong;
    Button playAgain, exit;

    String userCorrect, userWrong;

    FirebaseDatabase database;
    DatabaseReference scores;

    FirebaseAuth auth = FirebaseAuth.getInstance();
    FirebaseUser user = auth.getCurrentUser();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_score);

        database = FirebaseDatabase.getInstance(getApplicationContext().getResources().getString(R.string.firebase_database_url));
        scores = database.getReference().child("Scores");

        correct = findViewById(R.id.textViewScoreCorrectValue);
        wrong = findViewById(R.id.textViewScoreWrongValue);
        playAgain = findViewById(R.id.buttonPlayAgain);
        exit = findViewById(R.id.buttonExit);

        playAgain.setOnClickListener(v-> {
            Intent i = new Intent(Score.this, Trivia.class);
            startActivity(i);
            finish();
        });

        exit.setOnClickListener(v-> {
            finish();
        });

        scores.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                String userUid = user.getUid();
                userCorrect = snapshot.child(userUid).child("correct").getValue().toString();
                userWrong = snapshot.child(userUid).child("wrong").getValue().toString();

                correct.setText(userCorrect);
                wrong.setText(userWrong);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }
}