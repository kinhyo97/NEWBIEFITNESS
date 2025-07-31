package org.example.db;

import java.sql.*;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class JdbcStatistics {
    String url = "jdbc:mariadb://127.0.0.1:3306/newbiefitness";
    String user = "root";
    String password = "1234";
    String query = "SELECT * FROM statistics";
    String mariaUrl = "org.mariadb.jdbc.Driver";

    private Connection conn;

    public JdbcStatistics() {
        try {
            Class.forName(mariaUrl);
            conn = DriverManager.getConnection(url, user, password);
        } catch (ClassNotFoundException e) {
            System.out.println("클래스 로드 오류 : " + e);
        } catch (SQLException se) {
            System.out.println("SQL 예외 발생 : " + se);
        }
    }
    
    public List<String[]> fetchExerciseStatistics() {
        List<String[]> data = new ArrayList<>();

        String query = "SELECT id, day_of_week FROM statistics";

        try (PreparedStatement pstmt = conn.prepareStatement(query); // try-catch-resource -> 자원 자동 닫기
            ResultSet rs = pstmt.executeQuery()) {
            while (rs.next()) {
                String[] row = new String[2];
                row[0] = rs.getString("id");
                row[1] = rs.getString("day_of_week");
                data.add(row);
            }
        } catch (SQLException se2) {
            System.out.println("sql 예외 발생 : " + se2);
        }

        return data;
    }

    public String[] fetchBodyInfo() {
        String[] info = new String[3];

        String query = "SELECT weight, body_fat_after, muscle_mass_after FROM statistics WHERE user_id = 101 ORDER BY date DESC LIMIT 1";
        try (PreparedStatement pstmt = conn.prepareStatement(query);
            ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    info[0] = rs.getString("weight");
                    info[1] = rs.getString("body_fat_after");
                    info[2] = rs.getString("muscle_mass_after");
                }
        } catch (SQLException se) {
            System.out.println("sql 예외 발생 : " + se);
        }

        return info;
    }

    public Map<String, Integer> fetchWorkoutCountByDay() {
        Map<String, Integer> mapDay = new LinkedHashMap<>();  // LinkedHashMap<> : 입력한 순서대로 Key 보장
        // 텍스트 블록 """ """
        String sql = """
                    SELECT day_of_week, workout_count 
                    FROM statistics GROUP BY day_of_week 
                    ORDER BY FIELD(day_of_week, 'Mon', 'Tue', 'Wed', 'Thu', 'Fri', 'Sat', 'Sun')
                """;
        try {
            PreparedStatement pstmt = conn.prepareStatement(sql);
            ResultSet rs = pstmt.executeQuery();
            while (rs.next()) {
                String day = rs.getString("day_of_week");
                int workoutCount = rs.getInt("workout_count");
                mapDay.put(day, workoutCount);
            }
        }catch (SQLException se) {
            System.out.println("SQL 예외 : " + se);
        }

        return mapDay;
    }


    public static void main(String[] args) {
        JdbcStatistics jdbcStat = new JdbcStatistics();
    }
}
