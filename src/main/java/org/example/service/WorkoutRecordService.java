package org.example.service;

import org.example.db.DatabaseUtil;
import org.example.model.WorkoutRecord;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class WorkoutRecordService {
    public static List<WorkoutRecord> loadByDate(String userKey, String date) {
        List<WorkoutRecord> records = new ArrayList<>();
        try (Connection conn = DatabaseUtil.getConnection()) {
            String query = "SELECT e.exercise_name, r.reps, r.weight, r.sets FROM workout_record r " +
                    "JOIN exercise e ON r.exercise_id = e.exercise_id WHERE r.user_key = ? AND r.date = ?";
            PreparedStatement stmt = conn.prepareStatement(query);
            stmt.setString(1, userKey);
            stmt.setString(2, date);
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                records.add(new WorkoutRecord(
                        rs.getString("exercise_name"),
                        rs.getString("reps"),
                        rs.getString("weight"),
                        rs.getString("sets")
                ));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return records;
    }

    public static void saveRecords(String userKey, String date, List<WorkoutRecord> records) {
        try (Connection conn = DatabaseUtil.getConnection()) {
            // 기존 기록 삭제
            PreparedStatement deleteStmt = conn.prepareStatement(
                    "DELETE FROM workout_record WHERE user_key = ? AND date = ?");
            deleteStmt.setString(1, userKey);
            deleteStmt.setString(2, date);
            deleteStmt.executeUpdate();
            deleteStmt.close();

            for (WorkoutRecord rec : records) {
                int exerciseId = getExerciseIdByName(conn, rec.exerciseName);
                if (exerciseId == -1) {
                    System.out.println("❌ 운동 ID 찾을 수 없음: " + rec.exerciseName);
                    continue;
                }

                PreparedStatement insertStmt = conn.prepareStatement(
                        "INSERT INTO workout_record (user_key, exercise_id, reps, weight, sets, date) " +
                                "VALUES (?, ?, ?, ?, ?, ?)"
                );
                insertStmt.setString(1, userKey);
                insertStmt.setInt(2, exerciseId);
                insertStmt.setString(3, rec.reps);
                insertStmt.setString(4, rec.weight);
                insertStmt.setString(5, rec.sets);
                insertStmt.setString(6, date);
                insertStmt.executeUpdate();
                insertStmt.close();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    // 👇 이거 같이 추가해줘 (private 유틸)
    private static int getExerciseIdByName(Connection conn, String name) {
        try (PreparedStatement stmt = conn.prepareStatement(
                "SELECT exercise_id FROM exercise WHERE TRIM(LOWER(exercise_name)) = TRIM(LOWER(?))")) {
            stmt.setString(1, name);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) return rs.getInt("exercise_id");
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return -1;
    }
}
