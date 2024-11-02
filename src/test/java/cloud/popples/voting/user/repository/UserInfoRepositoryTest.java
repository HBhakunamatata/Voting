package cloud.popples.voting.user.repository;

import cloud.popples.voting.users.domain.UserInfo;
import cloud.popples.voting.users.domain.UserRole;
import cloud.popples.voting.users.repository.UserInfoRepository;
import com.google.common.collect.Sets;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.util.Assert;

import java.util.Set;

@SpringBootTest
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

    @Test
    void testSaveUserInfo() {
        Set<GrantedAuthority> authorities = Sets.newHashSet();
        authorities.add(roleUser);
        authorities.add(roleAdmin);

        UserInfo userInfo = UserInfo.builder()
                .username("testuser01")
                .password(passwordEncoder.encode("123456"))
                .name("Real Name")
                .email("qweqw@qq.com")
                .authorities(authorities)
                .build();

        UserInfo save = userInfoRepository.save(userInfo);

        Assert.isTrue(save.getId() != null, "saved user's id is null");

    }

}
