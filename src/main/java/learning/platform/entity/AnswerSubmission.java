package learning.platform.entity;

import jakarta.persistence.*;

@Entity
@Table(name = "answer_submissions")
public class AnswerSubmission {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "quiz_submission_id", nullable = false)
    private QuizSubmission quizSubmission;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "question_id", nullable = false)
    private Question question;

    // For multiple-choice questions
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "selected_option_id")
    private AnswerOption selectedOption;

    // For open-ended questions (not yet implemented in DTOs)
    @Column(length = 2000)
    private String answerText;

    // Getters y Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public QuizSubmission getQuizSubmission() { return quizSubmission; }
    public void setQuizSubmission(QuizSubmission quizSubmission) { this.quizSubmission = quizSubmission; }
    public Question getQuestion() { return question; }
    public void setQuestion(Question question) { this.question = question; }
    public AnswerOption getSelectedOption() { return selectedOption; }
    public void setSelectedOption(AnswerOption selectedOption) { this.selectedOption = selectedOption; }
    public String getAnswerText() { return answerText; }
    public void setAnswerText(String answerText) { this.answerText = answerText; }
}