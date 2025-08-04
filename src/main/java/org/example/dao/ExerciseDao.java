package org.example.dao;

import org.example.db.DBUtil;
import org.example.model.Exercise;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ExerciseDao {

    private static final String MYSQLURL = "jdbc:mariadb://localhost:3306/newbiehealth";
    private static final String USER = "root";
    private static final String PASSWORD = "1234";

    public List<Exercise> searchByKeyword(String keyword) {

        DBUtil dbUtil = new DBUtil();
        try {
            dbUtil.getConnection();
        } catch (SQLException e) {
            System.out.println("오류 " + e);
        }

        List<Exercise> list = new ArrayList<>();
        String sql = "SELECT * FROM exercise WHERE exercise_name LIKE ? OR target_muscle LIKE ? OR body_part LIKE ? OR type LIKE ?";

        try(Connection conn = DriverManager.getConnection(MYSQLURL, USER, PASSWORD );
            PreparedStatement stmt = conn.prepareStatement(sql)) {

           String likeKeyword = "%" + keyword + "%";
           for (int i = 1; i <= 4; i++) {
               stmt.setString(i, likeKeyword);
           }


           ResultSet rs = stmt.executeQuery();
           while (rs.next()) {
               list.add(new Exercise(
                   rs.getInt("machine_id"),
                       rs.getString("exercise_name"),
                       rs.getString("type"),
                       rs.getString("target_muscle"),
                       rs.getString("body_part"),
                       rs.getString("difficulty"),
                       rs.getString("image_url"),
                       rs.getString("video_url")
               ));
           }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return list;
    }
    public static List<Exercise> getAllExercises() {
        List<Exercise> exercises = new ArrayList<>();

        String sql = "SELECT * FROM exercise";

        try (Connection conn = DBUtil.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql);
             ResultSet rs = pstmt.executeQuery()) {

            while (rs.next()) {
                Exercise exercise = new Exercise(
                        rs.getInt("machine_id"),
                        rs.getString("exercise_name"),
                        rs.getString("type"),
                        rs.getString("target_muscle"),
                        rs.getString("body_part"),
                        rs.getString("difficulty"),
                        rs.getString("image_url"),
                        rs.getString("video_url")
                );

                exercises.add(exercise);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return exercises;
    }

}

