package learning.platform.dto;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public class CourseRequestDTO {

    //Validaciones para cursos
    @NotBlank(message = "El título no puede estar vacío.")
    @Size(min = 5, max = 100, message = "El título debe tener entre 5 y 100 caracteres.")
    private String title;

    @NotBlank(message = "La descripción no puede estar vacía.")
    private String description;

    @NotBlank(message = "La categoría no puede estar vacía.")
    private String category;

    @NotNull(message = "El estado de publicación es requerido.")
    private Boolean published;

    @Size(max = 150)
    private String urlPhoto;

    @Size(max = 255)
    private String about;

    //Agregamos los getters y setters


    public @Size(max = 150) String getUrlPhoto() {
        return urlPhoto;
    }

    public void setUrlPhoto(@Size(max = 150) String urlPhoto) {
        this.urlPhoto = urlPhoto;
    }

    public @Size(max = 255) String getAbout() {
        return about;
    }

    public void setAbout(@Size(max = 255) String about) {
        this.about = about;
    }

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