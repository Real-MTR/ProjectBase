package xyz.crystaldev.base.util.mongo.credentials;

import lombok.Getter;

@Getter
public class MongoCredentials {

    private final String host, database, user, password;
    private final int port;
    private final boolean auth;

    public MongoCredentials(String host, String database, int port, boolean isAuth, String user, String password) {
        this.host = host;
        this.database = database;
        this.port = port;
        this.auth = isAuth;
        this.user = user;
        this.password = password;
    }
}