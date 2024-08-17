# Fix
```
[01:20:05.438] [Service]: Lobby-1 files loaded...
[01:20:05.463] [Service]: Lobby-1 exited with code: 1
Exception in thread "Thread-41" java.lang.RuntimeException: java.io.FileNotFoundException: services/temp/Lobby-1/eula.txt (No such file or directory)
        at de.cloud.master.delta203.core.Service.writeFile(Service.java:148)
        at de.cloud.master.delta203.core.Service.addEula(Service.java:172)
        at de.cloud.master.delta203.core.Service.register(Service.java:277)
        at de.cloud.master.delta203.core.Group.runServices(Group.java:131)
        at de.cloud.master.delta203.core.Service.unregister(Service.java:376)
        at de.cloud.master.delta203.core.Service.run(Service.java:361)
Caused by: java.io.FileNotFoundException: services/temp/Lobby-1/eula.txt (No such file or directory)
        at java.base/java.io.FileOutputStream.open0(Native Method)
        at java.base/java.io.FileOutputStream.open(FileOutputStream.java:293)
        at java.base/java.io.FileOutputStream.<init>(FileOutputStream.java:235)
        at de.cloud.master.delta203.core.Service.writeFile(Service.java:145)
        ... 5 more
[01:20:05.567] [Service]: Lobby-2 files loaded...
[01:20:07.682] [Service]: Lobby-1 files loaded...
[01:20:41.281] [Channel]: Lobby-2:47531 successfully connected.
```