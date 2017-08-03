package bkoruznjak.from.hr.musae.controller.player;

import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.media.AudioManager;
import android.os.Binder;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.support.v4.app.NotificationCompat;
import android.telephony.PhoneStateListener;
import android.telephony.TelephonyManager;

import java.lang.ref.WeakReference;

import bkoruznjak.from.hr.musae.R;
import bkoruznjak.from.hr.musae.views.activities.MainActivity;

/**
 * Created by bkoruznjak on 16/05/2017.
 */

public class MusicService extends Service implements AudioManager.OnAudioFocusChangeListener, PbPhoneStateListener {
    
    private final IBinder PLAYER_SERVICE = new MusicService.MusicPlayerBinder();
    private final int NOTIFICATION_ID = 8888;

    private NotificationCompat.Builder mNotificationBuilder;
    private AudioManager mAudioManager;
    private TelephonyManager mTelephonyManager;
    private PhoneStateHandler mPhoneStateHandler;
    private MusicPlayer musicPlayer;
    private PlayerStateModel mPlayerState;
    private int mStreamVolumeHolder = 0;

    public static Intent newIntent(Context context) {
        return new Intent(context, MusicService.class);
    }

    @Override
    public void onCreate() {
        super.onCreate();
        createNotification();

        musicPlayer = MusicPlayer.getInstance();

        mTelephonyManager = (TelephonyManager) getSystemService(TELEPHONY_SERVICE);
        mAudioManager = (AudioManager) getSystemService(Context.AUDIO_SERVICE);
        mStreamVolumeHolder = mAudioManager.getStreamVolume(AudioManager.STREAM_MUSIC);
        mPlayerState = new PlayerStateModel();

        requestFocus();
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
        abandonFocus();
    }

    private void requestFocus() {
        if (mAudioManager != null) {
            mAudioManager.requestAudioFocus(this, AudioManager.STREAM_MUSIC,
                    AudioManager.AUDIOFOCUS_GAIN);
        }

        if (mTelephonyManager != null) {
            mPhoneStateHandler = new PhoneStateHandler(new WeakReference<PbPhoneStateListener>(this));
            mTelephonyManager.listen(mPhoneStateHandler,
                    PhoneStateListener.LISTEN_CALL_STATE);
        }
    }

    private void abandonFocus() {
        if (mAudioManager != null) {
            mAudioManager.abandonAudioFocus(this);
        }

        if (mTelephonyManager != null) {
            mPhoneStateHandler = null;
            mTelephonyManager.listen(mPhoneStateHandler,
                    PhoneStateListener.LISTEN_CALL_STATE);
        }
    }

    @Override
    public void onAudioFocusChange(int focusChange) {
        switch (focusChange) {
            case AudioManager.AUDIOFOCUS_GAIN:
                if (mAudioManager != null) {
                    mAudioManager.setStreamVolume(AudioManager.STREAM_MUSIC,
                            mStreamVolumeHolder,
                            AudioManager.FLAG_PLAY_SOUND);
                }
                break;

            case AudioManager.AUDIOFOCUS_LOSS:
                break;

            case AudioManager.AUDIOFOCUS_LOSS_TRANSIENT:
                break;

            case AudioManager.AUDIOFOCUS_LOSS_TRANSIENT_CAN_DUCK:

                if (mAudioManager != null) {
                    mStreamVolumeHolder = mAudioManager.getStreamVolume(AudioManager.STREAM_MUSIC);
                    mAudioManager.setStreamVolume(AudioManager.STREAM_MUSIC,
                            1,
                            AudioManager.FLAG_PLAY_SOUND);
                }
                break;
            default:
                break;
        }
    }

    @Override
    public void phoneStateChanged(int phoneState) {
        if (mPlayerState != null) {
            switch (phoneState) {
                case TelephonyManager.CALL_STATE_IDLE:
                    if (mPlayerState.wasMusicInterrupted() && musicPlayer != null) {
                        mPlayerState.setMusicInterrupted(false);
                        musicPlayer.play();
                    }
                    break;
                case TelephonyManager.CALL_STATE_RINGING:
                    if (mPlayerState.playWhenReady() && musicPlayer != null) {
                        mPlayerState.setMusicInterrupted(true);
                        musicPlayer.pause();
                    }
                    break;
                case TelephonyManager.CALL_STATE_OFFHOOK:
                    if (mPlayerState.playWhenReady() && musicPlayer != null) {
                        mPlayerState.setMusicInterrupted(true);
                        musicPlayer.pause();
                    }
                    break;
                default:
                    break;
            }
        }
    }

    public PlayerStateModel getPlayerState() {
        return mPlayerState;
    }

    public void setPlayerState(PlayerStateModel playerState) {
        this.mPlayerState = mPlayerState;
    }

    public class MusicPlayerBinder extends Binder {
        public MusicService getServiceInstance() {
            return MusicService.this;
        }
    }


}
