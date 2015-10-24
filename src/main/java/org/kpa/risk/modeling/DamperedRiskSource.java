package org.kpa.risk.modeling;

import com.google.common.base.Preconditions;
import org.apache.commons.math3.util.Precision;
import sun.reflect.ReflectionFactory;

import java.math.BigDecimal;

/**
 * Created by krucpav on 25.07.15.
 * <p>
 * Recoverable risk source.
 * 1) Upon profitable trading strategy can collect pnl to it's risk source
 * 2) Upon looses strategy can pull recover pnl from periods of doing nothing (waiting for risk to recover)
 */
public class DamperedRiskSource implements RiskSource {
    private final BigDecimal riskRecoveryPerPeriod;
    private final double targetPnlPerPeriod;
    private final double pnlDamperRate;
    //https://ru.wikipedia.org/wiki/%D0%9A%D0%BE%D1%8D%D1%84%D1%84%D0%B8%D1%86%D0%B8%D0%B5%D0%BD%D1%82_%D0%B7%D0%B0%D0%BF%D0%B0%D1%81%D0%B0
    private final double leverageSecureLevel;
    private double currentRiskAvailable;
    private BigDecimal overallPnlDampered = BigDecimal.ZERO;
    private BigDecimal overallRecoveredRisk = BigDecimal.ZERO;

    /**
     * @param config        strategy risk parameters
     * @param pnlDamperRate rate of pnl collected to future risk renewal
     */
    public DamperedRiskSource(StrategyConfig config, double pnlDamperRate) {
        this.pnlDamperRate = pnlDamperRate;
        riskRecoveryPerPeriod = new BigDecimal("" + config.getMaxDrawDown() / config.getPeriodCount());
        targetPnlPerPeriod = config.getTargetPnl() / config.getPeriodCount();
        currentRiskAvailable = riskRecoveryPerPeriod.doubleValue();
        leverageSecureLevel = 2;
    }

    /**
     * New period with incoming (rate)
     *
     * @param incoming
     */
    @Override
    public void newIncoming(double incoming) {
        Preconditions.checkArgument(-1. < incoming && incoming < 1, "incoming = (-1;+1)");
        double riskRenewFromExcessivePnl = 0;
        if (incoming > targetPnlPerPeriod) {
            riskRenewFromExcessivePnl = Precision.round(pnlDamperRate * (incoming - targetPnlPerPeriod), 8);
            overallPnlDampered = overallPnlDampered.add(new BigDecimal("" + riskRenewFromExcessivePnl));
        } else if (incoming < 0) {
            currentRiskAvailable += incoming;
        }
        overallRecoveredRisk = overallRecoveredRisk.add(riskRecoveryPerPeriod);
        currentRiskAvailable += riskRecoveryPerPeriod.doubleValue() + riskRenewFromExcessivePnl;
    }

    @Override
    public double getLeverage() {
        return Math.max(Math.min(getCurrentRiskAvailable() / riskRecoveryPerPeriod.doubleValue() / leverageSecureLevel, 1), 0);
    }

    @Override
    public double getCurrentRiskAvailable() {
        return currentRiskAvailable;
    }

    public double getOverallPnlDampered() {
        return overallPnlDampered.doubleValue();
    }

    public double getOverallRecoveredRisk() {
        return overallRecoveredRisk.doubleValue();
    }

    public static RiskSourceFactory factory(double pnlDamperRate){
        return config -> new DamperedRiskSource(config, pnlDamperRate);
    }
}
