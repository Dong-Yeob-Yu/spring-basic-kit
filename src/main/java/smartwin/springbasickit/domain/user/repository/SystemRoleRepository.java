package smartwin.springbasickit.domain.user.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import smartwin.springbasickit.domain.user.entity.SystemRole;

import java.util.List;

@Repository
public interface SystemRoleRepository extends JpaRepository<SystemRole, Long> {
    List<SystemRole> findByRole(String role);
}
