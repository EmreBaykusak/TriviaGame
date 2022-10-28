package com.company.triviagame;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;

public class Login extends AppCompatActivity {

    EditText email, password;
    Button signIn;
    SignInButton googleSignInButton;
    TextView signUp, forgotPassword;

    FirebaseAuth auth;

    GoogleSignInClient googleSignInClient;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        email = findViewById(R.id.editTextLoginEmail);
        password = findViewById(R.id.editTextLoginPassword);
        signIn = findViewById(R.id.buttonSignIn);
        googleSignInButton = findViewById(R.id.buttonGoogleSignIn);
        signUp = findViewById(R.id.textViewSignUp);
        forgotPassword = findViewById(R.id.textViewForgotPassword);

        auth = FirebaseAuth.getInstance();

        signIn.setOnClickListener(v->{
            String userEmail = email.getText().toString();
            String userPassword = password.getText().toString();
            firebaseSignIn(userEmail,userPassword);
        });

        googleSignInButton.setOnClickListener(v->{
            googleSignIn();
        });

        signUp.setOnClickListener(v->{
            Intent i = new Intent(Login.this,SignUp.class);
            startActivity(i);
        });

        forgotPassword.setOnClickListener(v->{

            Intent i = new Intent(Login.this, ForgotPassword.class);
            startActivity(i);

        });


    }

    private void firebaseSignIn(String userEmail, String userPassword)
    {
        auth.signInWithEmailAndPassword(userEmail,userPassword)
                .addOnCompleteListener(this,new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()){
                            Toast.makeText(Login.this, "Sign in is successful", Toast.LENGTH_LONG).show();
                            Intent i = new Intent(Login.this, MainActivity.class);
                            startActivity(i);
                            finish();
                        }
                        else{
                            Toast.makeText(Login.this, "Problem occurred when try to sign in", Toast.LENGTH_LONG).show();
                        }
                    }
                });
    }

    private void googleSignIn() {
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail().build();

        googleSignInClient = GoogleSignIn.getClient(this,gso);

        signInGoogle();
    }

    ActivityResultLauncher startSignInIntentGoogle = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            new ActivityResultCallback<ActivityResult>() {
                @Override
                public void onActivityResult(ActivityResult result)
                {
                    Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(result.getData());
                    firebaseSignInWithGoogle(task);

                }
            }
    );
    private void signInGoogle()
    {
        Intent signInIntent = googleSignInClient.getSignInIntent();
        startSignInIntentGoogle.launch(signInIntent);
    }

    private void firebaseSignInWithGoogle(Task<GoogleSignInAccount> task)
    {
        try {
            GoogleSignInAccount account = task.getResult(ApiException.class);
            Toast.makeText(this, "Sign in is successful", Toast.LENGTH_LONG).show();
            Intent i = new Intent(Login.this,MainActivity.class);
            startActivity(i);
            finish();
            firebaseGoogleAccount(account);
        } catch (ApiException e) {
            e.printStackTrace();
            Toast.makeText(this, "Sign in is not successful" + e.getMessage(), Toast.LENGTH_LONG).show();

        }
    }

    private void firebaseGoogleAccount(GoogleSignInAccount account)
    {
        AuthCredential authCredential = GoogleAuthProvider.getCredential(account.getIdToken(),null);
        auth.signInWithCredential(authCredential)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful())
                        {
                            FirebaseUser user = auth.getCurrentUser();
                        }
                        else
                        {

                        }
                    }
                });
    }

    @Override
    protected void onStart() {
        super.onStart();

        FirebaseUser currentUser = auth.getCurrentUser();
        if (currentUser != null){
            Intent i = new Intent(Login.this,MainActivity.class);
            startActivity(i);
            finish();
        }
    }
}