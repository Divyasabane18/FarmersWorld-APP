package com.example.farmersworld;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;

public class CustomerDashboardActivity extends AppCompatActivity {

    private RecyclerView productRecyclerView;
    private ProductAdapter adapter;
    private ArrayList<Product> productList;
    private DatabaseReference productRef;
    private String customerCity;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_customer_dashboard);

        // ✅ Get city from Intent
        customerCity = getIntent().getStringExtra("city");
        if (customerCity == null) {
            Toast.makeText(this, "City not found. Please login again.", Toast.LENGTH_SHORT).show();
            finish();
            return;
        }
        customerCity = customerCity.trim().toLowerCase(); // normalize

        productRecyclerView = findViewById(R.id.productRecyclerView);
        productRecyclerView.setLayoutManager(new LinearLayoutManager(this));

        productList = new ArrayList<>();
        adapter = new ProductAdapter(this, productList);
        productRecyclerView.setAdapter(adapter);

        productRef = FirebaseDatabase.getInstance().getReference("Products");

        loadProductsFromSameCity();

        Button viewCartButton = findViewById(R.id.viewCartButton);
        viewCartButton.setOnClickListener(v -> {
            Intent intent = new Intent(CustomerDashboardActivity.this, CartActivity.class);
            startActivity(intent);
        });
    }

    private void loadProductsFromSameCity() {
        productRef.addValueEventListener(new com.google.firebase.database.ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                productList.clear();

                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                    Product product = dataSnapshot.getValue(Product.class);
                    if (product != null) {
                        String productCity = product.getCity() != null ? product.getCity().trim().toLowerCase() : "";

                        if (productCity.equals(customerCity)) {
                            productList.add(product);
                        }
                    }
                }

                if (productList.isEmpty()) {
                    Toast.makeText(CustomerDashboardActivity.this, "No products found in your city", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(CustomerDashboardActivity.this, productList.size() + " products found", Toast.LENGTH_SHORT).show();
                }

                adapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(CustomerDashboardActivity.this, "Error loading products", Toast.LENGTH_SHORT).show();
                Log.e("FirebaseError", "Database error: " + error.getMessage());
            }
        });
    }
}
