package bkoruznjak.from.hr.musae.player;

/**
 * Created by bkoruznjak on 12/05/2017.
 * <p>
 * We use this for observer patterns to subscribe our Listeners where and when we need them
 * Convenience class for convenience methods
 */

public interface Watchable {

    void subscribeWatcher();


    void cancelWatcher();

}
