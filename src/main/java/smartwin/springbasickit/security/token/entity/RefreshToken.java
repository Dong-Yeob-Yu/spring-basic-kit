package smartwin.springbasickit.security.token.entity;

import jakarta.persistence.*;
import lombok.*;
import org.antlr.v4.runtime.Token;
import org.hibernate.annotations.Comment;
import org.hibernate.annotations.GenericGenerator;
import org.springframework.data.domain.Persistable;
import smartwin.springbasickit.common.entity.BaseEntityWithoutUpdatedAt;

import java.time.LocalDateTime;

@Entity
@Table(name = "refresh_token")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@Builder
public class RefreshToken extends BaseEntityWithoutUpdatedAt implements Persistable<String> {

    @Id
    private String id;

    @Column(nullable = false)
    @Comment("해쉬화된 UUID, 쿠키에 저장된 secret 값이랑 비교")
    private String secretHash;

    @Column(nullable = false)
    @Comment("만료 기간")
    private LocalDateTime expiredAt;

    @Enumerated(EnumType.STRING)
    @Comment("토큰 타입 (PC, MOBILE)")
    private TokenType tokenType;

    @Column(nullable = false)
    @Comment("사용자 ID값")
    private Long systemUserId;

    @Comment("무효화 여부 (true -> 무효화, false -> 비무효화)")
    private boolean isRevoked;

    @Override
    public boolean isNew() {
        return this.getCreatedAt() == null;
    }
}
