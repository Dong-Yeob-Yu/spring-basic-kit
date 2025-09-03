package smartwin.springbasickit.domain.user.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import smartwin.springbasickit.domain.user.entity.SystemUserEntity;

import java.util.List;
import java.util.Optional;

@Repository
public interface SystemUserRepository extends JpaRepository<SystemUserEntity, Long> {
    Optional<SystemUserEntity> findByLoginId(String loginId);

    @Query("SELECT sr.role " +
            "FROM SystemUserEntity sue " +
            "JOIN SystemUserRole sur ON sur.systemUserId = sue.id " +
            "JOIN SystemRole sr ON sr.id = sur.systemRoleId " +
            "WHERE sue.id = :systemUserId ")
    List<String> findRolesByUserId(Long systemUserId);

}
