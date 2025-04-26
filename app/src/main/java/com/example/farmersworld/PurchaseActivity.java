package com.example.farmersworld;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;

public class PurchaseActivity extends AppCompatActivity {

    private ImageView productImage;
    private TextView productName, productDescription, productPrice, totalPrice;
    private EditText quantityEditText;
    private Button confirmButton;

    private String name, description, price, imageUrl, city;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_purchase);

        productImage = findViewById(R.id.productImage);
        productName = findViewById(R.id.productName);
        productDescription = findViewById(R.id.productDescription);
        productPrice = findViewById(R.id.productPrice);
        totalPrice = findViewById(R.id.totalPrice);
        quantityEditText = findViewById(R.id.quantityEditText);
        confirmButton = findViewById(R.id.confirmButton);

        // Get product details from intent
        name = getIntent().getStringExtra("name");
        description = getIntent().getStringExtra("description");
        price = getIntent().getStringExtra("price");
        imageUrl = getIntent().getStringExtra("imageUrl");
        city = getIntent().getStringExtra("city");

        // Set product details
        productName.setText(name);
        productDescription.setText(description);
        productPrice.setText("Price: ₹" + price);
        Glide.with(this).load(imageUrl).into(productImage);

        confirmButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String qtyStr = quantityEditText.getText().toString().trim();
                if (TextUtils.isEmpty(qtyStr)) {
                    Toast.makeText(PurchaseActivity.this, "Please enter quantity", Toast.LENGTH_SHORT).show();
                    return;
                }

                int quantity = Integer.parseInt(qtyStr);
                // Remove non-digit characters from price (assuming price is like "50" or "₹50")
                String priceDigits = price.replaceAll("[^0-9.]", "");
                double unitPrice = Double.parseDouble(priceDigits);
                double total = unitPrice * quantity;
                totalPrice.setText("Total: ₹" + total);

                // Here, you can add code to save the order to Firebase.
                Toast.makeText(PurchaseActivity.this, "Order Confirmed!", Toast.LENGTH_SHORT).show();
            }
        });
    }
}
