package smartwin.springbasickit.security.jwt;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Positive;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.convert.DurationUnit;
import org.springframework.validation.annotation.Validated;

import java.time.Duration;
import java.time.temporal.ChronoUnit;

@Validated
@ConfigurationProperties(prefix = "security.jwt")
public record JwtProperties(
    @NotBlank String secret,
    @NotBlank String issuer,
    @DurationUnit(ChronoUnit.MINUTES) Duration accessTtl,
    @DurationUnit(ChronoUnit.DAYS) Duration refreshDays
) {
}
