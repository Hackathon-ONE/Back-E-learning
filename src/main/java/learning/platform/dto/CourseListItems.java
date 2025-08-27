package learning.platform.dto;

import learning.platform.entity.Course;
import learning.platform.enums.Category;

public record CourseListItems(
        Long id,
        String title,
        String description,
        Category category,
        boolean published,
        Long instructorId
) {
    public CourseListItems(Course course) {
        this(course.getId(), course.getTitle(), course.getDescription(),
                course.getCategory(), course.isPublished(), course.getInstructor().getId());
    }
}
