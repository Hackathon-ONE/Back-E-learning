package learning.platform.entity;

import jakarta.persistence.*;
import learning.platform.dto.lesson.LessonCreateRequest;
import learning.platform.enums.ContentType;
import io.swagger.v3.oas.annotations.media.Schema;
import java.time.Duration;
import java.util.Objects;

@Entity
@Table(name = "lessons")
@Schema(description = "Entidad que representa una lección en un curso.")
public class Lesson {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Schema(description = "ID único de la lección.", example = "1")
    private Long id;

    @ManyToOne(fetch=FetchType.LAZY)
    @JoinColumn(name = "course_id", referencedColumnName = "course_id", nullable=false)
    @Schema(description = "Curso al que pertenece la lección.")
    private Course course;

    @Column(name = "title")
    @Schema(description = "Título de la lección.", example = "Introducción a Java.")
    private String title;

    @Column(name = "content_url")
    @Schema(description = "URL del contenido de la lección", example = "https://example.com/video.mp4")
    private String contentUrl;

    @Enumerated(EnumType.STRING)
    @Column(name = "content_type")
    @Schema(description = "URL del contenido de la lección", example = "https://example.com/video.mp4")
    private ContentType contentType;

    @Column(name = "order_index")
    @Schema(description = "Índice de orden de la lección dentro del curso", example = "1")
    private Integer orderIndex;

    @Column(name = "duration", columnDefinition = "INTERVAL")
    @Schema(description = "Duración de la lección.")
    private Duration duration;

    /**
     * Constructor vacío requerido por Hibernate.
     */
    public Lesson() {
    }

    /**
     * Constructor para crear una lección a partir de un DTO y un curso.
     *
     * @param request DTO con los datos de la lección
     * @param course  Curso al que pertenece la lección
     */

    public Lesson(LessonCreateRequest request, Course course) {
        this.course = course;
        this.title = request.getTitle();
        this.contentUrl = request.getContentUrl();
        this.contentType = request.getContentType();
        this.orderIndex = request.getOrderIndex();
        this.duration = request.getDuration();
    }

    // Getters y Setters:

    public Long getId() {
        return id;
    }
    public void setId(Long id) {
        this.id = id;
    }

    public Course getCourse() {
        return course;
    }
    public void setCourse(Course course) {
        this.course = course;
    }

    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }

    public String getContentUrl() {
        return contentUrl;
    }
    public void setContentUrl(String contentUrl) {
        this.contentUrl = contentUrl;
    }

    public ContentType getContentType() {
        return contentType;
    }
    public void setContentType(ContentType contentType) {
        this.contentType = contentType;
    }

    public Integer getOrderIndex() {
        return orderIndex;
    }
    public void setOrderIndex(Integer orderIndex) {
        this.orderIndex = orderIndex;
    }

    public Duration getDuration() {
        return duration;
    }
    public void setDuration(Duration duration) {
        this.duration = duration;
    }

    // toString:
    @Override
    public String toString() {
        return "Lesson{" +
                "id=" + id +
                ", courseId=" + (course != null ? course.getId() : null) +
                ", title='" + title + '\'' +
                ", contentUrl='" + contentUrl + '\'' +
                ", contentType=" + contentType +
                ", orderIndex=" + orderIndex +
                ", duration=" + duration +
                '}';
    }

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
}

