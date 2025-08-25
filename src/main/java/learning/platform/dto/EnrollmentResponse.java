package learning.platform.dto;

import learning.platform.entity.Enrollment;
import learning.platform.enums.EnrollmentStatus;

import java.time.LocalDateTime;

public record EnrollmentResponse(
      Long id,
      Long studentId,
      Long courseId,
      EnrollmentStatus enrollmentStatus,
      LocalDateTime enrolledAt,
      int progressPercent
) {
    public EnrollmentResponse(Enrollment enrollment) {
        this(enrollment.getId(), enrollment.getStudent().getId(),
                enrollment.getCourse().getId(), enrollment.getStatus(),
                enrollment.getEnrolledAt(), enrollment.getProgressPercent());
    }
}
