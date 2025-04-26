package com.example.farmersworld;

import android.app.Dialog;
import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.widget.Button;
import android.widget.Toast;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class CartActivity extends AppCompatActivity {

    private RecyclerView cartRecyclerView;
    private ArrayList<CartItem> cartItemList;
    private CartAdapter cartAdapter;
    private TextView totalPriceTextView;
    private Button proceedToBuyButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cart);

        // Initialize RecyclerView and its layout
        cartRecyclerView = findViewById(R.id.cartRecyclerView);
        cartRecyclerView.setLayoutManager(new LinearLayoutManager(this));

        // Initialize the cart item list and adapter with the listener
        cartItemList = new ArrayList<>();
        cartAdapter = new CartAdapter(this, cartItemList, new CartAdapter.OnCartItemClickListener() {
            @Override
            public void onDeleteClick(CartItem cartItem) {
                deleteCartItem(cartItem);
            }

            @Override
            public void onIncrementClick(CartItem cartItem) {
                updateCartItemQuantity(cartItem, cartItem.getQuantity() + 1);
            }

            @Override
            public void onDecrementClick(CartItem cartItem) {
                if (cartItem.getQuantity() > 1) {
                    updateCartItemQuantity(cartItem, cartItem.getQuantity() - 1);
                }
            }

            @Override
            public void onQuantityChange(CartItem cartItem, int newQuantity) {
                // Empty implementation, since it's not needed in this context
            }
        });
        cartRecyclerView.setAdapter(cartAdapter);

        // Initialize total price TextView and buttons
        totalPriceTextView = findViewById(R.id.cartTotalPrice);
        proceedToBuyButton = findViewById(R.id.proceedToBuyButton);

        // Load the cart items from Firebase
        loadCartItems();

        // Set listeners for the buttons
        proceedToBuyButton.setOnClickListener(v -> handleProceedToBuy());
    }

    private void loadCartItems() {
        String userId = FirebaseAuth.getInstance().getCurrentUser() != null ?
                FirebaseAuth.getInstance().getCurrentUser().getUid() : null;

        if (userId == null) {
            Toast.makeText(this, "User not logged in", Toast.LENGTH_SHORT).show();
            return;
        }

        DatabaseReference cartRef = FirebaseDatabase.getInstance().getReference("Cart").child(userId);

        cartRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull com.google.firebase.database.DataSnapshot dataSnapshot) {
                cartItemList.clear();

                if (!dataSnapshot.exists()) {
                    Toast.makeText(CartActivity.this, "Your cart is empty", Toast.LENGTH_SHORT).show();
                    return;
                }

                for (com.google.firebase.database.DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    CartItem cartItem = snapshot.getValue(CartItem.class);
                    if (cartItem != null) {
                        cartItemList.add(cartItem);
                    }
                }

                updateTotalPrice();
                cartAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull com.google.firebase.database.DatabaseError databaseError) {
                Toast.makeText(CartActivity.this, "Error loading cart: " + databaseError.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void updateTotalPrice() {
        double totalPrice = 0.0;

        for (CartItem cartItem : cartItemList) {
            double price = Double.parseDouble(cartItem.getPrice());
            int quantity = cartItem.getQuantity();
            totalPrice += (price * quantity);
        }

        totalPriceTextView.setText("Total: ₹ " + totalPrice);
    }

    private void handleProceedToBuy() {
        // Store the products and show success
        storeProductsInDatabase("buy");

        // Show success dialog
        showSuccessDialog();
    }

    // Method to show the success dialog
    private void showSuccessDialog() {
        Dialog successDialog = new Dialog(CartActivity.this);
        successDialog.setContentView(R.layout.dialog_success);  // Use the custom layout for success dialog
        successDialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));

        // Find components inside the dialog
        TextView successMessage = successDialog.findViewById(R.id.successMessage);
        Button okButton = successDialog.findViewById(R.id.okButton);

        // Set the success message
        successMessage.setText("Your products were bought successfully!");

        // Close the dialog when OK button is clicked
        okButton.setOnClickListener(v -> successDialog.dismiss());

        // Show the dialog
        successDialog.show();
    }

    private void storeProductsInDatabase(String action) {
        String userId = FirebaseAuth.getInstance().getCurrentUser() != null ?
                FirebaseAuth.getInstance().getCurrentUser().getUid() : null;

        if (userId != null) {
            DatabaseReference orderRef = FirebaseDatabase.getInstance().getReference("Orders").child(userId).child(action);

            for (CartItem cartItem : cartItemList) {
                orderRef.push().setValue(cartItem).addOnSuccessListener(aVoid -> {
                    // Optional: Handle success here
                }).addOnFailureListener(e -> {
                    // Optional: Handle failure here
                });
            }
        }
    }

    private void deleteCartItem(CartItem cartItem) {
        String userId = FirebaseAuth.getInstance().getCurrentUser() != null ?
                FirebaseAuth.getInstance().getCurrentUser().getUid() : null;

        if (userId != null) {
            DatabaseReference cartRef = FirebaseDatabase.getInstance().getReference("Cart").child(userId).child(cartItem.getName());
            cartRef.removeValue().addOnSuccessListener(aVoid -> {
                Toast.makeText(CartActivity.this, "Item removed from cart", Toast.LENGTH_SHORT).show();
            }).addOnFailureListener(e -> {
                Toast.makeText(CartActivity.this, "Failed to remove item", Toast.LENGTH_SHORT).show();
            });
        }
    }

    private void updateCartItemQuantity(CartItem cartItem, int newQuantity) {
        String userId = FirebaseAuth.getInstance().getCurrentUser() != null ?
                FirebaseAuth.getInstance().getCurrentUser().getUid() : null;

        if (userId != null) {
            DatabaseReference cartRef = FirebaseDatabase.getInstance().getReference("Cart").child(userId).child(cartItem.getName());
            cartRef.child("quantity").setValue(newQuantity).addOnSuccessListener(aVoid -> {
                Toast.makeText(CartActivity.this, "Quantity updated", Toast.LENGTH_SHORT).show();
                updateTotalPrice();
            }).addOnFailureListener(e -> {
                Toast.makeText(CartActivity.this, "Failed to update quantity", Toast.LENGTH_SHORT).show();
            });
        }
    }
}
