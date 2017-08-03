package bkoruznjak.from.hr.musae.controller.player;

import android.support.annotation.NonNull;

import java.util.List;

import bkoruznjak.from.hr.musae.views.songs.SongModel;

/**
 * Created by bkoruznjak on 12/05/2017.
 */

public interface Deejay {

    void prepareSong(int index);

    void prepareSet(@NonNull List<SongModel> music);

}
