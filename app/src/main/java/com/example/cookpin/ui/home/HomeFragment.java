package com.example.cookpin.ui.home;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.example.cookpin.R;

public class HomeFragment extends Fragment {

    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View root = inflater.inflate(R.layout.fragment_home, container, false);

        TextView textHome = root.findViewById(R.id.textHome);
        TextView sayingTextView = root.findViewById(R.id.sayingTextView);

        textHome.setText(R.string.appName);
        sayingTextView.setText(R.string.saying);

        return root;
    }
}