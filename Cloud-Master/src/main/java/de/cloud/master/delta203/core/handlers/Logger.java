/*
 * Copyright 2024 Cloud System by Delta203
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package de.cloud.master.delta203.core.handlers;

import de.cloud.master.delta203.main.Cloud;
import java.io.*;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class Logger {

  public final int maxLines = 100;

  private final List<String> log;
  private final File latest;

  public Logger() {
    log = new ArrayList<>();
    latest = new File(Cloud.pathManager.getPathLogs() + "/latest.txt");
  }

  private String fileName() {
    return new SimpleDateFormat("yyyy-MM-dd_HH-mm-ss").format(Calendar.getInstance().getTime());
  }

  public void loadLog() {
    if (!latest.exists()) return;
    try (BufferedReader reader = new BufferedReader(new FileReader(latest))) {
      String line;
      while ((line = reader.readLine()) != null) {
        log.add(line);
      }
    } catch (IOException e) {
      throw new RuntimeException(e);
    }
  }

  private boolean createNewLog() {
    if (log.size() <= maxLines) return false;
    try (PrintWriter writer =
        new PrintWriter(
            new FileOutputStream(
                Cloud.pathManager.getPathLogs() + "/" + fileName() + ".log", true))) {
      for (String s : log) writer.println(s);
    } catch (FileNotFoundException e) {
      throw new RuntimeException(e);
    }
    log.clear();
    return true;
  }

  public void log(String message) {
    if (message == null || message.isEmpty()) return;
    boolean append = !createNewLog();
    log.add(message);
    try (PrintWriter writer = new PrintWriter(new FileOutputStream(latest, append))) {
      writer.println(message);
    } catch (FileNotFoundException e) {
      throw new RuntimeException(e);
    }
  }
}
