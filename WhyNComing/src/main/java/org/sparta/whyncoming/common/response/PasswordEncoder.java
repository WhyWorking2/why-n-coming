package org.sparta.whyncoming.common.response;

import at.favre.lib.crypto.bcrypt.BCrypt;

public class PasswordEncoder {

    public String encode(String rawPassword) {
        int cost = 12; //임시로 하드코딩 상태, 나중에 이 부분 조절할 수 있게 바꿔야 함.
        return BCrypt.withDefaults().hashToString(cost, rawPassword.toCharArray());
    }

    public boolean matches(String rawPassword, String encodedPassword) {
        BCrypt.Result result = BCrypt.verifyer().verify(rawPassword.toCharArray(), encodedPassword);
        return result.verified;
    }
}
