package com.springboot.tasktrackingapplication.services;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.ArgumentMatchers.isA;
import static org.mockito.ArgumentMatchers.isNull;
import static org.mockito.Mockito.*;

import com.springboot.tasktrackingapplication.converters.TaskConverter;
import com.springboot.tasktrackingapplication.dtos.requests.TaskRequestDTO;
import com.springboot.tasktrackingapplication.dtos.requests.UpdateTaskDetailsRequestDTO;
import com.springboot.tasktrackingapplication.dtos.responses.TaskResponseDTO;
import com.springboot.tasktrackingapplication.entity.Authority;
import com.springboot.tasktrackingapplication.entity.Status;
import com.springboot.tasktrackingapplication.entity.Task;
import com.springboot.tasktrackingapplication.entity.User;
import com.springboot.tasktrackingapplication.exceptions.NameNotFoundException;
import com.springboot.tasktrackingapplication.exceptions.TaskCreationException;
import com.springboot.tasktrackingapplication.exceptions.TaskRetrievalException;
import com.springboot.tasktrackingapplication.repository.TaskRepository;
import com.springboot.tasktrackingapplication.repository.UserRepository;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

import com.springboot.tasktrackingapplication.testconfigurations.TestDataSourceConfig;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.http.HttpStatus;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;

import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;

@ContextConfiguration(classes = {TaskService.class})
@ExtendWith(SpringExtension.class)
@Import(TestDataSourceConfig.class)
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@Slf4j
class TaskServiceTest {
    @MockBean
    private TaskConverter taskConverter;

    @MockBean
    private TaskRepository taskRepository;

    @Autowired
    private TaskService taskService;

    @MockBean
    private UserRepository userRepository;

    @Mock
    private SecurityContext securityContext;

    @Mock
    private Authentication authentication;

    @Mock
    private UserDetails userDetails;
    
    private User user, user2;
    private Task task;

    @BeforeEach
    public void setUp() {

        SecurityContextHolder.setContext(securityContext);
        
        user = new User();
        user.setId(1L);
        user.setUsername("janedoe");
        user.setPassword("1234");
        user.setTasks(new HashSet<>());
        user.setEmail("jane.doe@example.org");
        user.setEnabled(true);
        Authority authority = new Authority();
        authority.setId(1L);
        authority.setName("ADMIN");

        ArrayList<Authority> authorities = new ArrayList<>();
        authorities.add(authority);
        user.setAuthorities(authorities);

        user2 = new User();
        user2.setAuthorities(new ArrayList<>());
        user2.setEmail("john.smith@example.org");
        user2.setEnabled(false);
        user2.setId(2L);
        user2.setPassword("1234");
        user2.setTasks(new HashSet<>());
        user2.setUsername("johnsmith");
        Authority authority2 = new Authority();
        authority2.setId(1L);
        authority2.setName("USER");

        ArrayList<Authority> authorities2 = new ArrayList<>();
        authorities2.add(authority2);
        user2.setAuthorities(authorities2);
        
        // Mocking the behavior of SecurityContext and Authentication
        when(securityContext.getAuthentication()).thenReturn(authentication);
        when(authentication.getPrincipal()).thenReturn(userDetails);
        when(userDetails.getUsername()).thenReturn(user.getUsername());


        task = new Task();
        task.setDescription("The characteristics of someone or something");
        task.setDueDate(LocalDate.now().plusDays(4));
        task.setId(1L);
        task.setStatus(Status.IN_PROGRESS);
        task.setTask("Task");
        task.setUser(user);
    }

    @Test
    void testGetAllTasksEmptyList() {
        // Arrange
        when(taskRepository.findallTasks()).thenReturn(new ArrayList<>());

        // Act
        List<TaskResponseDTO> actualAllTasks = taskService.getAllTasks();

        // Assert
        verify(taskRepository).findallTasks();
        assertTrue(actualAllTasks.isEmpty());
    }

    @Test
    void testGetAllTasksSingleUser() {

        TaskResponseDTO buildResult = TaskResponseDTO.builder()
                .description("The characteristics of someone or something")
                .dueDate(LocalDate.of(2024, 7, 1))
                .status(Status.IN_PROGRESS)
                .task("Task")
                .taskId(1L)
                .username("janedoe")
                .build();
        when(taskConverter.mapToResponseDTO(Mockito.<Task>any())).thenReturn(buildResult);

        ArrayList<Task> taskList = new ArrayList<>();
        taskList.add(task);
        when(taskRepository.findallTasks()).thenReturn(taskList);

        List<TaskResponseDTO> actualAllTasks = taskService.getAllTasks();

        // Assert
        verify(taskConverter).mapToResponseDTO(isA(Task.class));
        verify(taskRepository).findallTasks();
        assertEquals(1, actualAllTasks.size());
    }

    @Test
    void testGetAllTasksFromMultipleUser() {
        // Arrange
        TaskResponseDTO buildResult = TaskResponseDTO.builder()
                .description("The characteristics of someone or something")
                .dueDate(LocalDate.of(2024, 7, 1))
                .status(Status.IN_PROGRESS)
                .task("Task")
                .taskId(1L)
                .username("janedoe")
                .build();
        when(taskConverter.mapToResponseDTO(Mockito.<Task>any())).thenReturn(buildResult);

        User user2 = new User();
        user2.setAuthorities(new ArrayList<>());
        user2.setEmail("john.smith@example.org");
        user2.setEnabled(false);
        user2.setId(2L);
        user2.setPassword("1234");
        user2.setTasks(new HashSet<>());
        user2.setUsername("johnsmith");

        Task task2 = new Task();
        task2.setDescription("Fetching all the Tasks");
        task2.setDueDate(LocalDate.of(2024, 6, 1));
        task2.setId(2L);
        task2.setStatus(Status.EXTENDED);
        task2.setTask("Task2");
        task2.setUser(user2);

        ArrayList<Task> taskList = new ArrayList<>();
        taskList.add(task2);
        taskList.add(task);
        when(taskRepository.findallTasks()).thenReturn(taskList);

        // Act
        List<TaskResponseDTO> actualAllTasks = taskService.getAllTasks();

        // Assert
        verify(taskConverter, atLeast(1)).mapToResponseDTO(Mockito.<Task>any());
        verify(taskRepository).findallTasks();
        assertEquals(2, actualAllTasks.size());
    }

    @Test
    void testGetAllTasksByUserNameNotFoundException() {

        when(userRepository.findByUsername(Mockito.<String>any())).thenReturn(user)
                .thenThrow(new NameNotFoundException("Username given not found"));

        // Act and Assert
        assertThrows(NameNotFoundException.class, () -> taskService.getAllTasksByUser("janed"));
        verify(userRepository, atLeast(2)).findByUsername(Mockito.<String>any());
    }

    @Test
    void testGetAllTasksByUser_TaskRetrievalException() {

        when(userRepository.findByUsername(Mockito.<String>any())).thenReturn(user2)
                .thenThrow(new TaskRetrievalException(HttpStatus.FORBIDDEN, "This User cannot view Task status"));

        // Act and Assert
        TaskRetrievalException exception = assertThrows(TaskRetrievalException.class,
                () -> taskService.getAllTasksByUser("janed"));

        log.info("Exception" + exception.getCause());
        assertEquals("This User cannot view Task status", exception.getMessage());
        verify(userRepository, atLeast(1)).findByUsername(Mockito.<String>any());

    }

    @Test
    void testGetAllTasksByUserValidUser() {
        when(userRepository.findByUsername(Mockito.<String>any())).thenReturn(user2);
        ArrayList<Task> allTasks = new ArrayList<>();
        allTasks.add(task);
        when(taskRepository.findTasksByUsername(Mockito.<String>any())).thenReturn(allTasks);
        // Arrange and Act
        List<TaskResponseDTO> tasksByUser = taskService.getAllTasksByUser("johnsmith");
        assertEquals(1, tasksByUser.size());
        verify(userRepository, atLeast(2)).findByUsername(Mockito.<String>any());
        verify(taskRepository, atMost(1)).findTasksByUsername(Mockito.<String>any());
        verify(taskConverter).mapToResponseDTO(isA(Task.class));
    }

    @Test
    void testGetAllTasksByUserAdminAuthority() {

        Task task2 = new Task();
        task2.setDescription("Description");
        task2.setDueDate(LocalDate.of(2024, 7, 1));
        task2.setId(2L);
        task2.setStatus(Status.IN_PROGRESS);
        task2.setTask("Task2");
        task2.setUser(user2);

        Task task3 = new Task();
        task3.setDescription("Description");
        task3.setDueDate(LocalDate.of(2024, 6, 24));
        task3.setId(3L);
        task3.setStatus(Status.EXTENDED);
        task3.setTask("Task3");
        task3.setUser(user2);

        ArrayList<Task> allTasks = new ArrayList<>();
        allTasks.add(task2);
        allTasks.add(task3);

        when(userRepository.findByUsername(Mockito.<String>any())).thenReturn(user);
        when(taskRepository.findTasksByUsername(Mockito.<String>any())).thenReturn(allTasks);
        // Arrange and Act
        List<TaskResponseDTO> tasksByUser = taskService.getAllTasksByUser("johnsmith");
        assertEquals(2, tasksByUser.size());
        verify(userRepository, atLeast(2)).findByUsername(Mockito.<String>any());
        verify(taskRepository, atMost(1)).findTasksByUsername(Mockito.<String>any());
    }

    @Test
    void testGetTaskStatusEmptyList() {
        // Arrange
        ArrayList<Task> taskList = new ArrayList<>();
        when(taskRepository.findUsersByTask(Mockito.<String>any())).thenReturn(taskList);

        // Act
        List<Task> actualTaskStatus = taskService.getTaskStatus("Task");

        // Assert
        verify(taskRepository).findUsersByTask(eq("Task"));
        assertTrue(actualTaskStatus.isEmpty());
        assertSame(taskList, actualTaskStatus);
    }

    @Test
    void testGetTaskStatusSuccess() {
        ArrayList<Task> taskList = new ArrayList<>();
        taskList.add(task);
        when(taskRepository.findUsersByTask(Mockito.<String>any())).thenReturn(taskList);

        // Act
        List<Task> actualTaskStatus = taskService.getTaskStatus("Task");

        // Assert
        verify(taskRepository).findUsersByTask(eq("Task"));
        assertEquals(1, actualTaskStatus.size());
        assertSame(taskList, actualTaskStatus);
    }

    @Test
    void testGetTaskStatusMultipleTaskSuccess() {

        Task task2 = new Task();
        task2.setDescription("Description");
        task2.setDueDate(LocalDate.of(1970, 1, 1));
        task2.setId(2L);
        task2.setStatus(Status.EXTENDED);
        task2.setTask("com.springboot.tasktrackingapplication.entity.Task");
        task2.setUser(user2);

        String taskname = "Task";
        ArrayList<Task> taskList = new ArrayList<>();
        if (task.getTask().equalsIgnoreCase(taskname)) {
            taskList.add(task);
        }
        if (task2.getTask().equalsIgnoreCase(taskname)) {
            taskList.add(task2);
        }
        when(taskRepository.findUsersByTask(Mockito.<String>any())).thenReturn(taskList);

        List<Task> actualTaskStatus = taskService.getTaskStatus(taskname);

        // Assert
        verify(taskRepository).findUsersByTask(eq("Task"));
        assertEquals(1, actualTaskStatus.size());
        assertSame(taskList, actualTaskStatus);
    }

    @Test
    void testSaveTaskSuccessfully() {
        // Arrange
        TaskRequestDTO taskRequest =  TaskRequestDTO.builder()
                .description("The characteristics of someone or something")
                .dueDate(LocalDate.now().plusDays(4))
                .status(Status.IN_PROGRESS)
                .task("Task")
                .username("janedoe")
                .build();
        when(userRepository.findByUsername(Mockito.<String>any())).thenReturn(user);
        when(taskConverter.convertDtotoEntityCreated(taskRequest)).thenReturn(task);
        // Arrange and Act
        taskService.saveTask(taskRequest);

        verify(taskConverter).mapToResponseDTO(isA(Task.class));
        verify(taskConverter).convertDtotoEntityCreated(isA(TaskRequestDTO.class));
        verify(userRepository).findByUsername(isNotNull());
        verify(taskRepository).save(isA(Task.class));
    }

    @Test
    void testSaveTaskNoUserFound() {
        TaskRequestDTO taskRequest =  TaskRequestDTO.builder()
                .description("The characteristics of someone or something")
                .dueDate(LocalDate.now().plusDays(4))
                .status(Status.IN_PROGRESS)
                .task("Task")
                .username("janedoe")
                .build();
        when(userRepository.findByUsername(Mockito.<String>any())).thenReturn(null);

        NameNotFoundException exception = assertThrows(NameNotFoundException.class, ()
                                                            -> taskService.saveTask(taskRequest));
        assertEquals("Username in request not found", exception.getMessage());

    }

    @Test
    void testSaveTask_DueDateException() {
        // Arrange
        TaskRequestDTO taskRequest =  TaskRequestDTO.builder()
                .description("The characteristics of someone or something")
                .dueDate(LocalDate.of(2024, 1, 1))
                .status(Status.IN_PROGRESS)
                .task("Task")
                .username("janedoe")
                .build();

        TaskCreationException exception = assertThrows(TaskCreationException.class, () -> taskService.saveTask(taskRequest));
        log.info(exception.getMessage());
        assertEquals("Due Date past the task created date", exception.getMessage());

    }

    @Test
    void testUpdateTask() {
        // Arrange
        TaskResponseDTO buildResult = TaskResponseDTO.builder()
                .description("The characteristics of someone or something")
                .dueDate(LocalDate.of(1970, 1, 1))
                .status(Status.IN_PROGRESS)
                .task("Task")
                .taskId(1L)
                .username("janedoe")
                .build();
        when(taskConverter.mapToResponseDTO(Mockito.<Task>any())).thenReturn(buildResult);
        doNothing().when(taskConverter).updateTaskDetails(Mockito.<UpdateTaskDetailsRequestDTO>any(), Mockito.<Task>any());

        Task task2 = new Task();
        task2.setDescription("The characteristics of someone or something");
        task2.setDueDate(LocalDate.of(2024, 6, 15));
        task2.setId(2L);
        task2.setStatus(Status.IN_PROGRESS);
        task2.setTask("Task");
        task2.setUser(user2);
        when(taskRepository.save(Mockito.<Task>any())).thenReturn(task2);
        when(taskRepository.findTaskFromUserTasks(Mockito.<String>any(), Mockito.<String>any())).thenReturn(task);

        User user3 = new User();
        user3.setAuthorities(new ArrayList<>());
        user3.setEmail("jane.doe@example.org");
        user3.setEnabled(true);
        user3.setId(1L);
        user3.setPassword("1234");
        user3.setTasks(new HashSet<>());
        user3.setUsername("janedoe");
        when(userRepository.findByUsername(Mockito.<String>any())).thenReturn(user3);

        // Act
        taskService.updateTask(new UpdateTaskDetailsRequestDTO());

        // Assert
        verify(taskConverter).mapToResponseDTO(isA(Task.class));
        verify(taskConverter).updateTaskDetails(isA(UpdateTaskDetailsRequestDTO.class), isA(Task.class));
        verify(taskRepository).findTaskFromUserTasks(eq("janedoe"), isNull());
        verify(userRepository).findByUsername(isNull());
        verify(taskRepository).save(isA(Task.class));
    }

    @Test
    void testUpdateTaskNameNotFoundException() {
        // Arrange
        UpdateTaskDetailsRequestDTO updateTaskDetailsRequestDTO = new UpdateTaskDetailsRequestDTO();
        updateTaskDetailsRequestDTO.setDescription("The characteristics of something or anything");
        updateTaskDetailsRequestDTO.setDueDate(null);
        updateTaskDetailsRequestDTO.setStatus(Status.IN_PROGRESS);
        updateTaskDetailsRequestDTO.setTask("Task2");
        updateTaskDetailsRequestDTO.setUsername("jane");

        when(userRepository.findByUsername(Mockito.<String>any())).thenReturn(null);

        // Act and Assert
        NameNotFoundException nameNotFoundException = assertThrows(NameNotFoundException.class, ()
                                                                -> taskService.updateTask(updateTaskDetailsRequestDTO));
        assertEquals("Username in request not found", nameNotFoundException.getMessage());
    }

    @Test
    void testUpdateTask_RetrievalException() {
        // Arrange
        when(taskRepository.findTaskFromUserTasks(Mockito.<String>any(), Mockito.<String>any()))
                                                                                .thenReturn(null);

        when(userRepository.findByUsername(Mockito.<String>any())).thenReturn(user2);

        UpdateTaskDetailsRequestDTO request = UpdateTaskDetailsRequestDTO.builder()
                                                    .description("The characteristics of someone or something")
                                                    .dueDate(LocalDate.of(2024, 6, 1))
                                                    .status(Status.IN_PROGRESS)
                                                    .task("Task")
                                                    .username("johnsmith")
                                                    .build();

        // Act and Assert
        assertThrows(TaskRetrievalException.class, () -> taskService.updateTask(request));
        verify(taskRepository).findTaskFromUserTasks(eq("johnsmith"), eq("Task"));
        verify(userRepository).findByUsername(eq("johnsmith"));
    }
}
