package cloud.popples.voting.users.service;

import cloud.popples.voting.users.domain.UserInfo;
import cloud.popples.voting.users.param.RegisterForm;

public interface UserService {

    UserInfo registerUser(RegisterForm registerForm);

    UserInfo getUserDetails(Long id);

}
