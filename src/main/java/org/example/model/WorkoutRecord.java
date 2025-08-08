package org.example.model;
//루틴에 있는 각 운동 한 줄을 표현하는 DTO
//데이터 구조만 정의함
//실제 수행기록을 저장하는 dto

public class WorkoutRecord {
    public String exerciseName;
    public String reps;
    public String weight;
    public String sets;

    public WorkoutRecord(String exerciseName, String reps, String weight, String sets) {
        this.exerciseName = exerciseName;
        this.reps = reps;
        this.weight = weight;
        this.sets = sets;
    }
}
