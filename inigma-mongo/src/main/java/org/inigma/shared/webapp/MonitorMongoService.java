package org.inigma.shared.webapp;

import com.mongodb.DB;
import com.mongodb.Mongo;
import com.mongodb.ReplicaSetStatus;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@Controller
@RequestMapping("/inigma")
public class MonitorMongoService extends RestService {
    private static class MongoStatusResponse {
        private String mongoMaster;
        private String replicaSet;
        private String mongoVersion;
        private Collection<String> usedDatabase;

        public String getMongoMaster() {
            return mongoMaster;
        }

        public String getMongoVersion() {
            return mongoVersion;
        }

        public String getReplicaSet() {
            return replicaSet;
        }

        public Collection<String> getUsedDatabase() {
            return usedDatabase;
        }

        public void setMongoMaster(String mongoMaster) {
            this.mongoMaster = mongoMaster;
        }

        public void setMongoVersion(String mongoVersion) {
            this.mongoVersion = mongoVersion;
        }

        public void setReplicaSet(String replicaSet) {
            this.replicaSet = replicaSet;
        }

        public void setUsedDatabase(Collection<String> usedDatabase) {
            this.usedDatabase = usedDatabase;
        }
    }

    @Autowired
    private MongoTemplate mongo;

    @RequestMapping(value = "/monitor/mongo", method = RequestMethod.GET)
    public ResponseEntity<MongoStatusResponse> status() {
        Set<String> collections = mongo.getDb().getCollectionNames();
        if (collections.isEmpty()) {
            stopImmediately("serviceUnavailable", HttpStatus.SERVICE_UNAVAILABLE);
        }

        MongoStatusResponse response = createResponse(MongoStatusResponse.class);

        Mongo info = mongo.getDb().getMongo();
        response.setMongoMaster(info.getConnectPoint());
        ReplicaSetStatus replicaSetStatus = info.getReplicaSetStatus();
        if (replicaSetStatus != null) {
            response.setReplicaSet(replicaSetStatus.getName());
        }
        response.setMongoVersion(info.getVersion());
        Collection<String> usedDbs = new ArrayList<String>();
        for (DB db : info.getUsedDatabases()) {
            usedDbs.add(db.getName());
        }
        response.setUsedDatabase(usedDbs);
        return response();
    }
}
