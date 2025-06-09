import java.net.URL;

public class TestUrl {
    public static void main(String[] args) {

        System.out.println(getHostFromReferer("http://asd198.168.92.99:18866"));
    }

    private static String getHostFromReferer(String referer) {
        String host = null;
        if(referer != null)
        {
            URL url = null;
            try {
                url = new URL(referer);
                host = url.getHost();

            } catch (Exception e) {

            }
        }
        return host;
    }
}
