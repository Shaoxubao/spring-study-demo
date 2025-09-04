package file;

import java.nio.file.Paths;
import java.nio.file.Files;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.ArrayList;
import java.util.List;

/**
 * 行内容字节收集：通过currentLineContentBytes列表记录每行实际内容的字节（自动排除CR/LF换行符）
 * 字节范围提取：extractBytesFromLine方法将目标行的字节列表中[startByte, endByte]范围转换为字符串
 * 换行符兼容：正确处理CR/LF/CRLF三种换行符，确保行内容字节收集的准确性
 * 比如一个文件里有：
 * hello世界
 * 你好！
 * 程序输入：行号:2  startByte:3  endByte:5
 * 程序输出： 好
 */
public class CharacterPositionAnalyzer3 {
    public static void main(String[] args) {
        String filePath = "D:\\test.txt";
        // 目标参数：行号(从1开始)、起始字节(从0开始)、结束字节(包含)
        int targetLine = 2;       
        int startByte = 3;        
        int endByte = 5;
        try {
            byte[] fileBytes = Files.readAllBytes(Paths.get(filePath));
            // 调用修改后的方法，传入目标参数
            String result = analyzeCharacterPositions(fileBytes, targetLine, startByte, endByte);
            System.out.printf("\n行号:%d, 字节范围:%d-%d 的内容: '%s'%n", targetLine, startByte, endByte, result);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // 修改方法签名：接收行号、起始字节、结束字节，返回提取内容
    private static String analyzeCharacterPositions(byte[] fileBytes, int targetLine, int startByte, int endByte) {
        int currentBytePos = 0;
        int lineNumber = 1;
        int charInLine = 0;
        // 新增：当前行内容字节列表（排除换行符）
        List<Byte> currentLineContentBytes = new ArrayList<>(); 
        String targetContent = "";

        while (currentBytePos < fileBytes.length) {
            int numBytes = getNumBytesForUTF8Char(fileBytes, currentBytePos);
            if (currentBytePos + numBytes > fileBytes.length) {
                System.err.println("Incomplete character at end of file, skipping");
                break;
            }

            byte[] charBytes = Arrays.copyOfRange(fileBytes, currentBytePos, currentBytePos + numBytes);
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

            // 输出字符位置信息（保留原有功能）
            System.out.printf("Line: %d, CharInLine: %d, Char: '%s', Start: %d, End: %d, Length: %d%n",
                    lineNumber, charInLine, escapeChar(c), start, end, length);

            // 新增：收集行内容字节（排除换行符和回车符）
            if (c != '\n' && c != '\r') {
                for (byte b : charBytes) {
                    currentLineContentBytes.add(b);
                }
            }

            // 更新行号和行内字符索引
            if (c == '\n') {
                // 处理目标行：当前行结束时检查是否为目标行
                if (lineNumber == targetLine) {
                    targetContent = extractBytesFromLine(currentLineContentBytes, startByte, endByte);
                }
                // 重置行内容字节列表，准备下一行
                currentLineContentBytes.clear();
                lineNumber++;
                charInLine = 0;
            } else if (c == '\r') {
                // 处理CRLF换行
                if (currentBytePos + numBytes < fileBytes.length) {
                    int nextNumBytes = getNumBytesForUTF8Char(fileBytes, currentBytePos + numBytes);
                    if (currentBytePos + numBytes + nextNumBytes <= fileBytes.length) {
                        byte[] nextCharBytes = Arrays.copyOfRange(fileBytes, currentBytePos + numBytes, currentBytePos + numBytes + nextNumBytes);
                        String nextCharStr = new String(nextCharBytes, StandardCharsets.UTF_8);
                        if (nextCharStr.length() == 1 && nextCharStr.charAt(0) == '\n') {
                            charInLine++; // CRLF时只在处理LF时换行
                        } else {
                            // 非CRLF情况，当前行结束
                            if (lineNumber == targetLine) {
                                targetContent = extractBytesFromLine(currentLineContentBytes, startByte, endByte);
                            }
                            currentLineContentBytes.clear();
                            lineNumber++;
                            charInLine = 0;
                        }
                    } else {
                        if (lineNumber == targetLine) {
                            targetContent = extractBytesFromLine(currentLineContentBytes, startByte, endByte);
                        }
                        currentLineContentBytes.clear();
                        lineNumber++;
                        charInLine = 0;
                    }
                } else {
                    if (lineNumber == targetLine) {
                        targetContent = extractBytesFromLine(currentLineContentBytes, startByte, endByte);
                    }
                    currentLineContentBytes.clear();
                    lineNumber++;
                    charInLine = 0;
                }
            } else {
                charInLine++;
            }

            currentBytePos += numBytes;
        }

        // 处理文件末尾无换行符的最后一行
        if (!currentLineContentBytes.isEmpty() && lineNumber == targetLine) {
            targetContent = extractBytesFromLine(currentLineContentBytes, startByte, endByte);
        }
        
        return targetContent;
    }

    // 新增：从行内容字节中提取指定范围的字节并转换为字符串
    private static String extractBytesFromLine(List<Byte> lineContentBytes, int startByte, int endByte) {
        // 边界检查
        if (startByte < 0 || endByte >= lineContentBytes.size() || startByte > endByte) {
            return "";
        }
        // 转换List<Byte>为byte[]
        byte[] byteArray = new byte[endByte - startByte + 1];
        for (int i = 0; i < byteArray.length; i++) {
            byteArray[i] = lineContentBytes.get(startByte + i);
        }
        return new String(byteArray, StandardCharsets.UTF_8);
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