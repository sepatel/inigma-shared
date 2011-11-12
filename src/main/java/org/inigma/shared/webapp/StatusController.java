package org.inigma.shared.webapp;

import java.util.HashMap;
import java.util.Set;

import javax.servlet.http.HttpServletResponse;

import org.inigma.shared.mongo.MongoDataStore;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.mongodb.Mongo;
import com.mongodb.ReplicaSetStatus;

@Controller
public class StatusController extends BaseController {
    @Autowired
    private MongoDataStore mds;

    @RequestMapping(value = "/monitor", method = RequestMethod.GET)
    public void status(HttpServletResponse response) {
        Set<String> collections = mds.getDb().getCollectionNames();
        if (collections.isEmpty()) {
            response.setStatus(HttpServletResponse.SC_SERVICE_UNAVAILABLE);
            getErrors().reject("serviceUnavailable", "Unable to connect to database.");
            response(response, null);
            return;
        }

        Mongo mongo = mds.getMongo();
        HashMap<String, Object> map = new HashMap<String, Object>();
        map.put("mongoMaster", mongo.getConnectPoint());
        ReplicaSetStatus replicaSetStatus = mongo.getReplicaSetStatus();
        if (replicaSetStatus != null) {
            map.put("replicaSet", replicaSetStatus.getName());
        } else {
            map.put("replicaSet", null);
        }
        map.put("mongoVersion", mongo.getVersion());
        map.put("usedDatabases", mongo.getUsedDatabases());
        response(response, map);
    }

    public void setMongoDataStore(MongoDataStore mds) {
        this.mds = mds;
    }
}
