package com.example.farmersworld;

import android.content.Intent;
import android.os.Bundle;
import android.util.Patterns;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;

public class FarmerRegisterActivity extends AppCompatActivity {

    EditText nameEditText, aadharEditText, stateEditText, cityEditText, emailEditText, passwordEditText;
    Button registerButton;

    FirebaseAuth firebaseAuth;
    DatabaseReference farmerDatabase;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_farmer_register);

        nameEditText = findViewById(R.id.editTextName);
        aadharEditText = findViewById(R.id.editTextAadhar);
        stateEditText = findViewById(R.id.editTextState);
        cityEditText = findViewById(R.id.editTextCity);
        emailEditText = findViewById(R.id.editTextRegisterEmail);
        passwordEditText = findViewById(R.id.editTextRegisterPassword);
        registerButton = findViewById(R.id.buttonRegister);

        firebaseAuth = FirebaseAuth.getInstance();
        farmerDatabase = FirebaseDatabase.getInstance().getReference("Farmers");

        registerButton.setOnClickListener(v -> registerFarmer());
    }

    private void registerFarmer() {
        String name = nameEditText.getText().toString().trim();
        String aadhar = aadharEditText.getText().toString().trim();
        String state = stateEditText.getText().toString().trim();
        String city = cityEditText.getText().toString().trim();
        String email = emailEditText.getText().toString().trim();
        String password = passwordEditText.getText().toString().trim();

        if (name.isEmpty() || aadhar.isEmpty() || state.isEmpty() || city.isEmpty() || email.isEmpty() || password.isEmpty()) {
            Toast.makeText(this, "Please fill in all fields", Toast.LENGTH_SHORT).show();
            return;
        }

        if (!aadhar.matches("\\d{12}")) {
            Toast.makeText(this, "Aadhar must be a 12-digit number", Toast.LENGTH_SHORT).show();
            return;
        }

        if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            Toast.makeText(this, "Invalid email format", Toast.LENGTH_SHORT).show();
            return;
        }

        if (password.length() < 6) {
            Toast.makeText(this, "Password must be at least 6 characters", Toast.LENGTH_SHORT).show();
            return;
        }

        firebaseAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        FirebaseUser user = firebaseAuth.getCurrentUser();
                        if (user != null) {
                            String uid = user.getUid(); // This is the Farmer ID

                            HashMap<String, Object> farmerInfo = new HashMap<>();
                            farmerInfo.put("farmerId", uid);
                            farmerInfo.put("name", name);
                            farmerInfo.put("aadhar", aadhar);
                            farmerInfo.put("state", state);
                            farmerInfo.put("city", city);
                            farmerInfo.put("email", email);
                            farmerInfo.put("isVerified", true); // We are setting this to true immediately after registration

                            farmerDatabase.child(uid).setValue(farmerInfo)
                                    .addOnSuccessListener(unused -> {
                                        Toast.makeText(this, "Registration successful!", Toast.LENGTH_LONG).show();
                                        // Navigate to Farmer Dashboard instead of OTP verification
                                        Intent intent = new Intent(FarmerRegisterActivity.this, FarmerDashboardActivity.class);
                                        startActivity(intent);
                                        finish();
                                    })
                                    .addOnFailureListener(e -> {
                                        Toast.makeText(this, "Failed to save farmer info: " + e.getMessage(), Toast.LENGTH_LONG).show();
                                    });
                        }
                    } else {
                        Toast.makeText(this, "Registration Failed: " + task.getException().getMessage(), Toast.LENGTH_LONG).show();
                    }
                });
    }
}
