package me.ele.ddq.udf;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.hadoop.hive.ql.exec.UDF;
import org.apache.hadoop.io.Text;

/**
 * Created by guowli on 09/01/2018.
 */
public class Geohash2Bound extends UDF {

    private static final Log logger = LogFactory.getLog(Geohash2Bound.class);

    private static final String base32 = "0123456789bcdefghjkmnpqrstuvwxyz";

    private Text result = new Text();

    public Text evaluate(Text text){
        try{
            String geohash = text.toString();
            if(geohash == null && geohash.length() == 0)
                return null;

            boolean eventBit = true;
            double latMin = -90;
            double latMax = 90;
            double lngMin = -180;
            double lngMax = 180;

            for (int i = 0; i < geohash.length(); i++){
                char chr = geohash.charAt(i);
                int idx = Geohash2Bound.base32.indexOf(chr);
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

            result.set(bounds.toString());

            return result;
        } catch(Exception e){

        }
        return null;
    }


    public static void main(String[] args){
        Geohash2Bound geo = new Geohash2Bound();
        Text text = new Text();
        text.set("wtw36r");
        Text evaluate = geo.evaluate(text);
        System.out.println(evaluate);
    }


}
