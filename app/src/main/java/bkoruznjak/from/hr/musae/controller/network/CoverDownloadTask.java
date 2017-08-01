package bkoruznjak.from.hr.musae.controller.network;

import android.os.AsyncTask;

import java.util.List;

import bkoruznjak.from.hr.musae.views.songs.SongModel;

/**
 * Created by bkoruznjak on 01/08/2017.
 */

public class CoverDownloadTask extends AsyncTask<Void, Void, Void> {

    private List<SongModel> mSongList;

    public CoverDownloadTask(List<SongModel> songList) {
        this.mSongList = songList;
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        //todo init database
    }

    @Override
    protected void onPostExecute(Void aVoid) {
        super.onPostExecute(aVoid);
    }

    @Override
    protected void onProgressUpdate(Void... values) {
        super.onProgressUpdate(values);
    }

    @Override
    protected Void doInBackground(Void... params) {

        for (SongModel song : mSongList) {
            if (song.getSongCoverUrl().isEmpty()) {
                //todo do network call
            }
        }
        return null;
    }
}
