package com.springboot.tasktrackingapplication.services;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.springboot.tasktrackingapplication.converters.UserConverter;
import com.springboot.tasktrackingapplication.dtos.requests.UserLoginRequestDTO;
import com.springboot.tasktrackingapplication.dtos.requests.UserRequestDTO;
import com.springboot.tasktrackingapplication.dtos.responses.UserLoginResponseDTO;
import com.springboot.tasktrackingapplication.entity.Authority;
import com.springboot.tasktrackingapplication.entity.User;
import com.springboot.tasktrackingapplication.exceptions.ApiRequestException;
import com.springboot.tasktrackingapplication.repository.UserRepository;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.TestingAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;

@ContextConfiguration(classes = {UserService.class, AuthenticationManager.class})
@ExtendWith(SpringExtension.class)
@Slf4j
class UserServiceTest {
    @MockBean
    private AuthenticationManager authenticationManager;

    @MockBean
    private UserConverter userConverter;

    @MockBean
    private UserRepository userRepository;

    @Autowired
    private UserService userService;
    private User testUser;

    @BeforeEach
    public void setUp() {
        userRepository.deleteAll();

        // Create and save a test user
        testUser = new User();
        testUser.setId(1L);
        testUser.setUsername("janedoe");
        testUser.setEmail("jane.doe@example.org");
        testUser.setPassword("1234");
        testUser.setEnabled(true);
        List<Authority> authorities = new ArrayList<>();
        Authority authority1 = new Authority(1L, "USER");
        authorities.add(authority1);
        testUser.setAuthorities(authorities);
        testUser.setTasks(new HashSet<>());
        //userRepository.save(testUser);
    }

    @Test
    @DirtiesContext
    void testAddUserSuccess() {

        UserRequestDTO userRequest = new UserRequestDTO("janejoe", "jane.joe@example.org",
                "1234", true, new ArrayList<>());

        when(userRepository.findByUsername(Mockito.<String>any())).thenReturn(null);

        when(userConverter.convertDtotoEntity(userRequest)).thenReturn(testUser);
        ResponseEntity<String> actualAddUserResult = userService.addUser(userRequest);
        log.info("Result - " + actualAddUserResult);

        // Assert
        verify(userRepository).findByUsername("janejoe");
        assertEquals("User janedoe has been registered successfully", actualAddUserResult.getBody());
        assertEquals(200, actualAddUserResult.getStatusCodeValue());

    }

    @Test
    @DirtiesContext
    void testAddUserUsernameExists() {

        when(userRepository.findByUsername(Mockito.<String>any())).thenReturn(testUser);

        ResponseEntity<String> actualAddUserResult = userService
                .addUser(new UserRequestDTO("janedoe", "jane.doe@example.org", "1234",
                                                                                    true, new ArrayList<>()));
        log.info("Result - " + actualAddUserResult);

        verify(userRepository).findByUsername(eq("janedoe"));
        assertEquals("Username is already taken", actualAddUserResult.getBody());
        assertEquals(400, actualAddUserResult.getStatusCodeValue());
        assertTrue(actualAddUserResult.getHeaders().isEmpty());
    }

    @Test
    void testUsernameNullAddUser() {
        // Arrange
        when(userRepository.findByUsername(Mockito.<String>any())).thenReturn(null);
        // Act and Assert
        UsernameNotFoundException exception = assertThrows(UsernameNotFoundException.class, 
        											() -> userService.addUser(new UserRequestDTO()));
        assertEquals("No username found in request", exception.getMessage());
    }

    @Test
    void testLoginSuccess() throws ApiRequestException, AuthenticationException {

        TestingAuthenticationToken testingAuthenticationToken = new TestingAuthenticationToken(testUser, "Credentials");

        when(authenticationManager.authenticate(Mockito.<Authentication>any())).thenReturn(testingAuthenticationToken);

        // Act
        UserLoginResponseDTO actualLoginResult = userService.login(new UserLoginRequestDTO("janedoe", "1234"));

        // Assert
        verify(authenticationManager).authenticate(isA(Authentication.class));
        assertEquals("janedoe", actualLoginResult.getUsername());
        assertEquals(1L, actualLoginResult.getId().longValue());
        //assertEquals(testUser.getAuthorities().getFirst().getName(), actualLoginResult.getAuthorities());
    }

    @Test
    void testLoginBadCredentials() throws ApiRequestException, AuthenticationException {

        //TestingAuthenticationToken testingAuthenticationToken = new TestingAuthenticationToken(testUser, "Credentials");

        when(authenticationManager.authenticate(Mockito.<Authentication>any()))
                                                .thenThrow(BadCredentialsException.class);

        // Assert
        Exception exception = assertThrows(ApiRequestException.class, () -> {
            userService.login(new UserLoginRequestDTO("janedoe", "wrongpasskey"));
        });
        assertEquals("Credentials are not valid!", exception.getMessage());
    }





}
