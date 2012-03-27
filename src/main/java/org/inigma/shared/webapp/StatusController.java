package org.inigma.shared.webapp;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Set;

import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
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
    private MongoTemplate mongo;

    @RequestMapping(value = "/monitor", method = RequestMethod.GET)
    @ResponseBody
    public RestResponse status(HttpServletResponse response) {
        Set<String> collections = mongo.getDb().getCollectionNames();
        if (collections.isEmpty()) {
            response.setStatus(HttpServletResponse.SC_SERVICE_UNAVAILABLE);
            ValidationFailureResponse result = new ValidationFailureResponse();
            result.reject("serviceUnavailable", "Unable to reach database.");
            return result;
        }

        HashMap<String, Object> map = new HashMap<String, Object>();
        Mongo info = mongo.getDb().getMongo();
        map.put("mongoMaster", info.getConnectPoint());
        ReplicaSetStatus replicaSetStatus = info.getReplicaSetStatus();
        if (replicaSetStatus != null) {
            map.put("replicaSet", replicaSetStatus.getName());
        } else {
            map.put("replicaSet", null);
        }
        map.put("mongoVersion", info.getVersion());
        Collection<String> usedDbs = new ArrayList<String>();
        for (DB db : info.getUsedDatabases()) {
            usedDbs.add(db.getName());
        }
        map.put("usedDatabases", usedDbs);
        return new MapRestResponse<String, Object>(map);
    }
}
