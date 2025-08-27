package learning.platform.entity;

import jakarta.persistence.*;
import learning.platform.enums.Category;
import lombok.Getter;

@Entity
@Table(name = "courses")
public class Course {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Getter
    private Long id;

    @Getter
    private String title;
    @Getter
    private String description;

    @Enumerated(EnumType.STRING)
    private Category category;
    private String slug;
    @Getter
    private boolean published;

    @Getter
    @ManyToOne
    @JoinColumn(name = "instructor_id")
    private User instructor;

    public Course() {
    }

    public Long getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public String getDescription() {
        return description;
    }

    public Category getCategory() {
        return category;
    }

    public String getSlug() {
        return slug;
    }

    public boolean isPublished() {
        return published;
    }

    public User getInstructor() {
        return instructor;
    }


}