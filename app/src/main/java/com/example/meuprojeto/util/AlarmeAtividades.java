package com.example.meuprojeto.util;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

import com.example.meuprojeto.R;
import com.example.meuprojeto.controller.MinhaRotinaActivity;

public class AlarmeAtividades extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
        Intent i = new Intent(context, MinhaRotinaActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        PendingIntent pendingIntent = PendingIntent.getActivity(context,0 ,i, 0);
        Log.e("Alarme", "notificação alarme");

        NotificationCompat.Builder builder = new NotificationCompat.Builder(context, "atividadeAlerta")
                .setSmallIcon(R.drawable.ic_launcher_background)
                .setContentTitle("Alerta Atividade")
                .setContentText("Começou a atividade")
                .setAutoCancel(true)
                .setDefaults(NotificationCompat.DEFAULT_ALL)
                .setPriority(NotificationCompat.PRIORITY_HIGH)
                .setContentIntent(pendingIntent);

        NotificationManagerCompat notificationManagerCompat = NotificationManagerCompat.from(context);
        notificationManagerCompat.notify(123, builder.build());

    }
}
