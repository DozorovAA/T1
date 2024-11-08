package t1.tests.controller;


import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import t1.tests.model.Task;
import t1.tests.repository.TaskRepository;
import t1.tests.testContainer.TestContainersConfiguration;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;


@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc
public class TaskControllerTest extends TestContainersConfiguration {
    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private TaskRepository taskRepository;
    @Autowired
    private ObjectMapper objectMapper;
    private List<Long> currentAllIdsInTestContainer;
    @BeforeEach
    public void setUp(){
        taskRepository.deleteAll();
        List<Task> tasks = new ArrayList<>();

        Task task1 = new Task();
        task1.setDescription("desk1");
        task1.setStatus(Task.Status.IN_PROGRESS);
        task1.setTitle("title1");
        tasks.add(task1);

        Task task2 = new Task();
        task2.setDescription("desk2");
        task2.setStatus(Task.Status.COMPLETED);
        task2.setTitle("title2");
        tasks.add(task2);

        currentAllIdsInTestContainer = taskRepository.saveAll(tasks).stream().map(Task::getId).collect(Collectors.toList());
    }

    @Test
    void testGetTaskByIdTasks() throws Exception {
        mockMvc.perform(
                get("/tasks/1"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.title").value("title1"))
                .andExpect(jsonPath("$.description").value("desk1"))
                .andExpect(jsonPath("$.status").value("IN_PROGRESS"));
    }
    @Test
    void testGetTaskByIdNotFound() throws Exception {
        mockMvc.perform(get("/tasks/999"))
                .andExpect(status().isNotFound())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.error").value("Task not found"));
    }

    @Test
    void testCreateTask() throws Exception {
        Task newTask = new Task();
        newTask.setTitle("New Task");
        newTask.setDescription("New task description");
        newTask.setStatus(Task.Status.IN_PROGRESS);

        mockMvc.perform(post("/tasks")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(newTask)))
                .andExpect(status().isCreated())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.title").value("New Task"))
                .andExpect(jsonPath("$.description").value("New task description"))
                .andExpect(jsonPath("$.status").value("IN_PROGRESS"));
    }

    @Test
    void testGetAllTasks() throws Exception {
        mockMvc.perform(get("/tasks")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.length()").value(2));
    }

    @Test
    void testUpdateTask() throws Exception {
        Task updatedTask = new Task();
        updatedTask.setTitle("Updated Title");
        updatedTask.setDescription("Updated Description");
        updatedTask.setStatus(Task.Status.COMPLETED);


        mockMvc.perform(put("/tasks/{id}", currentAllIdsInTestContainer.get(0))
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(updatedTask)))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.title").value("Updated Title"))
                .andExpect(jsonPath("$.description").value("Updated Description"))
                .andExpect(jsonPath("$.status").value("COMPLETED"));
    }

    @Test
    void testUpdateTaskNotFound() throws Exception {
        Long taskId = 999L;
        Task updatedTask = new Task();
        updatedTask.setTitle("Updated Title");
        updatedTask.setDescription("Updated Description");
        updatedTask.setStatus(Task.Status.COMPLETED);


        mockMvc.perform(put("/tasks/{id}", taskId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(updatedTask)))
                .andExpect(status().isNotFound())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.error").value("Task not found"));
    }

    @Test
    void testDeleteTaskSuccess() throws Exception {
        mockMvc.perform(delete("/tasks/{id}", currentAllIdsInTestContainer.get(0)))
                .andExpect(status().isNoContent());
    }
}
