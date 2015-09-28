package io.intercom.screener.geo;

import static java.lang.Math.abs;
import static java.lang.Math.acos;
import static java.lang.Math.cos;
import static java.lang.Math.sin;
import static java.lang.Math.toRadians;

public class GeoUtils {

    public static final double MEAN_EARTH_RADIUS = 6371.009;
    public static final double OFFICE_LATITUDE = 53.3381985;
    public static final double OFFICE_LONGITUDE = -6.2592576;
    public static final double CUTOFF_RADIUS = 100.0;

    public static double calculateDistance(double latitude1, double longitude1, double latitude2, double longitude2) {

        double latitudeInRadians1 = toRadians(latitude1);
        double longitudeInRadians1 = toRadians(longitude1);
        double latitudeInRadians2 = toRadians(latitude2);
        double longitudeInRadians2 = toRadians(longitude2);

        return acos(sin(latitudeInRadians1) * sin(latitudeInRadians2) + cos(latitudeInRadians1) * cos(latitudeInRadians2) * cos(
                abs(longitudeInRadians1 - longitudeInRadians2))) * MEAN_EARTH_RADIUS;
    }

    public static boolean isLatitudeValid(double latitude) {
        return latitude >= -90. && latitude <= 90.;
    }

    public static boolean isLongitudeValid(double longitude) {
        return longitude >= -180. && longitude <= 180.;
    }
}
