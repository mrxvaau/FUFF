import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;
import javax.swing.JTextArea;
import javax.swing.SwingUtilities;

public class URLChecker implements Runnable {
    private String host;
    private File file;
    private JTextArea resultArea;

  
    private static final String ANSI_GREEN = "\u001B[32m";
    private static final String ANSI_RED = "\u001B[31m";
    private static final String ANSI_RESET = "\u001B[0m";

    public URLChecker(String host, File file, JTextArea resultArea) {
        this.host = host;
        this.file = file;
        this.resultArea = resultArea;
    }

    @Override
    public void run() {
        try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
            String line;
            while ((line = reader.readLine()) != null) {
                checkUrl(host + "/" + line);
            }
        } catch (IOException e) {
            appendResult("Error reading file: " + e.getMessage(), ANSI_RED);
        }
    }

    private void checkUrl(String urlString) {
        try {
            URL url = new URL(urlString);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("GET");
            int responseCode = connection.getResponseCode();

            if (responseCode == 200) {
                appendResult(urlString + " - 200 OK", ANSI_GREEN);
            } else {
                appendResult(urlString + " - Bad Request", ANSI_RED);
            }
        } catch (IOException e) {
            appendResult(urlString + " - Bad Request", ANSI_RED);
        }
    }

    private void appendResult(String message, String colorCode) {
        SwingUtilities.invokeLater(() -> resultArea.append(message + "\n"));
        System.out.println(colorCode + message + ANSI_RESET); 
    }
}
