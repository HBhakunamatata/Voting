package cloud.popples.voting.users.service.impl;

import cloud.popples.voting.users.domain.UserInfo;
import cloud.popples.voting.users.domain.UserRole;
import cloud.popples.voting.users.param.RegisterForm;
import cloud.popples.voting.users.repository.UserInfoRepository;
import cloud.popples.voting.users.repository.UserRoleRepository;
import cloud.popples.voting.users.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

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
        Optional<UserInfo> userInfo = userInfoRepository.findByUsername(username);
        return userInfo.orElseThrow(() -> new UsernameNotFoundException(username));
    }

    @Override
    public UserInfo registerUser(RegisterForm registerForm) {
        final UserRole userRole = configDefaultRole(registerForm);
        final UserInfo userInfo = registerForm.toUserInfo(passwordEncoder, userRole);
        return userInfoRepository.save(userInfo);
    }

    @Override
    public UserInfo getUserDetails(Long id) {
        return userInfoRepository.getReferenceById(id);
    }


    private UserRole configDefaultRole(RegisterForm registerForm) {
        Optional<UserRole> roleUser = userRoleRepository.findByAuthority("ROLE_USER");
        return roleUser.orElse(new UserRole("ROLE_USER"));
    }

}
