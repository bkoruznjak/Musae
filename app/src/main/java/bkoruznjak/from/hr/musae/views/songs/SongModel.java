package bkoruznjak.from.hr.musae.views.songs;

/**
 * Created by bkoruznjak on 02/05/2017.
 */

public class SongModel {

    private String author;
    private String title;
    private String fileUri;
    private int songIndex;


    private String songCoverUrl;

    public SongModel(String author, String title, String fileUri, int songIndex, String songCoverUrl) {
        this.author = author;
        this.title = title;
        this.fileUri = fileUri;
        this.songIndex = songIndex;
        this.songCoverUrl = songCoverUrl;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getFileUri() {
        return fileUri;
    }

    public void setFileUri(String fileUri) {
        this.fileUri = fileUri;
    }

    public int getSongIndex() {
        return this.songIndex;
    }

    public void setSongIndex(int songIndex) {
        this.songIndex = songIndex;
    }

    public String getSongCoverUrl() {
        return songCoverUrl;
    }

    public void setSongCoverUrl(String songCoverUrl) {
        this.songCoverUrl = songCoverUrl;
    }
}
