package app.unsimpledev.appnotificaciones;


import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;

import androidx.annotation.NonNull;
import androidx.core.app.NotificationCompat;

import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

public class MyFirebaseMessagingService extends FirebaseMessagingService {

    private static final String TAG = "MyFirebaseMsgService";

    private static final String NOTIFICATION_CHANNEL_ID = "app.notificaciones.channel";

    @Override
    public void onMessageReceived(@NonNull RemoteMessage message) {

        String titulo = message.getData().get("title");
        String descripcion = message.getData().get("body");
        String linkUrl = message.getData().get("linkurl");

        NotificationChannel notificationChannel = new NotificationChannel(NOTIFICATION_CHANNEL_ID, NOTIFICATION_CHANNEL_ID+"_name", NotificationManager.IMPORTANCE_HIGH);
        NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        notificationManager.createNotificationChannel(notificationChannel);

        NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(this,NOTIFICATION_CHANNEL_ID)
                .setContentTitle(titulo)
                .setContentText(descripcion)
                .setAutoCancel(true)
                .setSmallIcon(R.drawable.ic_launcher_foreground)
                .setContentIntent(onclick(linkUrl));

        notificationManager.notify(0,notificationBuilder.build());

    }

    private PendingIntent onclick(String linkUrl) {
        if (linkUrl!=null && !linkUrl.trim().isEmpty()){
            return onClickAbrirNavegador(linkUrl);
        }
        return onclickAbrirApp();
    }

    private PendingIntent onclickAbrirApp() {
        Intent intent = new Intent(this,MainActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
        return PendingIntent.getActivity(this,0,intent,PendingIntent.FLAG_IMMUTABLE);
    }

    private PendingIntent onClickAbrirNavegador(String linkUrl) {
        Intent intent = new Intent(Intent.ACTION_VIEW);
        intent.setData(Uri.parse(linkUrl));
        return PendingIntent.getActivity(this, 0, intent,PendingIntent.FLAG_IMMUTABLE);
    }


}