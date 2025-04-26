package com.example.farmersworld;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.*;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.FirebaseDatabase;

import java.text.DecimalFormat;
import java.util.ArrayList;

public class ProductAdapter extends RecyclerView.Adapter<ProductAdapter.ViewHolder> {

    private Context context;
    private ArrayList<Product> productList;

    public ProductAdapter(Context context, ArrayList<Product> productList) {
        this.context = context;
        this.productList = productList;
    }

    @NonNull
    @Override
    public ProductAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_product, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ProductAdapter.ViewHolder holder, int position) {
        Product product = productList.get(position);

        // Set product details
        holder.productName.setText(product.getName());
        holder.productDescription.setText(product.getDescription());

        // Parse and format price
        String priceString = product.getPrice(); // Assuming price is a String
        double price = 0.0;
        try {
            price = Double.parseDouble(priceString);  // Parse the price to double
        } catch (NumberFormatException e) {
            e.printStackTrace();
            holder.productPrice.setText("Invalid Price"); // Handle invalid price
            return;
        }

        // Format price and set it
        DecimalFormat df = new DecimalFormat("₹ #,###.##");
        String formattedPrice = df.format(price); // Format the price
        holder.productPrice.setText(formattedPrice);  // Display formatted price

        // Load product image using Glide
        String imageUrl = product.getImageUrl();
        if (imageUrl != null && !imageUrl.isEmpty()) {
            Glide.with(context)
                    .load(imageUrl)
                    .into(holder.productImage);
        } else {
            // Fallback image in case URL is null or empty
            Glide.with(context)
                    .load(R.drawable.placeholder_image_background) // Replace with your placeholder image resource
                    .into(holder.productImage);
        }

        // Set the city for the product
        String city = product.getCity() != null ? product.getCity() : "City not specified";
        holder.productCity.setText(city);

        // Buy button click listener
        holder.buyButton.setOnClickListener(v -> {
            String userId = FirebaseAuth.getInstance().getCurrentUser() != null ?
                    FirebaseAuth.getInstance().getCurrentUser().getUid() : null;

            if (userId == null) {
                Toast.makeText(context, "User not logged in", Toast.LENGTH_SHORT).show();
                return;
            }

            // Using product name or a combination of fields to identify the product
            String productName = product.getName();
            if (productName == null || productName.isEmpty()) {
                Toast.makeText(context, "Product name is invalid", Toast.LENGTH_SHORT).show();
                return;
            }

            // Create CartItem object with price as string
            CartItem cartItem = new CartItem(
                    product.getName(),
                    priceString,  // Store price as string in CartItem
                    1,
                    product.getImageUrl() // Store image URL in CartItem
            );

            // Add to Firebase Cart under userID and productName (avoid overwriting entire cart)
            FirebaseDatabase.getInstance().getReference("Cart")
                    .child(userId)  // Path to the user cart
                    .child(productName)  // Use product name as unique key
                    .setValue(cartItem)  // The data you're saving
                    .addOnSuccessListener(unused -> {
                        Toast.makeText(context, "Product added to cart!", Toast.LENGTH_SHORT).show();
                    })
                    .addOnFailureListener(e -> {
                        Toast.makeText(context, "Failed: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                    });
        });
    }

    @Override
    public int getItemCount() {
        return productList.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView productName, productDescription, productPrice, productCity;
        ImageView productImage;
        Button buyButton;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            productName = itemView.findViewById(R.id.productName);
            productDescription = itemView.findViewById(R.id.productDescription);
            productPrice = itemView.findViewById(R.id.productPrice);
            productImage = itemView.findViewById(R.id.productImage);
            productCity = itemView.findViewById(R.id.productCity);
            buyButton = itemView.findViewById(R.id.buyButton);
        }
    }
}
