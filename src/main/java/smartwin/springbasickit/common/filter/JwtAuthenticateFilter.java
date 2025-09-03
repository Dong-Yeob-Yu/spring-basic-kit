package smartwin.springbasickit.common.filter;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;
import org.springframework.util.AntPathMatcher;
import org.springframework.web.filter.OncePerRequestFilter;
import smartwin.springbasickit.common.exception.code.ErrorCode;
import smartwin.springbasickit.common.response.ApiResponse;
import smartwin.springbasickit.config.PermitUrl;
import smartwin.springbasickit.domain.user.service.CustomUserDetailsService;
import smartwin.springbasickit.security.jwt.JwtUtil;

import java.io.IOException;

@Component
@RequiredArgsConstructor
public class JwtAuthenticateFilter extends OncePerRequestFilter {

    private final JwtUtil jwtUtil;
    private final CustomUserDetailsService customUserDetailsService;
    private final ObjectMapper objectMapper;
    private final PermitUrl permitUrl;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {

        String accessToken = jwtUtil.extractAccessTokenFromCookies(request);

        // 토큰이 없으면 401 에러 발생
        if (accessToken == null || accessToken.isBlank()) {
            this.setErrorResponse(response, ErrorCode.EXPIRED_TOKEN_NULL);
            return;
        }

        boolean isValid = jwtUtil.validateAccessToken(accessToken);

        if (isValid) {
            setAuthentication(Long.valueOf(jwtUtil.extractSystemUserId(accessToken)));
            filterChain.doFilter(request, response);
        } else {
            // 토큰 만료
            this.setErrorResponse(response, ErrorCode.EXPIRED_AT_TOKEN);
        }
    }

    /**
     * SecurityContextHolder에 SetAuthentication
     *
     */
    private void setAuthentication(Long systemUserId) {
        UserDetails userDetails = customUserDetailsService.loadUserByUsername(systemUserId);

        UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken = new UsernamePasswordAuthenticationToken(
                userDetails,
                null,
                userDetails.getAuthorities()
        );

        SecurityContextHolder.getContext().setAuthentication(usernamePasswordAuthenticationToken);

    }

    private void setErrorResponse(HttpServletResponse response, ErrorCode errorCode) throws IOException {
        response.setStatus(errorCode.getStatus().value());
        response.setContentType("application/json;charset=UTF-8");
        response.getWriter().write(
                objectMapper.writeValueAsString(ApiResponse.fail(
                        errorCode.getStatus().value(),
                        errorCode.getMessage(),
                        errorCode
                ))
        );
    }

    @Override
    protected boolean shouldNotFilter(HttpServletRequest request) {
        String uri = request.getRequestURI();
        AntPathMatcher matcher = new AntPathMatcher();
        return permitUrl.getPatterns().stream().anyMatch(p -> matcher.match(p, uri));
    }
}
