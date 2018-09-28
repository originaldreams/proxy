package com.originaldreams.proxycenter;


import com.originaldreams.proxycenter.util.JwtUtil;
import org.junit.Test;

public class JwtTest {

    @Test
    public void testJwt() {
        JwtUtil jwtUtil = new JwtUtil();
        String key = jwtUtil.createJWT("id", "asdasdsa", 10000L);
        System.out.println(jwtUtil.parseJWT(key).getId());

    }
}
