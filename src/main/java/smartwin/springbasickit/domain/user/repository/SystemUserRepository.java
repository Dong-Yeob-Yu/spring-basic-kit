package smartwin.springbasickit.domain.user.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import smartwin.springbasickit.domain.user.entity.SystemUserEntity;

@Repository
public interface SystemUserRepository extends JpaRepository<SystemUserEntity, Long> {
}
