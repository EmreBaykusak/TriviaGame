package com.company.triviagame;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.ActionCodeSettings;
import com.google.firebase.auth.FirebaseAuth;

public class ForgotPassword extends AppCompatActivity {

    EditText email;
    Button reset;

    FirebaseAuth auth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forgot_password);

        email = findViewById(R.id.editTextForgotEmail);
        reset = findViewById(R.id.buttonReset);

        auth = FirebaseAuth.getInstance();

        reset.setOnClickListener(v->{
            String userEmail = email.getText().toString();
            firebaseResetPassword(userEmail);
        });
    }

    private void firebaseResetPassword(String userEmail)
    {
        reset.setClickable(false);
        auth.sendPasswordResetEmail(userEmail)
                .addOnCompleteListener(this, new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()){
                            Toast.makeText(ForgotPassword.this, "We sent you an email to reset your password!", Toast.LENGTH_LONG).show();
                            finish();
                        }
                        else{
                            Toast.makeText(ForgotPassword.this, "Please enter a valid email", Toast.LENGTH_LONG).show();
                        }
                    }
                });
    }
}