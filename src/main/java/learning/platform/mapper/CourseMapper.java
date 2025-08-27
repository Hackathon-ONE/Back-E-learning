package learning.platform.mapper;

import learning.platform.dto.CourseCreateRequest;
import learning.platform.dto.CourseResponse;
import learning.platform.entity.Course;
import learning.platform.entity.User;
import org.springframework.stereotype.Component;

@Component
public class CourseMapper {

    public Course toEntity(CourseCreateRequest request, User instructor){
        var course = new Course(request,instructor);
        return course;
    }

    public CourseResponse toResponse(Course course) {
        return new CourseResponse(course);
    }
}
