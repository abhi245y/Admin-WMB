package com.example.adminwmb;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.location.Location;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

public class AddRemoveBus extends AppCompatActivity {

    public static final String KEY_LOCATION ="Location of bus";

    private EditText editTextBusNO;

    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_remove_bus);

        editTextBusNO = findViewById(R.id.editTextBusNo);
    }

    public void addBus(View v){

        String Bus_No = editTextBusNO.getText().toString();

        Map<String,Object> busData =new HashMap<>();

        busData.put(KEY_LOCATION,"90");

        db.collection("Stopes_test").document("Pattom").collection("Buses that come here").document(Bus_No).set(busData).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                Toast.makeText(AddRemoveBus.this, "Done", Toast.LENGTH_SHORT).show();
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(AddRemoveBus.this, e.toString(), Toast.LENGTH_SHORT).show();
            }
        });
     }
}
