package com.example.tinkering;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.fragment.app.Fragment;

public class Fragment5 extends Fragment {

    private ImageView imageViewInstagramAkshay, imageViewInstagramRaditya,
            imageViewLinkedinAkshay, imageViewLinkedinRaditya;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_fragment5, container, false);

//        // Initialize Instagram ImageViews
        imageViewInstagramAkshay = rootView.findViewById(R.id.instagramid_ak);
        imageViewInstagramRaditya = rootView.findViewById(R.id.instagramid_rd);
//
//        // Initialize LinkedIn ImageViews
        imageViewLinkedinAkshay = rootView.findViewById(R.id.linkedinid_ak);
        imageViewLinkedinRaditya = rootView.findViewById(R.id.linkedinid_rd);

        // Set click listeners for Instagram images
        imageViewInstagramAkshay.setOnClickListener(v -> openInstagram("ak_soni2"));
        imageViewInstagramRaditya.setOnClickListener(v -> openInstagram("rizzdityyaa"));

        // Set click listeners for LinkedIn images
        imageViewLinkedinAkshay.setOnClickListener(v -> openLinkedIn("akshay-verma-1b596b281"));
        imageViewLinkedinRaditya.setOnClickListener(v -> openLinkedIn("raditya-saraf-3707a5231"));

        return rootView;
    }

    private void openInstagram(String username) {
        String url = "https://www.instagram.com/" + username + "/";
        Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
        startActivity(intent);
    }

    private void openLinkedIn(String username) {
        String url = "https://www.linkedin.com/in/" + username + "/";
        Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
        startActivity(intent);
    }
}
