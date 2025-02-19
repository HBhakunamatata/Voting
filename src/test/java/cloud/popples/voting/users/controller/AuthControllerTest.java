package cloud.popples.voting.users.controller;

import cloud.popples.voting.users.service.UserService;
import cloud.popples.voting.utils.TestUtils;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import java.util.LinkedHashMap;
import java.util.Map;

import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(controllers = {AuthController.class})
class AuthControllerTest {

    @Autowired
    private MockMvc mvc;

    @MockBean
    private UserService userService;

    @Test
    void whenInvalidUserLoginThen401() throws Exception {
        mvc.perform(get("/auth/login"))
                .andExpect(status().is4xxClientError());
    }

    @Test
    @WithMockUser
    void whenRoleUserLoginThenGetWebPage() throws Exception {
        mvc.perform(get("/auth/login"))
                .andExpect(status().isOk())
                .andExpect(view().name("login"));
    }

    @Test
    @WithMockUser
    void whenRoleUserRegisterThenGetWebPage() throws Exception {
        mvc.perform(get("/auth/register"))
                .andExpect(status().isOk())
                .andExpect(view().name("register"));
    }

    @Test
    @WithMockUser
    void whenUserRegisterThenRegistered() throws Exception {
        Map<String, String> registerForm = new LinkedHashMap<>();
        registerForm.put("username", "testusername");
        registerForm.put("password", "testusername");
        registerForm.put("email", "test@test.com");
        registerForm.put("name", "testusername");

        mvc.perform(post("/auth/register")
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(TestUtils.mapToString(registerForm))
                )
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/auth/login"));
    }

}
