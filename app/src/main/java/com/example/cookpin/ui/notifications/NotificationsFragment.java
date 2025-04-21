package com.example.cookpin.ui.notifications;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.app.PendingIntent;
import android.content.Intent;
import com.example.cookpin.MainActivity;
import com.example.cookpin.RecipeDetailActivity;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import com.example.cookpin.R;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;
import android.app.Notification;



public class NotificationsFragment extends Fragment {


    private final String CHANNEL_ID = "cookpin_channel";
    private final int NOTIFICATION_ID = 1;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_notifications, container, false);

        TextView textNotifications = root.findViewById(R.id.textNotifications);
        TextView notification2 = root.findViewById(R.id.notification2);

        textNotifications = root.findViewById(R.id.textNotifications);
        notification2.setText(R.string.notification2);

        textNotifications.setOnClickListener(view ->
                {
                    Intent intent = new Intent(getActivity(), RecipeDetailActivity.class);
                    intent.putExtra("recipeName", "Pizza");
                    startActivity(intent);
                });

        notification2.setOnClickListener(v ->
        {
            Intent intent = new Intent(getActivity(), MainActivity.class);
            intent.putExtra("fragment", "dashboard");
            startActivity(intent);
        });


        return root;
    }

    @SuppressLint("MissingPermission")
    private void sendCookPinNotification()
    {
        Intent intent = new Intent(getActivity(), RecipeDetailActivity.class);
        intent.putExtra("recipeTitle", "Pizza");
        intent.putExtra("recipeIngredients", "Flour, Water, Yeast, Tomato Sauce, Cheese");
        intent.putExtra("recipeInstructions", "Mix flour, water, and yeast.. roll the dough, bake with toppings at 375 degrees for 20-25 minutes.");
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);

        PendingIntent pendingIntent = PendingIntent.getActivity(getActivity(), 0, intent, PendingIntent.FLAG_IMMUTABLE);

        Notification notification = new NotificationCompat.Builder(requireContext(), CHANNEL_ID)
                .setSmallIcon(R.drawable.notificationsymbol)
                .setContentTitle("Cook & Pin")
                .setContentText("We found a new recipe you might enjoy")
                .setPriority(NotificationCompat.PRIORITY_DEFAULT)
                .setContentIntent(pendingIntent)
                .setAutoCancel(true)
                .build();

        NotificationManagerCompat notificationManager = NotificationManagerCompat.from(requireContext());
        notificationManager.notify(NOTIFICATION_ID, notification);
    }
}