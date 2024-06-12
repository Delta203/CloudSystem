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

package de.cloud.master.delta203.core.utils;

/** This class contains all minecraft alternate color codes as ANSI code. */
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

  /**
   * This method translates alternate color codes in a string to ANSI color codes.
   *
   * @param altColorChar the character of alternate color code
   * @param remove if the color code should be removed or be translated
   * @param textToTranslate the text to translate
   * @return the translated input message
   */
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

  /**
   * This method translates alternate color codes in a string to ANSI color codes.
   *
   * @param altColorChar the character of alternate color code
   * @param textToTranslate the text to translate
   * @return the translated input message
   */
  public static String translateAlternateColorCodes(String altColorChar, String textToTranslate) {
    return translate(altColorChar, false, textToTranslate);
  }

  /**
   * This method removes alternate color codes from a string.
   *
   * @param altColorChar the character of alternate color code
   * @param textToTranslate the text to translate
   * @return the modified input message
   */
  public static String removeAlternateColorCodes(String altColorChar, String textToTranslate) {
    return translate(altColorChar, true, textToTranslate);
  }
}
