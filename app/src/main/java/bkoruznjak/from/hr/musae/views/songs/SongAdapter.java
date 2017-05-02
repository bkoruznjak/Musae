package bkoruznjak.from.hr.musae.views.songs;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

import bkoruznjak.from.hr.musae.R;

/**
 * Created by bkoruznjak on 02/05/2017.
 */

public class SongAdapter extends RecyclerView.Adapter<SongAdapter.ViewHolder> {

    private List<SongModel> mSongList;

    public static class ViewHolder extends RecyclerView.ViewHolder {
        public TextView textTitle;
        public TextView textAuthor;

        public ViewHolder(View v) {
            super(v);
            textTitle = (TextView) v.findViewById(R.id.txt_song_title);
            textAuthor = (TextView) v.findViewById(R.id.txt_song_author);
        }
    }

    public SongAdapter(List<SongModel> songList) {
        mSongList = songList;
    }

    @Override
    public SongAdapter.ViewHolder onCreateViewHolder(ViewGroup parent,
                                                     int viewType) {
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.card_view_song, parent, false);
        ViewHolder vh = new ViewHolder(v);
        return vh;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        holder.textTitle.setText(mSongList.get(position).getTitle());
        holder.textAuthor.setText(mSongList.get(position).getAuthor());
    }

    @Override
    public int getItemCount() {
        return mSongList.size();
    }
}
