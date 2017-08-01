package bkoruznjak.from.hr.musae.root;

import android.app.Application;

import com.facebook.drawee.backends.pipeline.Fresco;

/**
 * Created by bkoruznjak on 01/08/2017.
 */

public class MusaeApplication extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
        Fresco.initialize(this);
    }


}
