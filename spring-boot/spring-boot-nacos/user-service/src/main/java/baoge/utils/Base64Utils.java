package baoge.utils;

import org.apache.tomcat.util.codec.binary.Base64;

public class Base64Utils {

    public static String encode(String input) {
        return new String(Base64.encodeBase64(input.getBytes()));
    }

    public static void main(String[] args) {
        System.out.println(encode("12345678901234567890123456789012"));
    }

}
