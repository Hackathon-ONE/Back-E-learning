package learning.platform.service;

import learning.platform.dto.CourseCreateRequest;
import learning.platform.dto.CourseListItems;
import learning.platform.dto.CourseResponse;
import learning.platform.entity.Course;
import learning.platform.entity.User;
import learning.platform.enums.Category;
import learning.platform.mapper.CourseMapper;
import learning.platform.repository.CourseRepository;
import learning.platform.repository.UserRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;


@Service
public class CourseServiceImpl implements CourseService{

    private final UserRepository userRepository;
    private final CourseRepository courseRepository;
    private final CourseMapper courseMapper;

    public CourseServiceImpl(UserRepository userRepository, CourseRepository courseRepository, CourseMapper courseMapper) {
        this.userRepository = userRepository;
        this.courseRepository = courseRepository;
        this.courseMapper = courseMapper;
    }

    @Override
    public CourseResponse create(CourseCreateRequest request) {
        User instructor = userRepository.findById(request.instructorId())
                .orElseThrow(() -> new RuntimeException("Instructor not found"));

        Course course = courseMapper.toEntity(request, instructor);
        return courseMapper.toResponse(courseRepository.save(course));
    }

    @Override
    public CourseResponse getBySlug(String slug) {

        return courseRepository.findBySlug(slug)
                .map(courseMapper::toResponse)
                .orElseThrow(() -> new RuntimeException("Course not found"));
    }

    @Override
    public Page<CourseListItems> getByCategory(Category category, Pageable pageable) {
        return courseRepository.findByCategory(category,pageable).map(CourseListItems::new);
    }

    @Override
    public Page<CourseListItems> listPublished(Pageable pageable) {

        return courseRepository.findByPublishedTrue(pageable)
                .map(CourseListItems::new);
    }

    @Override
    public CourseResponse update(Long id, CourseCreateRequest request) {

        Course course = courseRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Course not found"));

        course.update(request);

        return courseMapper.toResponse(courseRepository.save(course));
    }

    @Override
    public void delete(Long id) {
        courseRepository.deleteById(id);
    }
}
