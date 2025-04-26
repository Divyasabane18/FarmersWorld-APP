package com.example.farmersworld;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.*;
import com.bumptech.glide.Glide;
import com.google.firebase.auth.FirebaseAuth;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;

public class CartAdapter extends RecyclerView.Adapter<CartAdapter.ViewHolder> {

    private Context context;
    private ArrayList<CartItem> cartItemList;
    private OnCartItemClickListener listener;

    // Define the listener interface
    public interface OnCartItemClickListener {
        void onDeleteClick(CartItem cartItem);
        void onIncrementClick(CartItem cartItem);
        void onDecrementClick(CartItem cartItem);

        void onQuantityChange(CartItem cartItem, int newQuantity);
    }

    public CartAdapter(Context context, ArrayList<CartItem> cartItemList, OnCartItemClickListener listener) {
        this.context = context;
        this.cartItemList = cartItemList;
        this.listener = listener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_cart, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        CartItem cartItem = cartItemList.get(position);
        holder.productName.setText(cartItem.getName());
        holder.productPrice.setText("₹ " + cartItem.getPrice());
        holder.productQuantity.setText("Quantity: " + cartItem.getQuantity());

        // Load product image using Glide
        Glide.with(context)
                .load(cartItem.getImageUrl())
                .placeholder(R.drawable.placeholder)
                .into(holder.productImage);

        // Handle click events using the listener
        holder.deleteButton.setOnClickListener(v -> listener.onDeleteClick(cartItem));
        holder.incrementButton.setOnClickListener(v -> listener.onIncrementClick(cartItem));
        holder.decrementButton.setOnClickListener(v -> listener.onDecrementClick(cartItem));
    }

    @Override
    public int getItemCount() {
        return cartItemList.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView productName, productPrice, productQuantity;
        ImageView productImage;
        Button incrementButton, decrementButton;
        ImageButton deleteButton; // Changed from Button to ImageButton

        @SuppressLint("WrongViewCast")
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            productName = itemView.findViewById(R.id.productName);
            productPrice = itemView.findViewById(R.id.productPrice);
            productQuantity = itemView.findViewById(R.id.productQuantity);
            productImage = itemView.findViewById(R.id.productImage);
            incrementButton = itemView.findViewById(R.id.incrementButton);
            decrementButton = itemView.findViewById(R.id.decrementButton);
            deleteButton = itemView.findViewById(R.id.removeButton); // Corrected to ImageButton
        }
    }
}
