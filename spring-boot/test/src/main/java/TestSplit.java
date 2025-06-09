import java.io.*;

public class TestSplit {
    public static String IN_FLIE = "D:\\javatools\\act_evt_log.sql";
    public static String OUT_FLIE_PATH = "D:\\common";
    public static String splitValue = "DROP TABLE IF EXISTS";

    public static void main(String[] args) throws IOException {
        //读取被分割的文件
        File file = new File(IN_FLIE);
        if (!file.exists() || (!file.isFile())) {
            System.out.println("文件不存在");
        }
        //用字符流读取文件内容
        BufferedReader bufferedReader = new BufferedReader(
                new InputStreamReader(new FileInputStream(file), "UTF-8"));
        String bufStrLine = null;
        Writer writer = null ;
        //一行一行读取
        while ((bufStrLine = bufferedReader.readLine()) != null) {
            //读到存在对应标识字符串
            if (bufStrLine.contains(splitValue)){
                //不为空，则把上一个文件输出流关闭
                if (writer != null){
                    writer.close();
                }
                //创建io输出流,通过分割改行数据获取文件名
                String fileName = OUT_FLIE_PATH+bufStrLine.split("`")[1]+".sql";
                writer = new FileWriter(fileName);
                System.out.println("创建文件"+fileName);
            }
            //写一行数据到输出文件
            if (writer != null){
                writer.write(bufStrLine+"\r\n");

            }
        }
        //关闭最后一个输出流
        if (writer != null){
            writer.close();
        }
        System.out.println("文件大小" + file.length());
    }
}
