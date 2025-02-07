package cloud.popples.voting.users.service;

import cloud.popples.voting.users.domain.UserInfo;
import cloud.popples.voting.users.domain.UserRole;
import cloud.popples.voting.users.param.RegisterForm;
import cloud.popples.voting.users.repository.UserInfoRepository;
import cloud.popples.voting.users.repository.UserRoleRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class UserServiceImpl implements UserService, UserDetailsService {

    private final UserInfoRepository userInfoRepository;

    private final UserRoleRepository userRoleRepository;

    private final PasswordEncoder passwordEncoder;

    @Autowired
    public UserServiceImpl(UserInfoRepository userInfoRepository,
                           UserRoleRepository userRoleRepository,
                           PasswordEncoder passwordEncoder) {
        this.userInfoRepository = userInfoRepository;
        this.userRoleRepository = userRoleRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        UserInfo userInfo = userInfoRepository.findByUsername(username);
        if (userInfo == null) {
            throw new UsernameNotFoundException(username);
        }
        return userInfoRepository.findByUsername(username);
    }

    @Override
    public UserInfo registerUser(RegisterForm registerForm) {
        UserRole userRole = configureUserRole(registerForm);
        UserInfo userInfo = registerForm.toUserInfo(passwordEncoder, userRole);
        UserInfo saved = userInfoRepository.save(userInfo);
        return saved;
    }

    @Override
    public UserInfo getUserDetails(Long id) {
        return userInfoRepository.getReferenceById(id);
    }


    private UserRole configureUserRole(RegisterForm registerForm) {
        UserRole roleUser = userRoleRepository.findByAuthority("ROLE_USER");
        if (roleUser == null) {
            roleUser = new UserRole("ROLE_USER");
        }
        return roleUser;
    }
}
