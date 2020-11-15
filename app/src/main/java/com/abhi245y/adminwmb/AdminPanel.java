package com.abhi245y.adminwmb;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;
import androidx.coordinatorlayout.widget.CoordinatorLayout;

import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.FirebaseAuth;

public class AdminPanel extends AppCompatActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_panel);

        CoordinatorLayout coordinatorLayout;
        coordinatorLayout = findViewById(R.id.admin_panel);
        Snackbar snackbar1 = Snackbar.make(coordinatorLayout, "Welcome Boss", Snackbar.LENGTH_SHORT);
        snackbar1.show();

        Button mLogout =  findViewById(R.id.logout);
        mLogout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FirebaseAuth.getInstance().signOut();
                Intent intent =new Intent(AdminPanel.this,MainActivity.class);
                startActivity(intent);
                finish();
            }
        });
    }

    public void addRemoveBus(View v){

       Intent intent = new Intent(AdminPanel.this,EditBusDetails.class);
       startActivity(intent);

    }

    public void StopMaps(View view) {

        Intent intent =new Intent(AdminPanel.this,AddStopsMap.class);
        startActivity(intent);
    }
}
