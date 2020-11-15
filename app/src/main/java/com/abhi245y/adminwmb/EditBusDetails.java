package com.abhi245y.adminwmb;

import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.coordinatorlayout.widget.CoordinatorLayout;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.GeoPoint;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class EditBusDetails extends AppCompatActivity {


    public static final String TAG = "EditBusDetails";
    private EditText editTextBusType;
    private AutoCompleteTextView editTextBusNO;
    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    private CollectionReference stopref = db.collection("Stop");
    private CollectionReference busref = db.collection("Bus List");
    private LinearLayout linearLayout1, linearLayout2;
    private List<AutoCompleteTextView> allEds = new ArrayList<>();
    private List<EditText> allEds2 = new ArrayList<>();
    public BusStopListModel busStopListModel;
    private ArrayList<String> busList = new ArrayList<>();
    private ArrayList<String> stopList = new ArrayList<>();
    ArrayList<GeoPoint> stop_loc = new ArrayList<>();
    private EditText editText2;
    private AutoCompleteTextView editText;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_bus_details);

        editTextBusNO = findViewById(R.id.editTextBusNo);
        editTextBusType = findViewById(R.id.editTextType);
        linearLayout1 = findViewById(R.id.parent_lay1);
        linearLayout2 = findViewById(R.id.parent_lay2);
        getData();


    }

    private void getData() {

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

        ArrayAdapter<String> adapterBus = new ArrayAdapter<>(this,
                R.layout.custom_bus_list_item, R.id.text_view_bus_list_item, busList);
        editTextBusNO.setAdapter(adapterBus);

        editTextBusNO.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Toast.makeText(getApplicationContext(), "Selected Item: " + parent.getItemAtPosition(position), Toast.LENGTH_SHORT).show();

            }
        });

        stopref.get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
            @Override
            public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                for (QueryDocumentSnapshot documentSnapshot : queryDocumentSnapshots) {
                    if (documentSnapshot.exists()) {
                        SearchStopModel searchStopModel = documentSnapshot.toObject(SearchStopModel.class);
                        stopList.add(searchStopModel.getBus_stop_name());
                        searchStopModel.setBus_stop_name(String.valueOf(stopList));
                    }
                }
                Log.d(TAG, "Stop List:" + stopList);
            }
        });

    }

    public void addField(View v) {
        int count = 1;
        int num = 11;


        editText2 = new EditText(EditBusDetails.this);
        allEds2.add(editText2);
        editText2.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT));
        editText2.setHint("Stop num");
        editText2.setBackgroundColor(Color.TRANSPARENT);
        editText2.setPadding(8, 20, 8, 8);
        editText2.setId(num);
        editText2.setGravity(LinearLayout.TEXT_ALIGNMENT_CENTER);

        linearLayout1.addView(editText2);

        editText = new AutoCompleteTextView(EditBusDetails.this);
        allEds.add(editText);
        editText.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT));
        editText.setHint("Enter Bus Stop");
        editText.setThreshold(1);
        editText.setBackgroundColor(Color.TRANSPARENT);
        editText.setDropDownHeight(LinearLayout.LayoutParams.WRAP_CONTENT);
        editText.setId(count);

        linearLayout2.addView(editText);


        ArrayAdapter<String> adapterStop = new ArrayAdapter<>(this,
                R.layout.custom_stop_list_item, R.id.text_view_stop_list_item, stopList);
        editText.setAdapter(adapterStop);
    }


    public void addBus(View v) {

        String Bus_No = editTextBusNO.getText().toString();
        String Bus_Type = editTextBusType.getText().toString();
        String[] all_stops = new String[allEds.size()];
        int[] all_stop_num = new int[allEds2.size()];

        for (int i = 0; i < allEds.size(); i++) {
            all_stops[i] = allEds.get(i).getText().toString();
            String stop_name = allEds.get(i).getText().toString();
            Log.d(TAG,"Log Stop Name: "+ stop_name);
            final int finalI = i;
            stopref.whereEqualTo("bus_stop_name",stop_name).get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                @Override
                public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                    for (QueryDocumentSnapshot documentSnapshot:queryDocumentSnapshots) {
                        StopList stopList = documentSnapshot.toObject(StopList.class);
                        stop_loc.add(stopList.getBus_stop_location());
                        Log.d(TAG, "Log Stop Loc: " + stop_loc);
                    }
                }
            });
        }

        for (int i = 0; i < allEds2.size(); i++) {
            all_stop_num[i] = Integer.parseInt(allEds2.get(i).getText().toString());
        }

        List<String> stop_list = Arrays.asList(all_stops);



        BusList busList = new BusList(Bus_No, Bus_Type);


        CollectionReference busref = db.collection("Bus List");

        CollectionReference busStopref = db.collection("Bus List").document(Bus_No).collection("Bus stop list");

        final ProgressBar progressBar = findViewById(R.id.progressBar2);

        busref.document(Bus_No).set(busList).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {

                progressBar.setVisibility(View.VISIBLE);
                if (task.isSuccessful()) {
                    progressBar.setVisibility(View.GONE);
                    CoordinatorLayout coordinatorLayout;
                    coordinatorLayout = findViewById(R.id.edi_bus_pannel);
                    Snackbar snackbar1 = Snackbar.make(coordinatorLayout, "Bus Details Added Successfully🎉🥳", Snackbar.LENGTH_SHORT);
                    snackbar1.show();
                    progressBar.setVisibility(View.INVISIBLE);
                }

            }
        });


        if (stop_list != null) {


            String bus_stop_list;
            int bus_stop_num_list;
            GeoPoint bus_stop_location;

            for (int i = 0; i < stop_loc.size(); i++) {

                bus_stop_list = all_stops[i];
                bus_stop_num_list = all_stop_num[i];
                bus_stop_location = stop_loc.get(i);

                Log.d(TAG,"Log bus_stop_location "+bus_stop_location);
                busStopListModel = new BusStopListModel(bus_stop_list, bus_stop_num_list, bus_stop_location);


                busStopref.document(bus_stop_list).set(busStopListModel);


            }


        }
    }

    public void RemoveField(View view) {

        for (int i = 0; i < allEds.size(); i++) {
            linearLayout1.removeView(editText);
            linearLayout2.removeView(editText2);
        }
    }
}
