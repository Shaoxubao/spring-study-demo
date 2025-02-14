package com.nari.dao;

import com.nari.connect.HbaseDaoConnectionPool;
import org.apache.hadoop.hbase.TableName;
import org.apache.hadoop.hbase.client.*;

import java.io.IOException;

public class HbaseDao {

    private HbaseDaoConnectionPool pool;

    /**
     * 创建表(不允许重复创建)
     */
    public void createTable(String tb_name){
        Connection conn = null;
        //获取连接
        try {
            conn = pool.getConnection();
            Admin admin = conn.getAdmin();
            TableName tableName = TableName.valueOf(tb_name);
            if (!admin.tableExists(tableName)){
                //指定表名
                TableDescriptorBuilder tdb = TableDescriptorBuilder.newBuilder(tableName);
                //添加列族(entityId,key,ts,val)
                ColumnFamilyDescriptor entityId = ColumnFamilyDescriptorBuilder.of("entityId");
                ColumnFamilyDescriptor key = ColumnFamilyDescriptorBuilder.of("key");
                ColumnFamilyDescriptor ts = ColumnFamilyDescriptorBuilder.of("ts");
                ColumnFamilyDescriptor val = ColumnFamilyDescriptorBuilder.of("val");
                tdb.setColumnFamily(entityId);
                tdb.setColumnFamily(key);
                tdb.setColumnFamily(ts);
                tdb.setColumnFamily(val);
                //创建表
                TableDescriptor td = tdb.build();
                admin.createTable(td);
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        } finally {
            if (null != conn){
                pool.releaseConnection(conn);
            }
        }
    }

}
