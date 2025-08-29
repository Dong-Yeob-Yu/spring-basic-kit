package smartwin.springbasickit.security.token.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import smartwin.springbasickit.security.token.entity.RefreshToken;
import smartwin.springbasickit.security.token.entity.TokenType;

import java.time.Instant;

@Repository
public interface RefreshTokenRepository extends JpaRepository<RefreshToken, Long> {

    @Modifying(clearAutomatically = true)
    @Query("UPDATE RefreshToken rs SET rs.isRevoked = true WHERE rs.id = :id ")
    int updateRevokedTrueById(Long id);

    @Modifying(clearAutomatically = true)
    @Query("UPDATE RefreshToken rs SET rs.isRevoked = true, rs.expiredAt = :now WHERE rs.systemUserId = :systemUserId AND rs.tokenType = :tokenType AND rs.isRevoked = false ")
    int updateTokenAtLogout(Long systemUserId, TokenType tokenType, Instant now);
}
