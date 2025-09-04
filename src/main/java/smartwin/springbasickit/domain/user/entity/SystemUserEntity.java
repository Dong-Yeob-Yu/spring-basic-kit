package smartwin.springbasickit.domain.user.entity;

import jakarta.persistence.*;
import lombok.*;
import smartwin.springbasickit.common.entity.BaseEntity;

import java.time.Instant;

@Entity
@Table(name = "system_users")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@Builder
public class SystemUserEntity extends BaseEntity {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private String loginId;

    private String loginPassword;

    private String name;

    private String nickname;

    private String email;

    private Instant lastLoginAt;

    public void recordLastLoginAt() {
        this.lastLoginAt = Instant.now();
    }

}
