package org.kpa.risk.modeling;

import org.junit.Assert;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Created by krucpav on 19.07.15.
 */
public class RandomStrategyTest {
    final static Logger logger = LoggerFactory.getLogger(RandomStrategyTest.class);

    @Test
    public void testNextRate() throws Exception {
        RandomStrategy strategy = new RandomStrategy("p1", -0.002, .002, 250);
        for (int i = 0; i < 100; i++) {
            strategy.nextPeriod();
        }
        Assert.assertTrue("Lossing strategy", strategy.getTrackList().getPl() < 0);
    }


}