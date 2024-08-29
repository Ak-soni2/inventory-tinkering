package com.example.tinkering;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.viewpager.widget.ViewPager;

import com.google.android.material.tabs.TabLayout;
import com.google.firebase.FirebaseApp;
import com.google.firebase.auth.FirebaseAuth;

public class MainActivity extends AppCompatActivity {

    private TabLayout tabLayout;
    private ViewPager viewPager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        FirebaseApp.initializeApp(this);
        setContentView(R.layout.activity_main);

        // Initialize views
        tabLayout = findViewById(R.id.tab_layout);
        viewPager = findViewById(R.id.viewhomepage);

        // Create adapter for ViewPager
        VPagerAdapter vPagerAdapter = new VPagerAdapter(getSupportFragmentManager(),
                FragmentPagerAdapter.BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT);
        SharedPreferences sharedPreferences = getSharedPreferences("user_details", Context.MODE_PRIVATE);
        vPagerAdapter.addFragment(new fragment1(), "Home");
        vPagerAdapter.addFragment(new fragment2(), "Projects");
//        vPagerAdapter.addFragment(new fragment3(), "Events");
        vPagerAdapter.addFragment(new fragment4(), "Inventory");
        vPagerAdapter.addFragment(new Fragment5(), "About Us");

        // Set adapter to ViewPager
        viewPager.setAdapter(vPagerAdapter);

        // Link TabLayout with ViewPager
        tabLayout.setupWithViewPager(viewPager);

        // Apply window insets (if needed)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        // Profile ImageButton click listener
        ImageButton profileButton = findViewById(R.id.profileButton);
        profileButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Handle Log Out button click
                // Clear user details from SharedPreferences
                SharedPreferences.Editor editor = sharedPreferences.edit();
                editor.clear();
                editor.apply();

                // Sign out from Firebase
                FirebaseAuth.getInstance().signOut();

                // Navigate to LoginActivity
                Intent intent = new Intent(MainActivity.this, LoginActivity.class);
                startActivity(intent);
                finish();
            }
        });
    }

    // Handle Instagram and LinkedIn intents
    public void openInstagramAkshay(View view) {
        Intent intent = new Intent(Intent.ACTION_VIEW);
        intent.setData(Uri.parse("https://www.instagram.com/akshay"));
        startActivity(intent);
    }

    public void openInstagramRaditya(View view) {
        Intent intent = new Intent(Intent.ACTION_VIEW);
        intent.setData(Uri.parse("https://www.instagram.com/raditya"));
        startActivity(intent);
    }

    public void openLinkedinAkshay(View view) {
        Intent intent = new Intent(Intent.ACTION_VIEW);
        intent.setData(Uri.parse("https://www.linkedin.com/in/akshay"));
        startActivity(intent);
    }

    public void openLinkedinRaditya(View view) {
        Intent intent = new Intent(Intent.ACTION_VIEW);
        intent.setData(Uri.parse("https://www.linkedin.com/in/raditya"));
        startActivity(intent);
    }
}
