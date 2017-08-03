package bkoruznjak.from.hr.musae.root;

import bkoruznjak.from.hr.musae.views.activities.MainActivity;
import dagger.Component;

/**
 * Created by bkoruznjak on 03/08/2017.
 */

@AppScope
@Component(modules = {AppModule.class})
public interface AppComponent {
    void inject(MainActivity target);
}
