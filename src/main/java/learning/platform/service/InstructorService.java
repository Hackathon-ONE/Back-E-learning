package learning.platform.service;

import learning.platform.dto.StudentCourseProgressDTO;
import java.util.List;

public interface InstructorService {
    /**
     * Devuelve la lista de estudiantes con su progreso en los cursos de un instructor
     *
     * @param instructorId ID del instructor
     * @return Lista de StudentCourseProgressDTO
     */
    List<StudentCourseProgressDTO> getStudentsByInstructor(Long instructorId);
}
