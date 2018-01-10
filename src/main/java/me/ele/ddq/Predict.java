package me.ele.ddq;

/**
 * Created by guowli on 08/01/2018.
 */
public class Predict {
    private String shop_id;
    private String shop_lat;
    private String shop_lng;

    private String deliver_geohash;
    private String deliver_lat;
    private String deliver_lng;
    private String deliver_bound_points;

    private String predict_order_cnt;

    public String getShop_id() {
        return shop_id;
    }

    public void setShop_id(String shop_id) {
        this.shop_id = shop_id;
    }

    public String getShop_lat() {
        return shop_lat;
    }

    public void setShop_lat(String shop_lat) {
        this.shop_lat = shop_lat;
    }

    public String getShop_lng() {
        return shop_lng;
    }

    public void setShop_lng(String shop_lng) {
        this.shop_lng = shop_lng;
    }

    public String getDeliver_geohash() {
        return deliver_geohash;
    }

    public void setDeliver_geohash(String deliver_geohash) {
        this.deliver_geohash = deliver_geohash;
    }

    public String getDeliver_lat() {
        return deliver_lat;
    }

    public void setDeliver_lat(String deliver_lat) {
        this.deliver_lat = deliver_lat;
    }

    public String getDeliver_lng() {
        return deliver_lng;
    }

    public void setDeliver_lng(String deliver_lng) {
        this.deliver_lng = deliver_lng;
    }

    public String getDeliver_bound_points() {
        return deliver_bound_points;
    }

    public void setDeliver_bound_points(String deliver_bound_points) {
        this.deliver_bound_points = deliver_bound_points;
    }

    public String getPredict_order_cnt() {
        return predict_order_cnt;
    }

    public void setPredict_order_cnt(String predict_order_cnt) {
        this.predict_order_cnt = predict_order_cnt;
    }
}
