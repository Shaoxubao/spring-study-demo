package com.baoge.jdbc;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class JDBCUtils {

    private static final String db_url = "jdbc:mysql://127.0.0.1:3306/babytun?useUnicode=true&characterEncoding=UTF-8&useAffectedRows=true&allowMultiQueries=true&useSSL=false&zeroDateTimeBehavior=convertToNull&failOverReadOnly=false&maxReconnects=10&connectTimeout=60000&socketTimeout=60000&serverTimezone=GMT%2B8";
    private static final String db_user = "root";
    private static final String db_password = "123456";

    public static Connection getConnect() {
        Connection connect = null;
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            connect = DriverManager.getConnection(db_url, db_user, db_password);
            connect.setAutoCommit(false);
        } catch (ClassNotFoundException | SQLException e) {
            e.printStackTrace();
        }
        return connect;
    }
}
