package com.christopher.example.sazi.bestro;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity ;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.analytics.FirebaseAnalytics;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.FirebaseDatabase;

public class login extends AppCompatActivity  {

    private FirebaseAnalytics mFirebaseAnalytics;
    private EditText editTextUsername, editTextEmail, editTextPassword;
    private Button signup;
    private FirebaseAuth mAuth;
    public String username,email,password;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        editTextUsername = (findViewById(R.id.username));
        editTextEmail = (findViewById(R.id.email));
        editTextPassword = (findViewById(R.id.password));

        mFirebaseAnalytics = FirebaseAnalytics.getInstance(this);
        mAuth = FirebaseAuth.getInstance();
    }


    @Override
    protected void onStart() {
        super.onStart();
        loadMain();
    }

    private void loadMain() {

        if (mAuth.getCurrentUser() !=null){
            startActivity (new Intent(getApplicationContext(),Main2Activity.class));
            finish();
        }else {
            startActivity(new Intent(getApplicationContext(), regist.class));
            finish();
        }
    }



    public void login_event(View v) {
        logintoFirebase(email, password);
    }


    private void logintoFirebase(String email, String password) {
        mAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            Log.d("TAG","Sign in with email and password succeed");
                            loadMain();
                        } else {
                            Log.w("TAG", "Sign in with email and password failure", task.getException());
                            startActivity(new Intent(getApplicationContext(),signup.getClass()));
                        }
                    }
                });

    }




    public void registration(View view){
        signup(username.trim(),email.trim(),password.trim());

    }

    public void signup(final String username,final String email, String password) {

        mAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            User user = new User(username, email);
                            FirebaseDatabase.getInstance().getReference("Users")
                                    .child(FirebaseAuth.getInstance().getCurrentUser().getUid())
                                    .setValue(user).addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    if (task.isSuccessful()) {
                                        Log.d("tag", "well done account created");
                                        Toast.makeText(getApplicationContext(), "Account created", Toast.LENGTH_LONG).show();
                                        startActivity(new Intent(getApplicationContext(), login.class));
                                        finish();

                                    } else {
                                        Log.d("tag", "failed to register");
                                        Toast.makeText(getApplicationContext(), "Sorry failed to register", Toast.LENGTH_LONG).show();
                                    }
                                }
                            });
                        } else {
                            Log.d("tag", "failed to register");
                            Toast.makeText(getApplicationContext(), "ohps registration failed", Toast.LENGTH_LONG).show();
                        }
                    }
                });

    }
}



