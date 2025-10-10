package org.sparta.whyncoming.common.encode;

import at.favre.lib.crypto.bcrypt.BCrypt;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class EncoderUtil {

    @Value("${security.bcrypt.cost}")
    private int cost;

    public String encode(String rawPassword) {
        return BCrypt.withDefaults().hashToString(cost, rawPassword.toCharArray());
    }

    public boolean matches(String rawPassword, String encodedPassword) {
        BCrypt.Result result = BCrypt.verifyer().verify(rawPassword.toCharArray(), encodedPassword);
        return result.verified;
    }
}
