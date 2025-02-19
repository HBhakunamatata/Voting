package cloud.popples.voting.users.repository;

import cloud.popples.voting.users.domain.UserRole;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRoleRepository extends JpaRepository<UserRole, Long> {

    Optional<UserRole> findByAuthority(String authority);

}
