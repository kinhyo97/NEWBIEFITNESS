package org.example.model;

public class Exercise {
    private int machineId;
    private String exerciseName;
    private String type;
    private String targetMuscle;
    private String bodyPart;
    private String difficulty;
    private String imageUrl;
    private String videoUrl;

    // 생성자
    public Exercise(int machineId, String exerciseName, String type, String targetMuscle, String bodyPart,
                    String difficulty, String imageUrl, String videoUrl) {
        this.machineId = machineId;
        this.exerciseName = exerciseName;
        this.type = type;
        this.targetMuscle = targetMuscle;
        this.bodyPart = bodyPart;
        this.difficulty = difficulty;
        this.imageUrl = imageUrl;
        this.videoUrl = videoUrl;
    }

    // Getter 메서드
    public int getMachineId() { return machineId; }
    public String getExerciseName() { return exerciseName; }
    public String getType() { return type; }
    public String getTargetMuscle() { return targetMuscle; }
    public String getBodyPart() { return bodyPart; }
    public String getDifficulty() { return difficulty; }
    public String getImageUrl() { return imageUrl; }
    public String getVideoUrl() { return videoUrl; }
}