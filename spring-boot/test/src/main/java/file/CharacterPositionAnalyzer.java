package file;

import java.nio.file.Paths;
import java.nio.file.Files;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;

public class CharacterPositionAnalyzer {
    public static void main(String[] args) {
        String filePath = "D:\\test.txt";
        try {
            byte[] fileBytes = Files.readAllBytes(Paths.get(filePath));
            analyzeCharacterPositions(fileBytes);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static void analyzeCharacterPositions(byte[] fileBytes) {
        int currentBytePos = 0;
        int lineNumber = 1;
        int charInLine = 0;

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

            // 输出字符位置信息
            System.out.printf("Line: %d, CharInLine: %d, Char: '%s', Start: %d, End: %d, Length: %d%n",
                    lineNumber, charInLine, escapeChar(c), start, end, length);

            // 更新行号和行内字符索引
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
    }

    // 判断UTF-8字符占用的字节数
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