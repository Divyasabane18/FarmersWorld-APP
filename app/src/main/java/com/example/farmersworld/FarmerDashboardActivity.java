package com.example.farmersworld;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.*;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.*;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class FarmerDashboardActivity extends AppCompatActivity {

    private ImageView productImageView;
    private EditText productNameEditText, productPriceEditText, productDescEditText;
    private Spinner stateSpinner, citySpinner;
    private Button selectImageButton, uploadButton;
    private Uri imageUri;

    private DatabaseReference databaseReference;
    private final Map<String, String[]> stateCityMap = new HashMap<>();

    private static final int PICK_IMAGE_REQUEST = 1;
    private static final int STORAGE_PERMISSION_CODE = 101;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_farmer_dashboard);

        productImageView = findViewById(R.id.productImageView);
        productNameEditText = findViewById(R.id.productNameEditText);
        productPriceEditText = findViewById(R.id.productPriceEditText);
        productDescEditText = findViewById(R.id.productDescEditText);
        selectImageButton = findViewById(R.id.selectImageButton);
        uploadButton = findViewById(R.id.uploadButton);
        stateSpinner = findViewById(R.id.stateSpinner);
        citySpinner = findViewById(R.id.citySpinner);

        databaseReference = FirebaseDatabase.getInstance().getReference("Products");

        checkStoragePermission();
        setupStateCitySpinners();

        // Removed verification check as per the new requirements
        selectImageButton.setEnabled(true);
        uploadButton.setEnabled(true);
        selectImageButton.setAlpha(1f);
        uploadButton.setAlpha(1f);

        selectImageButton.setOnClickListener(v -> chooseImage());
        uploadButton.setOnClickListener(v -> uploadProduct());
    }

    private void setupStateCitySpinners() {
        stateCityMap.put("Maharashtra", new String[]{"Mumbai", "Pune", "Nagpur", "Nashik", "Aurangabad"});
        stateCityMap.put("Punjab", new String[]{"Amritsar", "Ludhiana", "Patiala", "Jalandhar", "Bathinda"});
        stateCityMap.put("Gujarat", new String[]{"Ahmedabad", "Surat", "Vadodara", "Rajkot", "Bhavnagar"});
        stateCityMap.put("Karnataka", new String[]{"Bengaluru", "Mysore", "Hubli", "Mangalore", "Belgaum"});
        stateCityMap.put("Tamil Nadu", new String[]{"Chennai", "Coimbatore", "Madurai", "Tiruchirappalli", "Salem"});
        stateCityMap.put("Uttar Pradesh", new String[]{"Lucknow", "Kanpur", "Varanasi", "Agra", "Meerut"});

        String[] states = stateCityMap.keySet().toArray(new String[0]);
        ArrayAdapter<String> stateAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, states);
        stateSpinner.setAdapter(stateAdapter);

        stateSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String selectedState = stateSpinner.getSelectedItem().toString();
                ArrayAdapter<String> cityAdapter = new ArrayAdapter<>(FarmerDashboardActivity.this,
                        android.R.layout.simple_spinner_dropdown_item,
                        stateCityMap.get(selectedState));
                citySpinner.setAdapter(cityAdapter);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                citySpinner.setAdapter(null);
            }
        });
    }

    private void checkStoragePermission() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, STORAGE_PERMISSION_CODE);
        }
    }

    private void chooseImage() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent, "Select Product Image"), PICK_IMAGE_REQUEST);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK && data != null && data.getData() != null) {
            imageUri = data.getData();
            try {
                Bitmap bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), imageUri);
                productImageView.setImageBitmap(bitmap);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private void uploadProduct() {
        String name = productNameEditText.getText().toString().trim();
        String price = productPriceEditText.getText().toString().trim();
        String desc = productDescEditText.getText().toString().trim();
        String state = stateSpinner.getSelectedItem().toString();
        String city = citySpinner.getSelectedItem().toString();

        if (name.isEmpty() || price.isEmpty() || desc.isEmpty() || imageUri == null || state.isEmpty() || city.isEmpty()) {
            Snackbar.make(findViewById(android.R.id.content), "Please fill all fields and select image, state, and city.", Snackbar.LENGTH_SHORT).show();
            return;
        }

        ProgressDialog progressDialog = new ProgressDialog(this);
        progressDialog.setTitle("Uploading...");
        progressDialog.setCancelable(false);
        progressDialog.show();

        String fileName = "products/" + System.currentTimeMillis() + ".jpg";
        FirebaseStorage storage = FirebaseStorage.getInstance();
        StorageReference storageRef = storage.getReference().child(fileName);

        storageRef.putFile(imageUri)
                .addOnSuccessListener(taskSnapshot -> storageRef.getDownloadUrl()
                        .addOnSuccessListener(uri -> {
                            String imageUrl = uri.toString();
                            progressDialog.dismiss();
                            Snackbar.make(findViewById(android.R.id.content), "Product uploaded successfully!", Snackbar.LENGTH_SHORT).show();

                            Product product = new Product(name, price, desc, imageUrl, state, city);
                            databaseReference.push().setValue(product)
                                    .addOnSuccessListener(unused -> showSuccessDialog())
                                    .addOnFailureListener(e -> showErrorDialog("Database upload failed: " + e.getMessage()));
                        })
                        .addOnFailureListener(e -> {
                            progressDialog.dismiss();
                            showErrorDialog("Failed to get image URL: " + e.getMessage());
                        }))

                .addOnFailureListener(e -> {
                    progressDialog.dismiss();
                    showErrorDialog("Image upload failed: " + e.getMessage());
                });
    }

    private void showSuccessDialog() {
        new AlertDialog.Builder(this)
                .setTitle("Success")
                .setMessage("Image uploaded and product saved successfully!")
                .setPositiveButton("OK", (dialog, which) -> dialog.dismiss())
                .show();
    }

    private void showErrorDialog(String message) {
        new AlertDialog.Builder(this)
                .setTitle("Error")
                .setMessage(message)
                .setPositiveButton("OK", (dialog, which) -> dialog.dismiss())
                .show();
    }

    public static class Product {
        public String name, price, description, imageUrl, state, city;

        public Product() {}

        public Product(String name, String price, String description, String imageUrl, String state, String city) {
            this.name = name;
            this.price = price;
            this.description = description;
            this.imageUrl = imageUrl;
            this.state = state;
            this.city = city;
        }
    }
}
