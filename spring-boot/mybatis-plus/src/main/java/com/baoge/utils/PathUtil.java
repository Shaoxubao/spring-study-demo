package com.baoge.utils;

import lombok.extern.slf4j.Slf4j;
import org.springframework.util.ResourceUtils;

import java.io.*;
import java.net.URI;
import java.net.URISyntaxException;

/**
 *
 */

/**
 * 这个类提供了一些根据类的class文件位置来定位的方法。

 */
@Slf4j
public class PathUtil {

/** */
    /**
     * 获取一个Class的绝对路径
     *
     * @param clazz Class对象
     * @return Class的绝对路径
     */

    public static String getPathByClass(Class clazz) {

        String path = null;

        try {

            URI uri = clazz.getResource("").toURI();

            File file = new File(uri);

            path = file.getCanonicalPath();

        } catch (URISyntaxException e) {

            e.printStackTrace();

        } catch (IOException e) {

            e.printStackTrace();

        }

        return path;

    }

/** */
    /**
     * 获取一个文件相对于一个Class相对的绝对路径
     *
     * @param clazz        Class对象
     * @param relativePath Class对象的相对路径
     * @return 文件绝对路径
     */

    public static String getFilePathByClass(Class clazz, String relativePath) {

        String filePath = null;

        String clazzPath = getPathByClass(clazz);

        StringBuffer sbPath = new StringBuffer(clazzPath);

        sbPath.append(File.separator);

        sbPath.append(relativePath);

        File file = new File(sbPath.toString());

        try {

            filePath = file.getCanonicalPath();

        } catch (IOException e) {

            e.printStackTrace();

        }

        return filePath;

    }
    /**
     * 读取本文件
     * @param fileName 文件路径
     * @return InputStream 返回参数说明
     * @exception/throws
     */
    public static InputStream readFileAsStream(String fileName) {
        String path = null;
        InputStream inputStream=null;
        if(CheckRunInIDEA())
        {
            inputStream=readPathFileAsStream(fileName);
            log.info("readPathFileAsStream"+fileName);
        }
        else {
            inputStream=readJarFileAsStream(fileName);
            log.info("readJarFileAsStream "+fileName);
        }
        return inputStream;
    }
    /**
     * 读取本机文件文件
     * @param fileName 文件路径
     * @return InputStream 返回参数说明
    */
    private static InputStream readPathFileAsStream(String fileName) {
       String path = null;
       InputStream inputStream=null;
       try {
            path = ResourceUtils.getFile(ResourceUtils.CLASSPATH_URL_PREFIX + fileName)
                    .getAbsoluteFile().getPath();
           inputStream=new FileInputStream(path);
        } catch (FileNotFoundException e) {
            log.info(fileName + "readPathFileAsStream⽂件不存在：", e);
        }
        log.info(fileName + " path: " + path);
        return inputStream;
    }

    /**
     * 读取jar包文件
     * @param fileName 文件路径
     * @return InputStream 返回参数说明
    */
    public static InputStream readJarFileAsStream(String fileName){
        InputStream inputStream = null;
        BufferedReader br = null;
        StringBuilder sb = new StringBuilder();
        try {
            inputStream = PathUtil.class.getClassLoader().getResourceAsStream(fileName);
            }
        catch ( Exception e) {
            e.printStackTrace();
        }
        return inputStream;
    }

    /**
     * 检查运行环境
     * @return true 时为idea运行环境，false为jar包运行
    */
    public static boolean CheckRunInIDEA() {
        try {
            Class.forName("com.intellij.rt.execution.application.AppMainV2");
            return true;
        } catch (ClassNotFoundException ignored) {
            return false;
        }
    }

}