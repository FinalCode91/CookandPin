package com.example.cookpin.ui.notifications;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import com.example.cookpin.R;

import com.example.cookpin.databinding.FragmentNotificationsBinding;

public class NotificationsFragment extends Fragment {


    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_notifications, container, false);

        TextView textNotifications = root.findViewById(R.id.textNotifications);
        TextView notification2 = root.findViewById(R.id.notification2);

        textNotifications.setText(R.string.textNotifications);
        notification2.setText(R.string.notification2);

        return root;
    }
}