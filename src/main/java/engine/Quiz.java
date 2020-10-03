package engine;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

@Entity
public class Quiz {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @NotBlank
    private String title;

    @NotBlank
    private String text;

    @ElementCollection
    @NotNull
    @Size(min = 2)
    private List<String> options = new ArrayList<>();

    @ElementCollection
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
//    @JsonInclude(JsonInclude.Include.NON_NULL)
    private List<Long> answer;

    @ManyToOne
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    private User author;

    @OneToMany(mappedBy = "quiz", cascade = CascadeType.REMOVE)
    @JsonIgnore
    private Collection<QuizSolution> quizSolutions;

    public Quiz() {
    }

    public Quiz(String title, String text, List<String> options, List<Long> answer) {
        this.title = title;
        this.text = text;
        this.options = options;
        this.answer = answer;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public String getText() {
        return text;
    }

    public List<String> getOptions() {
        return options;
    }

    public List<Long> getAnswer() {
        return answer;
    }

    public User getAuthor() {
        return author;
    }

    public void setAuthor(User author) {
        this.author = author;
    }


    public Collection<QuizSolution> getQuizSolutions() {
        return quizSolutions;
    }

    public void setQuizSolutions(Collection<QuizSolution> quizSolution) {
        this.quizSolutions = quizSolution;
    }
}
