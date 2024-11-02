package cloud.popples.voting.users.controller;

import cloud.popples.voting.users.domain.UserInfo;
import cloud.popples.voting.users.param.RegisterForm;
import cloud.popples.voting.users.service.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.validation.Valid;

@Controller
@RequestMapping("/auth")
public class AuthController {

    private static final Logger log = LoggerFactory.getLogger(AuthController.class);
    private final UserService userService;

    @Autowired
    public AuthController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/login")
    public String login() {
        return "login";
    }

    @GetMapping("/register")
    public String registerPage() {
        return "register";
    }

    @PostMapping("/register")
    public String register(@Valid RegisterForm registerForm) {
        UserInfo userInfo = userService.registerUser(registerForm);
//        return "redirect:/users/" + userInfo.getId();
        return "redirect:/auth/login";
    }

}
