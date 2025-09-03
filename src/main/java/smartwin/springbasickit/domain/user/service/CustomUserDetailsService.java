package smartwin.springbasickit.domain.user.service;

import lombok.RequiredArgsConstructor;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import smartwin.springbasickit.domain.user.repository.SystemUserRepository;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CustomUserDetailsService {

    private final SystemUserRepository systemUserRepository;

    /**
     * username -> System User ID
     * */
    public UserDetails loadUserByUsername(Long systemUserId) throws UsernameNotFoundException {

        List<String> roles = systemUserRepository.findRolesByUserId(systemUserId);

        List<SimpleGrantedAuthority> authorities = roles.stream().map(SimpleGrantedAuthority::new).toList();

        return User.builder()
                   .username(String.valueOf(systemUserId))
                   .password("N/A")
                   .authorities(authorities)
                   .build();
    }
}
