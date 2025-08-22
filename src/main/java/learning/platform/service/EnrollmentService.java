package learning.platform.service;

import learning.platform.dto.EnrollmentRequest;
import learning.platform.dto.EnrollmentResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface EnrollmentService {
    EnrollmentResponse enrollStudent(EnrollmentRequest request);
    Page<EnrollmentResponse> findByStudent(Long studentId, Pageable pageable);
    Page<EnrollmentResponse> findByCourse(Long courseId, Pageable pageable);
}
