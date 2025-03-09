package cloud.popples.voting.users.controller;

import cloud.popples.voting.users.service.UserService;
import cloud.popples.voting.utils.TestUtils;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.LinkedHashMap;
import java.util.Map;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(value = AuthController.class, excludeAutoConfiguration = {SecurityAutoConfiguration.class})
class AuthControllerTest {

    @Autowired
    private MockMvc mvc;

    @MockBean
    private UserService userService;

    @Test
    void whenInvalidUserLoginThen401() throws Exception {
        mvc.perform(get("/auth/login"))
                .andExpect(status().is2xxSuccessful());
    }

    @Test
    void whenRoleUserLoginThenGetWebPage() throws Exception {
        mvc.perform(get("/auth/login"))
                .andExpect(status().isOk())
                .andExpect(view().name("login"));
    }

    @Test
    void whenRoleUserRegisterThenGetWebPage() throws Exception {
        mvc.perform(get("/auth/register"))
                .andExpect(status().isOk())
                .andExpect(view().name("register"));
    }

    @Test
    void whenUserRegisterThenRegistered() throws Exception {
        Map<String, String> registerForm = new LinkedHashMap<>();
        registerForm.put("username", "testusername");
        registerForm.put("password", "testusername");
        registerForm.put("email", "test@test.com");
        registerForm.put("name", "testusername");

        mvc.perform(post("/auth/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(TestUtils.mapToString(registerForm))
                )
                .andExpect(status().is(HttpStatus.SEE_OTHER.value()))
                .andExpect(redirectedUrl("/auth/login"));
    }

}
