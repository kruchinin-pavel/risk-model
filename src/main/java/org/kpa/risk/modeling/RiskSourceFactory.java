package org.kpa.risk.modeling;

/**
 * Created by krucpav on 29.09.15.
 */
public interface RiskSourceFactory {
    RiskSource createSource(StrategyConfig config);
}
