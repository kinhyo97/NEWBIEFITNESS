package org.example.service;

import org.example.db.DatabaseUtil;

import java.sql.*;
import java.sql.Date;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.time.temporal.WeekFields;
import java.util.*;


public class StatisticsService {

    public Connection conn;

    // 일요일
    public StatisticsService() {
        try {
            conn = DatabaseUtil.getConnection();
        } catch (SQLException e) {
            System.out.println("DB 연결 실패: " + e.getMessage());
        }
    }

    public String getUserKey_id() {
        String data = "";

        String query = "SELECT user_key, date FROM statistics";

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

    public Map<String, Object> fetchBodyInfo(String userKey, String dateStr) {
        Map<String, Object> info = new LinkedHashMap<>();

        String query = """
            SELECT present_weight, target_calories, diet_type
            FROM user_survey 
            WHERE user_key = ?
            LIMIT 1;
        """;

        try (Connection conn = DatabaseUtil.getConnection();
             PreparedStatement pstmtStats = conn.prepareStatement(query)) {
            pstmtStats.setString(1, userKey);
            ResultSet rsStats = pstmtStats.executeQuery();

            if (rsStats.next()) {
                info.put("present_weight", rsStats.getInt("present_weight"));
                info.put("target_calories", rsStats.getInt("target_calories"));
                info.put("diet_type", rsStats.getString("diet_type"));
            }

            rsStats.close();

        } catch (SQLException se) {
            System.out.println("신체 정보 SQL 예외 발생: " + se);
        }

        return info;
    }



    public Map<String, Integer> fetchWorkoutCountByDay(String dateStr) {
        Map<String, Integer> mapDay = new LinkedHashMap<>();  // LinkedHashMap<> : 입력한 순서대로 Key 보장
        // 요일까지 나오게 포맷 수정
        DateTimeFormatter fullFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd(E)", Locale.KOREAN);

        // 텍스트 블록 """ """
        String query = """
            SELECT DATE(date) AS date_str, SUM(activity_minutes) AS activity_minutes
            FROM statistics
            WHERE date BETWEEN DATE_SUB(STR_TO_DATE(?, '%Y-%m-%d'), INTERVAL DAYOFWEEK(STR_TO_DATE(?, '%Y-%m-%d')) + 1 DAY)
                          AND DATE_ADD(STR_TO_DATE(?, '%Y-%m-%d'), INTERVAL 7 - DAYOFWEEK(STR_TO_DATE(?, '%Y-%m-%d')) DAY)
            GROUP BY date   /* 같은 날짜끼리 묶어서 집계 */
            ORDER BY date ASC
        """;
        try (Connection conn = DatabaseUtil.getConnection();
            PreparedStatement pstmt = conn.prepareStatement(query)) {

            
            for(int i = 1; i <= 4; i++) {  // 일단 여기랑 바로 밑에 for 반복문으로 바꿨는데 i <= 4 조건 검토 필요
                pstmt.setString(i, dateStr);
            }

            try (ResultSet rs = pstmt.executeQuery()) {

                while (rs.next()) {
                    Date sqlDate = rs.getDate("date_str");
                    LocalDate localDate = sqlDate.toLocalDate();

                    String day = localDate.format(fullFormatter);
                    int activityMinutes = rs.getInt("activity_minutes");
                    mapDay.put(day, activityMinutes);
                }
            }
        } catch (SQLException se) {
            System.out.println("운동 시간 SQL 예외 : " + se);
        }

        return mapDay;
    }

    public String getLastRecordedDateBefore(String userKey, String dateStr) {
        String query = """
                SELECT MAX(date)
                FROM statistics
                WHERE user_key = ? AND date < ?
                """;
        String lastRecordedDate = null;

        try (Connection conn = DatabaseUtil.getConnection();
        PreparedStatement pstmt = conn.prepareStatement(query)) {

            pstmt.setString(1, userKey);
            pstmt.setString(2, dateStr);

            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    lastRecordedDate = rs.getString(1); // 컬럼명이 아닌 인덱스명으로도 불러올 수 있네
                }
            }
        } catch (SQLException se) {
            System.out.println("이전 기록 날짜 SQL 예외 : " + se);
        }


        return lastRecordedDate;
    }

    public int totalCalories(String userKey, String dateStr) {
        String query = """
                    SELECT calories
                    FROM eaten_by_date
                    WHERE date = STR_TO_DATE(?, '%Y-%m-%d');
                """;

     // + 그냥 ? 넣으면 문자열로 들어감   */
        int calories = 0;

        try (Connection conn = DatabaseUtil.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(query)) {
            pstmt.setString(1, dateStr);

            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    calories = rs.getInt("calories");
                }
            }
        } catch (SQLException se) {
            System.out.println("칼로리 계산 SQL 예외 : " + se.getMessage());  // getMessage() 주석 필요
        }

        return calories;
    }

    public double changeFatMass(String userKey, String dateStr) {
        String query = """
                    SELECT ROUND(ABS(recent.body_fat - past.body_fat), 2) AS difference_fat
                    FROM
                      -- 기준일 이전 최근 측정값
                      (SELECT body_fat
                       FROM statistics
                       WHERE user_key = ?
                         AND date <= STR_TO_DATE(?, '%Y-%m-%d')
                         AND body_fat IS NOT NULL
                       ORDER BY date DESC
                       LIMIT 1) AS recent,
                
                      -- 기준일 -14일 이전 최근 측정값
                      (SELECT body_fat
                       FROM statistics
                       WHERE user_key = ?
                         AND date <= DATE_SUB(STR_TO_DATE(?, '%Y-%m-%d'), INTERVAL 14 DAY)
                         AND body_fat IS NOT NULL
                       ORDER BY date DESC
                       LIMIT 1) AS past;
                
                """;
        // 지난 주 대비 -> 매일 변화하는 양 평균 구해서 저번 주 대비 비교
        // + sql은 두 개의 그룹 함수를 중첩해서 사용할 수 없음!!
        double differenceFat = 0.0;

        try (Connection conn = DatabaseUtil.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(query)) {
            pstmt.setString(1, userKey);
            pstmt.setString(2, dateStr);
            pstmt.setString(3, userKey);
            pstmt.setString(4, dateStr);

            // ? 갯수만큼 넣어줄 것

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
                    SELECT ROUND(ABS(recent.muscle_mass - past.muscle_mass), 2) AS difference_mass
                    FROM
                      -- 기준일 이전 최근 측정값
                      (SELECT muscle_mass
                       FROM statistics
                       WHERE user_key = ?
                         AND date <= STR_TO_DATE(?, '%Y-%m-%d')
                         AND muscle_mass IS NOT NULL
                       ORDER BY date DESC
                       LIMIT 1) AS recent,
                
                      -- 기준일 -14일 이전 최근 측정값
                      (SELECT muscle_mass
                       FROM statistics
                       WHERE user_key = ?
                         AND date <= DATE_SUB(STR_TO_DATE(?, '%Y-%m-%d'), INTERVAL 14 DAY)
                         AND muscle_mass IS NOT NULL
                       ORDER BY date DESC
                       LIMIT 1) AS past;
                
                """;
        double differenceMuscle = 0.0;

        // try-catch-resource -> 자원 자동 닫기

        try (Connection conn = DatabaseUtil.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(query)) {
            pstmt.setString(1, userKey);
            pstmt.setString(2, dateStr);
            pstmt.setString(3, userKey);
            pstmt.setString(4, dateStr);

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

    public int fetchLatestTargetCalories(String userKey) {
        String query = """
        SELECT target_calories
        FROM user_survey
        WHERE user_key = ?
        ORDER BY updated_at DESC
        LIMIT 1;
    """;

        try (Connection conn = DatabaseUtil.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(query)) {
            pstmt.setString(1, userKey);
            ResultSet rs = pstmt.executeQuery();

            if (rs.next()) {
                return rs.getInt("target_calories");
            }

        } catch (SQLException e) {
            System.out.println("target_calories 조회 SQL 예외: " + e);
        }

        return 0;
    }


    public int percentageAccomplishmentCaloriesWeekly(String userKey, String dateStr) {
        dateStr = dateStr.split("\\(")[0].trim();
        LocalDate selectedDate = LocalDate.parse(dateStr);
        Locale locale = Locale.KOREA;
        LocalDate startDate = selectedDate.with(WeekFields.of(locale).dayOfWeek(), 1);
        LocalDate endDate = selectedDate.with(WeekFields.of(locale).dayOfWeek(), 7);

        LocalDate today = LocalDate.now();
        LocalDate effectiveEnd = endDate.isAfter(today) ? today : endDate;

        int targetCalories = fetchLatestTargetCalories(userKey);
        if (targetCalories == 0) return 0;

        String query = """
        SELECT SUM(calories) AS total_intake
        FROM eaten_by_date
        WHERE date BETWEEN ? AND ?
    """;

        try (Connection conn = DatabaseUtil.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(query)) {

            pstmt.setDate(1, Date.valueOf(startDate));
            pstmt.setDate(2, Date.valueOf(effectiveEnd));

            ResultSet rs = pstmt.executeQuery();
            if (rs.next()) {
                int total = rs.getInt("total_intake");
                System.out.println(total);
                if (rs.wasNull()) return 0;


                int percent = (int) Math.round((double) total / (targetCalories) * 100);
                return Math.min(percent, 100);  // 필요 시 100% 제한
            }
        } catch (SQLException e) {
            System.out.println("주간 eaten_by_date 섭취량 계산 SQL 예외: " + e);
        }

        return 0;
    }



    public boolean shouldEnableCalorieViewToggle(String userKey, String dateStr) {

        if (dateStr == null || dateStr.isBlank()) {
            System.out.println("⚠️ 날짜 문자열이 null이거나 비어있습니다. 기본값으로 LocalDate.now() 사용");
            dateStr = LocalDate.now().toString();  // 또는 적절한 기본값으로 초기화
        }

        dateStr = dateStr.split("\\(")[0].trim();

        LocalDate selectedDate = LocalDate.parse(dateStr);
        Locale locale = Locale.KOREA;
        LocalDate startDate = selectedDate.with(WeekFields.of(locale).dayOfWeek(), 1);
        LocalDate endDate = selectedDate.with(WeekFields.of(locale).dayOfWeek(), 7);

        LocalDate today = LocalDate.now();
        LocalDate effectiveEnd = endDate.isAfter(today) ? today : endDate;

        String query = """
        SELECT COUNT(*) AS cnt
        FROM eaten_by_date
        WHERE date BETWEEN ? AND ?
    """;

        try (Connection conn = DatabaseUtil.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(query)) {

            pstmt.setDate(1, Date.valueOf(startDate));
            pstmt.setDate(2, Date.valueOf(effectiveEnd));

            ResultSet rs = pstmt.executeQuery();
            if (rs.next()) {
                int count = rs.getInt("cnt");
                return count > 1;  // 2일 이상 기록되었을 때만 활성화
            }

        } catch (SQLException e) {
            System.out.println("토글 버튼 활성화 조건 체크 SQL 예외: " + e);
        }

        return false;
    }


    public int percentageAccomplishmentCaloriesToday(String userKey, String dateStr) {
        dateStr = dateStr.split("\\(")[0].trim();
        LocalDate selectedDate = LocalDate.parse(dateStr);

        int targetCalories = fetchLatestTargetCalories(userKey);
        if (targetCalories == 0) return 0;

        String query = """
        SELECT calories
        FROM eaten_by_date
        WHERE date = ?
    """;

        try (Connection conn = DatabaseUtil.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(query)) {

            pstmt.setDate(1, Date.valueOf(selectedDate));

            ResultSet rs = pstmt.executeQuery();
            if (rs.next()) {
                int total = rs.getInt("calories");
                if (rs.wasNull()) return 0;

                int percent = (int) Math.round((double) total / targetCalories * 100);
                return Math.min(percent, 100);
            }
        } catch (SQLException e) {
            System.out.println("오늘 섭취 퍼센트 SQL 예외: " + e);
        }

        return 0;
    }


    public int percentageAccomplishmentCalories(String userKey, String dateStr) {
        if (dateStr == null || dateStr.isBlank()) return 0;

        dateStr = dateStr.split("\\(")[0].trim();
        LocalDate selectedDate = LocalDate.parse(dateStr);

        int targetCalories = fetchLatestTargetCalories(userKey);
        if (targetCalories == 0) return 0;

        String query = """
        SELECT calories
        FROM eaten_by_date
        WHERE date = ?
    """;

        try (Connection conn = DatabaseUtil.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(query)) {

            pstmt.setDate(1, Date.valueOf(selectedDate));

            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    int consumed = rs.getInt("calories");
                    if (rs.wasNull() || consumed == 0) return 0;

                    int percent = (int) Math.round((double) consumed / targetCalories * 100);
                    return Math.min(percent, 100);
                }
            }

        } catch (SQLException se) {
            System.out.println("eaten_by_date 기반 칼로리 달성률 SQL 예외: " + se);
        }

        return 0;
    }


    public Map<String, ArrayList<Object[]>> fetchWeeklyWorkoutGrouped(String userKey, String selectedDate) {
        Map<String, ArrayList<Object[]>> groupedMap = new LinkedHashMap<>();

        String query = """
            SELECT wr.date, e.exercise_name, wr.sets, wr.reps, wr.weight
            FROM workout_record wr
            JOIN exercise e ON wr.exercise_id = e.exercise_id
            WHERE wr.user_key = ?
              AND wr.date BETWEEN DATE_SUB(STR_TO_DATE(?, '%Y-%m-%d'), INTERVAL WEEKDAY(STR_TO_DATE(?, '%Y-%m-%d')) DAY)
                              AND DATE_ADD(STR_TO_DATE(?, '%Y-%m-%d'), INTERVAL (6 - WEEKDAY(STR_TO_DATE(?, '%Y-%m-%d'))) DAY)
            ORDER BY wr.date ASC
        """;

        try (Connection conn =  DatabaseUtil.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(query)) {

            pstmt.setString(1, userKey);
            pstmt.setString(2, selectedDate);
            pstmt.setString(3, selectedDate);
            pstmt.setString(4, selectedDate);
            pstmt.setString(5, selectedDate);

            ResultSet rs = pstmt.executeQuery();

            while (rs.next()) {
                String date = rs.getString("date");
                String[] row = new String[]{
                        date,
                        rs.getString("exercise_name"),
                        String.valueOf(rs.getInt("sets")),
                        String.valueOf(rs.getInt("reps")),
                        String.valueOf(rs.getInt("weight"))
                };

                groupedMap.computeIfAbsent(date, k -> new ArrayList<>()).add(row);
            }
        } catch (SQLException se) {
            System.out.println("이번 주 운동 기록 SQL 예외 : " + se);
        }

        System.out.println("[쿼리 요청 날짜] " + selectedDate);
        System.out.println("[userKey] " + userKey);
        System.out.println("[groupedMap size] " + groupedMap.size());

        return groupedMap;
    }

    public int fetchWeeklyGoalAchievement(String userKey, LocalDate startDate, LocalDate endDate) {
        String query = """
            SELECT
                ROUND(SUM(f.intaked_calories) / (s.target_calories * 7) * 100, 0) AS rate
            FROM user_eaten_food f
            JOIN user_survey s ON f.user_key = s.user_key
            WHERE f.user_key = ?
            AND f.date BETWEEN ? AND ?;
        """;

        try (Connection conn = DatabaseUtil.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(query)) {

            pstmt.setString(1, userKey);
            pstmt.setDate(2, Date.valueOf(startDate));
            pstmt.setDate(3, Date.valueOf(endDate));

            ResultSet rs = pstmt.executeQuery();
            if (rs.next()) {
                return rs.getInt("rate");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return 0;
    }

}

