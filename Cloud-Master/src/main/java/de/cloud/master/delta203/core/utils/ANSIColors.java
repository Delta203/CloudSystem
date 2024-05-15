package de.cloud.master.delta203.core.utils;

public enum ANSIColors {
  RESET("\033[0m"),
  DARK_RED("\033[38;2;190;0;0m"),
  RED("\033[38;2;254;63;63m"),
  GOLD("\033[38;2;217;163;52m"),
  YELLOW("\033[38;2;254;254;63m"),
  DARK_GREEN("\033[38;2;0;190;0m"),
  GREEN("\033[38;2;63;254;63m"),
  AQUA("\033[38;2;63;254;254m"),
  DARK_AQUA("\033[38;2;0;190;190m"),
  DARK_BLUE("\033[38;2;0;0;190m"),
  BLUE("\033[38;2;63;63;254m"),
  LIGHT_PURPLE("\033[38;2;254;63;254m"),
  PURPLE("\033[38;2;190;0;190m"),
  WHITE("\033[38;2;255;255;255m"),
  GRAY("\033[38;2;190;190;190m"),
  DARK_GRAY("\033[38;2;63;63;63m"),
  BLACK("\033[38;2;0;0;0m");

  public final String code;

  ANSIColors(String code) {
    this.code = code;
  }

  private static String translate(String altColorChar, boolean remove, String textToTranslate) {
    return textToTranslate
        .replace(altColorChar + "4", remove ? "" : DARK_RED.code)
        .replace(altColorChar + "c", remove ? "" : RED.code)
        .replace(altColorChar + "6", remove ? "" : GOLD.code)
        .replace(altColorChar + "e", remove ? "" : YELLOW.code)
        .replace(altColorChar + "2", remove ? "" : DARK_GREEN.code)
        .replace(altColorChar + "a", remove ? "" : GREEN.code)
        .replace(altColorChar + "b", remove ? "" : AQUA.code)
        .replace(altColorChar + "3", remove ? "" : DARK_AQUA.code)
        .replace(altColorChar + "1", remove ? "" : DARK_BLUE.code)
        .replace(altColorChar + "9", remove ? "" : BLUE.code)
        .replace(altColorChar + "d", remove ? "" : LIGHT_PURPLE.code)
        .replace(altColorChar + "5", remove ? "" : PURPLE.code)
        .replace(altColorChar + "f", remove ? "" : WHITE.code)
        .replace(altColorChar + "7", remove ? "" : GRAY.code)
        .replace(altColorChar + "8", remove ? "" : DARK_GRAY.code)
        .replace(altColorChar + "0", remove ? "" : BLACK.code)
        .replace(altColorChar + "r", remove ? "" : RESET.code);
  }

  public static String translateAlternateColorCodes(String altColorChar, String textToTranslate) {
    return translate(altColorChar, false, textToTranslate);
  }

  public static String removeAlternateColorCodes(String altColorChar, String textToTranslate) {
    return translate(altColorChar, true, textToTranslate);
  }
}
