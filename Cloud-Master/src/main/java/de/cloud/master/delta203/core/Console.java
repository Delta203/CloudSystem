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

package de.cloud.master.delta203.core;

import de.cloud.master.delta203.core.handlers.Logger;
import de.cloud.master.delta203.core.utils.ANSIColors;

import java.text.SimpleDateFormat;
import java.util.Calendar;

public class Console extends Logger {

  private final String format = "[%time%] §8[%sender%§8]§r: %string%";
  private final String defaultSender = "§3Cloud§r";

  public Console() {
    loadLog();
  }

  public String getDefaultSender() {
    return defaultSender;
  }

  private String getTime() {
    return new SimpleDateFormat("HH:mm:ss.SSS").format(Calendar.getInstance().getTime());
  }

  public void clear() {
    System.out.print("\033[H\033[2J");
    System.out.flush();
  }

  public void print(String string) {
    String message =
        format
            .replace("%time%", getTime())
            .replace("%sender%", defaultSender)
            .replace("%string%", string);
    System.out.println(
        ANSIColors.translateAlternateColorCodes("§", message + ANSIColors.RESET.code));
    log(ANSIColors.removeAlternateColorCodes("§", message));
  }

  public void print(String string, String sender) {
    String message =
        format.replace("%time%", getTime()).replace("%sender%", sender).replace("%string%", string);
    System.out.println(
        ANSIColors.translateAlternateColorCodes("§", message + ANSIColors.RESET.code));
    log(ANSIColors.removeAlternateColorCodes("§", message));
  }

  public void printRaw(String string) {
    System.out.println(string);
    log(string);
  }
}
