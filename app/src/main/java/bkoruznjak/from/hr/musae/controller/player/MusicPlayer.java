package bkoruznjak.from.hr.musae.controller.player;

import android.media.AudioManager;
import android.media.MediaPlayer;
import android.support.annotation.NonNull;
import android.util.Log;

import java.io.IOException;
import java.util.List;

import bkoruznjak.from.hr.musae.views.songs.SongModel;

/**
 * Created by bkoruznjak on 12/05/2017.
 */

public class MusicPlayer implements Controlable, Deejay, Watchable, MediaPlayer.OnCompletionListener, MediaPlayer.OnPreparedListener, MediaPlayer.OnErrorListener {

    private static MusicPlayer INSTANCE;
    public final int AUDIO_SESSION_ID;
    private MediaPlayer mediaPlayer;
    private List<SongModel> musicList;
    private SongModel mCurrentSong;
    private boolean hasSetBeenSet;
    private boolean hasPlayerEverBeenStarted;
    private boolean mShuffle;

    private MusicPlayer() {
        //override the public one
        mediaPlayer = new MediaPlayer();
        AUDIO_SESSION_ID = mediaPlayer.getAudioSessionId();
        mediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
    }

    public static MusicPlayer getInstance() {
        if (INSTANCE == null) {
            INSTANCE = new MusicPlayer();
        }
        return INSTANCE;
    }

    /**
     * Called when we want to replace the music list from the player
     * This should be set before prepare is called otherwise drama happens
     *
     * @param musicList must not be null
     */
    @Override
    public void prepareSet(@NonNull List<SongModel> musicList) {
        this.musicList = musicList;

        if (!hasSetBeenSet) {
            hasSetBeenSet = true;
            mCurrentSong = musicList.get(0);
            prepareSong(mCurrentSong.getSongIndex());
        }
    }

    /**
     * Prepare song to play
     * <p>
     * In case of the player already playing music will just switch song and continue playing.
     */
    @Override
    public void prepareSong(int index) {
        if (musicList == null) {
            throw new IllegalStateException("music list is empty");
        }

        if (musicList.size() == 0) {
            throw new IllegalStateException("no music to play");
        }

        stop();
        mCurrentSong = musicList.get(index);
        try {
            mediaPlayer.setDataSource(mCurrentSong.getFileUri());
            mediaPlayer.prepare();
            if (hasPlayerEverBeenStarted) {
                mediaPlayer.start();
            }
        } catch (IOException ioEx) {
            Log.e("musae", "Error loading data source:" + ioEx.toString());
        }
    }

    @Override
    public void play() {
        hasPlayerEverBeenStarted = true;
        mediaPlayer.start();
    }

    @Override
    public void pause() {
        mediaPlayer.pause();
    }

    /**
     * stops the playback and rollbacks to start
     */
    @Override
    public void stop() {
        pause();
        mediaPlayer.seekTo(0);
        mediaPlayer.stop();
        mediaPlayer.reset();
    }

    @Override
    public void next() {
        if (mCurrentSong.getSongIndex() + 1 < musicList.size()) {
            prepareSong(mCurrentSong.getSongIndex() + 1);
        } else {
            prepareSong(0);
        }

    }

    @Override
    public void previous() {
        if (mCurrentSong.getSongIndex() - 1 >= 0) {
            prepareSong(mCurrentSong.getSongIndex() - 1);
        } else {
            prepareSong(musicList.size() - 1);
        }
    }

    @Override
    public void volume(boolean crankUp) {

    }

    @Override
    public void repeat(RepeatEnum repeatEnum) {
        switch (repeatEnum) {
            case REPEAT:
                break;
            case REPEAT_ONCE:
                break;
            case NO_REPEAT:
                break;
        }
    }

    @Override
    public void shuffle(boolean turnOn) {
        mShuffle = turnOn;
    }

    @Override
    public void onCompletion(MediaPlayer mp) {
        Log.d("bbb", "COMPLETED");
        next();
    }

    @Override
    public boolean onError(MediaPlayer mp, int what, int extra) {
        Log.d("bbb", "ERROR:" + what + ", e:" + extra);
        return false;
    }

    @Override
    public void onPrepared(MediaPlayer mp) {
        Log.d("bbb", "PREPARED");
    }

    @Override
    public void subscribeWatcher() {
        if (mediaPlayer != null) {
            mediaPlayer.setOnCompletionListener(this);
            mediaPlayer.setOnPreparedListener(this);
            mediaPlayer.setOnErrorListener(this);
        }
    }

    @Override
    public void cancelWatcher() {
        if (mediaPlayer != null) {
            mediaPlayer.setOnCompletionListener(null);
            mediaPlayer.setOnPreparedListener(null);
            mediaPlayer.setOnErrorListener(null);
        }
    }
}
