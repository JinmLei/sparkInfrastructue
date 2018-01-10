package me.ele.ddq.util;

public class Geo {
    private static final double R = 6372.8;

    public static double greatCircleDistance(double lng1, double lat1, double lng2, double lat2) {
        double deltaLat = Math.toRadians(lat2 - lat1);
        double deltaLng = Math.toRadians(lng2 - lng1);
        lat1 = Math.toRadians(lat1);
        lat2 = Math.toRadians(lat2);

        double a = Math.sin(deltaLat / 2) * Math.sin(deltaLat / 2) + Math.sin(deltaLng / 2) * Math.sin(deltaLng / 2) * Math.cos(lat1) * Math.cos(lat2);
        double c = 2 * Math.asin(Math.sqrt(a));
        double distance = R * c * 1000.;

        return distance;
    }

    public static double calDistance(double fromLng, double fromLat, double toLng, double toLat) {
        double radical = Math.sqrt(Math.pow(Math.sin(0.017453292519943295D * ((fromLat - toLat) / 2.0D)), 2.0D) + Math.cos(0.017453292519943295D * fromLat) * Math.cos(0.017453292519943295D * toLat) * Math.pow(Math.sin(0.017453292519943295D * ((fromLng - toLng) / 2.0D)), 2.0D));
        return 1.2756274E7D * Math.asin(radical) * 1.4D;
    }
}