public class TestNUmber {
    public static void main(String[] args) {
        String str = "123";
        System.out.println(isValidNumber(str));
        str = "123.456";
        System.out.println(isValidNumber(str));
        str = "-123.456";
        System.out.println(isValidNumber(str));
        str = "+123.456";
        System.out.println(isValidNumber(str));
        str = "123.456.789";
        System.out.println(isValidNumber(str));
        str = "123.456e-789";
        System.out.println(isValidNumber(str));
    }

    public static boolean isValidNumber(String str) {
        return str != null &&
                (str.matches("^[+-]?\\d+$") || // 纯数字
                        str.matches("^[+-]?\\d+\\.\\d+$")); // 小数
    }
}
