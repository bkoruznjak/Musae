package bkoruznjak.from.hr.musae.library;

import android.util.Log;

import java.io.File;
import java.io.IOException;

/**
 * Created by bkoruznjak on 01/05/2017.
 */

public class FileScanner {

    public void scan(final File root) {

        new Thread(new Runnable() {
            @Override
            public void run() {
                File[] list = root.listFiles();
                if (list != null) {
                    for (File f : list) {
                        if (f.isDirectory()) {
                        //Log.d("", "Dir: " + f.getAbsoluteFile());
                            scan(f);
                        } else {
                            if (f.getAbsoluteFile() != null) {
                                try {
                                    Log.d("", "File: " + f.getCanonicalPath());
                                } catch (IOException ioEx) {
                                    Log.e("bbb", "ioex");
                                }
                            }

                        }
                    }
                }
            }
        }).start();
    }
}
