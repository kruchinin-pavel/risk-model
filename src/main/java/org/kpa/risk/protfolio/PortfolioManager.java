package org.kpa.risk.protfolio;

import org.kpa.charts.ChartBuilder;
import org.kpa.risk.modeling.*;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by krucpav on 19.07.15.
 */
public class PortfolioManager implements Strategy {
    private final List<Item> strategyList = new ArrayList<>();
    private final RiskSourceFactory factory;

    public PortfolioManager(RiskSourceFactory factory) {
        this.factory = factory;
    }

    public void addStrategy(Strategy strategy, StrategyConfig config) {
        strategyList.add(new Item(strategy, factory.createSource(config)));
    }

    private final TrackList trackList = new TrackList();

    public double nextPeriod() {
        double pnl = 0.;
        for (Item item : strategyList) {
            pnl += item.nextPeriod();
        }
        pnl /= strategyList.size();
        trackList.addTrack(pnl);
        return pnl;
    }

    private static class Item {
        private Strategy strategy;
        private RiskSource riskSource;

        public Item(Strategy strategy, RiskSource riskSource) {
            this.strategy = strategy;
            this.riskSource = riskSource;
        }

        public double nextPeriod() {
            double incoming = strategy.nextPeriod();
            riskSource.newIncoming(incoming);
            strategy.setLeverage(riskSource.getLeverage());
            return incoming;
        }
    }

    public TrackList getTrackList() {
        return trackList;
    }

    @Override
    public double getLeverage() {
        throw new UnsupportedOperationException();
    }

    @Override
    public void setLeverage(double leverage) {
        throw new UnsupportedOperationException();
    }


    public ChartBuilder buildChart() {
        ChartBuilder builder = getTrackList().buildChart("total");
//        for (Item item : strategyList) {
//            Strategy strategy = item.strategy;
//            strategy.getTrackList().buildChart(builder, strategy.getId());
//        }
        return builder;
    }

    @Override
    public String getId() {
        return "portfolio";
    }

    public Statistics getStatistics() {
        Statistics statistics = new Statistics();
        for (Item strategy : strategyList) {
            double dd = strategy.strategy.getTrackList().getDd();
            double pl = strategy.strategy.getTrackList().getPl();
            statistics.addResult(pl, dd);
        }
        return statistics;
    }
}
