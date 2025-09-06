package learning.platform.mapper;

import learning.platform.dto.EnrollmentResponse;
import learning.platform.entity.Enrollment;
import org.springframework.stereotype.Component;

@Component
public class EnrollmentMapper {
    public EnrollmentResponse toResponse(Enrollment enrollment) {
        return new EnrollmentResponse(enrollment);
    }
}
