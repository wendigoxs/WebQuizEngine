package engine;

import com.fasterxml.jackson.annotation.JsonIgnore;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.Optional;

@Entity
public class QuizSolution {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @JsonIgnore
    private long idSolution;

    @ManyToOne
    private Quiz quiz;

    @ManyToOne
    private User user;

    private LocalDateTime completedAt;

    public QuizSolution() {
    }

    public QuizSolution(Quiz quiz, User user) {
        this.quiz = quiz;
        this.user = user;
        this.completedAt = LocalDateTime.now();
    }

    public Long getId() {
        return Optional.ofNullable(quiz).map(Quiz::getId).orElse(null);
    }

    public LocalDateTime getCompletedAt() {
        return completedAt;
    }
}
