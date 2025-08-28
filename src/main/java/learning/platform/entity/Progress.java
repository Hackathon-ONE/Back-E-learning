package learning.platform.entity;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.*;

import java.time.Instant;

@Entity
@Table(name = "progress")
@Schema(description = "Entidad que representa el progreso de un usuario en una lección dentro" +
        "de un curso.")
public class Progress {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Schema(description = "ID único del progreso.")
    private Integer id;

    @ManyToOne
    @JoinColumn(name = "enrollment_id", nullable = false)
    @Schema(description = "Inscripción asociada al progreso.")
    private Enrollment enrollment;

    @ManyToOne
    @JoinColumn(name = "lesson_id")
    @Schema(description = "Lección asociada al progreso.")
    private Lesson lesson;

    @Column(name = "completed")
    @Schema(description = "Indica si la lección se completó o no.")
    private Boolean completed;

    @Column(name = "score")
    @Schema(description = "Puntaje obtenido (OPCIONAL).")
    private Integer score;

    @Column(name = "updated_at")
    @Schema(description = "Fecha y hora de la última actualización.")
    private Instant updatedAt;

    // Constructor vacío requerido por Hibernate
    public Progress() {
    }

    // Getters and Setters:
    public Integer getId() {
        return id;
    }
    public void setId(Integer id) {
        this.id = id;
    }

    public Enrollment getEnrollment() {
        return enrollment;
    }
    public void setEnrollment(Enrollment enrollment) {
        this.enrollment = enrollment;
    }

    public Lesson getLesson() {
        return lesson;
    }
    public void setLesson(Lesson lesson) {
        this.lesson = lesson;
    }

    public Boolean getCompleted() {
        return completed;
    }
    public void setCompleted(Boolean completed) {
        this.completed = completed;
    }

    public Integer getScore() {
        return score;
    }
    public void setScore(Integer score) {
        this.score = score;
    }

    public Instant getUpdatedAt() {
        return updatedAt;
    }
    public void setUpdatedAt(Instant updatedAt) {
        this.updatedAt = updatedAt;
    }
}
