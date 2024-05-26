package com.springboot.tasktrackingapplication.repository;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

import java.util.Arrays;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.springboot.tasktrackingapplication.entity.Task;
import com.springboot.tasktrackingapplication.entity.User;

@ExtendWith(MockitoExtension.class)
public class TaskRepositoryTest {

	@Mock
	private TaskRepository taskRepository;

	@BeforeEach
	public void setUp() {
		// Mock the behavior of taskRepository
		User user1 = new User();
		user1.setId(1L);
		user1.setUsername("testUser1");

		Task task1 = new Task();
		task1.setTask("Task 1");
		task1.setUser(user1);

		Task task2 = new Task();
		task2.setTask("Task 2");
		task2.setUser(user1);

		User user2 = new User();
		user2.setId(2L);
		user2.setUsername("testUser2");

		Task task3 = new Task();
		task3.setTask("Task 3");
		task3.setUser(user2);

		List<Task> tasks = Arrays.asList(task1, task2);
		when(taskRepository.findByUser(user1)).thenReturn(tasks);

		/*
		 * List<Task> allTasks = Arrays.asList(task1, task2, task3);
		 * when(taskRepository.findallTasks()).thenReturn(allTasks);
		 */
		 
	}

	@Test
	public void testFindByUser() {

		User user = new User();
		user.setId(1L);
		user.setUsername("testUser1");
		// Perform the test

		List<Task> resultTasks = taskRepository.findByUser(user);

		// Assert
		assertThat(resultTasks.size()).isEqualTo(2);
		assertThat(resultTasks.get(0).getTask()).isEqualTo("Task 1");
		assertThat(resultTasks.get(1).getTask()).isEqualTo("Task 2");

	}
	/*
	 * @Test public void testfindallTasks() { List<Task> resultTasks =
	 * taskRepository.findallTasks(); // Assert
	 * assertThat(resultTasks.size()).isEqualTo(3);
	 * assertThat(resultTasks.get(0).getTask()).isEqualTo("Task 1");
	 * assertThat(resultTasks.get(1).getTask()).isEqualTo("Task 2");
	 * assertThat(resultTasks.get(2).getTask()).isEqualTo("Task 3");
	 * 
	 * }
	 */

}
