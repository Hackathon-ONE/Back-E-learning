package learning.platform.dto.lesson;

import ch.qos.logback.core.util.Duration;
import learning.platform.entity.Lesson;
import learning.platform.enums.ContentType;

public class LessonResponse {

    private Integer id;
    private Integer courseId;
    private String title;
    private String contentUrl;
    private ContentType contentType;
    private Integer orderIndex;
    private Duration duration;

    public LessonResponse(Lesson lesson) {

    }

    // Getters and Setters:
    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getCourseId() {
        return courseId;
    }

    public void setCourseId(Integer courseId) {
        this.courseId = courseId;
    }

    public String getTitle() {
        return title;
    }
    public void setTitle(String title) {
        this.title = title;
    }

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
