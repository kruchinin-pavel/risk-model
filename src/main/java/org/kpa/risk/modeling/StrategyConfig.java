package org.kpa.risk.modeling;

/**
 * Created by krucpav on 25.07.15.
 */
public class StrategyConfig implements Cloneable {
    private double targetPnl;
    private double maxDrawDown;
    private int periodCount;

    private StrategyConfig() {
    }

    public double getTargetPnl() {
        return targetPnl;
    }

    public double getMaxDrawDown() {
        return maxDrawDown;
    }

    public int getPeriodCount() {
        return periodCount;
    }

    @Override
    public String toString() {
        return "StrategyParameter{" +
                "targetPnl=" + targetPnl +
                ", maxDrawDown=" + maxDrawDown +
                ", periodCount=" + periodCount +
                '}';
    }

    @Override
    protected StrategyConfig clone() {
        try {
            return (StrategyConfig) super.clone();
        } catch (CloneNotSupportedException e) {
            throw new RuntimeException(e);
        }
    }

    public static Builder pnl(double targetPnl) {
        return new Builder().targetPnl(targetPnl);
    }

    public static class Builder {
        private final StrategyConfig config = new StrategyConfig();

        public Builder targetPnl(double targetPnl) {
            config.targetPnl = targetPnl;
            return this;
        }

        public Builder dd(double maxDrawDown) {
            config.maxDrawDown = maxDrawDown;
            return this;
        }

        public Builder count(int periodCount) {
            config.periodCount = periodCount;
            return this;
        }

        public StrategyConfig config() {
            return config.clone();
        }
    }
}

