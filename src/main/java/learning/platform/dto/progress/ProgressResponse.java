package learning.platform.dto.progress;

import java.time.Instant;

public class ProgressResponse {
    private Integer id;
    private Integer enrollmentId;
    private Integer lessonId;
    private Boolean completed;
    private Integer score;
    private Instant updatedAt;

    // Constructor
    public ProgressResponse(Integer id, Integer enrollmentId, Integer lessonId,
                            Boolean completed, Integer score, Instant updatedAt) {
        this.id = id;
        this.enrollmentId = enrollmentId;
        this.lessonId = lessonId;
        this.completed = completed;
        this.score = score;
        this.updatedAt = updatedAt;
    }

    // Getters
    public Integer getId() {
        return id;
    }

    public Integer getEnrollmentId() {
        return enrollmentId;
    }

    public Integer getLessonId() {
        return lessonId;
    }

    public Boolean getCompleted() {
        return completed;
    }

    public Integer getScore() {
        return score;
    }

    public Instant getUpdatedAt() {
        return updatedAt;
    }
}
