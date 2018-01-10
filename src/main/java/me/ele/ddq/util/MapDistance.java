package me.ele.ddq.util;

/**
 * Created by liumengyan on 17/8/30
 */

public class MapDistance {

    private static double EARTH_RADIUS = 6378.137;

    private static double rad(double d) {
        return d * Math.PI / 180.0;
    }


    public static double getDistance(String lat1Str, String lng1Str, String lat2Str, String lng2Str) {
        Double lat1 = Double.parseDouble(lat1Str);
        Double lng1 = Double.parseDouble(lng1Str);
        Double lat2 = Double.parseDouble(lat2Str);
        Double lng2 = Double.parseDouble(lng2Str);

        double radLat1 = rad(lat1);
        double radLat2 = rad(lat2);
        double difference = radLat1 - radLat2;
        double mdifference = rad(lng1) - rad(lng2);
        double distance = 2 * Math.asin(Math.sqrt(Math.pow(Math.sin(difference / 2), 2)
                + Math.cos(radLat1) * Math.cos(radLat2)
                * Math.pow(Math.sin(mdifference / 2), 2)));
        distance = distance * EARTH_RADIUS;

        return distance*1000;  //单位为米
    }

    public static double calDistance(double fromLng, double fromLat, double toLng, double toLat) {
        double radical = Math.sqrt(Math.pow(Math.sin(0.017453292519943295D * ((fromLat - toLat) / 2.0D)), 2.0D) + Math.cos(0.017453292519943295D * fromLat) * Math.cos(0.017453292519943295D * toLat) * Math.pow(Math.sin(0.017453292519943295D * ((fromLng - toLng) / 2.0D)), 2.0D));
        return 1.2756274E7D * Math.asin(radical) * 1.4D;
    }

    public static void main(String[] args) {
        //测试经纬度：117.11811  36.68484
        //测试经纬度2：117.00999000000002  36.66123
        //System.out.println(getDistance("114.42405799","30.46306794","114.4123384","30.47024644") * 1.4);
        System.out.println(calDistance(114.42405799,30.46306794,114.4123384,30.47024644));
        System.out.println(Geo.greatCircleDistance(114.42405799,30.46306794,114.4123384,30.47024644) * 1.4);
        System.out.println("------------------------");
        //System.out.println(getDistance("120.38743995","30.32264985","120.378073","30.306417") * 1.4);
        System.out.println(calDistance(120.38743995,30.32264985,120.378073,30.306417));
        System.out.println(Geo.greatCircleDistance(120.38743995,30.32264985,120.378073,30.306417) * 1.4);
        System.out.println("------------------------");
        //System.out.println(getDistance("116.61050392","39.92479694","116.590855","39.915917") * 1.4);
        System.out.println(calDistance(116.61050392,39.92479694,116.590855,39.915917));
        System.out.println(Geo.greatCircleDistance(116.61050392,39.92479694,116.590855,39.915917) * 1.4);
    }

}
