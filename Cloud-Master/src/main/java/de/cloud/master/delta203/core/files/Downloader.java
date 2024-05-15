package de.cloud.master.delta203.core.files;

import de.cloud.master.delta203.main.Cloud;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

public class Downloader {

  private final String url;
  private final String path;
  private final String name;

  private final int bufferSize = 1024;
  private HttpURLConnection connection;

  public Downloader(String url, String path, String name) {
    this.url = url;
    this.path = path;
    this.name = name;
  }

  public int getBufferSize() {
    return bufferSize;
  }

  private void connect() throws IOException {
    URL url = new URL(this.url);
    connection = (HttpURLConnection) url.openConnection();
    connection.connect();
  }

  public void download() throws IOException {
    connect();
    if (connection.getResponseCode() == HttpURLConnection.HTTP_OK) {
      int fileSize = connection.getContentLength();
      int current = 0;
      InputStream inputStream = connection.getInputStream();
      FileOutputStream outputStream = new FileOutputStream(new File(path, name));
      byte[] buffer = new byte[bufferSize];
      int bytesRead;
      while ((bytesRead = inputStream.read(buffer)) != -1) {
        outputStream.write(buffer, 0, bytesRead);
        current += bytesRead;
        printLoading(fileSize, current);
      }
      outputStream.close();
      inputStream.close();
      System.out.print("\r");
      Cloud.console.print("§2" + name + " successfully downloaded!");
    }
  }

  private void printLoading(int max, int current) {
    int percent = (int) ((double) current / max * 100);
    System.out.print("\r[");
    for (int i = 0; i < 50; i++) {
      if (i < percent / 2) System.out.print("=");
      else if (i == percent / 2) System.out.print(">");
      else System.out.print(" ");
    }
    System.out.print("] " + percent + "%");
  }
}
