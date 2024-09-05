# Cloud System: 1.0.1-SNAPSHOT
![](https://github.com/Delta203/CloudSystem/blob/main/.img/thumbnail.png)

## Single-Root
The **single-root** cloud concept is based on the fact that there is only one root server that operates
the cloud. There is one proxy and all other servers connect to this. As a result, a multi-root function 
where the proxy and the servers run on different root servers is not yet possible!

## Cloud-API
The Cloud-API handles the individual **service**, i.e. the proxy or the server. This is used to create
a socket connection with the **Cloud-Master** so that these components can communicate with each other.
[Here](https://github.com/Delta203/CloudSystem/tree/main/Cloud-API) you can find more information about 
the API.

## Cloud-Master
The cloud master is the **main component** of the cloud and takes care of all service handling. It can
be operated using a console and can be used to create service groups, send commands to servers and
much more.

## Cloud-Modules
- AntiVPN
- SyncProxy
- ~~Sign~~

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
