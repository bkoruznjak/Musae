package bkoruznjak.from.hr.musae.controller.player;

/**
 * Created by bkoruznjak on 16/05/2017.
 */

public class PlayerStateModel {
    private boolean wasMusicInterrupted;
    private boolean playWhenReady;
    private boolean isLoading;
    private int playbackState;

    private String songName = "";
    private String songAuthor = "Unknown";
    private String songTitle = "Unknown";
    private String albumArtworkUrl = "Unknown";

    public PlayerStateModel() {
        this.wasMusicInterrupted = false;
        this.playWhenReady = false;
        this.isLoading = false;
        this.playbackState = 0;
    }

    public PlayerStateModel(boolean wasMusicInterrupted, boolean playWhenReady, boolean isLoading, int playbackState) {
        this.wasMusicInterrupted = wasMusicInterrupted;
        this.playWhenReady = playWhenReady;
        this.isLoading = isLoading;
        this.playbackState = playbackState;
    }

    public boolean wasMusicInterrupted() {
        return wasMusicInterrupted;
    }

    public void setMusicInterrupted(boolean wasMusicInterrupted) {
        this.wasMusicInterrupted = wasMusicInterrupted;
    }

    public boolean playWhenReady() {
        return playWhenReady;
    }

    public void setPlayWhenReady(boolean playWhenReady) {
        this.playWhenReady = playWhenReady;
    }

    public boolean isLoading() {
        return isLoading;
    }

    public void setLoading(boolean loading) {
        isLoading = loading;
    }

    public int getPlaybackState() {
        return playbackState;
    }

    public void setPlaybackState(int playbackState) {
        this.playbackState = playbackState;
    }

    public String getSongAuthor() {
        return songAuthor;
    }

    public void setSongAuthor(String songAuthor) {
        this.songAuthor = songAuthor;
    }

    public String getSongTitle() {
        return songTitle;
    }

    public void setSongTitle(String songTitle) {
        this.songTitle = songTitle;
    }

    public String getAlbumArtworkUrl() {
        return albumArtworkUrl;
    }

    public void setAlbumArtworkUrl(String albumArtworkUrl) {
        this.albumArtworkUrl = albumArtworkUrl;
    }

    public String getSongName() {
        return songName;
    }

    public void setSongName(String songName) {
        this.songName = songName;
    }
}