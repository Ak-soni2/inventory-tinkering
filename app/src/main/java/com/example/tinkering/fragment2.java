package com.example.tinkering;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class fragment2 extends Fragment {

    private ListView listView;
    private TextView noProjectsTextView;
    private ProjectListAdapter adapter;
    private ArrayList<Project> projects;

    public fragment2() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_fragment2, container, false);
        listView = view.findViewById(R.id.listView);
        noProjectsTextView = view.findViewById(R.id.noProjectsTextView);
        projects = new ArrayList<>();
        adapter = new ProjectListAdapter(getContext(), projects);
        listView.setAdapter(adapter);

        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("Projects/pdf");
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                projects.clear();
                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                    Project project = dataSnapshot.getValue(Project.class);
                    if (project != null) {
                        projects.add(project);
                    }
                }
                adapter.notifyDataSetChanged();

                // Toggle visibility based on whether there are projects
                if (projects.isEmpty()) {
                    noProjectsTextView.setVisibility(View.VISIBLE);
                    listView.setVisibility(View.GONE);
                } else {
                    noProjectsTextView.setVisibility(View.GONE);
                    listView.setVisibility(View.VISIBLE);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(getContext(), "Failed to load data", Toast.LENGTH_SHORT).show();
            }
        });

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String pdfUrl = projects.get(position).getPdfUrl();
                Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(pdfUrl));
                startActivity(intent);
            }
        });

        return view;
    }
}
