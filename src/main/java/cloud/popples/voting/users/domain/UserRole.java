package cloud.popples.voting.users.domain;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;
import org.apache.commons.lang3.StringUtils;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.util.Assert;

import javax.persistence.*;

@EqualsAndHashCode(callSuper = true)
@Entity
@Table(name = "role")
@Data
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

}
