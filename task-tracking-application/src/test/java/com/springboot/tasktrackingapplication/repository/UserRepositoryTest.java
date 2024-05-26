package com.springboot.tasktrackingapplication.repository;

import com.springboot.tasktrackingapplication.entity.User;

import java.util.ArrayList;
import java.util.HashSet;

import com.springboot.tasktrackingapplication.testconfigurations.TestDataSourceConfig;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.ContextConfiguration;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ContextConfiguration(classes = {UserRepository.class})
@EnableAutoConfiguration
@EntityScan(basePackages = {"com.springboot.tasktrackingapplication.entity"})
@DataJpaTest
@Import(TestDataSourceConfig.class)
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@Slf4j
class UserRepositoryTest {
    @Autowired
    private UserRepository userRepository;

    @MockBean
    private PasswordEncoder passwordEncoder;

    @Test
    void testFindByUsername() {
        when(passwordEncoder.encode(Mockito.<CharSequence>any())).thenReturn("secret");

        // Arrange
        User user = new User();
        user.setAuthorities(new ArrayList<>());
        user.setEmail("jane13@example.org");
        user.setEnabled(true);
        user.setPassword(passwordEncoder.encode("1234"));
        user.setTasks(new HashSet<>());
        user.setUsername("jane13");

        User user2 = new User();
        user2.setAuthorities(new ArrayList<>());
        user2.setEmail("john13@example.org");
        user2.setEnabled(true);
        user2.setPassword(passwordEncoder.encode("1234"));
        user2.setTasks(new HashSet<>());
        user2.setUsername("john13");

        userRepository.save(user);
        userRepository.save(user2);

        User byUsername = userRepository.findByUsername("jane13");
        log.info("byUsername" + byUsername.toString());

        //verify(passwordEncoder).encode(isA(CharSequence.class));
        assertEquals("jane13@example.org", byUsername.getEmail());
        assertEquals("jane13", byUsername.getUsername());
        assertEquals("secret", byUsername.getPassword());

    }
}
