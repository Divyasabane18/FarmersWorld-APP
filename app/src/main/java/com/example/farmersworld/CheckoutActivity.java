package com.example.farmersworld;

import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.*;

import java.util.ArrayList;
import java.util.HashMap;

public class CheckoutActivity extends AppCompatActivity {

    private EditText nameEditText, addressEditText;
    private Button confirmOrderButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_checkout);

        nameEditText = findViewById(R.id.nameEditText);
        addressEditText = findViewById(R.id.addressEditText);
        confirmOrderButton = findViewById(R.id.confirmOrderButton);

        confirmOrderButton.setOnClickListener(v -> {
            String name = nameEditText.getText().toString().trim();
            String address = addressEditText.getText().toString().trim();

            if (name.isEmpty() || address.isEmpty()) {
                Toast.makeText(this, "Please fill in all details", Toast.LENGTH_SHORT).show();
                return;
            }

            placeOrder(name, address);
        });
    }

    private void placeOrder(String name, String address) {
        String userId = FirebaseAuth.getInstance().getCurrentUser().getUid();
        DatabaseReference cartRef = FirebaseDatabase.getInstance().getReference("Cart").child(userId);
        DatabaseReference orderRef = FirebaseDatabase.getInstance().getReference("Orders").child(userId).push();

        cartRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                ArrayList<CartItem> orderItems = new ArrayList<>();
                for (DataSnapshot data : snapshot.getChildren()) {
                    CartItem item = data.getValue(CartItem.class);
                    if (item != null) {
                        orderItems.add(item);
                    }
                }

                HashMap<String, Object> orderData = new HashMap<>();
                orderData.put("name", name);
                orderData.put("address", address);
                orderData.put("items", orderItems);
                orderData.put("timestamp", System.currentTimeMillis());

                orderRef.setValue(orderData).addOnSuccessListener(unused -> {
                    cartRef.removeValue(); // Clear cart after order placed
                    Toast.makeText(CheckoutActivity.this, "Order placed successfully!", Toast.LENGTH_SHORT).show();
                    finish(); // Optional: close activity
                });
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(CheckoutActivity.this, "Order failed: " + error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }
}
