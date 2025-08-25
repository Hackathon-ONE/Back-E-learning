package learning.platform.dto;

import lombok.Data;

@Data
public class CourseRequestDTO {
    private String title;
    private String description;
    private String category;
    private Boolean published;
}