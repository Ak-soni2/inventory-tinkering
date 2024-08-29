package com.example.tinkering;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.UUID;

public class IssueItemActivity extends AppCompatActivity {

    private EditText editTextItemName;
    private EditText editTextIssuedBy;
    private EditText editTextDescription;
    private EditText editTextQuantity;
    private Button buttonSubmit;
    private DatabaseReference databaseReference;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_issue_item);

        editTextItemName = findViewById(R.id.editTextItemName);
        editTextIssuedBy = findViewById(R.id.editTextIssuedBy);
        editTextDescription = findViewById(R.id.editTextDescription);
        editTextQuantity = findViewById(R.id.editTextQuantity);
        buttonSubmit = findViewById(R.id.buttonSubmit);

        // Initialize Firebase Realtime Database reference
        databaseReference = FirebaseDatabase.getInstance().getReference();

        // Get item name from Intent
        Intent intent = getIntent();
        String itemName = intent.getStringExtra("ITEM_NAME");
        String issuedBy = getUserEmail(); // Method to get user email

        // Set item name and issued by fields
        editTextItemName.setText(itemName);
        editTextIssuedBy.setText(issuedBy);

        buttonSubmit.setOnClickListener(v -> checkItemQuantityAndSubmit());
    }

    private String getUserEmail() {
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        return user != null ? user.getEmail() : "unknown";
    }

    private void checkItemQuantityAndSubmit() {
        String itemName = editTextItemName.getText().toString();
        String quantityStr = editTextQuantity.getText().toString();

        int quantity;
        try {
            quantity = Integer.parseInt(quantityStr);
            if (quantity > 5) {
                Toast.makeText(IssueItemActivity.this, "You cannot issue more than 5 pieces per item at once.", Toast.LENGTH_SHORT).show();
                return;
            }
        } catch (NumberFormatException e) {
            Toast.makeText(IssueItemActivity.this, "Please enter a valid quantity.", Toast.LENGTH_SHORT).show();
            return;
        }

        // Check if the user has already issued 5 different types of items
        DatabaseReference userRef = databaseReference.child("users").child(FirebaseAuth.getInstance().getCurrentUser().getUid());

        userRef.child("issuedItems").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.getChildrenCount() >= 5) {
                    Toast.makeText(IssueItemActivity.this, "You cannot issue more than 5 different types of items.", Toast.LENGTH_SHORT).show();
                    return;
                }

                // Check item availability
                DatabaseReference itemsRef = databaseReference.child("items");
                itemsRef.orderByChild("name").equalTo(itemName).addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        if (dataSnapshot.exists()) {
                            // Assuming there is only one item with this name
                            DataSnapshot itemSnapshot = dataSnapshot.getChildren().iterator().next();
                            Integer availableQuantity = itemSnapshot.child("quantity").getValue(Integer.class);

                            if (availableQuantity == null) {
                                Toast.makeText(IssueItemActivity.this, "Failed to retrieve item quantity.", Toast.LENGTH_SHORT).show();
                                return;
                            }

                            if (quantity > availableQuantity) {
                                Toast.makeText(IssueItemActivity.this, "Requested quantity exceeds available quantity.", Toast.LENGTH_SHORT).show();
                            } else {
                                // Proceed with issuing the item
                                String itemUID = UUID.randomUUID().toString();
                                String time = new SimpleDateFormat("HH:mm:ss", Locale.getDefault()).format(new Date());
                                String date = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(new Date());

                                // Create a map to store the item details
                                Map<String, Object> itemDetails = new HashMap<>();
                                itemDetails.put("date", date);
                                itemDetails.put("description", editTextDescription.getText().toString());
                                itemDetails.put("issuedBy", editTextIssuedBy.getText().toString());
                                itemDetails.put("itemName", itemName);
                                itemDetails.put("quantity", quantity);
                                itemDetails.put("status", "pending");
                                itemDetails.put("time", time);

                                // Store user email and issued item details
                                Map<String, Object> userUpdates = new HashMap<>();
                                userUpdates.put("email", editTextIssuedBy.getText().toString());
                                userUpdates.put("issuedItems/" + itemUID, itemDetails);

                                userRef.updateChildren(userUpdates)
                                        .addOnCompleteListener(task -> {
                                            if (task.isSuccessful()) {
                                                Toast.makeText(IssueItemActivity.this, "Item issued successfully.", Toast.LENGTH_SHORT).show();
                                                finish();
                                            } else {
                                                Toast.makeText(IssueItemActivity.this, "Failed to submit item.", Toast.LENGTH_SHORT).show();
                                            }
                                        });
                            }
                        } else {
                            Toast.makeText(IssueItemActivity.this, "Item not found in database.", Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {
                        Toast.makeText(IssueItemActivity.this, "Failed to check item availability.", Toast.LENGTH_SHORT).show();
                    }
                });
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Toast.makeText(IssueItemActivity.this, "Failed to check issued items count.", Toast.LENGTH_SHORT).show();
            }
        });
    }
}
