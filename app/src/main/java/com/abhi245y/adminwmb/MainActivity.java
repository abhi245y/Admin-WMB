package com.abhi245y.adminwmb;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.coordinatorlayout.widget.CoordinatorLayout;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;


public class MainActivity extends AppCompatActivity {


    private EditText email_id, pass_word;
    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        // Initialize Firebase Auth
        mAuth = FirebaseAuth.getInstance();


        email_id = findViewById(R.id.admin_email);
        pass_word = findViewById(R.id.admin_password);
        Button login = findViewById(R.id.login);

        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                //getting email id and password
                final String email = email_id.getText().toString();
                final String password = pass_word.getText().toString();

                final ProgressBar progressBar= findViewById(R.id.progressBar);

                progressBar.setVisibility(View.VISIBLE);

                //calling Firebase to complete signin process
                mAuth.signInWithEmailAndPassword(email, password)
                        .addOnCompleteListener(MainActivity.this, new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {
                                if (task.isSuccessful()) {
                                    String user_id = FirebaseAuth.getInstance().getUid();
                                    assert user_id != null;
                                    if (user_id.equals("wnFmFtBexSSyx0KFx90eX7kD4H32")) {
                                        progressBar.setVisibility(View.GONE);
                                        Intent intent = new Intent(MainActivity.this, AdminPanel.class);
                                        startActivity(intent);
                                        finish();
                                    }
                                } else {
                                    CoordinatorLayout coordinatorLayout;
                                    coordinatorLayout = findViewById(R.id.login_screen);
                                    Snackbar snackbar1 = Snackbar.make(coordinatorLayout, "You Are Not My Admin 🤨🤨", Snackbar.LENGTH_SHORT);
                                    snackbar1.show();
                                }
                            }
                        }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        progressBar.setVisibility(View.GONE);
                        Toast.makeText(MainActivity.this, "Unable to login Please check internet connection"+e.toString(), Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });

    }
}

