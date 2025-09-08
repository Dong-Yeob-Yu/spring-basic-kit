package smartwin.springbasickit.domain.user.service;

import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import smartwin.springbasickit.domain.user.repository.SystemUserRepository;
import smartwin.springbasickit.security.jwt.JwtUtil;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CustomUserDetailsService {

    private final SystemUserRepository systemUserRepository;
    private final JwtUtil jwtUtil;

    /**
     * username -> System User ID
     * */
    public UserDetails loadUserByUsername(Long systemUserId, String accessToken) throws UsernameNotFoundException {

        List<String> roles = jwtUtil.extractClaim(accessToken, "roles", List.class);

        List<SimpleGrantedAuthority> authorities = roles.stream().map(SimpleGrantedAuthority::new).toList();

        return User.builder()
                   .username(String.valueOf(systemUserId))
                   .password("N/A")
                   .authorities(authorities)
                   .build();
    }
}
