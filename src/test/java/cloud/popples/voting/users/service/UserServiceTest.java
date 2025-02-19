package cloud.popples.voting.users.service;

import cloud.popples.voting.users.domain.UserInfo;
import cloud.popples.voting.users.domain.UserRole;
import cloud.popples.voting.users.param.RegisterForm;
import cloud.popples.voting.users.repository.UserInfoRepository;
import cloud.popples.voting.users.repository.UserRoleRepository;
import cloud.popples.voting.users.service.impl.UserServiceImpl;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.BDDMockito.given;

@ExtendWith(MockitoExtension.class)
class UserServiceTest {

    @InjectMocks
    private UserServiceImpl userService;

    @Mock
    private PasswordEncoder passwordEncoder;

    @Mock
    private UserInfoRepository userInfoRepository;

    @Mock
    private UserRoleRepository userRoleRepository;

    private UserRole userRole;

    @BeforeEach
    void setUp() {
        userRole = new UserRole("ROLE_USER");
    }

    @AfterEach
    void tearDown() {
    }

    @Test
    void givenUserInfoThenRegistered() {
        String expectUserName = "test";
        String expectPassword = "test";
        String expectEmail = "test@test.com";

        UserInfo unsavedUser = UserInfo.builder()
                .username(expectUserName)
                .password(expectPassword)
                .email(expectEmail)
                .build();

        LocalDateTime time = LocalDateTime.of(2024, 11, 10, 0, 0, 0);

        UserInfo savedUser = UserInfo.builder()
                .id(1L)
                .username(expectUserName)
                .password(expectPassword)
                .name(expectUserName)
                .email(expectEmail)
                .createTime(time)
                .updateTime(time)
                .build();

        given(userRoleRepository.findByAuthority("ROLE_USER")).willReturn(userRole);
        given(userInfoRepository.save(unsavedUser)).willReturn(savedUser);
        given(passwordEncoder.encode(expectPassword)).willReturn(expectPassword);

        // Input Data
        RegisterForm registerForm = new RegisterForm();
        registerForm.setUsername(expectUserName);
        registerForm.setPassword(expectPassword);
        registerForm.setEmail(expectEmail);

        UserInfo expectedUser = userService.registerUser(registerForm);
        assertAll(
                () -> assertNotNull(expectedUser),
                () -> assertEquals(expectedUser.getUsername(), expectUserName),
                () -> assertEquals(expectedUser.getPassword(), expectPassword),
                () -> assertEquals(expectedUser.getEmail(), expectEmail),
                () -> assertEquals(expectedUser.getName(), expectUserName)
        );
    }
}