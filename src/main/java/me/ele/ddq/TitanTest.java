package me.ele.ddq;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.spark.api.java.JavaRDD;
import org.apache.spark.api.java.JavaSparkContext;
import org.apache.spark.api.java.function.Function;
import org.apache.spark.sql.AnalysisException;
import org.apache.spark.sql.Row;
import org.apache.spark.sql.SparkSession;
import org.apache.spark.sql.Dataset;

import java.util.List;

/**
 * Created by guowli on 05/12/2017.
 */
public class TitanTest {
    private static String dt;
    private static SparkSession spark = null;
    private static JavaSparkContext jsc = null;

    private static final Log logger = LogFactory.getLog(TitanTest.class);


    public static void main(String[] args) {
        dt = "2017-12-04";

        long startTime = System.currentTimeMillis();
        spark = SparkSession.builder()
                    .appName("titan test")
                    .enableHiveSupport()
                    .getOrCreate();

        jsc = new JavaSparkContext(spark.sparkContext());

        //get waybill list
        List<WayBillBean> wayBillBeanList = getWayBill();
        if(wayBillBeanList != null && wayBillBeanList.size() > 0){
            persistence(wayBillBeanList);
        }

        spark.close();

        long endTime = System.currentTimeMillis();
        System.out.println("total run time : " + (endTime - startTime));

    }

    private static List<WayBillBean> getWayBill(){

        JavaRDD<Row> waybillRdd = spark.sql("select order_date,tracking_id,platform_source_id,user_latitude,user_longitude,restaurant_id,restaurant_name " +
                "from dm.dm_tms_apollo_waybill_wide_detail where dt='2017-12-04' limit 10").javaRDD();

        System.out.println("waybills.count" + waybillRdd.count());

        List<WayBillBean> waybillList = getWayBillList(waybillRdd);

        return waybillList;

    }

    private static List<WayBillBean> getWayBillList(JavaRDD<Row> waybillRdd){

        JavaRDD<WayBillBean> map = waybillRdd.map(new Function<Row, WayBillBean>() {
            @Override
            public WayBillBean call(Row row) throws Exception {
                String orderDate = row.getString(0);

                Long tracking_id = row.getLong(1);
                Long platform_source_id = row.getLong(2);
                String user_latitude = row.getString(3);
                String user_longitude = row.getString(4);
                String restaurant_id = row.getString(5);
                String restaurant_name = row.getString(6);

                WayBillBean bean = new WayBillBean();
                bean.setOrder_date(orderDate);
                bean.setTracking_id(tracking_id);
                bean.setPlatform_source_id(platform_source_id);
                bean.setUser_latitude(user_latitude);
                bean.setUser_longitude(user_longitude);
                bean.setRestaurant_id(restaurant_id);
                bean.setRestaurant_name(restaurant_name);
                return bean;
            }
        });
        return map.collect();
    }

    private static void persistence(List<WayBillBean> waybillList){
        try {
            SparkSession sparkSession = spark.newSession();
            Dataset<Row> dataset = sparkSession.createDataFrame(waybillList, WayBillBean.class);
            dataset.createTempView("waybills");
            String sqlInsert = "insert into dw_lpd.dw_lpd_ddq PARTITION (dt = '" + dt + "') select " +
                    " order_date,tracking_id,platform_source_id,user_latitude,user_longitude,restaurant_id,restaurant_name from waybills";

            Dataset<Row> dsInsert = sparkSession.sql(sqlInsert);
            System.out.println(dsInsert.count());
        } catch (AnalysisException e) {
            e.printStackTrace();
        }


    }
}
