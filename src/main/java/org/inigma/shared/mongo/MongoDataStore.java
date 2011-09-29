package org.inigma.shared.mongo;

import org.springframework.beans.factory.annotation.Autowired;

import com.mongodb.DB;
import com.mongodb.DBCollection;
import com.mongodb.Mongo;
import com.mongodb.MongoURI;
import com.mongodb.gridfs.GridFS;

/**
 * A spring friendly wrapper for accessing a specific database instance.
 * 
 * @author <a href="mailto:sejal@inigma.org">Sejal Patel</a>
 */
public class MongoDataStore {
    private DB db;
    private Mongo mongo;

    @Autowired
    public MongoDataStore(String uri) throws Exception {
        MongoURI mongoUri = new MongoURI(uri);
        this.mongo = new Mongo(mongoUri);
        this.db = mongo.getDB(mongoUri.getDatabase());
    }

    public void endSession() {
        this.db.requestDone();
    }

    @Override
    protected void finalize() throws Throwable {
        this.mongo.close();
        super.finalize();
    }

    public DBCollection getCollection(String name) {
        return getCollection(name, false);
    }

    public DBCollection getCollection(String name, boolean slave) {
        DBCollection collection = db.getCollection(name);
        if (slave) {
            collection.slaveOk();
        }
        return collection;
    }

    public GridFS getGridFS() {
        return new GridFS(db); // default is 'fs'
    }

    public GridFS getGridFS(String directory) {
        return new GridFS(db, directory);
    }

    public void startSession() {
        this.db.requestStart();
    }
}
