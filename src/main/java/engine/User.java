package engine;

import com.fasterxml.jackson.annotation.JsonProperty;

import javax.persistence.*;
import javax.validation.constraints.Email;
import javax.validation.constraints.Size;
import java.util.Collection;

@Entity
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;

    @Email(regexp = "[a-zA-Z0-9_]+@[a-zA-Z0-9_]+\\.[a-zA-Z0-9_]+")
    String email;

    @Size(min = 5)
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    private String password;

    @OneToMany(mappedBy = "user")
    private Collection<QuizSolution> quizSolutions;

    public Long getId() {
        return id;
    }

    public String getEmail() {
        return email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        //todo hash here
        this.password = password;
    }

    @Override
    public boolean equals(Object obj) {
        return obj instanceof User && ((User) obj).getEmail().equalsIgnoreCase(getEmail());
    }

    @Override
    public int hashCode() {
        return getEmail().toLowerCase().hashCode();
    }

    public Collection<QuizSolution> getQuizSolutions() {
        return quizSolutions;
    }

    public void setQuizSolutions(Collection<QuizSolution> quizSolutions) {
        this.quizSolutions = quizSolutions;
    }
}
