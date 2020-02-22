package com.example.adminwmb;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

import java.util.Objects;

public class MainActivity extends AppCompatActivity {


    private EditText email_id, pass_word;
    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        // Initialize Firebase Auth
        mAuth = FirebaseAuth.getInstance();


        email_id= findViewById(R.id.admin_email);
        pass_word= findViewById(R.id.admin_password);
        Button login =  findViewById(R.id.login);

        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                //getting email id and password
                final String email = email_id.getText().toString();
                final String password = pass_word.getText().toString();


                //calling Firebase to complete signin process
                mAuth.signInWithEmailAndPassword(email, password)
                        .addOnCompleteListener(MainActivity.this, new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {
                                if (!task.isSuccessful()) {
                                    Toast.makeText(MainActivity.this, "Error Logging in", Toast.LENGTH_SHORT).show();
                                } else {

                                    String user_id= Objects.requireNonNull(mAuth.getCurrentUser()).getUid();//getting user id

                                    if(user_id.equals("wnFmFtBexSSyx0KFx90eX7kD4H32")) {

                                        Intent intent= new Intent(MainActivity.this,AdminPanel.class);
                                        startActivity(intent);

                                    }
                                    else
                                    {
                                        Toast.makeText(MainActivity.this, "Hey You, You are not the admin", Toast.LENGTH_SHORT).show();
                                    }

                                }
                            }
                        });
            }
        });



    }
}

