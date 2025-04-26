import java.io.File;
import java.io.IOException;
import java.io.RandomAccessFile;

public class Merger {
    public static void merge(String outputFileName, int numberOfParts) throws IOException {
        try (RandomAccessFile outputFile = new RandomAccessFile(outputFileName, "rw")) {
            for (int i = 0; i < numberOfParts; i++) {
                String tempFileName = "part_" + i + ".tmp";
                try (RandomAccessFile partFile = new RandomAccessFile(tempFileName, "r")) {
                    byte[] buffer = new byte[4096];
                    int bytesRead;
                    while ((bytesRead = partFile.read(buffer)) != -1) {
                        outputFile.write(buffer, 0, bytesRead);
                    }
                }
                new File(tempFileName).delete(); 
            }
        }
    }
}
