package cloud.popples.voting.users.domain;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;
import org.springframework.security.core.GrantedAuthority;

import javax.persistence.*;

@EqualsAndHashCode(callSuper = true)
@Entity
@Table(name = "role")
@Data
@NoArgsConstructor(force = true)
@AllArgsConstructor
@SuperBuilder
public class UserRole extends BaseAuthEntity implements GrantedAuthority {

    public UserRole(String role) {
        this(null, role);
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
