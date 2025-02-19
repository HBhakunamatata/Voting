package cloud.popples.voting.users.param;

import cloud.popples.voting.users.domain.UserInfo;
import cloud.popples.voting.users.domain.UserRole;
import com.google.common.collect.Sets;
import lombok.*;
import org.springframework.security.crypto.password.PasswordEncoder;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import java.util.Objects;

@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
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

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        RegisterForm that = (RegisterForm) o;
        return Objects.equals(username, that.username)
                && Objects.equals(password, that.password)
                && Objects.equals(email, that.email)
                && Objects.equals(name, that.name);
    }

    @Override
    public int hashCode() {
        return Objects.hash(username, password, email, name);
    }
}
