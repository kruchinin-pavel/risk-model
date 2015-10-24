package org.kpa.risk.modeling;

/**
 * Created by krucpav on 29.09.15.
 */
public interface RiskSource {
    void newIncoming(double incoming);

    double getLeverage();

    double getCurrentRiskAvailable();
}
