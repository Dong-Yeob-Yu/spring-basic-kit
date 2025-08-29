package smartwin.springbasickit.domain.user.entity;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.Comment;

@Entity
@Table(name = "system_user_role")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@Builder
public class SystemUserRole {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Comment("롤 설정 ex)ROLE_USER, ROLE_ADMIN 등등")
    @Column(nullable = false, length = 64, unique = true)
    private String role;

    @Comment("설명")
    private String description;

    @Builder.Default
    @Comment("활성화 여부(true, false)")
    @Column(nullable = false)
    private boolean isEnabled = true;
}
