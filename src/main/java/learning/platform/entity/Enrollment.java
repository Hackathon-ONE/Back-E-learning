package learning.platform.entity;

import jakarta.persistence.*;
import learning.platform.dto.EnrollmentRequest;
import learning.platform.enums.EnrollmentStatus;
import org.hibernate.annotations.CurrentTimestamp;

import java.time.LocalDateTime;

@Entity
@Table(name = "enrollments")
public class Enrollment {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User student;

    @ManyToOne
    @JoinColumn(name = "course_id")
    private Course course;

    @Enumerated(EnumType.STRING)
    private EnrollmentStatus enrollmentStatus;

    @CurrentTimestamp
    private LocalDateTime enrolledAt;

    private int progressPercent;

    public  Enrollment(){}

    public Enrollment(EnrollmentRequest request, Course course, User student) {
        this.student = student;
        this.course = course;
    }

    public Long getId() {
        return id;
    }

    public User getStudent() {
        return student;
    }

    public Course getCourse() {
        return course;
    }

    public EnrollmentStatus getStatus() {
        return enrollmentStatus;
    }

    public LocalDateTime getEnrolledAt() {
        return enrolledAt;
    }

    public int getProgressPercent() {
        return progressPercent;
    }
}
