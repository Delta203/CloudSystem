package de.cloud.master.delta203.core.utils;

import de.cloud.master.delta203.main.Cloud;

public enum GroupType {
  PROXY(Cloud.config.getData().get("versions").getAsJsonObject().get("proxy").getAsString()),
  SERVER(Cloud.config.getData().get("versions").getAsJsonObject().get("server").getAsString());

  public final String version;

  GroupType(String version) {
    this.version = version;
  }
}
