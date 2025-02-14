package com.nari.connect;

import org.apache.hadoop.hbase.HBaseConfiguration;
import org.apache.hadoop.hbase.client.Connection;
import org.apache.hadoop.hbase.client.ConnectionFactory;

public class HbaseConnection {

    /**
     * 创建连接
     * @return
     */
    private Connection connection() {
        /** hbase 连接 */
        Connection conn = null;
        // 创建一个数据库连接
        try {
            conn = ConnectionFactory.createConnection(hbaseConfiguration());
        } catch (Exception e) {
            System.out.println("HBase 连接失败...");
        }

        // 返回创建的新的数据库连接
        return conn;
    }

    public org.apache.hadoop.conf.Configuration hbaseConfiguration() {
        org.apache.hadoop.conf.Configuration conf = HBaseConfiguration.create();
        conf.set("hbase.zookeeper.quorum", "");
        conf.set("hbase.zookeeper.property.clientPort", "");
        conf.set("hbase.master.port", String.valueOf(0));
        conf.set("hbase.master.info.port", "16010");
        conf.set("hbase.client.keyvalue.maxsize", "20971520");
        return HBaseConfiguration.create(conf);
    }
}
