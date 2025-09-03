package smartwin.springbasickit.domain.user.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import smartwin.springbasickit.domain.user.entity.SystemUserRole;

@Repository
public interface SystemUserRoleRepository extends JpaRepository<SystemUserRole, Long> {
}
