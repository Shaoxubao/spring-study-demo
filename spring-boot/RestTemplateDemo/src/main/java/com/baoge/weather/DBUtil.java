package com.baoge.weather;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.Properties;

public class DBUtil {
    private static final Logger log = LoggerFactory.getLogger(DBUtil.class);
    private static String driver;
    private static String url;
    private static String username;
    private static String password;

    static {
        try {
            Properties prop = new Properties();
            prop.load(DBUtil.class.getClassLoader().getResourceAsStream("db.properties"));
            driver = prop.getProperty("db.driver");
            url = prop.getProperty("db.url");
            username = prop.getProperty("db.username");
            password = prop.getProperty("db.password");
            Class.forName(driver);
        } catch (Exception e) {
            log.error("加载数据库配置失败", e);
        }
    }

    public static Connection getConnection() throws SQLException {
        return DriverManager.getConnection(url, username, password);
    }

    public static void close(Connection conn, PreparedStatement ps) {
        try { if (ps != null) ps.close(); } catch (SQLException e) { log.error("关闭Statement失败", e); }
        try { if (conn != null) conn.close(); } catch (SQLException e) { log.error("关闭Connection失败", e); }
    }
}