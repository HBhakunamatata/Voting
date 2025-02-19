package cloud.popples.voting.users.repository;

import cloud.popples.voting.users.domain.UserInfo;
import cloud.popples.voting.users.domain.UserRole;
import cloud.popples.voting.utils.TestUtils;
import com.google.common.collect.Sets;
import org.instancio.Instancio;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DynamicTest;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestFactory;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.Set;

import static org.instancio.Select.field;
import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.DynamicTest.dynamicTest;
import static org.mockito.Mockito.when;

@DataJpaTest
@ExtendWith({MockitoExtension.class})
class UserInfoRepositoryTest {

    @Autowired
    private UserInfoRepository userInfoRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    GrantedAuthority roleUser;
    GrantedAuthority roleAdmin;

    private final static String USER_PASSWORD = "123456";

    @BeforeEach
    void before() {
        roleUser = new UserRole("ROLE_USER");
        roleAdmin = new UserRole("ROLE_ADMIN");
    }

    @TestFactory
    @Rollback
    @Transactional
    @Sql(scripts = "/unit-test-db-scripts/user_info.sql")
    Iterable<DynamicTest> testSaveUserInfo() {
        Set<GrantedAuthority> authorities = Sets.newHashSet();
        authorities.add(roleUser);
        authorities.add(roleAdmin);

        when(passwordEncoder.encode(USER_PASSWORD)).thenReturn(USER_PASSWORD);

        UserInfo userInfo = Instancio.of(UserInfo.class)
                .set(field(UserInfo::getUsername), "NewCreatedUser")
                .set(field(UserInfo::getPassword), passwordEncoder.encode(USER_PASSWORD))
                .set(field(UserInfo::getName), "NewCreatedUser")
                .set(field(UserInfo::getEmail), "qweqw@qq.com")
                .set(field(UserInfo::getAuthorities), authorities)
                .create();

        UserInfo result = userInfoRepository.save(userInfo);

        return Arrays.asList(
                dynamicTest("Check id not null", () -> assertNotNull(result.getId())),
                dynamicTest("Check username equals", () -> assertEquals(userInfo.getUsername(), result.getUsername())),
                dynamicTest("Check password equals", () -> assertEquals(userInfo.getPassword(), result.getPassword())),
                dynamicTest("Check email equals", () -> assertEquals(userInfo.getEmail(), result.getEmail()))
        );
    }

    @Test
    @Transactional
    @Rollback
    @Sql(scripts = "/unit-test-db-scripts/user_info.sql")
    void whenFindUserInfoByLegalUsernameThenReturnUserInfo() throws Exception {
        String existedUserName = "test01";
        when(passwordEncoder.encode(USER_PASSWORD)).thenReturn(USER_PASSWORD);

        UserInfo result = userInfoRepository.findByUsername(existedUserName);

        UserInfo expected = mockExistedUserInfo();
        assertEquals(TestUtils.mapToString(result), TestUtils.mapToString(expected));
    }

    private UserInfo mockExistedUserInfo() {
        LocalDateTime mockTime = LocalDateTime.of(2024, 11, 8, 18, 30, 1);
        return UserInfo.builder()
                .id(10000L)
                .name("test01")
                .email("test01@fjfie.jie")
                .username("test01")
                .password(passwordEncoder.encode(USER_PASSWORD))
                .authorities(Sets.newHashSet(roleUser))
                .createTime(mockTime)
                .updateTime(mockTime)
                .build();
    }

    @Test
    @Sql(scripts = "/unit-test-db-scripts/user_info.sql")
    void whenFindNonExistentUserInfoThenThrowException() {
        String nonExistedUserName = "fake_name";
        UserInfo userInfo = userInfoRepository.findByUsername(nonExistedUserName);
        assertNull(userInfo);
    }

}
