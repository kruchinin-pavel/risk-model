package org.kpa.risk.modeling;

import org.kpa.charts.ChartBuilder;

import java.util.LinkedList;

/**
 * Created by krucpav on 25.07.15.
 */
public class TrackList {
    private final LinkedList<Track> trackList = new LinkedList<>();
    private final Statistics statistics = new Statistics();

    public void addTrack(double rate) {
        Track track;
        track = trackList.size() == 0 ? new Track(rate) : trackList.getLast().add(rate);
        trackList.add(track);
    }

    public Track[] getList() {
        return trackList.toArray(new Track[trackList.size()]);
    }

    public ChartBuilder buildChart(ChartBuilder cb, String name) {
        cb.dataset(name, "rate");
        long i = 0;
        for (Track track : getList()) {
            cb.value(name, i++, track.getCumulatedPnl());
        }
        return cb;
    }

    public ChartBuilder buildChart(String name) {
        ChartBuilder cb = ChartBuilder.builder().name("Portfolio");
        buildChart(cb, name);
        return cb;
    }

    public ChartBuilder buildChart() {
        return buildChart("pnl");
    }

    public double getDd() {
        double maxDd = 0;
        double maxPl = 0;
        for (Track track : trackList) {
            maxPl = Math.max(maxPl, track.getCumulatedPnl());
            maxDd = Math.max(maxPl - track.getCumulatedPnl(), maxDd);
        }
        return maxDd;
    }

    public double getPl() {
        return trackList.getLast().getCumulatedPnl();
    }

}
