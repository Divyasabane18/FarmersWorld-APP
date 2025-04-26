package com.example.farmersworld;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.*;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.database.FirebaseDatabase;
import android.widget.EditText;
import java.util.ArrayList;

public class FarmerAdapter extends RecyclerView.Adapter<FarmerAdapter.FarmerViewHolder> {

    private final Context context;
    private final ArrayList<Farmer> farmerList;

    public FarmerAdapter(Context context, ArrayList<Farmer> farmerList) {
        this.context = context;
        this.farmerList = farmerList;
    }

    @NonNull
    @Override
    public FarmerViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_farmer, parent, false);
        return new FarmerViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull FarmerViewHolder holder, int position) {
        Farmer farmer = farmerList.get(position);

        holder.nameText.setText("Name: " + farmer.getName());
        holder.aadharText.setText("Aadhar: " + farmer.getAadhar());
        holder.stateText.setText("State: " + farmer.getState());
        holder.cityText.setText("City: " + farmer.getCity());
        holder.emailText.setText("Email: " + farmer.getEmail());
        holder.mobileNumberText.setText("Mobile: " + farmer.getMobileNumber());  // 🔹 Added mobile number display

        if (farmer.isVerified()) {
            holder.verifyButton.setText("Verified");
            holder.verifyButton.setEnabled(false);
            holder.verifyButton.setBackgroundColor(0xFF4CAF50); // Green
        } else {
            holder.verifyButton.setText("Verify");
            holder.verifyButton.setEnabled(true);
            holder.verifyButton.setBackgroundColor(0xFF2196F3); // Blue
            holder.verifyButton.setOnClickListener(v -> verifyFarmer(farmer, position));
        }
    }

    private void verifyFarmer(Farmer farmer, int position) {
        FirebaseDatabase.getInstance().getReference("Farmers")
                .child(farmer.getUid())
                .child("isVerified")
                .setValue(true)
                .addOnSuccessListener(unused -> {
                    Toast.makeText(context, "Farmer Verified!", Toast.LENGTH_SHORT).show();
                    farmer.setVerified(true);
                    notifyItemChanged(position);
                })
                .addOnFailureListener(e ->
                        Toast.makeText(context, "Failed: " + e.getMessage(), Toast.LENGTH_SHORT).show());
    }

    @Override
    public int getItemCount() {
        return farmerList.size();
    }

    public static class FarmerViewHolder extends RecyclerView.ViewHolder {
        TextView nameText, aadharText, stateText, cityText, emailText, mobileNumberText;  // 🔹 Added mobile number TextView
        Button verifyButton;

        public FarmerViewHolder(@NonNull View itemView) {
            super(itemView);
            nameText = itemView.findViewById(R.id.textFarmerName);
            aadharText = itemView.findViewById(R.id.textFarmerAadhar);
            stateText = itemView.findViewById(R.id.textFarmerState);
            cityText = itemView.findViewById(R.id.textFarmerCity);
            emailText = itemView.findViewById(R.id.textFarmerEmail);
            mobileNumberText = itemView.findViewById(R.id.textFarmerMobileNumber);  // 🔹 Added mobile number view
            verifyButton = itemView.findViewById(R.id.buttonVerify);
        }
    }
}
