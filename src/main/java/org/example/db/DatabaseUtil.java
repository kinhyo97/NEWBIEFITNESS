// 📁 위치: src/main/java/org/example/db/DatabaseUtil.java

package org.example.db;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DatabaseUtil {
    private static final String URL = "jdbc:mariadb://localhost:3306/newbiehealth";
    private static final String USER = "root";
    private static final String PASSWORD = "1234";
    private static final String DRIVER = "org.mariadb.jdbc.Driver";

    static {
        try {
            Class.forName(DRIVER); // 드라이버는 한 번만 로드하면 됨
        } catch (ClassNotFoundException e) {
            System.err.println("JDBC 드라이버 로드 실패: " + e.getMessage());
        }
    }

    public static Connection getConnection() throws SQLException {
        return DriverManager.getConnection(URL, USER, PASSWORD);
    }
}
