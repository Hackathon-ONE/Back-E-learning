package learning.platform.mapper;

import learning.platform.dto.lesson.LessonCreateRequest;
import learning.platform.dto.lesson.LessonResponse;
import learning.platform.entity.Lesson;
import org.springframework.stereotype.Component;

@Component
public class ProgressMapper {

    // Convierte un DTO en entidad (sin asignar Course)
    public Lesson toEntity(LessonCreateRequest request) {
        Lesson lesson = new Lesson(); // constructor vacío
        lesson.setTitle(request.getTitle());
        lesson.setContentUrl(request.getContentUrl());
        lesson.setContentType(request.getContentType());
        lesson.setOrderIndex(request.getOrderIndex());
        lesson.setDuration(request.getDuration());
        return lesson;
    }

    // Convierte una entidad en DTO de respuesta
    public LessonResponse toResponse(Lesson lesson) {
        return new LessonResponse(lesson);
    }

    // Actualiza los campos simples de una entidad a partir del DTO
    public void updateEntityFromRequest(Lesson lesson, LessonCreateRequest request) {
        lesson.setTitle(request.getTitle());
        lesson.setContentUrl(request.getContentUrl());
        lesson.setContentType(request.getContentType());
        lesson.setOrderIndex(request.getOrderIndex());
        lesson.setDuration(request.getDuration());
        // NOTA: Course no se toca aquí.
    }
}
