package com.abhi245y.adminwmb;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.coordinatorlayout.widget.CoordinatorLayout;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.FragmentActivity;

import com.google.android.gms.common.api.Status;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.libraries.places.api.Places;
import com.google.android.libraries.places.api.model.Place;
import com.google.android.libraries.places.widget.Autocomplete;
import com.google.android.libraries.places.widget.AutocompleteActivity;
import com.google.android.libraries.places.widget.model.AutocompleteActivityMode;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.GeoPoint;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class AddStopsMap extends FragmentActivity implements OnMapReadyCallback {

    private static final int AUTOCOMPLETE_REQUEST_CODE = 123;
    public static final String PLACES_API = "";
    public static final String TAG = "AddStopsMap";
    private GoogleMap mMap;
    private TextView latLngText, stop_name_text, bus_name_text;
    private LatLng FinallatLng;
    LatLng latLngget;
    AutoCompleteTextView loc_name;
    EditText bus_list;
    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    private CollectionReference stopref = db.collection("Stop");
    private ArrayList<String> busList = new ArrayList<>();
    private CollectionReference busref = db.collection("Bus List");
    private ArrayList<String> stopList = new ArrayList<>();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Places.initialize(getApplicationContext(), PLACES_API);
        setContentView(R.layout.activity_add_stops_map);

        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        stopref.get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
            @Override
            public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                for (QueryDocumentSnapshot documentSnapshot : queryDocumentSnapshots) {
                    if (documentSnapshot.exists()) {
                        SearchStopModel searchStopModel = documentSnapshot.toObject(SearchStopModel.class);
                        stopList.add(searchStopModel.getBus_stop_name());
                    }

                }
                Log.d(TAG, "Stop List:" + stopList);


            }
        });

        busref.get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
            @Override
            public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                for (QueryDocumentSnapshot documentSnapshot : queryDocumentSnapshots) {
                    if (documentSnapshot.exists()) {
                        SeachBusModel seachBusModel = documentSnapshot.toObject(SeachBusModel.class);
                        busList.add(seachBusModel.getBus_no());
                    }
                }

                Log.d(TAG, "Bus List:" + busList);
            }
        });

        latLngText = findViewById(R.id.latlang);
        loc_name = findViewById(R.id.loc_name);
        bus_list = findViewById(R.id.list_of_bus);
        stop_name_text = findViewById(R.id.textView9);
        bus_name_text = findViewById(R.id.textView10);

        get_bus_details();
    }

    private void get_bus_details() {


        ArrayAdapter<String> adapterStop = new ArrayAdapter<String>(this, R.layout.custom_stop_list_item, R.id.text_view_stop_list_item, stopList);

            loc_name.setAdapter(adapterStop);
            loc_name.setVisibility(View.VISIBLE);
            stop_name_text.setVisibility(View.VISIBLE);

            bus_name_text.setVisibility(View.VISIBLE);
            bus_list.setVisibility(View.VISIBLE);

            Button save_btn = findViewById(R.id.save_btn);
            save_btn.setVisibility(View.VISIBLE);


        loc_name.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String place_name = parent.getItemAtPosition(position).toString();

                stopref.document(place_name).get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                    @Override
                    public void onSuccess(DocumentSnapshot documentSnapshot) {
                        if (documentSnapshot.exists()) {
                            SearchStopModel searchStopModel = documentSnapshot.toObject(SearchStopModel.class);

                            LatLng pos = new LatLng(searchStopModel.getBus_stop_location().getLatitude(), searchStopModel.getBus_stop_location().getLongitude());
                            latLngText.setText(searchStopModel.getBus_stop_location().toString());


                            latLngget=new LatLng(searchStopModel.getBus_stop_location().getLatitude(),searchStopModel.getBus_stop_location().getLongitude());



                            MarkerOptions markerOptions = new MarkerOptions();
                            markerOptions.position(pos);
                            markerOptions.title(searchStopModel.getBus_stop_name());
                            markerOptions.icon(bitmapDescriptorFromVector(getApplicationContext()));
                            mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(pos, 15));
                            mMap.addMarker(markerOptions);
                        }
                    }
                });
            }
        });


    }

    @Override
    public void onMapReady(final GoogleMap googleMap) {
        mMap = googleMap;

        googleMap.setOnMapClickListener(new GoogleMap.OnMapClickListener() {
            @Override
            public void onMapClick(LatLng latLng) {

                String latlangs = latLng.latitude + "," + latLng.longitude;
                MarkerOptions markerOptions = new MarkerOptions();
                markerOptions.position(latLng);
                markerOptions.title(latlangs);
                markerOptions.icon(bitmapDescriptorFromVector(getApplicationContext()));
                googleMap.clear();
                googleMap.addMarker(markerOptions);
                latLngText.setText(latlangs);
                FinallatLng = latLng;


            }
        });
    }

    private BitmapDescriptor bitmapDescriptorFromVector(Context context) {
        Drawable vectorDrawable = ContextCompat.getDrawable(context, R.drawable.bus_stop_marker);
        assert vectorDrawable != null;
        vectorDrawable.setBounds(50, 50, vectorDrawable.getIntrinsicWidth(), vectorDrawable.getIntrinsicHeight());

        Bitmap bitmap = Bitmap.createBitmap(vectorDrawable.getMinimumWidth(), vectorDrawable.getIntrinsicHeight(), Bitmap.Config.ARGB_8888);

        Canvas canvas = new Canvas(bitmap);
        vectorDrawable.draw(canvas);
        return BitmapDescriptorFactory.fromBitmap(bitmap);

    }


    public void search(View view) {

        List<Place.Field> fields = Arrays.asList(Place.Field.NAME, Place.Field.LAT_LNG);
        Intent intent = new Autocomplete.IntentBuilder(
                AutocompleteActivityMode.OVERLAY, fields)
                .build(AddStopsMap.this);
        startActivityForResult(intent, AUTOCOMPLETE_REQUEST_CODE);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == AUTOCOMPLETE_REQUEST_CODE) {
            if (resultCode == RESULT_OK) {
                final Place place = Autocomplete.getPlaceFromIntent(data);
                loc_name.setText(place.getName());
                latLngText.setText(String.valueOf(place.getLatLng()));
                LatLng latLng = place.getLatLng();
                loc_name.setText(place.getName());

                MarkerOptions markerOptions = new MarkerOptions();
                markerOptions.position(latLng);
                markerOptions.title(place.getName());
                markerOptions.icon(bitmapDescriptorFromVector(getApplicationContext()));
                mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(latLng, 15));
                mMap.addMarker(markerOptions);

            } else if (resultCode == AutocompleteActivity.RESULT_ERROR) {
                Status status = Autocomplete.getStatusFromIntent(data);
                Log.i("tag", status.getStatusMessage());
            } else if (resultCode == RESULT_CANCELED) {
                // The user canceled the operation.
                Toast.makeText(this, "cancelled", Toast.LENGTH_SHORT).show();
            }
        }
    }

    public void saveStop(View view) {

       try {
           String stop_name = loc_name.getText().toString();
           GeoPoint geoPoint = new GeoPoint(latLngget.latitude, latLngget.longitude);

           String bus_list_input = bus_list.getText().toString();
           String[] bus_list_array = bus_list_input.split("\\s*,\\s*");
           List<String> bus_list = Arrays.asList(bus_list_array);

           StopList stopList = new StopList(stop_name, geoPoint, bus_list);

           stopref.document(stop_name).set(stopList).addOnCompleteListener(new OnCompleteListener<Void>() {
               @Override
               public void onComplete(@NonNull Task<Void> task) {
                   if (task.isSuccessful()) {
                       CoordinatorLayout coordinatorLayout;
                       coordinatorLayout = findViewById(R.id.add_stops_Map);
                       Snackbar snackbar1 = Snackbar.make(coordinatorLayout, "Bus Details Added Successfully🎉🥳", Snackbar.LENGTH_SHORT);
                       snackbar1.show();
                   }
               }
           });
       }catch (Exception e){
           String stop_name = loc_name.getText().toString();
           GeoPoint geoPoint = new GeoPoint(8.465675, 76.954986);


           Log.d(TAG," Error Saving: "+e);



           ArrayList<String>bus_list=new ArrayList<>();
           bus_list.add("Bus 3");

           StopList stopList = new StopList(stop_name, geoPoint, bus_list);
           stopref.document(stop_name).set(stopList).addOnCompleteListener(new OnCompleteListener<Void>() {
               @Override
               public void onComplete(@NonNull Task<Void> task) {
                   if (task.isSuccessful()) {
                       CoordinatorLayout coordinatorLayout;
                       coordinatorLayout = findViewById(R.id.add_stops_Map);
                       Snackbar snackbar1 = Snackbar.make(coordinatorLayout, "Bus Details Added Successfully🎉🥳", Snackbar.LENGTH_SHORT);
                       snackbar1.show();
                   }
               }
           });
       }
    }
}
