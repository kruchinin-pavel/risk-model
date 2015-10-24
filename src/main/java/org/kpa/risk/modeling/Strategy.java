package org.kpa.risk.modeling;

/**
 * Created by krucpav on 19.07.15.
 */
public interface Strategy {
    String getId();

    double nextPeriod();

    double getLeverage();

    void setLeverage(double leverage);

    TrackList getTrackList();

}
