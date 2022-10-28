package com.company.triviagame;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class SignUp extends AppCompatActivity {

    EditText email, password;
    Button signUp;

    FirebaseAuth auth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        email = findViewById(R.id.editTextSignUpEmail);
        password = findViewById(R.id.editTextSignUpPassword);
        signUp = findViewById(R.id.buttonSignUp);

        auth = FirebaseAuth.getInstance();

        signUp.setOnClickListener(v-> {
            String userEmail = email.getText().toString();
            String userPassword = password.getText().toString();
            firebaseSignUp(userEmail,userPassword);
        });
    }

    private void firebaseSignUp(String userEmail, String userPassword)
    {
        signUp.setClickable(false);
        auth.createUserWithEmailAndPassword(userEmail,userPassword)
                .addOnCompleteListener(this,new OnCompleteListener<AuthResult>()
                {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task)
                    {
                        if (task.isSuccessful()) {
                            Toast.makeText(SignUp.this, "Sign up successful", Toast.LENGTH_LONG).show();
                            finish();
                        }else {
                            Toast.makeText(SignUp.this, "Problem occurred when sign up", Toast.LENGTH_LONG).show();
                        }
                    }
                });
    }
}