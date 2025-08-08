package org.example.service;

import org.example.db.DatabaseUtil;

import java.sql.*;
import java.util.*;

//운동의 운동부위, 이름을 map으로 받아서 return 해주는 함수
public class ExerciseService {
    public static Map<String, List<String>> getExerciseMapByBodyPart() {
        Map<String, List<String>> map = new LinkedHashMap<>();
        try (Connection conn = DatabaseUtil.getConnection()) {
            PreparedStatement stmt = conn.prepareStatement(
                    "SELECT body_part, exercise_name FROM exercise ORDER BY body_part, exercise_name"
            );
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                String part = rs.getString("body_part");
                String name = rs.getString("exercise_name");
                map.computeIfAbsent(part, k -> new ArrayList<>()).add(name);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return map;
    }



}
