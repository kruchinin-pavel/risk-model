package org.kpa.risk.modeling;

import org.junit.Test;

import static org.junit.Assert.*;

/**
 * Created by krucpav on 27.09.15.
 */
public class TrackListTest {

    @Test
    public void testGetDd() throws Exception {
        TrackList tl = new TrackList();
        tl.addTrack(1);
        tl.addTrack(2);
        tl.addTrack(-10);
        tl.addTrack(-1);
        assertEquals(11., tl.getDd(), 1e-5);
        assertEquals(-8., tl.getPl(), 1e-5);
    }

}