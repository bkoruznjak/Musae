package bkoruznjak.from.hr.musae.views.songs;

/**
 * Created by bkoruznjak on 02/05/2017.
 */

public class SongModel {

    private String author;
    private String title;
    private String fileUri;
    private int songIndex;

    public SongModel(String author, String title, String fileUri, int songIndex) {
        this.author = author;
        this.title = title;
        this.fileUri = fileUri;
        this.songIndex = songIndex;
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

    public void setSongIndex(int songIndex) {
        this.songIndex = songIndex;
    }

    public int getSongIndex() {
        return this.songIndex;
    }
}
