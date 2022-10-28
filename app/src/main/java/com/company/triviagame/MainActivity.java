package com.company.triviagame;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;

import com.google.firebase.auth.FirebaseAuth;

public class MainActivity extends AppCompatActivity {

    Button startTrivia, signOut;

    FirebaseAuth auth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        startTrivia = findViewById(R.id.buttonStart);
        signOut = findViewById(R.id.buttonSignOut);

        auth = FirebaseAuth.getInstance();

        startTrivia.setOnClickListener(v->{
            Intent i = new Intent(MainActivity.this, Trivia.class);
            startActivity(i);
        });

        signOut.setOnClickListener(v->{
            auth.signOut();
            Intent i = new Intent(MainActivity.this, com.company.triviagame.Login.class);
            startActivity(i);
            finish();
        });
    }
}