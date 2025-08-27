package learning.platform.repository;

import learning.platform.entity.Course;
import learning.platform.enums.Category;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface CourseRepository extends JpaRepository<Course, Long> {
    Page<Course> findByPublishedTrue(Pageable pageable);

    Page<Course> findByCategory(Pageable pageable, Category category);


    Optional<Course> findBySlug(String slug);
}
