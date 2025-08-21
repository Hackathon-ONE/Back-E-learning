package learning.platform.dto;

import learning.platform.entity.Course;
import learning.platform.enums.Category;

public record CourseResponse(
        Long id,
        String title,
        String description,
        Category category,
        boolean published,
        Long instructorId
) {
    public CourseResponse(Course course) {
        this(course.getId(), course.getTitle(), course.getDescription(),
                course.getCategory(), course.isPublished(), course.getInstructor().getId());
    }
}
