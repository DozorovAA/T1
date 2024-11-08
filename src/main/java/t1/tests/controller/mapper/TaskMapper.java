package t1.tests.controller.mapper;

import t1.tests.model.Task;
import t1.tests.model.TaskDTO;
public class TaskMapper {
    public static TaskDTO toDTO(Task task) {
        return new TaskDTO(task.getTitle(), task.getDescription(), task.getStatus().name());
    }
}
