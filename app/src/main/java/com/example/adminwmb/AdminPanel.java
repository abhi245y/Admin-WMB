package com.example.adminwmb;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.google.firebase.auth.FirebaseAuth;

public class AdminPanel extends AppCompatActivity {


    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_panel);

        mAuth = FirebaseAuth.getInstance();

        Button mLogout =  findViewById(R.id.logout);
        mLogout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FirebaseAuth.getInstance().signOut();//firebase command to sign out
                Intent intent =new Intent(AdminPanel.this,MainActivity.class);
                startActivity(intent);
                finish();
            }
        });
    }

    public void addRemoveBus(View v){

        Intent intent= new Intent(AdminPanel.this,AddRemoveBus.class);
        startActivity(intent);
        finish();
    }

    public void  addRemoveStops(View v){

        Intent intent =new Intent(AdminPanel.this,AddRemoveStopes.class);
        startActivity(intent);
        finish();
    }
}
