package com.example.farmersworld;

import android.os.Bundle;
import android.widget.TextView;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.*;
import java.text.DecimalFormat;
import java.util.ArrayList;

public class CheckoutSummaryActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private CartAdapter cartAdapter;
    private ArrayList<CartItem> cartItemList;
    private TextView totalCostText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_checkout_summary);

        recyclerView = findViewById(R.id.checkoutRecyclerView);
        totalCostText = findViewById(R.id.checkoutTotalText);

        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        cartItemList = new ArrayList<>();

        // Provide dummy listener since no interaction needed in checkout
        cartAdapter = new CartAdapter(this, cartItemList, new CartAdapter.OnCartItemClickListener() {
            @Override
            public void onDeleteClick(CartItem cartItem) {
                // Not needed in checkout
            }

            @Override
            public void onIncrementClick(CartItem cartItem) {
                // Not needed in checkout
            }

            @Override
            public void onDecrementClick(CartItem cartItem) {
                // Not needed in checkout
            }

            @Override
            public void onQuantityChange(CartItem cartItem, int newQuantity) {
                // Not needed in checkout
            }
        });

        recyclerView.setAdapter(cartAdapter);

        loadCartItems();
    }

    private void loadCartItems() {
        String userId = FirebaseAuth.getInstance().getCurrentUser().getUid();

        DatabaseReference cartRef = FirebaseDatabase.getInstance().getReference("Cart").child(userId);

        cartRef.addListenerForSingleValueEvent(new ValueEventListener() {
            // Removed the @Override annotation
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                cartItemList.clear();
                double totalCost = 0.0;

                for (DataSnapshot itemSnapshot : snapshot.getChildren()) {
                    CartItem cartItem = itemSnapshot.getValue(CartItem.class);
                    if (cartItem != null) {
                        cartItemList.add(cartItem);

                        try {
                            totalCost += Double.parseDouble(cartItem.getPrice()) * cartItem.getQuantity();
                        } catch (NumberFormatException e) {
                            e.printStackTrace();
                        }
                    }
                }

                cartAdapter.notifyDataSetChanged();

                DecimalFormat df = new DecimalFormat("₹ #,###.##");
                totalCostText.setText("Total: " + df.format(totalCost));
            }

            // Removed the @Override annotation
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(CheckoutSummaryActivity.this, "Failed to load cart", Toast.LENGTH_SHORT).show();
            }
        });
    }
}
