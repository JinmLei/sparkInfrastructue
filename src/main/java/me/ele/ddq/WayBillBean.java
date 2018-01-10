package me.ele.ddq;

/**
 * Created by guowli on 05/12/2017.
 */
public class WayBillBean {

    private String order_date;
    private Long tracking_id;
    private Long platform_source_id;
    private String user_latitude;
    private String user_longitude;
    private String restaurant_id;
    private String restaurant_name;

    public String getOrder_date() {
        return order_date;
    }

    public void setOrder_date(String order_date) {
        this.order_date = order_date;
    }

    public Long getTracking_id() {
        return tracking_id;
    }

    public void setTracking_id(Long tracking_id) {
        this.tracking_id = tracking_id;
    }

    public Long getPlatform_source_id() {
        return platform_source_id;
    }

    public void setPlatform_source_id(Long platform_source_id) {
        this.platform_source_id = platform_source_id;
    }

    public String getUser_latitude() {
        return user_latitude;
    }

    public void setUser_latitude(String user_latitude) {
        this.user_latitude = user_latitude;
    }

    public String getUser_longitude() {
        return user_longitude;
    }

    public void setUser_longitude(String user_longitude) {
        this.user_longitude = user_longitude;
    }

    public String getRestaurant_id() {
        return restaurant_id;
    }

    public void setRestaurant_id(String restaurant_id) {
        this.restaurant_id = restaurant_id;
    }

    public String getRestaurant_name() {
        return restaurant_name;
    }

    public void setRestaurant_name(String restaurant_name) {
        this.restaurant_name = restaurant_name;
    }
}
