package org.inigma.shared.webapp;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Set;

import javax.servlet.http.HttpServletResponse;

import org.inigma.shared.mongo.MongoDataStore;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.mongodb.DB;
import com.mongodb.Mongo;
import com.mongodb.ReplicaSetStatus;

@Controller
public class StatusController extends BaseController {
    @Autowired
    private MongoDataStore mds;

    @RequestMapping(value = "/monitor", method = RequestMethod.GET)
    @ResponseBody
    public RestResponse status(HttpServletResponse response) {
        Set<String> collections = mds.getDb().getCollectionNames();
        if (collections.isEmpty()) {
            response.setStatus(HttpServletResponse.SC_SERVICE_UNAVAILABLE);
            ValidationFailureResponse result = new ValidationFailureResponse();
            result.reject("serviceUnavailable", "Unable to reach database.");
            return result;
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
        Collection<String> usedDbs = new ArrayList<String>();
        for (DB db : mongo.getUsedDatabases()) {
            usedDbs.add(db.getName());
        }
        map.put("usedDatabases", usedDbs);
        return new MapRestResponse<String, Object>(map);
    }

    public void setMongoDataStore(MongoDataStore mds) {
        this.mds = mds;
    }
}
