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
    private String url;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    @Schema(description = "Tipo de contenido del material.")
    private ContentType type;

    public Material() {}

    // Getters y Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public Lesson getLesson() { return lesson; }
    public void setLesson(Lesson lesson) { this.lesson = lesson; }

    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }

    public String getUrl() { return url; }
    public void setUrl(String url) { this.url = url; }

    public ContentType getType() { return type; }
    public void setType(ContentType type) { this.type = type; }
}
