# Server - API Communication
## Structure
```json
{
  "key": String,    // The server key
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
```
Type {
  CONNECT(0),
  DISCONNECT(1)
}
```