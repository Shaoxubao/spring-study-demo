import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;

public class LineCounter {
    public static void main(String[] args) {
        String path = "H:\\code0725\\znyw-client\\src\\main\\java";
        try {
            long totalLines = countLinesInDirectory(new File(path));
            System.out.println("Total lines of code: " + totalLines);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static long countLinesInDirectory(File directory) throws IOException {
        long totalLines = 0;
        File[] files = directory.listFiles();
        if (files != null) {
            for (File file : files) {
                if (file.isDirectory()) {
                    totalLines += countLinesInDirectory(file);
                } else if (file.getName().endsWith(".java")) {
                    totalLines += countLinesInFile(file);
                }
            }
        }
        return totalLines;
    }

    private static long countLinesInFile(File file) throws IOException {
        long lines = 0;
        try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
            while (reader.readLine() != null) {
                lines++;
            }
        }
        return lines;
    }
}