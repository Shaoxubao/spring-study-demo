import java.util.Enumeration;
import java.util.Vector;

public class TestVector {
    public static void main(String[] args) {
        Vector vcConnections = new Vector();

        for (int i = 0; i < 10; i++) {
            vcConnections.addElement(i);
        }

        Enumeration enumerate = vcConnections.elements();
        while (enumerate.hasMoreElements()) {
            Object object = enumerate.nextElement();
            if (Integer.parseInt(object.toString()) == 7) {
                vcConnections.remove(object);
            }
        }

        Enumeration enumerateNew = vcConnections.elements();
        while (enumerateNew.hasMoreElements()) {
            Object object = enumerateNew.nextElement();
            System.out.println(object.toString());
        }
    }
}
