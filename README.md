# Cloud System
Single root cloud system for minecraft servers. <br>
Version: 1.0.1-SNAPSHOT <br>
![](https://github.com/Delta203/CloudSystem/blob/main/.img/icon.png)

## Single Root
The **single root** cloud concept is based on the fact that there is only one proxy and all other servers
connect to it. As a result, a multi root function is not yet possible!

## Cloud-API
The Cloud-API handles the individual **services**, i.e. the proxy or the server. This is used to create
a socket connection with the **Cloud-Master** so that these components can communicate with each other.
[Here](https://github.com/Delta203/CloudSystem/tree/main/Cloud-API) you can find more information about the API.

## Cloud-Master
The cloud master is the **main component** of the cloud and takes care of all service handling. It can
be operated using a console and can be used to create service groups, send commands to servers and
much more.

## Cloud-Modules
- Sign
- SyncProxy

## Supported Versions
### Proxy
- [BungeeCord](https://ci.md-5.net/job/BungeeCord/)
- [Waterfall](https://papermc.io/downloads/waterfall)
### Server
- [Spigot](https://getbukkit.org/download/spigot)

## Others
### Requirements
- java 1.17
### Installation
1. Run `Cloud-Master.jar`
2. Set up the cloud
3. Paste `Cloud-API.jar` into _assets/api/_
4. Restart `Cloud-Master.jar`
### Start Scrips
- Windows: `java -Xms256M -Xmx512M -jar Cloud-Master.jar -NOROOT`
- Linux: `screen -S Cloud-Master java -Xms256M -Xmx512M -jar Cloud-Master.jar -NOROOT`
