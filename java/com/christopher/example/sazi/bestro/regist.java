package com.christopher.example.sazi.bestro;

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

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

public class regist extends AppCompatActivity {

    private FirebaseAnalytics mFirebaseAnalytics;
    private EditText editTextUsername, editTextEmail, editTextPassword;
    private Button signup;
    private FirebaseAuth mAuth;
    public String username;
    public String email,password;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_regist);

        editTextUsername = (findViewById(R.id.username));
        editTextEmail = (findViewById(R.id.email));
        editTextPassword = (findViewById(R.id.password));

        mFirebaseAnalytics = FirebaseAnalytics.getInstance(this);
        mAuth = FirebaseAuth.getInstance();

    }


    public void registration(View view){
        signup(username.trim(),email.trim(),password.trim());

    }

    public void signup(final String username, final String email, String password){
        //String username = editTextUsername.getText().toString();
        //String email = editTextEmail.getText().toString();
        //String  password = editTextPassword.getText().toString();
        mAuth.createUserWithEmailAndPassword(email,password)
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            User user = new User(username,email);
                            FirebaseDatabase.getInstance().getReference("Users")
                                    .child(FirebaseAuth.getInstance().getCurrentUser().getUid())
                                    .setValue(user).addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    if (task.isSuccessful()) {
                                        Log.d("tag","well done account created");
                                        Toast.makeText(getApplicationContext(),"Account created", Toast.LENGTH_LONG).show();
                                        startActivity(new Intent(getApplicationContext(),login.class));
                                        finish();

                                    }else {
                                        Log.d("tag","failed to register");
                                        Toast.makeText(getApplicationContext(),"Sorry failed to register", Toast.LENGTH_LONG).show();
                                    }
                                }
                            });
                        } else {
                            Log.d("tag","failed to register");
                            Toast.makeText(getApplicationContext(),"ohps registration failed", Toast.LENGTH_LONG).show();
                        }
                    }
                });

    }
}
