package learning.platform.dto;

// No se importa lombok.Data
public class CourseRequestDTO {
    private String title;
    private String description;
    private String category;
    private Boolean published;

    // --- Getters y Setters añadidos manualmente ---

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public Boolean getPublished() {
        return published;
    }

    public void setPublished(Boolean published) {
        this.published = published;
    }
}