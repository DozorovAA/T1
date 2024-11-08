package t1.tests.service;

import org.springframework.stereotype.Service;
import t1.tests.controller.exception.TaskNotFoundException;
import t1.tests.controller.mapper.TaskMapper;
import t1.tests.model.Task;
import t1.tests.model.TaskDTO;
import t1.tests.repository.TaskRepository;

import java.util.List;

@Service
public class TaskService {
    private final TaskRepository taskRepository;

    public TaskService(TaskRepository taskRepository) {
        this.taskRepository = taskRepository;
    }

    public TaskDTO createTask(Task task) {
        return TaskMapper.toDTO(taskRepository.save(task));
    }

    public TaskDTO getTaskById(Long id) {
        return TaskMapper.toDTO(taskRepository.findById(id)
                .orElseThrow(() -> new TaskNotFoundException("Task not found")));
    }

    public List<TaskDTO> getAllTasks() {
        return taskRepository.findAll().stream().map(TaskMapper::toDTO).toList();
    }

    public TaskDTO updateTask(Long id, Task updatedTask) {
        Task task = findTaskById(id);
        task.setTitle(updatedTask.getTitle());
        task.setDescription(updatedTask.getDescription());
        task.setStatus(updatedTask.getStatus());
        return TaskMapper.toDTO(taskRepository.save(task));
    }
    private Task findTaskById(Long id) {
        return taskRepository.findById(id)
                .orElseThrow(() -> new TaskNotFoundException("Task not found"));
    }

    public void deleteTask(Long id) {
        if (!taskRepository.existsById(id)) {
            throw new TaskNotFoundException("Task not found");
        }
        taskRepository.deleteById(id);
    }
}
