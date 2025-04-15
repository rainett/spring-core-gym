package com.rainett.service.impl;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

import com.rainett.model.User;
import com.rainett.repository.UserRepository;
import java.util.stream.Stream;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class CredentialServiceImplTest {
    @InjectMocks
    private CredentialServiceImpl service;

    @Mock
    private UserRepository userRepository;

    static Stream<Arguments> getArgs() {
        return Stream.of(
                Arguments.of("John", "Doe", 0, "John.Doe"),
                Arguments.of("John", "Doe", 2, "John.Doe.2"),
                Arguments.of("Robert", "Armstrong", 1, "Robert.Armstrong.1"),
                Arguments.of("LHsdkljbckashdlkj", "KNCjhxuYkk29", 99999,
                        "LHsdkljbckashdlkj.KNCjhxuYkk29.99999"),
                Arguments.of("x", "y", 0, "x.y")
        );
    }

    @ParameterizedTest
    @MethodSource("getArgs")
    @DisplayName("Generates username and password")
    void testGeneratesCredentials(String firstName, String lastName,
                                  int suffixes, String expected) {
        when(userRepository.findSuffixUsernameCount(anyString())).thenReturn((long) suffixes);
        User user = new User();
        user.setFirstName(firstName);
        user.setLastName(lastName);
        service.createCredentials(user);
        assertEquals(expected, user.getUsername());
        assertTrue(user.getPassword().length() >= 6);
    }
}