package learning.platform.entity;

import jakarta.persistence.*;
import learning.platform.dto.lesson.LessonCreateRequest;
import learning.platform.enums.ContentType;
import java.time.Duration;

@Entity
@Table(name = "lessons")
public class Lesson {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch=FetchType.LAZY)
    @JoinColumn(name = "course_id", referencedColumnName = "course_id", nullable=false)
    private Course course;

    @Column(name = "title")
    private String title;

    @Column(name = "content_url")
    private String contentUrl;

    @Enumerated(EnumType.STRING)
    @Column(name = "content_type")
    private ContentType contentType;

    @Column(name = "order_index")
    private Integer orderIndex;

    @Column(name = "duration", columnDefinition = "INTERVAL")
    private Duration duration;

    public Lesson(LessonCreateRequest request, Course title) {
        this.title = request.getTitle();
        this.contentUrl = request.getContentUrl();
        this.contentType = request.getContentType();
        this.orderIndex = request.getOrderIndex();
        this.duration = request.getDuration();
    }

    public Lesson() {
        // Constructor vacío requerido por Hibernate
    }

    // Getters and Setters:
    
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
}

