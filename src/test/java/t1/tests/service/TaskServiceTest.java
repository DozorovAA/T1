package t1.tests.service;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.junit.jupiter.MockitoSettings;
import org.mockito.quality.Strictness;
import t1.tests.controller.exception.TaskNotFoundException;
import t1.tests.model.Task;
import t1.tests.model.TaskDTO;
import t1.tests.repository.TaskRepository;

import java.util.List;
import java.util.Optional;

import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@MockitoSettings(strictness = Strictness.LENIENT)
class TaskServiceTest {
    @InjectMocks
    private TaskService taskService;
    @Mock
    private TaskRepository taskRepository;

    @BeforeEach
    void before() {
        final Task task = new Task(1L, "Test Task", "Test Desc", Task.Status.UNKNOWN);

        when(taskRepository.findById(1L)).thenReturn(Optional.of(task));
        when(taskRepository.findById(argThat(id -> id != 1L))).thenReturn(Optional.empty());
        when(taskRepository.findAll()).thenReturn(List.of(task));

    }

    @Test
    void testGetByIdSuccess() {
        TaskDTO result = taskService.getTaskById(1L);
        Assertions.assertNotNull(result);
        Assertions.assertEquals("Test Task", result.title());
        Assertions.assertEquals("Test Desc", result.description());
        Assertions.assertEquals("UNKNOWN", result.status());
    }

    @Test
    void testGetByIdIdNotFound() {
        Exception exception = Assertions.assertThrows(TaskNotFoundException.class, () -> taskService.getTaskById(2L));
        Assertions.assertEquals("Task not found", exception.getMessage());
    }

    @Test
    void testGetAllSuccess() {
        List<TaskDTO> result = taskService.getAllTasks();
        Assertions.assertNotNull(result);
        Assertions.assertEquals(1L, result.size());

        TaskDTO dto = result.get(0);
        Assertions.assertEquals("Test Task", dto.title());
        Assertions.assertEquals("Test Desc", dto.description());
        Assertions.assertEquals("UNKNOWN", dto.status());
    }

    @Test
    void testUpdateSuccess() {
        final Task task = new Task(1L, "Test Update Task", "Test Update Desc", Task.Status.IN_PROGRESS);

        when(taskRepository.save(argThat(tas -> tas.getId() == 1L))).thenReturn(task);

        TaskDTO result = taskService.updateTask(1L, task);
        Assertions.assertNotNull(result);

        Assertions.assertEquals("Test Update Task", result.title());
        Assertions.assertEquals("Test Update Desc", result.description());
        Assertions.assertEquals("IN_PROGRESS", result.status());
    }

    @Test
    void testUpdateNotExist() {
        final Task task = new Task(1L, "Test Update Task", "Test Update Desc", Task.Status.IN_PROGRESS);

        when(taskRepository.save(argThat(tas -> tas.getId() == 1L))).thenReturn(task);

        Exception exception = Assertions.assertThrows(TaskNotFoundException.class, () -> taskService.updateTask(2L, task));
        Assertions.assertEquals("Task not found", exception.getMessage());
    }

    @Test
    void testCreateSuccess() {
        final Task task = new Task(1L, "Test Create Task", "Test Create Desc", Task.Status.IN_PROGRESS);

        when(taskRepository.save(any())).thenReturn(task);

        TaskDTO result = taskService.createTask(task);
        Assertions.assertNotNull(result);

        Assertions.assertEquals("Test Create Task", result.title());
        Assertions.assertEquals("Test Create Desc", result.description());
        Assertions.assertEquals("IN_PROGRESS", result.status());
    }
    @Test
    void testDeleteSuccess() {
        when(taskRepository.existsById(1L)).thenReturn(true);
        taskService.deleteTask(1L);
    }

    @Test
    void testDeleteNotFound() {
        when(taskRepository.existsById(1L)).thenReturn(false);
        Exception exception = Assertions.assertThrows(TaskNotFoundException.class, () -> taskService.deleteTask(1L));
        Assertions.assertEquals("Task not found", exception.getMessage());
    }
}
