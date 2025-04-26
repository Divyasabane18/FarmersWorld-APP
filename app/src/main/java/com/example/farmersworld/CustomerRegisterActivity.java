package com.example.farmersworld;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class CustomerRegisterActivity extends AppCompatActivity {

    EditText editTextName, editTextState, editTextCity, editTextRegisterEmail, editTextRegisterPassword;
    Button buttonRegister;

    FirebaseAuth mAuth;
    DatabaseReference customersRef;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_customer_register);

        editTextName = findViewById(R.id.editTextName);
        editTextState = findViewById(R.id.editTextState);
        editTextCity = findViewById(R.id.editTextCity);
        editTextRegisterEmail = findViewById(R.id.editTextRegisterEmail);
        editTextRegisterPassword = findViewById(R.id.editTextRegisterPassword);
        buttonRegister = findViewById(R.id.buttonRegister);

        mAuth = FirebaseAuth.getInstance();
        customersRef = FirebaseDatabase.getInstance().getReference("Customers");

        buttonRegister.setOnClickListener(v -> registerCustomer());
    }

    private void registerCustomer() {
        String name = editTextName.getText().toString().trim();
        String state = editTextState.getText().toString().trim();
        String city = editTextCity.getText().toString().trim();
        String email = editTextRegisterEmail.getText().toString().trim();
        String password = editTextRegisterPassword.getText().toString().trim();

        if (TextUtils.isEmpty(name) || TextUtils.isEmpty(state) || TextUtils.isEmpty(city) ||
                TextUtils.isEmpty(email) || TextUtils.isEmpty(password)) {
            Toast.makeText(this, "Please fill in all fields", Toast.LENGTH_SHORT).show();
            return;
        }

        if (password.length() < 6) {
            Toast.makeText(this, "Password must be at least 6 characters", Toast.LENGTH_SHORT).show();
            return;
        }

        mAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        FirebaseUser customer = mAuth.getCurrentUser();
                        if (customer != null) {
                            String customerId = customer.getUid();

                            Customer customerData = new Customer(name, state, city, email);
                            customersRef.child(customerId).setValue(customerData)
                                    .addOnCompleteListener(dbTask -> {
                                        if (dbTask.isSuccessful()) {
                                            Toast.makeText(this, "Registered successfully", Toast.LENGTH_SHORT).show();
                                            startActivity(new Intent(CustomerRegisterActivity.this, CustomerLoginActivity.class));
                                            finish();
                                        } else {
                                            Toast.makeText(this, "Failed to save data: " + dbTask.getException().getMessage(), Toast.LENGTH_SHORT).show();
                                        }
                                    });
                        }
                    } else {
                        String errorMessage = task.getException() != null ? task.getException().getMessage() : "Registration failed";
                        if (errorMessage.toLowerCase().contains("email address is already in use")) {
                            Toast.makeText(this, "Email already registered. Please login instead.", Toast.LENGTH_LONG).show();
                        } else {
                            Toast.makeText(this, "Registration failed: " + errorMessage, Toast.LENGTH_LONG).show();
                        }
                    }
                });
    }

    public static class Customer {
        public String name, state, city, email;

        public Customer() {} // Required for Firebase

        public Customer(String name, String state, String city, String email) {
            this.name = name;
            this.state = state;
            this.city = city;
            this.email = email;
        }
    }
}
