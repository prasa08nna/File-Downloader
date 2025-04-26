import java.io.InputStream;
import java.io.RandomAccessFile;
import java.net.HttpURLConnection;
import java.net.URL;

public class DownloadThread  extends Thread{
    private String fileURL;
    private int startByte;
    private int endByte;
    private int partNumber;
    
    public DownloadThread(String fileURL, int startByte, int endByte, int partNumber) {
        this.fileURL = fileURL;
        this.startByte = startByte;
        this.endByte = endByte;
        this.partNumber = partNumber;
    }
    public void run() {
        try {
            HttpURLConnection conn = (HttpURLConnection) new URL(fileURL).openConnection();
            conn.setRequestProperty("User-Agent", "Mozilla/5.0");
            conn.setRequestProperty("Range", "bytes=" + startByte + "-" + endByte);
            InputStream inputStream = conn.getInputStream();

            String tempFileName = "part_" + partNumber + ".tmp";
            RandomAccessFile partFile = new RandomAccessFile(tempFileName, "rw");

            byte[] buffer = new byte[4096];
            int bytesRead;
            long downloadedBytes = 0;
            while ((bytesRead = inputStream.read(buffer)) != -1) {
                partFile.write(buffer, 0, bytesRead);
                downloadedBytes += bytesRead;
                if (downloadedBytes % (1024 * 100) == 0) { // every 100 KB
                    System.out.println("Part " + partNumber + " downloaded " + downloadedBytes / 1024 + " KB");
                }
            }

            partFile.close();
            inputStream.close();
            conn.disconnect();

            System.out.println("Part " + partNumber + " downloaded.");

        } catch (Exception e) {
            System.out.println("Error downloading part " + partNumber + ": " + e.getMessage());
        }
    }
}
