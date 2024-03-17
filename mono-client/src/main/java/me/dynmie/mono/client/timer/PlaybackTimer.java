package me.dynmie.mono.client.timer;

import javax.sound.sampled.DataLine;

/**
 * @author dynmie
 */
public interface PlaybackTimer {

    void init();

    long elapsedMicros();

    static PlaybackTimer create() {
        return new StartPlaybackTimer();
    }

    static PlaybackTimer create(DataLine dataLine) {
        return new DataLinePlaybackTimer(dataLine);
    }

}
