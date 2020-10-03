package engine;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

@Service
public class QuizService {

    private static final int PAGE_SIZE = 10;

    @Autowired
    QuizRepository quizRepository;
    @Autowired
    QuizSolutionRepository quizSolutionRepository;
    @Autowired
    UserService userService;

    public Quiz getQuiz(Long id) {
        return quizRepository.findById(id)
                .orElseThrow(() -> new QuizNotFoundException(id));
    }

    public SolveResponse solveQuiz(Long id, List<Long> answer) {
        return checkQuiz(id, answer)
                ? processSolveQuiz(id)
                : getSolvedFailedResponse();
    }

    private SolveResponse processSolveQuiz(Long id) {
        QuizSolution solution = new QuizSolution(getQuiz(id), userService.getCurrentUser());
        quizSolutionRepository.save(solution);
        return getSolvedOkResponse();
    }

    private boolean checkQuiz(Long id, List<Long> answer) {
        List<Long> storedAnswers = Optional.ofNullable(getQuiz(id).getAnswer()).orElse(Collections.emptyList());
        List<Long> solvedAnswers = Optional.ofNullable(answer).orElse(Collections.emptyList());
        //System.out.printf("zzz %d %s %s \n", id, storedAnswers, solvedAnswers); //todo remove
        return storedAnswers.size() == solvedAnswers.size() && storedAnswers.containsAll(solvedAnswers);
    }

    private SolveResponse getSolvedOkResponse() {
        return new SolveResponse(true, "Congratulations, you're right!");
    }

    private SolveResponse getSolvedFailedResponse() {
        return new SolveResponse(false, "Wrong answer! Please, try again.");
    }

    public Quiz insertQuiz(Quiz quiz) {
        quiz.setAuthor(userService.getCurrentUser());
        return quizRepository.save(quiz);
    }

    /*public Collection<Quiz> getAllQuizzes() {
        return quizRepository.findAll();
    }*/

    public Page<Quiz> getAllQuizzesPaged(int pageNum) {
        PageRequest pageRequest = PageRequest.of(pageNum, PAGE_SIZE, Sort.by("id"));
        return quizRepository.findAll(pageRequest);
    }

    public Page<QuizSolution> getAllQuizzesCompletedPaged(int pageNum) {
        PageRequest pageRequest = PageRequest.of(pageNum, PAGE_SIZE, Sort.by("completedAt").descending());
        return quizSolutionRepository.findAllByUser(userService.getCurrentUser(), pageRequest);
    }

    public boolean removeQuiz(long id) {
        if (!quizRepository.existsById(id)) {
            return false;
        }
        Quiz quiz = quizRepository.findById(id).get();
        if (!quiz.getAuthor().equals(userService.getCurrentUser())) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN);
        }
        quizRepository.deleteById(id);
        return true;

    }
}
