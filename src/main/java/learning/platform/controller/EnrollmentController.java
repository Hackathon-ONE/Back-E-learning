package learning.platform.controller;

import jakarta.validation.Valid;
import learning.platform.config.TokenService;
import learning.platform.dto.EnrollmentRequest;
import learning.platform.dto.EnrollmentResponse;
import learning.platform.service.EnrollmentServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/enrollment")
public class EnrollmentController {

    @Autowired
    private EnrollmentServiceImpl enrollmentService;

    @Autowired
    private TokenService tokenService;

    @PostMapping("/create")
    public ResponseEntity<EnrollmentResponse> createEnrollment(@Valid @RequestBody EnrollmentRequest datos){
        var enrollment = enrollmentService.enrollStudent(datos);
        return ResponseEntity.ok(enrollment);
    }

    @PutMapping("/complete/{enrollmentId}")
    public ResponseEntity<EnrollmentResponse> endEnrollment(@PathVariable Long enrollmentId){
        var erollment = enrollmentService.completeCourse(enrollmentId);
        return  ResponseEntity.ok(erollment);
    }

    @PutMapping("/cancel/{enrollmentId}")
    public ResponseEntity<EnrollmentResponse> cancellEnrollment(@PathVariable Long enrollmentId){
        var erollment = enrollmentService.cancelCourse(enrollmentId);
        return  ResponseEntity.ok(erollment);
    }

    @GetMapping("/my-enrollments")
    public ResponseEntity<Page<EnrollmentResponse>> allMyEnrolls(@RequestHeader("Authorization") String token, Pageable pageable){
        return ResponseEntity.ok(enrollmentService.findByStudent(tokenService.getClaimId(token), pageable));
    }

    @GetMapping("/course-enrollments")
    public ResponseEntity<Page<EnrollmentResponse>> courseEnrolls(@RequestHeader("Authorization") String token, Pageable pageable){
        return ResponseEntity.ok(enrollmentService.findByCourse(tokenService.getClaimId(token), pageable));
    }

    @DeleteMapping("/{enrollmentId}")
    public ResponseEntity delateEnrollment(@PathVariable Long enrollmentId){
        enrollmentService.deleteEnrollment(enrollmentId);
        return ResponseEntity.ok(HttpStatus.ACCEPTED);
    }

}
