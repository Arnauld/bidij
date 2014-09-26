
[How to Create Client/Server Keystores using Java Keytool](http://ruchirawageesha.blogspot.fr/2010/07/how-to-create-clientserver-keystores.html)

```bash
keytool -genkey -alias client -keyalg RSA -keystore client.jks
keytool -list -v -keystore server.jks -storepass ruchira
keytool -list -v -keystore server.jks -storepass serverp
keytool -export -file server.cert -keystore server.jks -storepass serverp -alias server
keytool -export -file client.cert -keystore client.jks -storepass clientp -alias client
keytool -import -file client.cert -keystore server.jks -storepass serverp -alias client
keytool -import -file server.cert -keystore client.jks -storepass clientp -alias server
```

