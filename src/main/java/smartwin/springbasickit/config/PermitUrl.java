package smartwin.springbasickit.config;

import lombok.Getter;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@Getter
public class PermitUrl {

    private final List<String> patterns = List.of(
            "/api/auth/login",
            "/api/public/**",
            "/api/users"
    );
}
