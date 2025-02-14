package com.nari.connect;


import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.hbase.client.Connection;
import org.apache.hadoop.hbase.client.ConnectionFactory;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;
import java.util.Enumeration;
import java.util.Vector;

/**
 * @author wangxing
 * @Description
 * @create 2024-15-25 12:39
 */

@Slf4j
public class HbaseDaoConnectionPool {

    /**
     * 连接池的初始大小
     * 连接池的创建步长
     * 连接池最大的大小
     */
    private int nInitConnectionAmount = 3;
    private int nIncrConnectionAmount = 3;
    private int nMaxConnections = 20;

    /**
     * 存放连接池中数据库连接的向量
     */
    private Vector vcConnections = new Vector();

    /**
     * 注入连接配置
     */
    @Resource
    private Configuration hbaseConfiguration;

    /**
     * 初始化连接
     */
    @PostConstruct
    public void init() {
        createConnections(nInitConnectionAmount);
    }

    /**
     * 获取可用连接
     * @return
     */
    public synchronized Connection getConnection() {
        Connection conn;
        while (null == (conn =getFreeConnection())){
            try {
                wait(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        // 返回获得的可用的连接
        return conn;
    }

    /**
     * 释放连接
     * @param conn
     */
    public synchronized void releaseConnection(Connection conn) {
        ConnectionWrapper connWrapper;
        Enumeration enumerate = this.vcConnections.elements();
        while(enumerate.hasMoreElements()) {
            connWrapper = (ConnectionWrapper) enumerate.nextElement();
            if (conn == connWrapper.getConnection()) {
                connWrapper.setBusy(false);
                break;
            }
        }
    }

    /**
     * 获取可用连接 当前无可用连接则创建 如果已达到最大连接数则返回 null 阻塞后重试获取
     * @return
     */
    private Connection getFreeConnection() {
        Connection conn;
        if (null == (conn = findFreeConnection())) {
            // 创建新连接
            createConnections(nIncrConnectionAmount);
            // 查看是否有可用连接
            if (null == (conn = findFreeConnection())) {
                return null;
            }
        }
        return conn;
    }

    /**
     * 查找可用连接
     * @return
     */
    private Connection findFreeConnection() {
        ConnectionWrapper connWrapper;
        //遍历向量内连接对象
        Enumeration enumerate = vcConnections.elements();
        while (enumerate.hasMoreElements()) {
            connWrapper = (ConnectionWrapper) enumerate.nextElement();
            //判断当前连接是否被占用
            if (!connWrapper.isBusy()) {
                connWrapper.setBusy(true);
                return connWrapper.getConnection();
            }
        }
        // 返回 NULL
        return null;
    }

    /**
     * 创建新连接
     * @param counts
     */
    private void createConnections(int counts) {
        // 循环创建指定数目的数据库连接
        try {
            for (int i = 0; i < counts; i++) {
                if (this.nMaxConnections > 0  && this.vcConnections.size() >= this.nMaxConnections) {
                    log.warn("已达到最大连接数...");
                    break;
                }
                // 创建一个新连接并加到向量
                vcConnections.addElement(new ConnectionWrapper(newConnection()));
            }
        } catch (Exception e) {
            log.error("创建连接失败...");
        }
    }

    /**
     * 创建新连接
     * @return
     */
    private Connection newConnection() {
        /** hbase 连接 */
        Connection conn = null;
        // 创建一个数据库连接
        try {
            conn = ConnectionFactory.createConnection(hbaseConfiguration);
        } catch (Exception e) {
            log.error("HBase 连接失败...");
        }

        // 返回创建的新的数据库连接
        return conn;
    }

    /**
     * 封装连接对象
     */
    @Data
    class ConnectionWrapper {

        /**
         * 数据库连接
         */
        private Connection connection;

        /**
         * 此连接是否正在使用的标志，默认没有正在使用
         */
        private boolean busy = false;

        /**
         * 构造函数，根据一个 Connection 构告一个 PooledConnection 对象
         */
        public ConnectionWrapper(Connection connection) {
            this.connection = connection;
        }
    }

}

