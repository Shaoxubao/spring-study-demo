public class UrlSubstringExample {
    public static void main(String[] args) {
        String url = "http://192.168.43.213:8080/qk/xtgl/system/czrzlb";
        int startIndex = url.indexOf("/qk");
        if (startIndex != -1) {
            String result = url.substring(startIndex + 3);
            System.out.println(result); // 输出: /xtgl/system/czrzlb
        }
    }
}