package cloud.popples.voting.users.repository;

import cloud.popples.voting.users.domain.UserRole;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRoleRepository extends JpaRepository<UserRole, Long> {

    UserRole findByAuthority(String authority);

}
