package com.example.adminwmb;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.common.api.Status;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.libraries.places.api.Places;
import com.google.android.libraries.places.api.model.Place;
import com.google.android.libraries.places.widget.Autocomplete;
import com.google.android.libraries.places.widget.AutocompleteActivity;
import com.google.android.libraries.places.widget.model.AutocompleteActivityMode;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.GeoPoint;

import java.lang.reflect.Array;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class AddRemoveStopes extends AppCompatActivity {

    public static final String KEY_LOCATION ="Location of bus";
    public static final String KEY_BUS_TYPE="Bus Type";

    private static final String GOOGLE_PLACES_API_KEY = "AIzaSyCnkn0VsVabsU0rPFVq8Kh2bpHNIMu9Msc";
    private static final int AUTOCOMPLETE_REQUEST_CODE = 123;
    public TextView tvLocationName, tvLatLon;
    public String  location_name;
    private LatLng latLng;
     private Button  btn;


    private FirebaseFirestore db = FirebaseFirestore.getInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_remove_stopes);
        Places.initialize(getApplicationContext(), GOOGLE_PLACES_API_KEY);

        tvLocationName = findViewById(R.id.textView6);
        tvLatLon = findViewById(R.id.textView5);
        btn =findViewById(R.id.add_stops);


    }

    public void getStops(View v){
        tvLocationName.setText("");
        tvLatLon.setText("");
        List<Place.Field> fields = Arrays.asList(Place.Field.NAME, Place.Field.LAT_LNG);
        Intent intent = new Autocomplete.IntentBuilder(
                AutocompleteActivityMode.FULLSCREEN, fields)
                .build(AddRemoveStopes.this);
        startActivityForResult(intent, AUTOCOMPLETE_REQUEST_CODE);

    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == AUTOCOMPLETE_REQUEST_CODE) {
            if (resultCode == RESULT_OK) {
                Place place = Autocomplete.getPlaceFromIntent(data);
                tvLocationName.setText(place.getName());
                tvLatLon.setText(String.valueOf(place.getLatLng()));
                 latLng = place.getLatLng();

                 location_name = place.getName();

             final Double   mLatitude = latLng.latitude;
             final Double   mLongitude = latLng.longitude;

                Toast.makeText(this, "Lati: "+mLatitude+" longi: "+mLongitude, Toast.LENGTH_SHORT).show();

                btn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        String stop_name = location_name;
                        GeoPoint geoPoint= new GeoPoint(mLatitude,mLongitude);

                        Map<String, Object> data = new HashMap<>();
                        data.put(KEY_LOCATION,geoPoint);


                        db.collection("Stop").document(stop_name).set(data).addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                Toast.makeText(AddRemoveStopes.this, "Done", Toast.LENGTH_SHORT).show();
                            }
                        }).addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                Toast.makeText(AddRemoveStopes.this, "Error: "+e.toString(), Toast.LENGTH_SHORT).show();
                            }
                        });
                    }
                });


            } else if (resultCode == AutocompleteActivity.RESULT_ERROR) {
                // TODO: Handle the error.
                Status status = Autocomplete.getStatusFromIntent(data);
                Log.i("tag", status.getStatusMessage());
            } else if (resultCode == RESULT_CANCELED) {
                // The user canceled the operation.
                Toast.makeText(this, "cancelled", Toast.LENGTH_SHORT).show();
            }
        }
    }




}