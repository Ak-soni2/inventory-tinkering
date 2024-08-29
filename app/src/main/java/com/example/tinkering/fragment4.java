package com.example.tinkering;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class fragment4 extends Fragment {

    private RecyclerView recyclerView;
    private ItemAdapter itemAdapter;
    private List<Item> itemList;
    private DatabaseReference databaseReference;
    private Button buttonViewIssuedItem; // Declare the button
    private FirebaseAuth auth; // FirebaseAuth instance

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_fragment4, container, false);

        // Initialize FirebaseAuth
        auth = FirebaseAuth.getInstance();

        recyclerView = view.findViewById(R.id.recyclerViewCategories);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        itemList = new ArrayList<>();
        itemAdapter = new ItemAdapter(getContext(), itemList);
        recyclerView.setAdapter(itemAdapter);

        buttonViewIssuedItem = view.findViewById(R.id.buttonViewIssuedItem); // Initialize the button

        databaseReference = FirebaseDatabase.getInstance().getReference("items");
        fetchItemsFromDatabase();

        itemAdapter.setOnItemClickListener(new ItemAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(Item item) {
                Intent intent = new Intent(getActivity(), IssueItemActivity.class);
                intent.putExtra("ITEM_NAME", item.getName());
                startActivity(intent);
            }
        });

        buttonViewIssuedItem.setOnClickListener(new View.OnClickListener() { // Set OnClickListener for the button
            @Override
            public void onClick(View v) {
                // Get the current user's ID
                String userId = auth.getCurrentUser().getUid();

                Intent intent = new Intent(getActivity(), IssuedItemsActivity.class);
                intent.putExtra("USER_ID", userId); // Pass the user ID
                startActivity(intent);
            }
        });

        return view;
    }

    private void fetchItemsFromDatabase() {
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                itemList.clear();
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    Item item = snapshot.getValue(Item.class);
                    if (item != null) {
                        itemList.add(item);
                    }
                }
                itemAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                // Handle possible errors
            }
        });
    }
}
