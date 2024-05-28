package com.springboot.tasktrackingapplication.controllers;

import static org.mockito.Mockito.when;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.springboot.tasktrackingapplication.dtos.requests.TaskRequestDTO;
import com.springboot.tasktrackingapplication.dtos.requests.UpdateTaskDetailsRequestDTO;
import com.springboot.tasktrackingapplication.dtos.responses.TaskResponseDTO;
import com.springboot.tasktrackingapplication.entity.Status;
import com.springboot.tasktrackingapplication.services.TaskService;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import com.springboot.tasktrackingapplication.testconfigurations.TestDataSourceConfig;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInfo;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestBuilders;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

@ContextConfiguration(classes = {TaskController.class})
@ExtendWith(SpringExtension.class)
@ActiveProfiles("test")
//@Import(TestDataSourceConfig.class)
class TaskControllerTest {
    @Autowired
    private TaskController taskController;

    @MockBean
    private TaskService taskService;

    TaskRequestDTO taskRequestDTO;

    @BeforeEach
    public void setUp(TestInfo testInfo) {
        if ("testCreateTask".equals(Objects.requireNonNull(testInfo.getTestMethod().orElse(null)).getName())) {
            taskRequestDTO = new TaskRequestDTO();
            taskRequestDTO.setTask("Task");
            taskRequestDTO.setUsername("jane13");
            taskRequestDTO.setDescription("The characteristics of someone or something");
            taskRequestDTO.setDueDate(null);
            taskRequestDTO.setStatus(Status.IN_PROGRESS);
        }
    }

    @Test
    void testCreateTask() throws Exception {

        TaskResponseDTO taskResponseResult = TaskResponseDTO.builder()
                            .taskId(1L)
                            .username(taskRequestDTO.getUsername())
                            .task(taskRequestDTO.getTask())
                            .description(taskRequestDTO.getDescription())
                            .dueDate(LocalDate.of(2024, 7, 1))
                            .status(taskRequestDTO.getStatus())
                            .build();
        when(taskService.saveTask(Mockito.<TaskRequestDTO>any())).thenReturn(taskResponseResult);

        String content = (new ObjectMapper()).writeValueAsString(taskRequestDTO);
        MockHttpServletRequestBuilder requestBuilder = MockMvcRequestBuilders.post("/tasks/create")
                .contentType(MediaType.APPLICATION_JSON)
                .content(content);

        // Act
        ResultActions actualPerformResult = MockMvcBuilders.standaloneSetup(taskController).build().perform(requestBuilder);

        // Assert
        actualPerformResult.andExpect(MockMvcResultMatchers.status().isCreated())
                .andExpect(MockMvcResultMatchers.content().contentType("application/json"))
                .andExpect(MockMvcResultMatchers.content()
                        .string(
                                "{\"taskId\":1,\"username\":\"jane13\",\"task\":\"Task\",\"description\":\"The characteristics of someone or"
                                        + " something\",\"dueDate\":[2024,7,1],\"status\":\"IN_PROGRESS\"}"));
    }

    @Test
    void testUpdateTask() throws Exception {

        TaskResponseDTO taskResponseResult = TaskResponseDTO.builder()
                .taskId(1L)
                .username("janedoe")
                .task("Task2")
                .description("The characteristics of something or anything")
                .dueDate(LocalDate.of(2024, 7, 1))
                .status(Status.IN_PROGRESS)
                .build();
        when(taskService.updateTask(Mockito.<UpdateTaskDetailsRequestDTO>any())).thenReturn(taskResponseResult);

        UpdateTaskDetailsRequestDTO updateTaskDetailsRequestDTO = new UpdateTaskDetailsRequestDTO();
        updateTaskDetailsRequestDTO.setDescription("The characteristics of something or anything");
        updateTaskDetailsRequestDTO.setDueDate(null);
        updateTaskDetailsRequestDTO.setStatus(Status.IN_PROGRESS);
        updateTaskDetailsRequestDTO.setTask("Task2");
        updateTaskDetailsRequestDTO.setUsername("janedoe");
        String content = (new ObjectMapper()).writeValueAsString(updateTaskDetailsRequestDTO);
        MockHttpServletRequestBuilder requestBuilder = MockMvcRequestBuilders.put("/tasks/update")
                .contentType(MediaType.APPLICATION_JSON)
                .content(content);

        ResultActions actualPerformResult = MockMvcBuilders.standaloneSetup(taskController).build().perform(requestBuilder);

        actualPerformResult.andExpect(MockMvcResultMatchers.status().is(206))
                .andExpect(MockMvcResultMatchers.content().contentType("application/json"))
                .andExpect(MockMvcResultMatchers.content()
                        .string(
                                "{\"taskId\":1,\"username\":\"janedoe\",\"task\":\"Task2\",\"description\":\"The characteristics"
                                        + " of something or anything\",\"dueDate\":[2024,7,1],\"status\":\"IN_PROGRESS\"}"));

    }

    @Test
    void testViewAllTask() throws Exception {
        List<TaskResponseDTO> taskList = new ArrayList<>();
        TaskResponseDTO taskResponseResult1 = TaskResponseDTO.builder()
                .taskId(1L)
                .username("janedoe")
                .task("Task")
                .description("The characteristics of something or anything")
                .dueDate(LocalDate.of(2024, 7, 1))
                .status(Status.IN_PROGRESS)
                .build();
        TaskResponseDTO taskResponseResult2 = TaskResponseDTO.builder()
                .taskId(2L)
                .username("johndoe")
                .task("Task2")
                .description("The characteristics of anything")
                .dueDate(LocalDate.of(2024, 6, 1))
                .status(Status.IN_PROGRESS)
                .build();
        taskList.add(taskResponseResult1);
        taskList.add(taskResponseResult2);
        when(taskService.getAllTasks()).thenReturn(taskList);
        MockHttpServletRequestBuilder requestBuilder = MockMvcRequestBuilders.get("/tasks/viewAll");

        MockMvcBuilders.standaloneSetup(taskController)
                .build()
                .perform(requestBuilder)
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().contentType("application/json"))
                .andExpect(MockMvcResultMatchers.content().string(
                        "[" +
                        "{\"taskId\":1,\"username\":\"janedoe\",\"task\":\"Task\"," +
                                "\"description\":\"The characteristics of something or anything\",\"dueDate\":[2024,7,1],\"status\":\"IN_PROGRESS\"}," +
                        "{\"taskId\":2,\"username\":\"johndoe\",\"task\":\"Task2\"," +
                                "\"description\":\"The characteristics of anything\",\"dueDate\":[2024,6,1],\"status\":\"IN_PROGRESS\"}" +
                        "]"));
    }

    @Test
    void testViewAllTaskByUser() throws Exception {

        when(taskService.getAllTasksByUser(Mockito.<String>any())).thenReturn(new ArrayList<>());
        MockHttpServletRequestBuilder requestBuilder = MockMvcRequestBuilders.get("/tasks/view/user/{username}", "janedoe");

        MockMvcBuilders.standaloneSetup(taskController)
                .build()
                .perform(requestBuilder)
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().contentType("application/json"))
                .andExpect(MockMvcResultMatchers.content().string("[]"));
    }

    @Test
    void testViewTaskStatus() throws Exception {

        when(taskService.getTaskStatus(Mockito.<String>any())).thenReturn(new ArrayList<>());
        MockHttpServletRequestBuilder requestBuilder = MockMvcRequestBuilders.get("/tasks/viewOne/{task}", "Task");

        MockMvcBuilders.standaloneSetup(taskController)
                .build()
                .perform(requestBuilder)
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().contentType("application/json"))
                .andExpect(MockMvcResultMatchers.content().string("[]"));
    }

    @Test
    void testDeleteTask() throws Exception {
        // Arrange
        when(taskService.deleteTask(Mockito.<Long>any())).thenReturn(true);
        MockHttpServletRequestBuilder requestBuilder = MockMvcRequestBuilders.delete("/tasks/delete/{taskId}", 1L);

        // Act and Assert
        MockMvcBuilders.standaloneSetup(taskController)
                .build()
                .perform(requestBuilder)
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().contentType("text/plain;charset=ISO-8859-1"))
                .andExpect(MockMvcResultMatchers.content().string("Task deleted successfully"));
    }
    
    @Test
    void testDeleteWhenTaskNotFound() throws Exception {
        // Arrange
        when(taskService.deleteTask(Mockito.<Long>any())).thenReturn(false);
        MockHttpServletRequestBuilder requestBuilder = MockMvcRequestBuilders.delete("/tasks/delete/{taskId}", 1L);

        // Act
        ResultActions actualPerformResult = MockMvcBuilders.standaloneSetup(taskController).build().perform(requestBuilder);

        // Assert
        actualPerformResult.andExpect(MockMvcResultMatchers.status().isNotFound())
                .andExpect(MockMvcResultMatchers.content().contentType("text/plain;charset=ISO-8859-1"))
                .andExpect(MockMvcResultMatchers.content().string("Task not found"));
    }
    
}
