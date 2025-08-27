package learning.platform.dto;

import jakarta.validation.constraints.NotNull;
import learning.platform.enums.Category;

public record CourseCreateRequest(

        @NotNull
        String title,

        String description,

        @NotNull
        Category category,

        @NotNull
        Long instructorId
) {
}
