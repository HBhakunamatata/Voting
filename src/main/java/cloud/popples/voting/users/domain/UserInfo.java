package cloud.popples.voting.users.domain;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import lombok.experimental.SuperBuilder;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.util.Assert;

import javax.persistence.*;
import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.*;

@EqualsAndHashCode(callSuper = true)
@Data
@Entity
@Table(name = "user_info")
@NoArgsConstructor(force = true)
@RequiredArgsConstructor
@SuperBuilder
public class UserInfo extends BaseAuthEntity implements UserDetails {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private final String username;
    private final String password;
    private final String name;
    private final String email;

    @ManyToMany(cascade = CascadeType.ALL, targetEntity = UserRole.class)
    @JoinTable(name = "relation_user_role",
            joinColumns = {
                    @JoinColumn(name = "role_id", table = "user_role",
                            foreignKey = @ForeignKey(name = "none", value = ConstraintMode.NO_CONSTRAINT))
            },
            inverseJoinColumns = {
                    @JoinColumn(name = "user_id", table = "user",
                            foreignKey = @ForeignKey(name = "none", value = ConstraintMode.NO_CONSTRAINT))
            })
    private final Set<? extends GrantedAuthority> authorities;


    public UserInfo(String username, String password, String name, String email,
                    Collection<? extends GrantedAuthority> authorities) {
        Assert.isTrue(username != null && !"".equals(username) && password != null, "Cannot pass null or empty values to constructor");
        this.username = username;
        this.password = password;
        this.name = name;
        this.email = email;
        this.authorities = Collections.unmodifiableSet(sortAuthorities(authorities));

        LocalDateTime now = LocalDateTime.now();
        this.createTime = now;
        this.updateTime = now;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return Collections.emptyList();
    }

    @Override
    public String getPassword() {
        return this.password;
    }

    @Override
    public String getUsername() {
        return this.username;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }

    private static SortedSet<? extends GrantedAuthority> sortAuthorities(Collection<? extends GrantedAuthority> authorities) {
        Assert.notNull(authorities, "Cannot pass a null GrantedAuthority collection");
        SortedSet<GrantedAuthority> sortedAuthorities = new TreeSet<>(new AuthorityComparator());
        sortedAuthorities.addAll(authorities);
        return sortedAuthorities;
    }


    private static class AuthorityComparator implements Comparator<GrantedAuthority>, Serializable {

        private static final Long serialVersionUID = 1L;

        private AuthorityComparator() {}

        @Override
        public int compare(GrantedAuthority o1, GrantedAuthority o2) {
            if (o1 == null) {
                return -1;
            } else if (o2 == null) {
                return 1;
            } else {
                return o1.getAuthority().compareTo(o2.getAuthority());
            }
        }
    }

}
