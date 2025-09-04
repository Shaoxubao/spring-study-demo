package file;

import java.nio.file.Paths;
import java.nio.file.Files;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;

public class CharacterPositionAnalyzer2 {
    public static void main(String[] args) {
        String filePath = "D:\\test.txt";
        // 目标行号（从1开始）、起始字符位置（行内索引，从0开始）、结束字符位置（行内索引，包含）
        int targetLine = 2;       // 要读取的目标行号
        int targetStartChar = 1;  // 行内起始字符索引
        int targetEndChar = 1;    // 行内结束字符索引
        try {
            byte[] fileBytes = Files.readAllBytes(Paths.get(filePath));
            // 调用修改后的方法，传入目标参数并接收结果
            String extractedChars = analyzeCharacterPositions(fileBytes, targetLine, targetStartChar, targetEndChar);
            System.out.printf("\n从第 %d 行提取的字符（位置 %d-%d）: %s%n", 
                    targetLine, targetStartChar, targetEndChar, extractedChars);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // 修改方法签名，添加目标行号、起始和结束字符位置参数，返回提取的字符
    private static String analyzeCharacterPositions(byte[] fileBytes, int targetLine, int targetStartChar, int targetEndChar) {
        int currentBytePos = 0;
        int lineNumber = 1;
        int charInLine = 0;
        StringBuilder extractedChars = new StringBuilder(); // 用于收集目标字符
        boolean isTargetLine = false; // 是否正在处理目标行

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

            // 输出字符位置信息（原有功能保留）
            System.out.printf("Line: %d, CharInLine: %d, Char: '%s', Start: %d, End: %d, Length: %d%n",
                    lineNumber, charInLine, escapeChar(c), start, end, length);

            // 判断是否为目标行，开启字符收集
            isTargetLine = (lineNumber == targetLine);
            
            // 当处理目标行，且字符位置在指定范围内时，收集字符（跳过换行/回车符）
            if (isTargetLine && c != '\n' && c != '\r') {
                if (charInLine >= targetStartChar && charInLine <= targetEndChar) {
                    extractedChars.append(c);
                }
            }

            // 更新行号和行内字符索引（原有逻辑保留）
            if (c == '\n') {
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
                            lineNumber++;
                            charInLine = 0;
                        }
                    } else {
                        lineNumber++;
                        charInLine = 0;
                    }
                } else {
                    lineNumber++;
                    charInLine = 0;
                }
            } else {
                charInLine++;
            }

            currentBytePos += numBytes;
        }
        
        return extractedChars.toString();
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

    // 转义控制字符以便显示
    private static String escapeChar(char c) {
        if (c == '\n') return "\\n";
        if (c == '\r') return "\\r";
        if (c == '\t') return "\\t";
        if (Character.isISOControl(c)) return String.format("\\u%04x", (int) c);
        return String.valueOf(c);
    }
}