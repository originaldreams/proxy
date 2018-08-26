package com.originaldreams.proxycenter;

import org.json.JSONObject;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

public class ProxycenterApplicationTests {

    @Test
    public void test() throws Exception{
        String a = "{\"success\":0,\"data\":null,\"message\":null}";
        JSONObject json = new JSONObject(a);
        json.getJSONObject("data");
        System.out.println(json.getJSONObject("data"));
    }


}
