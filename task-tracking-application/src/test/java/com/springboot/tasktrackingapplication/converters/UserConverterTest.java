package com.springboot.tasktrackingapplication.converters;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.ArgumentMatchers.isA;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.springboot.tasktrackingapplication.dtos.requests.UserRequestDTO;
import com.springboot.tasktrackingapplication.dtos.responses.UserLoginResponseDTO;
import com.springboot.tasktrackingapplication.entity.Authority;
import com.springboot.tasktrackingapplication.entity.User;
import com.springboot.tasktrackingapplication.exceptions.NameNotFoundException;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.aot.DisabledInAotMode;
import org.springframework.test.context.junit.jupiter.SpringExtension;

@ContextConfiguration(classes = {UserConverter.class, PasswordEncoder.class})
@ExtendWith(SpringExtension.class)
@Slf4j
class UserConverterTest {
    @MockBean
    private PasswordEncoder passwordEncoder;

    @Autowired
    private UserConverter userConverter;

    @Test
    void testConvertDtotoEntityNullUsername() {
        assertThrows(NameNotFoundException.class, () -> userConverter.convertDtotoEntity(new UserRequestDTO()));
    }

    @Test
    void testConvertDtotoEntityDefaultUserAuthority() {
        when(passwordEncoder.encode(Mockito.<CharSequence>any())).thenReturn("secret");

        User actualConvertDtotoEntityResult = userConverter
                .convertDtotoEntity(new UserRequestDTO("jane13", "jane13@example.org", "1234", true, new ArrayList<>()));

        verify(passwordEncoder).encode(isA(CharSequence.class));
        List<Authority> authorities = actualConvertDtotoEntityResult.getAuthorities();
        log.info("Authority " + authorities);
        assertEquals(1, authorities.size());
        Authority getResult = authorities.getFirst();
        assertEquals("User", getResult.getAuthority());
        assertEquals("jane13@example.org", actualConvertDtotoEntityResult.getEmail());
        assertEquals("jane13", actualConvertDtotoEntityResult.getUsername());
        assertEquals("secret", actualConvertDtotoEntityResult.getPassword());
        assertNull(getResult.getId());
        assertTrue(actualConvertDtotoEntityResult.isEnabled());
        assertTrue(actualConvertDtotoEntityResult.getTasks().isEmpty());
    }

    @Test
    void testConvertDtotoEntitySetUserAuthority() {

        when(passwordEncoder.encode(Mockito.<CharSequence>any())).thenReturn("secret");

        Authority authority = new Authority();
        authority.setId(1L);
        authority.setName("User");

        ArrayList<Authority> authorities = new ArrayList<>();
        authorities.add(authority);

        User actualConvertDtotoEntityResult = userConverter
                .convertDtotoEntity(new UserRequestDTO("jane13", "jane13@example.org", "1234", true, authorities));

        // Assert
        verify(passwordEncoder).encode(isA(CharSequence.class));
        assertEquals("jane13@example.org", actualConvertDtotoEntityResult.getEmail());
        assertEquals("jane13", actualConvertDtotoEntityResult.getUsername());
        assertEquals("secret", actualConvertDtotoEntityResult.getPassword());
        assertEquals(1, actualConvertDtotoEntityResult.getAuthorities().size());
        assertTrue(actualConvertDtotoEntityResult.isEnabled());
        assertTrue(actualConvertDtotoEntityResult.getTasks().isEmpty());
    }
}
