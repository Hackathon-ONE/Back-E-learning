package learning.platform.service;

import learning.platform.dto.CourseCreateRequest;
import learning.platform.dto.CourseListItems;
import learning.platform.dto.CourseResponse;
import learning.platform.enums.Category;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;


public interface CourseService {
    CourseResponse create(CourseCreateRequest request);
    CourseResponse getBySlug(String slug);
    Page<CourseListItems> getByCategory(Category category, Pageable pageable);
    Page<CourseListItems> listPublished(Pageable pageable);
    CourseResponse update(Long id, CourseCreateRequest request);
    void delete(Long id);
}
