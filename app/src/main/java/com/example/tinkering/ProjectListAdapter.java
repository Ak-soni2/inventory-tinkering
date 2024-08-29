package com.example.tinkering;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.util.ArrayList;

public class ProjectListAdapter extends ArrayAdapter<Project> {
    private Context context;
    private ArrayList<Project> projects;

    public ProjectListAdapter(@NonNull Context context, ArrayList<Project> projects) {
        super(context, R.layout.list_item_project, projects);
        this.context = context;
        this.projects = projects;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        if (convertView == null) {
            convertView = LayoutInflater.from(context).inflate(R.layout.list_item_project, parent, false);
        }

        TextView titleTextView = convertView.findViewById(R.id.projectTitleTextView);
        TextView statusTextView = convertView.findViewById(R.id.projectStatusTextView);

        Project project = projects.get(position);
        titleTextView.setText(project.getProjectTitle());
        statusTextView.setText("Status: " + project.getStatus());

        return convertView;
    }
}
