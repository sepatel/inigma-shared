package org.inigma.shared.tracking;

import static org.junit.Assert.*;

import org.junit.Test;

public class TrackingUtilTest {

    @Test
    public void fedExExpress() {
        assertEquals(TrackingType.FedEx, TrackingUtil.getTrackingType("012345678983"));
        assertNull(TrackingUtil.getTrackingType("012345678984"));
        assertNull(TrackingUtil.getTrackingType("112345678983"));
    }

    @Test
    public void fedExGround() {
        assertEquals(TrackingType.FedEx, TrackingUtil.getTrackingType("9611020 9876543 12345672"));
        assertNull(TrackingUtil.getTrackingType("9611020 9876543 12345671"));
        assertNull(TrackingUtil.getTrackingType("9611020 8976543 12345672"));
        assertEquals(TrackingType.FedEx, TrackingUtil.getTrackingType("9876543 12345672"));
    }
    
    @Test
    public void ups1Zformat() {
        assertEquals(TrackingType.UPS, TrackingUtil.getTrackingType("1Z12345E1512345676"));
        assertEquals(TrackingType.UPS, TrackingUtil.getTrackingType("1ZR441800196847996"));
        assertEquals(TrackingType.UPS, TrackingUtil.getTrackingType("1Z 999 AA1 01 2345 6784"));
        assertNull(TrackingUtil.getTrackingType("1ZW098R41276681979"));
        assertNull(TrackingUtil.getTrackingType("1Z12345E1512345677"));
    }
    
    @Test
    public void usps() {
        assertEquals(TrackingType.USPS, TrackingUtil.getTrackingType("91 7196 9010 7560 0307 7385")); // 22 digit format
        assertEquals(TrackingType.USPS, TrackingUtil.getTrackingType("91 2101 0521 2978 9436 8008")); // 22 digit format
        assertEquals(TrackingType.USPS, TrackingUtil.getTrackingType("7112 3456 7891 2345 6787")); // 20 digit format
        assertEquals(TrackingType.USPS, TrackingUtil.getTrackingType("EA 1234 5678 4 US")); // Express Domestic Mod 10
        assertEquals(TrackingType.USPS, TrackingUtil.getTrackingType("EF123456785US")); // Express Mod 11
    }
    
    @Test
    public void isUpsNotFedEx() {
        assertEquals(TrackingType.UPS, TrackingUtil.getTrackingType("1Z1882X30377043164"));
    }
}
