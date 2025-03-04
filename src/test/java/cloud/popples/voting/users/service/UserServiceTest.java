package cloud.popples.voting.users.service;

import cloud.popples.voting.users.domain.UserInfo;
import cloud.popples.voting.users.domain.UserRole;
import cloud.popples.voting.users.param.RegisterForm;
import cloud.popples.voting.users.repository.UserInfoRepository;
import cloud.popples.voting.users.repository.UserRoleRepository;
import cloud.popples.voting.users.service.impl.UserServiceImpl;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.time.LocalDateTime;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.isA;
import static org.mockito.Mockito.when;

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

    private final UserRole userRole = new UserRole("ROLE_USER");

    @Test
    void givenUserInfoThenRegistered() {
        String expectUserName = "test";
        String expectPassword = "test";
        String expectEmail = "test@test.com";

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

        when(userRoleRepository.findByAuthority("ROLE_USER")).thenReturn(Optional.of(userRole));
        when(userInfoRepository.save(isA(UserInfo.class))).thenReturn(savedUser);
        when(passwordEncoder.encode(expectPassword)).thenReturn(expectPassword);

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

    @Test
    void whenFindUserByNameThenReturn() {
        String expectUserName = "test";
        String expectPassword = "test";
        String expectEmail = "test@test.com";

        LocalDateTime time = LocalDateTime.of(2024, 11, 10, 0, 0, 0);

        UserInfo existedUser = UserInfo.builder()
                .id(1L)
                .username(expectUserName)
                .password(expectPassword)
                .name(expectUserName)
                .email(expectEmail)
                .createTime(time)
                .updateTime(time)
                .build();

        when(userInfoRepository.findByUsername(expectUserName)).thenReturn(Optional.of(existedUser));

        UserDetails userDetails = userService.loadUserByUsername(expectUserName);
        assertAll(
                () -> assertNotNull(userDetails),
                () -> assertEquals(userDetails.getUsername(), expectUserName),
                () -> assertEquals(userDetails.getPassword(), expectPassword)
        );
    }

    @Test
    void whenFindUserByInvalidNameThenThrowNotFoundException() {
        String expectUserName = "test";
        when(userInfoRepository.findByUsername(expectUserName)).thenReturn(Optional.empty());
        assertThrows(UsernameNotFoundException.class, () -> userService.loadUserByUsername(expectUserName));
    }
}