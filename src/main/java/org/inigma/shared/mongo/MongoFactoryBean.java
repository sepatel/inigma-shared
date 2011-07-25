package org.inigma.shared.mongo;

import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.springframework.beans.factory.FactoryBean;

import com.mongodb.Mongo;
import com.mongodb.MongoOptions;
import com.mongodb.MongoURI;
import com.mongodb.ServerAddress;

public class MongoFactoryBean implements FactoryBean<Mongo> {
    private MongoOptions options;
    private List<ServerAddress> servers;
    private MongoURI mongoUri;

    public MongoFactoryBean() {
        this.options = new MongoOptions();
        this.servers = new ArrayList<ServerAddress>();
    }

    @Override
    public Mongo getObject() throws Exception {
        if (mongoUri != null) {
            return new Mongo(mongoUri);
        }

        if (servers.isEmpty()) {
            servers.add(new ServerAddress());
        }
        return new Mongo(servers, options);
    }

    @Override
    public Class<?> getObjectType() {
        return Mongo.class;
    }

    @Override
    public boolean isSingleton() {
        return true;
    }

    /**
     * This controls whether the system retries automatically on connection errors. defaults to false
     */
    public void setAutoConnectRetry(boolean autoConnectRetry) {
        options.autoConnectRetry = autoConnectRetry;
    }

    /**
     * <p>
     * The number of connections allowed per host (the pool size, per host)
     * </p>
     * <p>
     * Once the pool is exhausted, this will block.
     * 
     * @see {@linkplain MongoOptions#threadsAllowedToBlockForConnectionMultiplier}
     *      </p>
     */
    public void setConnectionsPerHost(int connectionsPerHost) {
        options.connectionsPerHost = connectionsPerHost;
    }

    /**
     * The connection timeout in milliseconds; this is for establishing the socket connections (open). 0 is default and
     * infinite
     */
    public void setConnectTimeout(int connectTimeout) {
        options.connectTimeout = connectTimeout;
    }

    /**
     * <p>
     * The description for <code>Mongo</code> instances created with these options. This is used in various places like
     * logging.
     * </p>
     */
    public void setDescription(String description) {
        options.description = description;
    }

    /**
     * Sets the fsync value of WriteConcern for the connection.
     * 
     * Defaults to false; implies safe = true
     */
    public void setFsync(boolean fsync) {
        options.fsync = fsync;
    }

    /**
     * The max wait time for a blocking thread for a connection from the pool in ms.
     */
    public void setMaxWaitTime(int maxWaitTime) {
        options.maxWaitTime = maxWaitTime;
    }

    public void setMongoUri(String mongoUri) {
        this.mongoUri = new MongoURI(mongoUri);
    }

    /**
     * If <b>true</b> the driver sends a getLastError command after every update to ensure it succeeded (see also w and
     * wtimeout) If <b>false</b>, the driver does not send a getlasterror command after every update.
     * 
     * defaults to false
     */
    public void setSafe(boolean safe) {
        options.safe = safe;
    }

    public void setServers(String servers) throws UnknownHostException {
        this.servers.clear();
        for (String server : Arrays.asList(servers.split(" *, *"))) {
            this.servers.add(new ServerAddress(server));
        }
    }

    /**
     * Specifies if the driver is allowed to read from secondaries or slaves.
     * 
     * defaults to false
     */
    public void setSlaveOk(boolean slaveOk) {
        options.slaveOk = slaveOk;
    }

    /**
     * This controls whether or not to have socket keep alive turned on (SO_KEEPALIVE).
     * 
     * defaults to false
     */
    public void setSocketKeepAlive(boolean socketKeepAlive) {
        options.socketKeepAlive = socketKeepAlive;
    }

    /**
     * The socket timeout; this value is passed to {@link java.net.Socket#setSoTimeout(int)}. 0 is default and infinite
     */
    public void setSocketTimeout(int socketTimeout) {
        options.socketTimeout = socketTimeout;
    }

    /**
     * multiplier for connectionsPerHost for # of threads that can block if connectionsPerHost is 10, and
     * threadsAllowedToBlockForConnectionMultiplier is 5, then 50 threads can block more than that and an exception will
     * be throw
     */
    public void setThreadsAllowedToBlockForConnectionMultiplier(int threadsAllowedToBlockForConnectionMultiplier) {
        options.threadsAllowedToBlockForConnectionMultiplier = threadsAllowedToBlockForConnectionMultiplier;
    }

    /**
     * If set, the wtimeout value of WriteConcern for the connection is set to this.
     * 
     * Defaults to 0; implies safe = true
     */
    public void setWritetimeout(int wtimeout) {
        options.wtimeout = wtimeout;
    }
}
