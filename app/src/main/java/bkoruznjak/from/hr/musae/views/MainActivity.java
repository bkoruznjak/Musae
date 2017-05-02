package bkoruznjak.from.hr.musae.views;

import android.Manifest;
import android.content.pm.PackageManager;
import android.databinding.DataBindingUtil;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import bkoruznjak.from.hr.musae.R;
import bkoruznjak.from.hr.musae.databinding.ActivityMainBinding;
import bkoruznjak.from.hr.musae.library.MusicScanner;
import bkoruznjak.from.hr.musae.views.songs.SongAdapter;
import bkoruznjak.from.hr.musae.views.songs.SongModel;

public class MainActivity extends AppCompatActivity implements MusicScanner.MediaListener {

    private ActivityMainBinding mainBinding;
    private final int READ_EXTERNAL_STORAGE_PERMISSION_ID = 69;
    private MusicScanner musicScanner;
    private List<SongModel> mSongList = new ArrayList<>(4);
    private SongAdapter mSongAdapter;
    private RecyclerView.LayoutManager layoutManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mainBinding = DataBindingUtil.setContentView(this, R.layout.activity_main);

        musicScanner = new MusicScanner();
        mSongAdapter = new SongAdapter(mSongList);

        layoutManager = new LinearLayoutManager(this);
        mainBinding.recyclerViewSongs.setLayoutManager(layoutManager);
        mainBinding.recyclerViewSongs.setAdapter(mSongAdapter);

        mainBinding.btnStartPlayer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    MediaPlayer mediaPlayer = new MediaPlayer();
                    mediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
                    mediaPlayer.setDataSource("/storage/emulated/0/Music/The weekend - sidewalks.mp3");
                    mediaPlayer.prepare();
                    mediaPlayer.start();
                } catch (IOException ioEx) {
                    Log.e("bbb", "ioEx:" + ioEx);
                }
            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();
        musicScanner.prepare(this, this);
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (hasRightsToDoMagic()) {
            musicScanner.gatherMusicInfo();
        }
    }

    @Override
    protected void onStop() {
        super.onStop();
        musicScanner.clean();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        switch (requestCode) {
            case READ_EXTERNAL_STORAGE_PERMISSION_ID:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    musicScanner.gatherMusicInfo();
                } else {
                    Toast.makeText(getApplicationContext(), "App does not have sufficient permissions to check your media", Toast.LENGTH_SHORT).show();
                }
                break;
        }
    }

    private boolean hasRightsToDoMagic() {
        int permissionCheck = ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE);

        if (permissionCheck != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(
                    this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, READ_EXTERNAL_STORAGE_PERMISSION_ID);

            return false;
        } else {
            return true;
        }
    }


    @Override
    public void onMediaFound(String data) {
        String songData = data.substring(data.lastIndexOf("/") + 1);
        String[] songDataArray = songData.split(" - ");
        if (songDataArray.length == 2) {
            String author = songDataArray[0];
            String title = songDataArray[1];

            mSongList.add(new SongModel(author, title, data));
        } else {
            mSongList.add(new SongModel("", songData, data));
        }
        mSongAdapter.notifyDataSetChanged();

    }
}
