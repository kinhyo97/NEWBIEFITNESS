package org.example.service;

import java.sql.*;
import java.util.*;

import org.example.db.DatabaseUtil;
import org.example.model.RoutineDetail;

//루틴 목록 조회 + 선택 책임
public class RoutineService {

    public static Map<Integer, String> getRoutineListByUserKey(String userKey) {
        Map<Integer, String> routines = new LinkedHashMap<>();
        try (Connection conn = DatabaseUtil.getConnection()) {
            PreparedStatement stmt = conn.prepareStatement(
                    "SELECT routine_id, routine_name FROM workout_routine WHERE user_key = ?"
            );
            stmt.setString(1, userKey);
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                routines.put(rs.getInt("routine_id"), rs.getString("routine_name"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return routines;
    }

    //서비스 로직으로써 데이터를
    public static List<RoutineDetail> getRoutineDetails(int routineId) {
        List<RoutineDetail> list = new ArrayList<>(); // 루틴 리스트를 담을 routinedetail dto를 생성
        try (Connection conn = DatabaseUtil.getConnection()) {
            PreparedStatement stmt = conn.prepareStatement(
                    "SELECT e.exercise_name, r.reps, r.weight, r.sets FROM routine_exercise_detail r " +
                            "JOIN exercise e ON r.exercise_id = e.exercise_id WHERE r.routine_id = ?"
            ); // DAO로부터 데이터를 받아서 entity로 반환하면 service가 dto를 생성해서 entity로부터 dto객체에 담는다
            stmt.setInt(1, routineId);
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                list.add(new RoutineDetail(
                        rs.getString("exercise_name"),
                        rs.getString("reps"),
                        rs.getString("weight"),
                        rs.getString("sets")
                ));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }
}