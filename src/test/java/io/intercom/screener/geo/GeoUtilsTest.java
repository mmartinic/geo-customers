package io.intercom.screener.geo;

import org.junit.Assert;
import org.junit.Test;

public class GeoUtilsTest {

    @Test
    public void testCalculateDistanceValidCoordinates() throws Exception {
        double distance =
                GeoUtils.calculateDistance(GeoUtils.OFFICE_LATITUDE, GeoUtils.OFFICE_LONGITUDE, 45.840196, 15.9643316);
        Assert.assertEquals(1795.51, distance, 0.01);
    }

    @Test
    public void testCalculateDistanceSameCoordiantes() throws Exception {
        double distance = GeoUtils
                .calculateDistance(GeoUtils.OFFICE_LATITUDE, GeoUtils.OFFICE_LONGITUDE, GeoUtils.OFFICE_LATITUDE,
                        GeoUtils.OFFICE_LONGITUDE);
        Assert.assertEquals(0, distance, 0.01);
    }

    @Test
    public void testCalculateDistanceSmallDistance() throws Exception {
        double distance =
                GeoUtils.calculateDistance(GeoUtils.OFFICE_LATITUDE, GeoUtils.OFFICE_LONGITUDE, 53.3381, -6.2592);
        Assert.assertEquals(0.01160, distance, 0.01);
    }

    @Test
    public void testCalculateDistanceBigDistance() throws Exception {
        double distance =
                GeoUtils.calculateDistance(GeoUtils.OFFICE_LATITUDE, GeoUtils.OFFICE_LONGITUDE, GeoUtils.OFFICE_LATITUDE * -1,
                        GeoUtils.OFFICE_LONGITUDE + 180);
        Assert.assertEquals(GeoUtils.MEAN_EARTH_RADIUS * Math.PI, distance, 0.01);
    }
}
