package learning.platform.dto;

import lombok.Data;

@Data
public class CourseResponseDTO {
    private Long id;
    private String title;
    private String description;
    private String category;
    private boolean published;
    private String instructorName;
}