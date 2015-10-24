package org.kpa.risk.modeling;

import java.util.concurrent.ThreadLocalRandom;

/**
 * Created by krucpav on 19.07.15.
 */
public class RandomStrategy implements Strategy {
    private final double factPnlRate;
    private final double factDrawDownRate;
    private double leverage = 1.;
    private final TrackList trackList = new TrackList();
    private final String id;

    public RandomStrategy(String id, double pl, double dd, int periodCount) {
        if (dd <= 0 || dd >= 1) {
            throw new IllegalArgumentException("dd=(0;1): " + dd);
        }
        if (pl <= -1 || pl >= 1) {
            throw new IllegalArgumentException("pl=(-1;1)=" + pl);
        }
        this.id = id;
        this.factPnlRate = pl / periodCount;
        this.factDrawDownRate = dd / Math.sqrt(periodCount);
    }

    @Override
    public String getId() {
        return id;
    }

    @Override
    public double nextPeriod() {
        double aDouble = ThreadLocalRandom.current().nextDouble(-.5, +.5);
        double rate = (factPnlRate + aDouble * factDrawDownRate) * leverage;
        trackList.addTrack(rate);
        return rate;
    }


    @Override
    public double getLeverage() {
        return leverage;
    }

    @Override
    public void setLeverage(double leverage) {
        if (leverage < 0 || leverage > 1.) {
            throw new IllegalArgumentException("Should be [0;1]=" + leverage);
        }
        this.leverage = leverage;
    }

    @Override
    public String toString() {
        return "RandomPortfolio{" +
                "id='" + id + '\'' +
                ", pnl=" + factPnlRate +
                ", factDrawDawnRate=" + factDrawDownRate +
                ", leverage=" + leverage +
                ", trackList=" + trackList +
                '}';
    }

    @Override
    public TrackList getTrackList() {
        return trackList;
    }

}
