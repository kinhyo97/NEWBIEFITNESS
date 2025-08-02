package org.example.service;

import org.example.db.DatabaseUtil;

import java.sql.*;
import java.util.*;


public class StatisticsService {

    private Connection conn;

    public StatisticsService() {
        try {
            conn = DatabaseUtil.getConnection();
        } catch (SQLException e) {
            System.out.println("DB 연결 실패: " + e.getMessage());
        }
    }

    public String getUserKey_id() {
        String data = "";

        String query = "SELECT user_key, day_of_week FROM statistics";

        try (   Connection conn = DatabaseUtil.getConnection();
                PreparedStatement pstmt = conn.prepareStatement(query); // try-catch-resource -> 자원 자동 닫기
                ResultSet rs = pstmt.executeQuery()) {
            while (rs.next()) {
                data = rs.getString("user_key");
            }
        } catch (SQLException se2) {
            System.out.println("유저 키 sql 예외 발생 : " + se2);
        }

        return data;
    }

    // 날짜별로 신체 데이터 받아오기

    public Map<String, String> fetchBodyInfo(String userKey, String dateStr) {
        Map<String, String> info = new LinkedHashMap<>();

        String query = """
                    SELECT stat.date, user.weight_kg, stat.body_fat_after, stat.muscle_mass_after
                    FROM statistics AS stat
                    JOIN user ON stat.user_key = user.user_key
                    WHERE stat.user_key = ? AND stat.date = ?
                    ORDER BY stat.date DESC
                    LIMIT 1;
                """;

        try {
            Connection conn = DatabaseUtil.getConnection();

            PreparedStatement pstmt = conn.prepareStatement(query);
            System.out.println("PreparedStatement 생성 완료");

            pstmt.setString(1, userKey);
            pstmt.setString(2, dateStr);
            System.out.println("파라미터 설정 완료");

            ResultSet rs = pstmt.executeQuery();
            System.out.println("쿼리 실행 성공");

            if (rs.next()) {
                info.put("date", rs.getString("date"));
                info.put("weight_kg", rs.getString("weight_kg"));
                info.put("body_fat_after", rs.getString("body_fat_after"));
                info.put("muscle_mass_after", rs.getString("muscle_mass_after"));
            }

            rs.close();
            pstmt.close();
            conn.close();

        } catch (SQLException se) {
            System.out.println("신체 정보 SQL 예외 발생: " + se);
        }

        return info;
    }


    public Map<String, Integer> fetchWorkoutCountByDay() {
        Map<String, Integer> mapDay = new LinkedHashMap<>();  // LinkedHashMap<> : 입력한 순서대로 Key 보장
        // 텍스트 블록 """ """
        String query = """
                    SELECT day_of_week, activity_minutes
                    FROM statistics GROUP BY day_of_week 
                    ORDER BY FIELD(day_of_week, 'Sun', 'Mon', 'Tue', 'Wed', 'Thu', 'Fri', 'Sat')
                """;
        try (Connection conn = DatabaseUtil.getConnection();
            PreparedStatement pstmt = conn.prepareStatement(query);
            ResultSet rs = pstmt.executeQuery()) {

            while (rs.next()) {
                String day = rs.getString("day_of_week");
                int activityMinutes = rs.getInt("activity_minutes");
                mapDay.put(day, activityMinutes);
            }
        } catch (SQLException se) {
            System.out.println("SQL 예외1 : " + se);
        }

        return mapDay;
    }

    public int totalCalories(String userKey, String dateStr) {
        String query = """
                    SELECT IFNULL(SUM(calories_burned), 0) AS total_calories
                    FROM statistics
                    WHERE user_key = ?
                    AND date BETWEEN DATE_SUB(STR_TO_DATE(?, '%Y-%m-%d'), INTERVAL WEEKDAY(STR_TO_DATE(?, '%Y-%m-%d')) + 1 DAY)
                                  AND DATE_ADD(STR_TO_DATE(?, '%Y-%m-%d'), INTERVAL 6 - WEEKDAY(STR_TO_DATE(?, '%Y-%m-%d')) DAY)
                    ORDER BY date ASC
                """;  /* sql 쿼리 : 지정일 기준으로 일요일부터 토요일까지의 일주일 데이터 가져와줘
        
        IFNULL 함수 의미 -> NULL값일 경우 0으로 처리해줘

        기준일(?, yyyy-MM-dd)을 날짜로 변환한 뒤, WEEKDAY()로 요일 인덱스를 구함
        WEEKDAY()는 월:0, 화:1, ..., 토:5, 일:6
        따라서 +1을 해서 "일요일" 기준으로 보정
        예: 기준일이 목요일(3) → 3+1 = 4일 전 = 해당 주 일요일
        주간 시작일: (기준일 - n일) → 그 주의 일요일
        
        중간 AND는 BETWEEN ~ AND문
         
        기준일 기준으로 이번 주 토요일 계산
        WEEKDAY()는 월:0, ..., 토:5, 일:6
        예: 기준일이 화요일(1)이라면 → 6 - 1 = 5일 뒤 = 해당 주 토요일


      + 그냥 ? 넣으면 문자열로 들어감   */
        int calories = 0;

        try (Connection conn = DatabaseUtil.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(query)) {
            pstmt.setString(1, userKey);
            pstmt.setString(2, dateStr);
            pstmt.setString(3, dateStr);
            pstmt.setString(4, dateStr);
            pstmt.setString(5, dateStr);
            pstmt.setString(6, dateStr);


            // ? 갯수만큼 넣어줄 것

            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    calories = rs.getInt("total_calories");
                }
            }
        } catch (SQLException se) {
            System.out.println("칼로리 계산 SQL 예외 : " + se.getMessage());  // getMessage() 주석 필요
        }

        return calories;
    }

    public double changeFatMass(String userKey, String dateStr) {
        String query = """
                    SELECT SUM(ABS(body_fat_after - body_fat_before)) AS difference_fat 
                    FROM statistics
                    WHERE user_key = ? AND date = ?
                """;
        double differenceFat = 0.0;

        try (Connection conn = DatabaseUtil.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(query)) {
            pstmt.setString(1, userKey);
            pstmt.setString(2, dateStr);

            try (ResultSet rs = pstmt.executeQuery()) {

                if (rs.next()) {
                    differenceFat = rs.getDouble("difference_fat");
                }
            }
        } catch (SQLException se) {
            System.out.println("체지방 SQL 예외 : " + se);
        }

        return differenceFat;
    }

    public double changeMuscleMass(String userKey, String dateStr) {
        String query = """
                    SELECT SUM(ABS(muscle_mass_after - muscle_mass_before)) AS difference_mass 
                    FROM statistics
                    WHERE user_key = ? AND date = ?
                """;
        double differenceMuscle = 0.0;

        // try-catch-resource -> 자원 자동 닫기

        try (Connection conn = DatabaseUtil.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(query)) {
            pstmt.setString(1, userKey);
            pstmt.setString(2, dateStr);

            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    differenceMuscle = rs.getDouble("difference_mass");
                }
            }
        } catch (SQLException se) {
            System.out.println("근육량 SQL 예외 : " + se);
        }

        return differenceMuscle;
    }
    public int percentAccomplish(String userKey, String dateStr) {
        String query = """
                    SELECT ROUND(goal_achievement_rate) AS goal_achievement_rate 
                    FROM statistics
                    WHERE user_key = ? AND date = ?
                """;  // sql 쿼리 : 날짜 던져주면 그 날의 달성률 데이터 가져옴.
        int percentage = 0;

        try (Connection conn = DatabaseUtil.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(query)) {
            pstmt.setString(1, userKey);
            pstmt.setString(2, dateStr);

            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    percentage = rs.getInt("goal_achievement_rate");
                }
            }
        } catch (SQLException se) {
            System.out.println("달성률 SQL 예외 : " + se);
        }

        return percentage;
    }
}
