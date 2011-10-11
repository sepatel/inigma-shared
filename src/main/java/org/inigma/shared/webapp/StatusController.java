package org.inigma.shared.webapp;

import java.util.HashMap;

import javax.servlet.http.HttpServletResponse;

import org.inigma.shared.mongo.MongoDataStore;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.mongodb.ReplicaSetStatus;

@Controller
public class StatusController extends BaseController {
    @Autowired
    private MongoDataStore mds;

    @RequestMapping(value = "/monitor", method = RequestMethod.GET)
    public void status(HttpServletResponse response) {
        HashMap<String, Object> map = new HashMap<String, Object>();
        map.put("mongoMaster", mds.getMongo().getConnectPoint());
        ReplicaSetStatus replicaSetStatus = mds.getMongo().getReplicaSetStatus();
        if (replicaSetStatus != null) {
            map.put("replicaSet", replicaSetStatus.getName());
        } else {
            map.put("replicaSet", null);
        }
        map.put("mongoVersion", mds.getMongo().getVersion());
        map.put("usedDatabases", mds.getMongo().getUsedDatabases());
        response(response, map);
    }

    public void setMongoDataStore(MongoDataStore mds) {
        this.mds = mds;
    }
}
