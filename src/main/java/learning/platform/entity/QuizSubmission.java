package learning.platform.entity;

import jakarta.persistence.*;
import java.time.LocalDateTime;
import java.util.List;

@Entity
@Table(name = "quiz_submissions")
public class QuizSubmission {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "quiz_id", nullable = false)
    private Quiz quiz;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user; // The student who submitted the quiz

    @Column(nullable = false)
    private Integer score;

    @Column(nullable = false)
    private Double percentage;

    @Column(nullable = false)
    private Boolean passed;

    @Column(name = "submitted_at", nullable = false)
    private LocalDateTime submittedAt = LocalDateTime.now();

    @OneToMany(mappedBy = "quizSubmission", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<AnswerSubmission> answers;

    // Getters y Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public Quiz getQuiz() { return quiz; }
    public void setQuiz(Quiz quiz) { this.quiz = quiz; }
    public User getUser() { return user; }
    public void setUser(User user) { this.user = user; }
    public Integer getScore() { return score; }
    public void setScore(Integer score) { this.score = score; }
    public Double getPercentage() { return percentage; }
    public void setPercentage(Double percentage) { this.percentage = percentage; }
    public Boolean getPassed() { return passed; }
    public void setPassed(Boolean passed) { this.passed = passed; }
    public LocalDateTime getSubmittedAt() { return submittedAt; }
    public void setSubmittedAt(LocalDateTime submittedAt) { this.submittedAt = submittedAt; }
    public List<AnswerSubmission> getAnswers() { return answers; }
    public void setAnswers(List<AnswerSubmission> answers) { this.answers = answers; }
}