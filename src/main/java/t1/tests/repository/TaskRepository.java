package t1.tests.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import t1.tests.model.Task;
@Repository
public interface TaskRepository extends JpaRepository<Task, Long> {
}
