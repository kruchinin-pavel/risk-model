package org.kpa.risk.modeling;

import org.junit.Test;

import static org.junit.Assert.*;

/**
 * Created by krucpav on 25.07.15.
 */
public class RiskSourceTest {

    @Test
    public void testRiskSource() {
        DamperedRiskSource source = new DamperedRiskSource(StrategyConfig.pnl(.15).dd(.15 / 2).count(100).config(), .2);
        source.newIncoming(0.0022);
        assertTrue(source.getCurrentRiskAvailable() > 0);
        assertTrue(source.getOverallPnlDampered() > 0);
        assertTrue(source.getOverallRecoveredRisk() > 0);
        source.newIncoming(-.9);
        assertTrue(source.getCurrentRiskAvailable() < 0);
        assertEquals(0, source.getLeverage(), 1e-5);
    }

    @Test
    public void testGetOverallRecoveredRisk() {
        DamperedRiskSource source = new DamperedRiskSource(StrategyConfig.pnl(.15).dd(.15 / 2).count(100).config(), .2);
        for (int i = 0; i < 100; i++) {
            source.newIncoming(.0);
        }
        assertEquals(.15 / 2, source.getOverallRecoveredRisk(), 1e-4);
    }

    @Test
    public void testGetOverallPnlDampered() {
        double pnlDamperRate = .2;
        double overRate = 0.1;
        DamperedRiskSource source = new DamperedRiskSource(StrategyConfig.pnl(.15).dd(.15 / 2).count(100).config(), pnlDamperRate);
        for (int i = 0; i < 100; i++) {
            source.newIncoming(.15 / 100 + overRate / 100);
        }
        assertEquals(overRate * pnlDamperRate, source.getOverallPnlDampered(), 1e-7);
    }

}