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

package de.cloud.master.delta203.core.security;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Random;

public class KeyGenerator {

  private String key;

  public KeyGenerator() {
    key = null;
  }

  private String getDate() {
    return new SimpleDateFormat("yyyy-MM-dd_HH-mm-ss").format(Calendar.getInstance().getTime());
  }

  private int getRandom() {
    return new Random().nextInt(1000000);
  }

  private String sha256(String string) {
    try {
      MessageDigest messageDigest = MessageDigest.getInstance("sha256");
      byte[] bytesHash = messageDigest.digest(string.getBytes());
      StringBuilder sb = new StringBuilder();
      for (byte b : bytesHash) {
        sb.append(String.format("%02x", b));
      }
      return sb.toString();
    } catch (NoSuchAlgorithmException e) {
      throw new RuntimeException(e);
    }
  }

  public void generate() {
    String raw = getRandom() + getDate() + getRandom();
    key = sha256(raw);
  }

  public String getKey() {
    return key;
  }
}
