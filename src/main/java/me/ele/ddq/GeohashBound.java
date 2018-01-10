package me.ele.ddq;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.spark.api.java.JavaRDD;
import org.apache.spark.api.java.JavaSparkContext;
import org.apache.spark.api.java.function.Function;
import org.apache.spark.sql.AnalysisException;
import org.apache.spark.sql.Dataset;
import org.apache.spark.sql.Row;
import org.apache.spark.sql.SparkSession;
import org.apache.spark.sql.api.java.UDF1;
import org.apache.spark.sql.types.DataTypes;

import java.util.List;

/**
 * 生成GeoHash边框
 *
 * */
public class GeohashBound {

    private static SparkSession spark = null;
    private static JavaSparkContext jsc = null;

    private static final String base32 = "0123456789bcdefghjkmnpqrstuvwxyz";

    private static final Log logger = LogFactory.getLog(GeohashBound.class);

    public static void main(String[] args) {

        long startTime = System.currentTimeMillis();
        spark = SparkSession.builder()
                    .appName("GeohashBound")
                    .enableHiveSupport()
                    .getOrCreate();

        jsc = new JavaSparkContext(spark.sparkContext());

        spark.udf().register("geohash2Bound", new UDF1<String, String>() {
            @Override
            public String call(String geohash) throws Exception {

                try{
                    if(geohash == null && geohash.length() == 0)
                        return null;

                    boolean eventBit = true;
                    double latMin = -90;
                    double latMax = 90;
                    double lngMin = -180;
                    double lngMax = 180;

                    for (int i = 0; i < geohash.length(); i++){
                        char chr = geohash.charAt(i);
                        int idx = GeohashBound.base32.indexOf(chr);
                        if (idx == -1) throw new Exception("Invalid geohash");

                        for(int n = 4; n >= 0; n--){
                            int bitN = idx >> n & 1;
                            if (eventBit){
                                //longitude
                                double lngMid = (lngMin + lngMax) / 2;
                                if(bitN  == 1){
                                    lngMin = lngMid;
                                }else{
                                    lngMax = lngMid;
                                }
                            }else{
                                // latitude
                                double latMid = (latMin + latMax) / 2;
                                if(bitN == 1){
                                    latMin = latMid;
                                }else{
                                    latMax = latMid;
                                }
                            }
                            eventBit = !eventBit;
                        }
                    }

                    //生成5对坐标,用来画geohash矩形
                    StringBuffer bounds = new StringBuffer();
                    bounds.append(latMin + ","); bounds.append(lngMin + ";");
                    bounds.append(latMin + ","); bounds.append(lngMax + ";");

                    bounds.append(latMax + ","); bounds.append(lngMax + ";");
                    bounds.append(latMax + ","); bounds.append(lngMin + ";");

                    bounds.append(latMin + ","); bounds.append(lngMin);

                    return bounds.toString();
                } catch(Exception e){

                }
                return null;
            }
        }, DataTypes.StringType);

        //get predict list
        List<Predict> predictList = getPredict();
        if(predictList != null && predictList.size() > 0){
            persistence(predictList);
        }

        spark.close();

        long endTime = System.currentTimeMillis();
        System.out.println("total run time : " + (endTime - startTime));

    }


    private static List<Predict> getPredict(){

        JavaRDD<Row> predictRdd = spark.sql("select shop_id, shop_lat, shop_lng, deliver_geohash, deliver_lat, deliver_lng, predict_order_cnt, geohash2Bound(deliver_geohash) " +
                "from temp.temp_lpd_ddq_sh_predict_2").javaRDD();

        System.out.println("predictRdd.count=" + predictRdd.count());

        List<Predict> predictList = getPredictList(predictRdd);

        return predictList;

    }

    private static List<Predict> getPredictList(JavaRDD<Row> predictRdd){

        JavaRDD<Predict> map = predictRdd.map(new Function<Row, Predict>() {
            @Override
            public Predict call(Row row) throws Exception {
                String shop_id = row.getString(0);
                String shop_lat = row.getString(1);
                String shop_lng = row.getString(2);

                String deliver_geohash = row.getString(3);
                String deliver_lat = row.getString(4);
                String deliver_lng = row.getString(5);
                String predict_order_cnt = row.getString(6);
                String boundPoints = row.getString(7);

                //System.out.println("geoInfo = "+ geoInfo);

                Predict bean = new Predict();
                bean.setShop_id(shop_id);
                bean.setShop_lat(shop_lat);
                bean.setShop_lng(shop_lng);

                bean.setDeliver_geohash(deliver_geohash);
                bean.setDeliver_lat(deliver_lat);
                bean.setDeliver_lng(deliver_lng);
                bean.setDeliver_bound_points(boundPoints);

                bean.setPredict_order_cnt(predict_order_cnt);

                return bean;
            }
        });
        return map.collect();
    }

    private static void persistence(List<Predict> predictList){
        try {
            SparkSession sparkSession = spark.newSession();
            Dataset<Row> dataset = sparkSession.createDataFrame(predictList, Predict.class);
            dataset.createTempView("predicts");
            String sqlInsert = "insert into temp.temp_lpd_ddq_sh_predict select " +
                    " shop_id, shop_lat, shop_lng, deliver_geohash, deliver_lat, deliver_lng, predict_order_cnt, deliver_bound_points from predicts";

            Dataset<Row> dsInsert = sparkSession.sql(sqlInsert);
            System.out.println(dsInsert.count());
        } catch (AnalysisException e) {
            e.printStackTrace();
        }


    }

}
