package org.example.model;


//루틴에 있는 각 운동 한 줄을 표현하는 DTO
//데이터 구조만 정의함
//루틴 작성시 사용하는 dto
public class RoutineDetail {
    public String exerciseName;
    public String reps;
    public String weight;
    public String sets;

    public RoutineDetail(String exerciseName, String reps, String weight, String sets) {
        this.exerciseName = exerciseName;
        this.reps = reps;
        this.weight = weight;
        this.sets = sets;
    }
}
