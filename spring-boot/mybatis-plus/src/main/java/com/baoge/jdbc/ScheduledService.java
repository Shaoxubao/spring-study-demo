package com.baoge.jdbc;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.sql.Connection;
import com.mysql.cj.jdbc.PreparedStatement;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

@Slf4j
@Component
public class ScheduledService {
//   @Scheduled(cron = "0/5 * * * * *")
   public void scheduled(){
       log.info("=====>>>>>使用cron  {}",System.currentTimeMillis());
   }
//   @Scheduled(fixedRate = 5000)
   public void scheduled1() {
       log.info("=====>>>>>使用fixedRate{}", System.currentTimeMillis());
   }
//   @Scheduled(fixedDelay = 5000)
   public void scheduled2() {
       log.info("=====>>>>>fixedDelay{}",System.currentTimeMillis());
   }

    @Scheduled(cron = "0/30 * * * * *")
    public void test(){
        log.info("=====>>>>>使用cron  {}",System.currentTimeMillis());
        PreparedStatement ps = null;
        try {
            Connection connect = JDBCUtils.getConnect();
            ps = (PreparedStatement) connect.prepareStatement("");

            List<EMpPowerCurveDatamid> curveDataMids = buildEMpPowerCurveDataMids();
            List<JSONObject> list  = new ArrayList<>();
            for (EMpPowerCurveDatamid item: curveDataMids) {
                System.out.println("receive data:" + JSON.toJSONString(item));
                if (item.getStart() > 0) {
                    JSONObject curve = new JSONObject();
                    List<Double> doubles = JSON.parseArray(item.getData(), Double.class);
                    String[] arr = item.getWhole().split("");
                    curve.put("id", item.getId());
                    curve.put("dataDate", item.getDate());
                    curve.put("dataType", 1);
                    curve.put("dataPointFlag", item.getDensity() == 15 ? 1 : item.getDensity() == 60 ? 3 : 1);
                    curve.put("sqlkey", "");
                    curve.put("sqlvalue", "");
                    curve.put("sqlupdate", "");
                    int startPoint = Constants.hourmap.get(item.getStart());  //这个点是开始点位 ，然后往后赋值N列
                    String dataWholeFlag = "";
                    for (int i = 0; i < item.getPoints(); i++) { //这里是需要赋值N列
                        if (doubles.get(i) != null && "1".equals(arr[i])) {
                            String pi = "p" + (startPoint + i);
                            curve.put("sqlkey", curve.getString("sqlkey") + "," + pi);
                            curve.put("sqlvalue", curve.getString("sqlvalue") + "," + doubles.get(i));
                            curve.put("sqlupdate", curve.getString("sqlupdate") + "," + pi + " = " + doubles.get(i));
                        }
                    }
                    if (item.getPoints() == 96) {
                        dataWholeFlag = "CONCAT( SUBSTRING( DATA_WHOLE_FLAG, 1, " + item.getWhole().indexOf("1") + " ), '" + item.getWhole().replace("0", "") + "', SUBSTRING( DATA_WHOLE_FLAG,  " + (item.getWhole().lastIndexOf("1") + 2) + ", CHAR_LENGTH( DATA_WHOLE_FLAG )))";
                    } else {
                        dataWholeFlag = "CONCAT( SUBSTRING( DATA_WHOLE_FLAG, 1, " + (startPoint - 1) + " ), '" + item.getWhole() + "', SUBSTRING( DATA_WHOLE_FLAG,  " + (startPoint + item.getPoints()) + ", CHAR_LENGTH( DATA_WHOLE_FLAG )))";
                    }
                    curve.put("dataWholeFlag", dataWholeFlag);
                    list.add(curve);
                }
            }

            for (JSONObject value : list) {
                ps.addBatch(getDynamicSQL(value));
            }
            int[] count = ps.executeBatch();  //批量后执行
            // 3.清空缓存
            ps.clearBatch();
            connect.commit();
            log.info("executeBatch finished.");
        } catch (SQLException e) {
            log.error("executeBatch fail:", e);
            throw new RuntimeException(e);
        } finally {
            if (ps != null) {
                try {
                    ps.close();
                } catch (SQLException e) {
                    throw new RuntimeException(e);
                }
            }
        }
    }

    private List<EMpPowerCurveDatamid> buildEMpPowerCurveDataMids() {
        List<EMpPowerCurveDatamid> result = new ArrayList<>();
        EMpPowerCurveDatamid item = new EMpPowerCurveDatamid();
        item.setDate("20240726");
        item.setDensity(15);
        item.setId(12233L);
        item.setMeter(190189101L);
        item.setMpId(12233L);
        item.setOrg("614054101");
        item.setType(1);
        item.setWrite("2024-07-26 07:29:00");
        item.setData("[21, 22, 11, 10]"); // 数据值
//        item.setData("[21,22,11,10,21,22,11,10,21,22,11,10,21,22,11,10,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0]");
        item.setPoints(4);               // 数据个数
        item.setStart(510);             // 开始点位
//        item.setWhole("111111111111111100000000000000000000000000000000000000000000000000000000000000000000000000000000");          // 数据完整性
        item.setWhole("1111");
        result.add(item);
        return result;
    }

    private static String getDynamicSQL(JSONObject value) {
        try {
            String sql = "INSERT INTO e_mp_power_curve_kafka(" +
                    "id," +
                    "data_date," +
                    "data_type," +
                    "data_point_flag," +
                    "data_whole_flag" +
                    value.getString("sqlkey") + ")" +
                    "values(" +
                    value.getLong("id") + ","
                    + value.getString("dataDate") + ","
                    + value.getLong("dataType") + ","
                    + value.getLong("dataPointFlag") + ","
                    + value.getString("dataWholeFlag")
                    + value.getString("sqlvalue") + ")"
                    + " on DUPLICATE KEY UPDATE data_type = 1 , data_whole_flag = "+value.getString("dataWholeFlag")+" "
                    + value.getString("sqlupdate");
            return sql;
        } catch (Exception ex) {
            System.out.println(ex.getMessage());
        }
        return "";
    }
}