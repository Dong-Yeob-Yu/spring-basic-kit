package smartwin.springbasickit.domain.user.entity;

import jakarta.persistence.*;
import lombok.*;
import smartwin.springbasickit.common.entity.BaseEntity;

@Entity
@Table(name = "system_users")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@Builder
public class SystemUserEntity extends BaseEntity {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String loginId;

    private String loginPassword;

    private String name;

    private String nickname;

    private String email;

    private Long userRoleId;

}
