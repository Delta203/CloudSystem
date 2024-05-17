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
