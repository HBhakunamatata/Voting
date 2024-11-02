package cloud.popples.voting.users.param;

import cloud.popples.voting.users.domain.UserInfo;
import cloud.popples.voting.users.domain.UserRole;
import com.google.common.collect.Sets;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;

@Data
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = false)
public class RegisterForm {

    @NotNull
    private String username;

    @NotNull
    private String password;

    @Pattern(regexp = "^(.+)@(\\S+)$")
    private String email;

    @NotNull
    private String name;

    public UserInfo toUserInfo(PasswordEncoder passwordEncoder, UserRole userRole) {
        return UserInfo.builder()
                .username(this.username)
                .password(passwordEncoder.encode(this.password))
                .name(this.name)
                .email(this.email)
                .authorities(Sets.newHashSet(userRole))
                .build();
    }

}
