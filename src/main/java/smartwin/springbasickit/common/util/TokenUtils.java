package smartwin.springbasickit.common.util;

import org.springframework.stereotype.Component;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class TokenUtils {

    /**
     *  RT 생성 유틸
     * @return string RefreshToken
     */
    public static String generateRefreshToken() {
        byte[] rtBytes = new byte[32];
        new java.security.SecureRandom().nextBytes(rtBytes);
        return java.util.Base64.getUrlEncoder().withoutPadding().encodeToString(rtBytes); // 쿠키에 담을 평문 RT
    }

    /**
     * RefreshToken 평문을 hash화 시키는 유틸
     * @param rt - RefreshToken
     * @return hash화된 secret rt
     * */
    public static String hashToken(String rt) {
        try {
            MessageDigest digest = MessageDigest.getInstance("SHA-256");
            byte[] digest1 = digest.digest(rt.getBytes(StandardCharsets.UTF_8));

            StringBuilder hex = new StringBuilder();
            for (byte b : digest1) {
                hex.append(String.format("%02x", b));
            }
            return hex.toString();

        } catch (NoSuchAlgorithmException e) {
            throw new IllegalStateException(e);
        }
    }


    public static boolean equalsToken(String rawToken, String secretToken) {
        return hashToken(rawToken).equals(secretToken);
    }

}
