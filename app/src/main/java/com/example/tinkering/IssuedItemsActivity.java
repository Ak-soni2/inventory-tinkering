package com.example.tinkering;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class IssuedItemsActivity extends AppCompatActivity {

    private ListView issuedItemsListView;
    private ArrayAdapter<String> issuedItemsAdapter;
    private ArrayList<String> issuedItemsList;
    private ArrayList<String> issuedItemIds; // Store issued item IDs
    private ArrayList<String> issuedItemStatuses; // Store issued item statuses
    private String userId;
    private TextView noItemsTextView; // TextView to display no items message

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_issued_items);

        issuedItemsListView = findViewById(R.id.issuedItemsListView);
        noItemsTextView = findViewById(R.id.noItemsTextView); // Initialize the TextView
        issuedItemsList = new ArrayList<>();
        issuedItemIds = new ArrayList<>(); // Initialize the list for item IDs
        issuedItemStatuses = new ArrayList<>(); // Initialize the list for item statuses
        issuedItemsAdapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, issuedItemsList);
        issuedItemsListView.setAdapter(issuedItemsAdapter);

        userId = getIntent().getStringExtra("USER_ID");

        if (userId == null || userId.isEmpty()) {
            Log.e("IssuedItemsActivity", "User ID is null or empty");
            Toast.makeText(this, "Failed to load issued items.", Toast.LENGTH_SHORT).show();
            finish();
            return;
        }

        Log.d("IssuedItemsActivity", "User ID: " + userId);

        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("users").child(userId).child("issuedItems");

        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                issuedItemsList.clear();
                issuedItemIds.clear(); // Clear the list of IDs
                issuedItemStatuses.clear(); // Clear the list of statuses

                if (!dataSnapshot.exists()) {
                    // If no items exist, show the message and hide the list
                    noItemsTextView.setVisibility(View.VISIBLE);
                    issuedItemsListView.setVisibility(View.GONE);
                } else {
                    noItemsTextView.setVisibility(View.GONE); // Hide the message if items exist
                    issuedItemsListView.setVisibility(View.VISIBLE); // Show the list

                    for (DataSnapshot itemSnapshot : dataSnapshot.getChildren()) {
                        String itemId = itemSnapshot.getKey(); // Get the item ID
                        String itemName = itemSnapshot.child("itemName").getValue(String.class);
                        String issuedBy = itemSnapshot.child("issuedBy").getValue(String.class);
                        Long quantity = itemSnapshot.child("quantity").getValue(Long.class);
                        String description = itemSnapshot.child("description").getValue(String.class);
                        String status = itemSnapshot.child("status").getValue(String.class);
                        String date = itemSnapshot.child("date").getValue(String.class);
                        String time = itemSnapshot.child("time").getValue(String.class);

                        String itemDetails = "Item Name: " + itemName + "\n" +
                                "Issued By: " + issuedBy + "\n" +
                                "Quantity: " + (quantity != null ? quantity : 0) + "\n" +
                                "Description: " + description + "\n" +
                                "Status: " + status + "\n" +
                                "Date: " + date + "\n" +
                                "Time: " + time + "\n";
                        issuedItemsList.add(itemDetails);
                        issuedItemIds.add(itemId); // Store the item ID
                        issuedItemStatuses.add(status); // Store the item status
                    }
                }
                issuedItemsAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Log.e("FirebaseError", databaseError.getMessage());
                Toast.makeText(IssuedItemsActivity.this, "Failed to load issued items.", Toast.LENGTH_SHORT).show();
            }
        });

        // Set OnItemClickListener to show dialog on item click
        issuedItemsListView.setOnItemClickListener((parent, view, position, id) -> {
            String selectedItemId = issuedItemIds.get(position); // Get the item ID
            String selectedItemStatus = issuedItemStatuses.get(position); // Get the item status
            if ("approved".equalsIgnoreCase(selectedItemStatus)) {
                showReturnDialog(selectedItemId); // Show return dialog only if status is approved
            } else {
                Toast.makeText(this, "Item cannot be returned because it is not approved.", Toast.LENGTH_SHORT).show();
            }
        });
    }

    // Method to show the return dialog
    private void showReturnDialog(String itemId) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Return Item");
        builder.setMessage("Do you want to return this item?");

        builder.setPositiveButton("Return", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                // Update item status and date/time in Firebase
                updateItemStatus(itemId);
            }
        });

        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });

        builder.show();
    }

    // Method to update item status and date/time in Firebase
    private void updateItemStatus(String itemId) {
        DatabaseReference itemRef = FirebaseDatabase.getInstance().getReference("users").child(userId).child("issuedItems").child(itemId);

        String currentDate = new SimpleDateFormat("yyyy-MM-dd").format(new Date());
        String currentTime = new SimpleDateFormat("HH:mm:ss").format(new Date());

        itemRef.child("status").setValue("Applied Return");
        itemRef.child("date").setValue(currentDate);
        itemRef.child("time").setValue(currentTime)
                .addOnSuccessListener(aVoid -> {
                    Toast.makeText(IssuedItemsActivity.this, "Item returned successfully.", Toast.LENGTH_SHORT).show();
                })
                .addOnFailureListener(e -> {
                    Log.e("FirebaseError", e.getMessage());
                    Toast.makeText(IssuedItemsActivity.this, "Failed to update item status.", Toast.LENGTH_SHORT).show();
                });
    }
}
