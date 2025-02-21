package cloud.popples.voting.users.domain;

import lombok.*;
import lombok.experimental.SuperBuilder;
import org.apache.commons.lang3.StringUtils;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.util.Assert;

import javax.persistence.*;
import java.io.Serializable;
import java.util.*;

@Getter
@Setter
@ToString
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

    @ManyToMany(cascade = CascadeType.ALL, targetEntity = UserRole.class, fetch = FetchType.EAGER)
    @JoinTable(name = "relation_user_role",
            inverseJoinColumns = {
                    @JoinColumn(name = "role_id", table = "user_role",
                            foreignKey = @ForeignKey(name = "none", value = ConstraintMode.NO_CONSTRAINT))
            },
            joinColumns = {
                    @JoinColumn(name = "user_id", table = "user",
                            foreignKey = @ForeignKey(name = "none", value = ConstraintMode.NO_CONSTRAINT))
            })
    @ToString.Exclude
    private final Set<? extends GrantedAuthority> authorities;


    public UserInfo(String username, String password, String name, String email,
                    Collection<? extends GrantedAuthority> authorities) {
        Assert.isTrue(!StringUtils.isAnyBlank(username, password, name, email), "Cannot pass null or empty values to constructor");
        this.username = username;
        this.password = password;
        this.name = name;
        this.email = email;
        this.authorities = Collections.unmodifiableSet(sortAuthorities(authorities));
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return Collections.unmodifiableSet(authorities);
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

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        UserInfo userInfo = (UserInfo) o;
        return Objects.equals(id, userInfo.id)
                && Objects.equals(username, userInfo.username)
                && Objects.equals(password, userInfo.password)
                && Objects.equals(name, userInfo.name)
                && Objects.equals(email, userInfo.email)
                && Objects.equals(authorities, userInfo.authorities);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, username, password, name, email, authorities);
    }

    private static SortedSet<? extends GrantedAuthority> sortAuthorities(Collection<? extends GrantedAuthority> authorities) {
        Assert.notEmpty(authorities, "Cannot pass an empty GrantedAuthority collection");
        SortedSet<GrantedAuthority> sortedAuthorities = new TreeSet<>(new AuthorityComparator());
        sortedAuthorities.addAll(authorities);
        return sortedAuthorities;
    }

    private static final class AuthorityComparator implements Comparator<GrantedAuthority>, Serializable {

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
