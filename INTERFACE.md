# Server - API Communication
All necessary messages and information between server and sockets will be documented in this file.
## Structure
```json
{
  "key": String,    // Server key
  "type": int,      // Message type
  "data" {
    ...             // Content          
  }
}
```
## Key Generation
```java
public void generate() {
  String raw = getRandom() + getDate() + getRandom();
  key = sha256(raw);
}
```
## Message Types
```json
Type {
  CONNECT,          // 0
  INGAME,           // 1
  DISCONNECT        // Automatically
}
```
### Connect:
A socket connects to the server and is then registered by the server.
```json
"data": {
  "name": String,   // Server name
  "port": int,      // Server port
}
```
### In game:
Switch a server to `ingame` and a new server will be started automatically if the cloud storage is not exhausted.
```json
"data": {}
```
### Disconnect:
As soon as a socket is closed, it automatically disconnects from the server. Therefore, the message is not required manually.