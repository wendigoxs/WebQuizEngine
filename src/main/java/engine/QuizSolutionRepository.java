package engine;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface QuizSolutionRepository extends JpaRepository<QuizSolution, Long> {

    /*@Query("select QS FROM QuizSolution QS where QS.user.id = :user")
    Page<QuizSolution> findAllByUser(@Param("user") User user, Pageable pageable);*/

    Page<QuizSolution> findAllByUser(User user, Pageable pageable);
}
