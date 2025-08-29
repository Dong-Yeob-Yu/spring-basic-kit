package smartwin.springbasickit.common.util;

import org.springframework.security.authentication.AuthenticationCredentialsNotFoundException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Set;
import java.util.stream.Collectors;

/**
 * 회원 정보 관련 공통 Utils
 *
 */
public class AuthUtils {

    /**
     * 회원 ID 세션에서 추출
     *
     */
    public static Long getMemberId() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();


        if (authentication != null && authentication.getPrincipal() instanceof UserDetails userDetails) {
            return Long.parseLong(userDetails.getUsername());
        }

        throw new AuthenticationCredentialsNotFoundException("로그인된 사용자를 찾을 수 없습니다.");
    }

    /**
     * 권한 반환
     *
     */
    public static Set<String> getRoles() {
        var authentication = SecurityContextHolder.getContext().getAuthentication();

        if (authentication == null || authentication.getAuthorities() == null) {
            throw new AuthenticationCredentialsNotFoundException("로그인된 사용자를 찾을 수 없습니다.");
        }


        return authentication.getAuthorities().stream()
                             .map(GrantedAuthority::getAuthority)
                             .map(String::toUpperCase)
                             .collect(Collectors.toUnmodifiableSet());  // 불변 컬렉션으로 반환, add, remove 시 UnsupportedOperationException 발생
    }

    /**
     *  role이 있는지 확인
     */
    public static boolean hasAnyRole(String... roles) {
        var set = getRoles();
        for (String role : roles) {
            String want = role == null ? "" : role.trim().toUpperCase();

            if (!want.startsWith("ROLE_")) {
                want = "ROLE_" + want;
            }

            if (set.contains(want)) {
                return true;
            }
        }
        return false;
    }

}