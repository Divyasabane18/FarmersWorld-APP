package com.example.farmersworld;

import android.os.Bundle;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.database.*;

import java.util.ArrayList;

public class AdminVerifyActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private FarmerAdapter adapter;
    private ArrayList<Farmer> farmerList;
    private DatabaseReference farmerRef;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_verify);

        recyclerView = findViewById(R.id.farmerRecyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        farmerList = new ArrayList<>();
        adapter = new FarmerAdapter(this, farmerList);
        recyclerView.setAdapter(adapter);

        farmerRef = FirebaseDatabase.getInstance().getReference("Farmers");

        farmerRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                farmerList.clear();
                for (DataSnapshot farmerSnap : snapshot.getChildren()) {
                    Farmer farmer = farmerSnap.getValue(Farmer.class);
                    if (farmer != null) {
                        farmer.setUid(farmerSnap.getKey()); // Save UID to use later
                        farmerList.add(farmer);
                    }
                }
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(AdminVerifyActivity.this, "Failed: " + error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }
}
