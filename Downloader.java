import java.net.HttpURLConnection;
import java.net.URL;

public class Downloader {

    public static void main(String[] args) throws Exception{
        String fileURL = "https://neodlcdn5.s3.ap-southeast-1.wasabisys.com/Black.Mirror.S02.720p.x264.Hindi.English.Esubs.Vegamovies.ml.zip?X-Amz-Content-Sha256=UNSIGNED-PAYLOAD&X-Amz-Algorithm=AWS4-HMAC-SHA256&X-Amz-Credential=A8POA605KWGSKX3LDQ6G/20250426/ap-southeast-1/s3/aws4_request&X-Amz-Date=20250426T110120Z&X-Amz-SignedHeaders=host&X-Amz-Expires=590400&X-Amz-Signature=f9eb7cc7cc13cd1e20171d8b60b80c4e850ef88dc0a2ca7368c03b471b089a24";
        String outputFile = "BlackMirrorS2.zip";
        int numberOfThreads =7 ;

        URL url = new URL(fileURL);
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        conn.setRequestProperty("User-Agent", "Mozilla/5.0"); 
        int fileSize = conn.getContentLength();
        conn.disconnect();
        System.out.println("File Size: " + fileSize + " bytes");
        if (fileSize == -1) {
            System.out.println("Server did not provide file size. Cannot download in parts.");
            return; 
        }
        int partSize = fileSize / numberOfThreads;
        DownloadThread[] threads = new DownloadThread[numberOfThreads];
        for (int i = 0; i < numberOfThreads; i++) {
            int startByte = i * partSize;
            int endByte = (i == numberOfThreads - 1) ? fileSize : (startByte + partSize - 1);

            threads[i] = new DownloadThread(fileURL, startByte, endByte, i);
            threads[i].start();
        }

        for (DownloadThread thread : threads) {
            thread.join();
        }

        Merger.merge(outputFile, numberOfThreads);
        System.out.println("Download Complete!");
    }
}