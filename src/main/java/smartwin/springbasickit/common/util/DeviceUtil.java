package smartwin.springbasickit.common.util;

import org.springframework.stereotype.Component;

@Component
public class DeviceUtil {

    // Mobile, PC 확인
    public static String resolve(String userAgent) {

        if (userAgent == null) {
            throw new IllegalArgumentException("userAgent cannot be null");
        }

        String userAgentLowerCase = userAgent.toLowerCase();

        if (userAgentLowerCase.contains("mobile") || userAgentLowerCase.contains("android") || userAgentLowerCase.contains("iphone")) {
            return "MOBILE";
        } else {
            return "PC";
        }
    }

}
