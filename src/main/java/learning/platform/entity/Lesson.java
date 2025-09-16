package learning.platform.entity;

import jakarta.persistence.*;
import learning.platform.enums.ContentType;
import io.swagger.v3.oas.annotations.media.Schema;

import java.util.Objects;

@Entity
@Table(name = "lessons")
@Schema(description = "Entidad que representa una lección en un curso.")
public class Lesson {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Schema(description = "ID único de la lección.", example = "1")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "course_id", nullable = false)
    @Schema(description = "Curso al que pertenece la lección.")
    private Course course;

    @Column(name = "title")
    @Schema(description = "Título de la lección.", example = "Introducción a Java.")
    private String title;

    @Column(name = "order_index")
    private Integer orderIndex;

    @Column(name = "duration", columnDefinition = "INTERVAL", nullable = false)
    @org.hibernate.annotations.ColumnTransformer(
            write = "? * INTERVAL '1 SECOND'",
            read = "EXTRACT(EPOCH FROM duration)::bigint"
    )
    private Long durationSeconds;

    // Constructor vacío obligatorio para JPA:
    public Lesson() {
    }

    // Getters y setters:
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public Course getCourse() { return course; }
    public void setCourse(Course course) { this.course = course; }

    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }

    public Integer getOrderIndex() { return orderIndex; }
    public void setOrderIndex(Integer orderIndex) { this.orderIndex = orderIndex; }

    public Long getDurationSeconds() { return durationSeconds; }
    public void setDurationSeconds(Long durationSeconds)
    { this.durationSeconds = durationSeconds; }

    // equals y hashCode:
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Lesson)) return false;
        Lesson lesson = (Lesson) o;
        return Objects.equals(id, lesson.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

    // toString:
    @Override
    public String toString() {
        return "Lesson{" +
                "id=" + id +
                ", courseId=" + (course != null ? course.getId() : null) +
                ", title='" + title + '\'' +
                ", orderIndex=" + orderIndex +
                ", durationSeconds=" + durationSeconds +
                '}';
    }
}
