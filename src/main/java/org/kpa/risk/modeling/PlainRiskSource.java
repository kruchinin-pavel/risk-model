package org.kpa.risk.modeling;

/**
 * Created by krucpav on 29.09.15.
 */
public class PlainRiskSource implements RiskSource {
    private final StrategyConfig config;

    public PlainRiskSource(StrategyConfig config) {
        this.config = config;
    }

    @Override
    public void newIncoming(double incoming) {
        // nothing do
    }

    @Override
    public double getLeverage() {
        return 1;
    }

    @Override
    public double getCurrentRiskAvailable() {
        return config.getTargetPnl() / config.getPeriodCount();
    }

    public static RiskSourceFactory factory() {
        return config -> new PlainRiskSource(config);
    }
}
