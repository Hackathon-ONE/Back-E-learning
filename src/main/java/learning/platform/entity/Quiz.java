package learning.platform.entity;

import jakarta.persistence.*;
import java.util.List;
import java.time.LocalDateTime;

/**
 * Representa un cuestionario o examen dentro de la plataforma de aprendizaje.
 * Un Quiz puede estar asociado a un curso o a una lección, pero no a ambos.
 * Contiene una lista de preguntas.
 */
@Entity
@Table(name = "quizzes")
public class Quiz {
    /**
     * Identificador único del quiz.
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /**
     * Título del quiz. No puede ser nulo y tiene un tamaño máximo de 255 caracteres.
     */
    @Column(nullable = false, length = 255)
    private String title;

    /**
     * Descripción opcional del quiz, proporcionando contexto adicional.
     */
    @Column(length = 1000)
    private String description;

    /**
     * Relación con la entidad {@link Course}. Un quiz puede pertenecer a un curso.
     * Esta relación es opcional.
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "course_id")
    private Course course;

    /**
     * Relación con la entidad {@link Lesson}. Un quiz puede pertenecer a una lección.
     * Esta relación es opcional y excluyente con la de 'course'.
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "lesson_id")
    private Lesson lesson;

    /**
     * Lista de preguntas asociadas a este quiz.
     * La cascada de tipo ALL asegura que las operaciones (guardar, actualizar, eliminar)
     * se propaguen a las preguntas y sus opciones. 'orphanRemoval' elimina preguntas
     * que ya no están asociadas a un quiz.
     */
    @OneToMany(mappedBy = "quiz", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Question> questions;

    /**
     * Fecha y hora de creación del quiz. Se establece automáticamente al crear la entidad.
     */
    @Column(name = "created_at", nullable = false)
    private LocalDateTime createdAt = LocalDateTime.now();

    /**
     * Fecha y hora de la última actualización del quiz.
     */
    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    // --- Getters y Setters ---

    /**
     * Obtiene el identificador único del quiz.
     * @return El ID del quiz.
     */
    public Long getId() {
        return id;
    }

    /**
     * Establece el identificador único del quiz.
     * @param id El ID a establecer.
     */
    public void setId(Long id) {
        this.id = id;
    }

    /**
     * Obtiene el título del quiz.
     * @return El título del quiz.
     */
    public String getTitle() {
        return title;
    }

    /**
     * Establece el título del quiz.
     * @param title El título a establecer.
     */
    public void setTitle(String title) {
        this.title = title;
    }

    /**
     * Obtiene la descripción del quiz.
     * @return La descripción del quiz.
     */
    public String getDescription() {
        return description;
    }

    /**
     * Establece la descripción del quiz.
     * @param description La descripción a establecer.
     */
    public void setDescription(String description) {
        this.description = description;
    }

    /**
     * Obtiene el curso asociado al quiz.
     * @return La entidad {@link Course} asociada.
     */
    public Course getCourse() {
        return course;
    }

    /**
     * Establece el curso asociado al quiz.
     * @param course La entidad {@link Course} a asociar.
     */
    public void setCourse(Course course) {
        this.course = course;
    }

    /**
     * Obtiene la lección asociada al quiz.
     * @return La entidad {@link Lesson} asociada.
     */
    public Lesson getLesson() {
        return lesson;
    }

    /**
     * Establece la lección asociada al quiz.
     * @param lesson La entidad {@link Lesson} a asociar.
     */
    public void setLesson(Lesson lesson) {
        this.lesson = lesson;
    }

    /**
     * Obtiene la lista de preguntas del quiz.
     * @return La lista de entidades {@link Question}.
     */
    public List<Question> getQuestions() {
        return questions;
    }

    /**
     * Establece la lista de preguntas para el quiz.
     * @param questions La lista de entidades {@link Question} a establecer.
     */
    public void setQuestions(List<Question> questions) {
        this.questions = questions;
    }

    /**
     * Obtiene la fecha y hora de creación del quiz.
     * @return La fecha de creación.
     */
    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    /**
     * Establece la fecha y hora de creación del quiz.
     * @param createdAt La fecha de creación a establecer.
     */
    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    /**
     * Obtiene la fecha y hora de la última actualización del quiz.
     * @return La fecha de actualización.
     */
    public LocalDateTime getUpdatedAt() {
        return updatedAt;
    }

    /**
     * Establece la fecha y hora de la última actualización del quiz.
     * @param updatedAt La fecha de actualización a establecer.
     */
    public void setUpdatedAt(LocalDateTime updatedAt) {
        this.updatedAt = updatedAt;
    }
}