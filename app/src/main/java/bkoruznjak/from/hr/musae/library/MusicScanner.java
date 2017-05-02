package bkoruznjak.from.hr.musae.library;

import android.content.ContentResolver;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.provider.MediaStore;
import android.util.Log;

/**
 * Created by bkoruznjak on 02/05/2017.
 */

public class MusicScanner {

    private boolean goodToGo;
    private Context mContext;

    /**
     * Prepares the MusicScanner for music retrieval. We need the context for the Content Resolver.
     * prepare() should be called in onStart()
     * @param context
     */
    public void prepare(Context context) {
        goodToGo = true;
        this.mContext = context;
    }

    /**
     * Call clean() when you no longer use the MusicScanner, preferably onStop()
     */
    public void clean() {
        goodToGo = false;
        this.mContext = null;
    }

    /**
     * Collects music info from the Content Resolver and returns with a notification when finished.
     *
     * Throws Illegal Argument Exception when called prior to a prepare().
     */
    public void gatherMusicInfo() {
        if (goodToGo) {
            ContentResolver cr = mContext.getContentResolver();

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
        } else {
            throw new IllegalArgumentException("read called before prepare");
        }
    }
}
