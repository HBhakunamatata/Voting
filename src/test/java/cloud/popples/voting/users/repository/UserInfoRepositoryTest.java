package cloud.popples.voting.users.repository;

import cloud.popples.voting.config.PasswordConfig;
import cloud.popples.voting.users.domain.UserInfo;
import cloud.popples.voting.users.domain.UserRole;
import cloud.popples.voting.utils.TestUtils;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.google.common.collect.Sets;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DynamicTest;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.DynamicTest.dynamicTest;

@DataJpaTest
@Import(PasswordConfig.class)
@Sql(scripts = "/unit-test-db-scripts/user_info.sql")
public class UserInfoRepositoryTest {

    @Autowired
    private UserInfoRepository userInfoRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    GrantedAuthority roleUser;
    GrantedAuthority roleAdmin;

    @BeforeEach
    void before() {
        roleUser = new UserRole("ROLE_USER");
        roleAdmin = new UserRole("ROLE_ADMIN");
    }

    @TestFactory
    @Rollback
    @Transactional
    Iterable<DynamicTest> testSaveUserInfo() throws JsonProcessingException {
        Set<GrantedAuthority> authorities = Sets.newHashSet();
        authorities.add(roleUser);
        authorities.add(roleAdmin);

        UserInfo userInfo = UserInfo.builder()
                .username("NewCreatedUser")
                .password(passwordEncoder.encode("123456"))
                .name("NewCreatedUser")
                .email("qweqw@qq.com")
                .authorities(authorities)
                .build();

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
    void whenFindUserInfoByLegalUsernameThenReturnUserInfo() throws Exception {
        String existedUserName = "test01";
        UserInfo result = userInfoRepository.findByUsername(existedUserName);
        UserInfo expected = mockExistedUserInfo();
        assertEquals(TestUtils.mapToString(result), TestUtils.mapToString(expected));
    }

    private UserInfo mockExistedUserInfo() {
        LocalDateTime mockTime = LocalDateTime.of(2024, 11, 8, 18, 30, 1);
        return UserInfo.builder()
                .id(1L)
                .name("test01")
                .email("test01@fjfie.jie")
                .username("test01")
                .password(passwordEncoder.encode("123456"))
                .authorities(Sets.newHashSet(roleUser))
                .createTime(mockTime)
                .updateTime(mockTime)
                .build();
    }

    @Test
    void whenFindNonExistentUserInfoThenThrowException() {
        String nonExistedUserName = "fake_name";
        UserInfo userInfo = userInfoRepository.findByUsername(nonExistedUserName);
        assertNull(userInfo);
    }

}
