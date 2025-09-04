package file;

import java.nio.file.Paths;
import java.nio.file.Files;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.io.BufferedReader;
import java.io.IOException;

/**
 * 逐行读取机制：使用BufferedReader.readLine()按行读取文件，避免一次性加载整个文件到内存，适合大文件场景
 * 目标行定向处理：仅在读取到目标行时才转换为字节数组并提取内容，减少无效计算
 * 内存效率：每行处理完成后内存自动释放，整体内存占用与文件行数无关，仅取决于单行最大字节数
 * 兼容性：保留原有的字符位置信息输出格式，同时通过StandardCharsets.UTF_8确保字节计算与文件编码一致
 * 比如一个文件里有：
 * hello世界
 * 你好！
 * 程序输入：行号:2  startByte:3  endByte:5
 * 程序输出： 好
 */
public class CharacterPositionAnalyzer4 {
    public static void main(String[] args) {
        String filePath = "D:\\test.txt";
        // 目标参数：行号(从1开始)、起始字节(从0开始)、结束字节(包含)
        int targetLine = 2;
        int startByte = 3;
        int endByte = 5;
        String result = "";
        
        try (BufferedReader reader = Files.newBufferedReader(Paths.get(filePath), StandardCharsets.UTF_8)) {
            String line;
            int currentLineNumber = 1;
            
            // 逐行读取文件
            while ((line = reader.readLine()) != null) {
                // 打印当前行所有字符的位置信息（保留原有功能）
                printCharacterPositions(line, currentLineNumber);
                
                // 处理目标行：提取指定字节范围内容
                if (currentLineNumber == targetLine) {
                    byte[] lineBytes = line.getBytes(StandardCharsets.UTF_8);
                    result = extractBytesFromLine(lineBytes, startByte, endByte);
                }
                
                currentLineNumber++;
            }
            
            System.out.printf("\n行号:%d, 字节范围:%d-%d 的内容: '%s'%n", targetLine, startByte, endByte, result);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // 新增：打印单行内所有字符的位置信息（替代原analyzeCharacterPositions中的循环逻辑）
    private static void printCharacterPositions(String line, int lineNumber) {
        byte[] lineBytes = line.getBytes(StandardCharsets.UTF_8);
        int currentBytePos = 0;
        int charInLine = 0;
        
        while (currentBytePos < lineBytes.length) {
            int numBytes = getNumBytesForUTF8Char(lineBytes, currentBytePos);
            if (currentBytePos + numBytes > lineBytes.length) {
                System.err.println("Incomplete character in line " + lineNumber + ", skipping");
                break;
            }
            
            byte[] charBytes = Arrays.copyOfRange(lineBytes, currentBytePos, currentBytePos + numBytes);
            String charStr = new String(charBytes, StandardCharsets.UTF_8);
            if (charStr.length() != 1) {
                System.err.println("Unexpected multiple characters decoded from " + numBytes + " bytes");
                currentBytePos += numBytes;
                continue;
            }
            char c = charStr.charAt(0);
            
            int start = currentBytePos;
            int length = numBytes;
            int end = start + length - 1;
            
            // 输出字符位置信息（格式与原有保持一致）
            System.out.printf("Line: %d, CharInLine: %d, Char: '%s', Start: %d, End: %d, Length: %d%n",
                    lineNumber, charInLine, escapeChar(c), start, end, length);
            
            charInLine++;
            currentBytePos += numBytes;
        }
    }

    // 优化：从行字节数组中提取指定范围字节（原方法逻辑保留，参数改为byte[]）
    private static String extractBytesFromLine(byte[] lineBytes, int startByte, int endByte) {
        // 边界检查：处理起始/结束字节超出范围的情况
        if (startByte < 0 || endByte >= lineBytes.length || startByte > endByte) {
            return "";
        }
        byte[] targetBytes = Arrays.copyOfRange(lineBytes, startByte, endByte + 1);
        return new String(targetBytes, StandardCharsets.UTF_8);
    }

    // 判断UTF-8字符占用的字节数（原有代码不变）
    private static int getNumBytesForUTF8Char(byte[] bytes, int pos) {
        if (pos >= bytes.length) {
            throw new IndexOutOfBoundsException("Position exceeds byte array length");
        }
        byte b = bytes[pos];
        if ((b & 0x80) == 0) { // 1字节字符 (0xxxxxxx)
            return 1;
        } else if ((b & 0xE0) == 0xC0) { // 2字节字符 (110xxxxx)
            return 2;
        } else if ((b & 0xF0) == 0xE0) { // 3字节字符 (1110xxxx)
            return 3;
        } else if ((b & 0xF8) == 0xF0) { // 4字节字符 (11110xxx)
            return 4;
        } else {
            throw new IllegalArgumentException("Invalid UTF-8 leading byte: 0x" + Integer.toHexString(b & 0xFF) + " at position " + pos);
        }
    }

    // 转义控制字符以便显示（原有代码不变）
    private static String escapeChar(char c) {
        if (c == '\n') return "\\n";
        if (c == '\r') return "\\r";
        if (c == '\t') return "\\t";
        if (Character.isISOControl(c)) return String.format("\\u%04x", (int) c);
        return String.valueOf(c);
    }
}