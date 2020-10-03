package engine;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import javax.validation.constraints.Min;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api")
public class QuizController {

    @Autowired
    QuizService quizService;

    /*@GetMapping("/quizzes")
    public Collection<Quiz> getQuizzes() {
        return quizService.getAllQuizzes();
    }*/

    @GetMapping("/quizzes")
    public Page<Quiz> getQuizzes(@RequestParam @Min(0) Integer page) {
        return quizService.getAllQuizzesPaged(page);
    }

    @GetMapping("/quizzes/completed")
    public Page<QuizSolution> getQuizzesCompleted(@RequestParam @Min(0) Integer page) {
        return quizService.getAllQuizzesCompletedPaged(page);
    }

    @PostMapping("/quizzes")
    public Quiz createQuiz(@RequestBody @Valid Quiz quiz) {
        return quizService.insertQuiz(quiz);
    }

    @GetMapping("/quizzes/{id}")
    public Quiz getQuiz(@PathVariable Long id) {
        return quizService.getQuiz(id);
    }

    @PostMapping("/quizzes/{id}/solve")
    public SolveResponse solveQuiz(@PathVariable long id, @RequestBody Map<String, List<Long>> answerBody) {
        List<Long> answer = answerBody.get("answer");
        return quizService.solveQuiz(id, answer);
    }

    @DeleteMapping("/quizzes/{id}")
    public ResponseEntity<?> deleteQuiz(@PathVariable long id) {
        boolean isOkRemoved = quizService.removeQuiz(id);
        return isOkRemoved
                ? new ResponseEntity<Void>(HttpStatus.NO_CONTENT)
                : new ResponseEntity<Void>(HttpStatus.NOT_FOUND);
    }
}
