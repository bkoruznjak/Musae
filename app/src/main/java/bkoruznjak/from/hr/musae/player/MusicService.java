package bkoruznjak.from.hr.musae.player;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.Binder;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.support.v4.app.NotificationCompat;

import bkoruznjak.from.hr.musae.R;
import bkoruznjak.from.hr.musae.views.MainActivity;

/**
 * Created by bkoruznjak on 16/05/2017.
 */

public class MusicService extends Service {
    
    private final IBinder PLAYER_SERVICE = new MusicService.MusicPlayerBinder();
    private final int NOTIFICATION_ID = 8888;

    private NotificationManager mNotificationManager;
    private NotificationCompat.Builder mNotificationBuilder;

    @Override
    public void onCreate() {
        super.onCreate();
        createNotification();
    }

    private void createNotification() {
        Intent resultIntent = new Intent(this, MainActivity.class);
        resultIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP |
                Intent.FLAG_ACTIVITY_SINGLE_TOP);
        PendingIntent resultPendingIntent =
                PendingIntent.getActivity(
                        this,
                        0,
                        resultIntent,
                        0
                );
        mNotificationManager =
                (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        mNotificationBuilder =
                new NotificationCompat.Builder(this)
                        .setContentTitle(getString(R.string.notification_title))
                        .setSmallIcon(R.drawable.ic_musae_notification)
                        .setOngoing(true);
        mNotificationBuilder.setContentIntent(resultPendingIntent);
        startForeground(NOTIFICATION_ID, mNotificationBuilder.build());
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        return START_STICKY;
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return PLAYER_SERVICE;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    public class MusicPlayerBinder extends Binder {
        public MusicService getServiceInstance() {
            return MusicService.this;
        }
    }


}
