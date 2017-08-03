package bkoruznjak.from.hr.musae.root;

import android.content.SharedPreferences;
import android.preference.PreferenceManager;

import dagger.Module;
import dagger.Provides;

/**
 * Created by bkoruznjak on 03/08/2017.
 */

@Module
public class AppModule {

    private MusaeApplication mApplication;
    private SharedPreferences mSharedPreferences;

    public AppModule(MusaeApplication application) {
        this.mApplication = application;
        this.mSharedPreferences = PreferenceManager.getDefaultSharedPreferences(application);
    }

    @Provides
    @AppScope
    public MusaeApplication providesApplication() {
        return this.mApplication;
    }

    @Provides
    @AppScope
    public SharedPreferences providesSharedPreferences() {
        return this.mSharedPreferences;
    }
}
