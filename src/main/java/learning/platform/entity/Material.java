package learning.platform.entity;

import jakarta.persistence.*;
import learning.platform.enums.ContentType;
import io.swagger.v3.oas.annotations.media.Schema;

@Entity
@Table(name = "materials")
@Schema(description = "Entidad que representa un material vinculado a una lección.")
public class Material {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Schema(description = "ID único del material.", example = "1")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "lesson_id", nullable = false)
    @Schema(description = "Lección a la que pertenece este material.")
    private Lesson lesson;

    @Column(nullable = false)
    @Schema(description = "Título del material.", example = "Video introductorio")
    private String title;

    @Column(nullable = false)
    @Schema(description = "URL del contenido del material (pdf, video, etc.)", example = "https://example.com/video.mp4")
    private String contentUrl;

    @Enumerated(EnumType.STRING)
    @Column(name = "content_type", nullable = false)
    @Schema(description = "Tipo de contenido del material.")
    private ContentType contentType;

    public Material() {}

    // Getters y Setters:
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public Lesson getLesson() { return lesson; }
    public void setLesson(Lesson lesson) { this.lesson = lesson; }

    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }

    public String getContentUrl() { return contentUrl; }
    public void setContentUrl(String contentUrl) { this.contentUrl = contentUrl; }

    public ContentType getContentType() { return contentType; }
    public void setContentType(ContentType contentType) { this.contentType = contentType; }

    // Equals & hashCode:
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Material)) return false;
        Material that = (Material) o;
        return id != null && id.equals(that.id);
    }

    @Override
    public int hashCode() {
        return getClass().hashCode();
    }

    @Override
    public String toString() {
        return "Material{" +
                "id=" + id +
                ", lessonId=" + (lesson != null ? lesson.getId() : null) +
                ", title='" + title + '\'' +
                ", contentUrl='" + contentUrl + '\'' +
                ", contentType=" + contentType +
                '}';
    }


}
