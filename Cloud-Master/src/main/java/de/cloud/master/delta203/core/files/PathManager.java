package de.cloud.master.delta203.core.files;

import de.cloud.master.delta203.core.utils.Constants;
import java.io.File;

public class PathManager {

  private final String root;

  private final File logs;
  private final File assets_proxy;
  private final File assets_server;
  private final File data_groups;
  private final File templates_default_proxy;
  private final File templates_default_server;

  public PathManager() {
    root = Constants.Locals.ROOT;
    logs = new File(root + "/logs");
    assets_proxy = new File(root + "/assets/proxy");
    assets_server = new File(root + "/assets/server");
    data_groups = new File(root + "/data/groups");
    templates_default_proxy = new File(root + "/templates/default/proxy");
    templates_default_server = new File(root + "/templates/default/server");
  }

  public boolean mkdir() {
    return logs.mkdirs()
        && assets_proxy.mkdirs()
        && assets_server.mkdirs()
        && data_groups.mkdirs()
        && templates_default_proxy.mkdirs()
        && templates_default_server.mkdirs();
  }

  public String getPathLogs() {
    return root + "/logs";
  }

  public String getPathAssetsProxy() {
    return root + "/assets/proxy";
  }

  public String getPathAssetsServer() {
    return root + "/assets/server";
  }

  public String getPathData() {
    return root + "/data";
  }

  public String getPathDataGroup() {
    return root + "/data/groups";
  }

  public String getPathTemplates() {
    return root + "/templates";
  }

  public String getPathTemplatesDefaultProxy() {
    return root + "/templates/default/proxy";
  }

  public String getPathTemplatesDefaultServer() {
    return root + "/templates/default/server";
  }
}
