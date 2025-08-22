package learning.platform.dto;

public record EnrollmentRequest(
        Long studentId,
        Long courseId
) {
}
