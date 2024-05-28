package de.cloud.master.delta203.core.files;

import de.cloud.master.delta203.core.utils.Constants;
import java.io.File;
import java.io.IOException;
import java.nio.file.*;
import java.nio.file.attribute.BasicFileAttributes;

public class PathManager {

  private final String root;

  private final File logs;
  private final File assets_api;
  private final File assets_proxy;
  private final File assets_server;
  private final File data_groups;
  private final File templates_default_proxy;
  private final File templates_default_server;
  private final File services_static;
  private final File services_temp;

  public PathManager() {
    root = Constants.Locals.ROOT;
    logs = new File(root + "logs");
    assets_api = new File(root + "assets/api");
    assets_proxy = new File(root + "assets/proxy");
    assets_server = new File(root + "assets/server");
    data_groups = new File(root + "data/groups");
    templates_default_proxy = new File(root + "templates/default/proxy");
    templates_default_server = new File(root + "templates/default/server");
    services_static = new File(root + "services/static");
    services_temp = new File(root + "services/temp");
  }

  public boolean mkdir() {
    return logs.mkdirs()
        && assets_api.mkdirs()
        && assets_proxy.mkdirs()
        && assets_server.mkdirs()
        && data_groups.mkdirs()
        && templates_default_proxy.mkdirs()
        && templates_default_server.mkdirs()
        && services_static.mkdirs()
        && services_temp.mkdirs();
  }

  public String getPathLogs() {
    return root + "logs";
  }

  public String getPathAssetsAPI() {
    return root + "assets/api";
  }

  public String getPathAssetsProxy() {
    return root + "assets/proxy";
  }

  public String getPathAssetsServer() {
    return root + "assets/server";
  }

  public String getPathData() {
    return root + "data";
  }

  public String getPathDataGroup() {
    return root + "data/groups";
  }

  public String getPathTemplates() {
    return root + "templates";
  }

  public String getPathTemplatesDefaultProxy() {
    return root + "templates/default/proxy";
  }

  public String getPathTemplatesDefaultServer() {
    return root + "templates/default/server";
  }

  public String getPathServicesStatic() {
    return root + "services/static";
  }

  public String getPathServicesTemp() {
    return root + "services/temp";
  }

  public void copyFile(Path from, Path to) {
    try {
      Files.copy(from, to, StandardCopyOption.REPLACE_EXISTING);
    } catch (IOException e) {
      throw new RuntimeException(e);
    }
  }

  public void copyDirectory(Path from, Path to) {
    try {
      Files.walkFileTree(
          from,
          new SimpleFileVisitor<>() {
            @Override
            public FileVisitResult preVisitDirectory(Path dir, BasicFileAttributes attributes)
                throws IOException {
              Path targetDir = to.resolve(from.relativize(dir));
              try {
                Files.createDirectories(targetDir);
              } catch (FileAlreadyExistsException e) {
                if (!Files.isDirectory(targetDir)) {
                  throw e;
                }
              }
              return FileVisitResult.CONTINUE;
            }

            @Override
            public FileVisitResult visitFile(Path file, BasicFileAttributes attributes)
                throws IOException {
              Files.copy(
                  file, to.resolve(from.relativize(file)), StandardCopyOption.REPLACE_EXISTING);
              return FileVisitResult.CONTINUE;
            }
          });
    } catch (IOException e) {
      throw new RuntimeException(e);
    }
  }

  public void deleteDirectory(Path path) {
    if (!path.toFile().exists()) return;
    try {
      Files.walkFileTree(
          path,
          new SimpleFileVisitor<>() {
            @Override
            public FileVisitResult visitFile(Path file, BasicFileAttributes attributes)
                throws IOException {
              Files.delete(file);
              return FileVisitResult.CONTINUE;
            }

            @Override
            public FileVisitResult postVisitDirectory(Path dir, IOException e) throws IOException {
              Files.delete(dir);
              return FileVisitResult.CONTINUE;
            }
          });
    } catch (IOException e) {
      throw new RuntimeException(e);
    }
  }
}
