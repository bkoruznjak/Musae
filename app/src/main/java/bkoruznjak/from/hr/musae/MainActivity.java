package bkoruznjak.from.hr.musae;

import android.Manifest;
import android.content.ContentResolver;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.databinding.DataBindingUtil;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import java.io.IOException;

import bkoruznjak.from.hr.musae.databinding.ActivityMainBinding;
import bkoruznjak.from.hr.musae.library.FileScanner;

public class MainActivity extends AppCompatActivity {

    private ActivityMainBinding mainBinding;
    private final int READ_EXTERNAL_STORAGE_PERMISSION_ID = 69;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mainBinding = DataBindingUtil.setContentView(this, R.layout.activity_main);

        final FileScanner scanner = new FileScanner();


        mainBinding.btnScanFolders.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (hasRightsToDoMagic()) {
                    readTheMusic();
                }


            }


        });

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

    private void readTheMusic() {
        ContentResolver cr = getContentResolver();

        Uri uri = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI;
        String selection = MediaStore.Audio.Media.IS_MUSIC + "!= 0";
        String sortOrder = MediaStore.Audio.Media.TITLE + " ASC";


        Cursor cur = cr.query(uri, null, selection, null, sortOrder);
        int count = 0;

        if (cur != null) {
            count = cur.getCount();

            if (count > 0) {
                while (cur.moveToNext()) {
                    String data = cur.getString(cur.getColumnIndex(MediaStore.Audio.Media.DATA));
                    // Add code to get more column here
                    Log.d("bbb", "data..:" + data);
                    // Save to your list here
                }

            }
        }

        cur.close();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        switch (requestCode) {
            case READ_EXTERNAL_STORAGE_PERMISSION_ID:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    readTheMusic();
                } else {
                    Toast.makeText(getApplicationContext(), "Aplikacija nema potrebne ovlasti za dijeljenje raspoloženja", Toast.LENGTH_SHORT).show();
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


}
