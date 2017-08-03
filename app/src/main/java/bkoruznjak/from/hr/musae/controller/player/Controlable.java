package bkoruznjak.from.hr.musae.controller.player;

/**
 * Created by bkoruznjak on 12/05/2017.
 */

public interface Controlable {

    void play();

    void pause();

    void stop();

    void next();

    void previous();

    void volume(boolean crankUp);

    void repeat(RepeatEnum repeatEnum);

    void shuffle(boolean turnOn);
}
