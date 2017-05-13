package bkoruznjak.from.hr.musae.views;

import android.Manifest;
import android.content.pm.PackageManager;
import android.databinding.DataBindingUtil;
import android.media.audiofx.Visualizer;
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

import java.util.ArrayList;
import java.util.List;

import bkoruznjak.from.hr.musae.R;
import bkoruznjak.from.hr.musae.databinding.ActivityMainBinding;
import bkoruznjak.from.hr.musae.library.MusicScanner;
import bkoruznjak.from.hr.musae.player.MusicPlayer;
import bkoruznjak.from.hr.musae.views.songs.SongAdapter;
import bkoruznjak.from.hr.musae.views.songs.SongModel;
import bkoruznjak.from.hr.musae.views.songs.SongSelectionListener;

public class MainActivity extends AppCompatActivity implements MusicScanner.MediaListener {

    private ActivityMainBinding mainBinding;
    private final int READ_EXTERNAL_STORAGE_PERMISSION_ID = 69;
    private final int RECORD_AUDIO_SETTINGS_PERMISSION_ID = 96;
    private MusicScanner musicScanner;
    private List<SongModel> mSongList = new ArrayList<>(4);
    private SongAdapter mSongAdapter;
    private RecyclerView.LayoutManager layoutManager;
    private MusicPlayer musicPlayer;
    private Visualizer mVisualizer;
    private SongSelectionListener mSongSelectListener;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    private void init() {
        mainBinding = DataBindingUtil.setContentView(this, R.layout.activity_main);
        musicPlayer = MusicPlayer.getInstance();

        musicScanner = new MusicScanner();
        mSongAdapter = new SongAdapter(mSongList);

        setupVisualizerFxAndUI();

        layoutManager = new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false);
        mainBinding.recyclerViewSongs.setLayoutManager(layoutManager);
        mainBinding.recyclerViewSongs.setAdapter(mSongAdapter);

        setupClickListeners();
    }

    private void setupClickListeners() {
        //very bad and temp solution
        mainBinding.btnPlay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                musicPlayer.play();
                mVisualizer.setEnabled(true);
            }
        });

        mainBinding.btnPause.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                musicPlayer.pause();
                mVisualizer.setEnabled(false);
            }
        });

        mainBinding.btnNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                musicPlayer.next();
            }
        });

        mainBinding.btnPrev.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                musicPlayer.previous();
            }
        });

        mainBinding.btnRepeat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d("bbb", "repeat set");
            }
        });

        mSongSelectListener = new SongSelectionListener(this, mainBinding.recyclerViewSongs, new SongSelectionListener.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                // do whatever
                musicPlayer.prepareSong(position);
            }

            @Override
            public void onLongItemClick(View view, int position) {
                // do whatever
            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();

        init();

        musicScanner.prepare(this, this);
        musicPlayer.subscribeWatcher();

        mainBinding.recyclerViewSongs.addOnItemTouchListener(mSongSelectListener);
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (hasReadStorageRights()) {
            musicScanner.gatherMusicInfo();
        }

        if (hasModAudioRights()) {
            Log.d("bbb", "imam rightse");
        } else {
            Log.d("bbb", "nemam rightse");
        }


    }

    @Override
    protected void onPause() {
        super.onPause();
    }

    @Override
    protected void onStop() {
        super.onStop();
        musicScanner.clean();
        musicPlayer.cancelWatcher();
        mainBinding.recyclerViewSongs.removeOnItemTouchListener(mSongSelectListener);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mSongSelectListener = null;
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
            case RECORD_AUDIO_SETTINGS_PERMISSION_ID:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    Log.d("bbb", "we have rights gg wp");
                } else {
                    Toast.makeText(getApplicationContext(), "App does not have sufficient permissions to visualize audio", Toast.LENGTH_SHORT).show();
                }
                break;
        }
    }

    private boolean hasModAudioRights() {
        int permissionCheckModifySound = ContextCompat.checkSelfPermission(this, Manifest.permission.RECORD_AUDIO);

        if (permissionCheckModifySound != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(
                    this, new String[]{Manifest.permission.RECORD_AUDIO}, RECORD_AUDIO_SETTINGS_PERMISSION_ID);

            return false;
        } else {
            return true;
        }
    }

    private boolean hasReadStorageRights() {
        int permissionCheckExternalStorage = ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE);

        if (permissionCheckExternalStorage != PackageManager.PERMISSION_GRANTED) {
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

            mSongList.add(new SongModel(author, title, data, mSongList.size()));
        } else {
            mSongList.add(new SongModel("", songData, data, mSongList.size()));
        }

        musicPlayer.prepareSet(mSongList);
        mSongAdapter.notifyDataSetChanged();

    }

    private void setupVisualizerFxAndUI() {

        // Create the Visualizer object and attach it to our media player.
        int audioSessionId = musicPlayer.AUDIO_SESSION_ID;
            mVisualizer = new Visualizer(audioSessionId);
            boolean shouldEnable = false;
            if (mVisualizer.getEnabled()){
                mVisualizer.setEnabled(false);
                shouldEnable = true;
            }
            mVisualizer.setCaptureSize(Visualizer.getCaptureSizeRange()[1]);
            mVisualizer.setDataCaptureListener(
                    new Visualizer.OnDataCaptureListener() {
                        public void onWaveFormDataCapture(Visualizer visualizer,
                                                          byte[] bytes, int samplingRate) {
                            mainBinding.visualizerView.updateVisualizer(bytes);
                        }

                        public void onFftDataCapture(Visualizer visualizer,
                                                     byte[] bytes, int samplingRate) {
                            mainBinding.visualizerView.updateVisualizer(bytes);
                        }
                    }, Visualizer.getMaxCaptureRate(), true, false);

        if (shouldEnable){
            mVisualizer.setEnabled(true);
        }
    }
}
