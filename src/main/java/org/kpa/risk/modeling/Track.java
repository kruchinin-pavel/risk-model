package org.kpa.risk.modeling;

/**
 * Created by krucpav on 19.07.15.
 */
public class Track {
    private final double pnl;
    private final double cumulatedPnl;

    public Track(double pnl) {
        this(pnl, pnl);
    }

    public Track(double pnl, double cumulatedPnl) {
        this.pnl = pnl;
        this.cumulatedPnl = cumulatedPnl;
    }

    public Track add(double pnl) {
        return new Track(this.pnl, cumulatedPnl + pnl);
    }

    public double getPnl() {
        return pnl;
    }

    public double getCumulatedPnl() {
        return cumulatedPnl;
    }

    @Override
    public String toString() {
        return "Track{" +
                "cumulatedPnl=" + cumulatedPnl +
                ", pnl=" + pnl +
                '}';
    }
}
