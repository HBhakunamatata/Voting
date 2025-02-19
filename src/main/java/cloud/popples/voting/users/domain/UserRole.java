package cloud.popples.voting.users.domain;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import lombok.experimental.SuperBuilder;
import org.apache.commons.lang3.StringUtils;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.util.Assert;

import javax.persistence.*;
import java.util.Objects;

@Entity
@Table(name = "role")
@Getter
@Setter
@ToString
@NoArgsConstructor(force = true)
@SuperBuilder
public class UserRole extends BaseAuthEntity implements GrantedAuthority {

    public UserRole(String role) {
        this(null, role);
    }

    public UserRole(Long id, String role) {
        Assert.isTrue(StringUtils.isNotBlank(role), "Role cannot be blank");
        this.id = id;
        this.authority = role;
    }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private final Long id;

    @Column(name = "role_name")
    private final String authority;

    @Override
    public String getAuthority() {
        return this.authority;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        UserRole userRole = (UserRole) o;
        return Objects.equals(id, userRole.id) && Objects.equals(authority, userRole.authority);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, authority);
    }
}
